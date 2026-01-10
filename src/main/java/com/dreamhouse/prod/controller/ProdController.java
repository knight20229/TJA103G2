package com.dreamhouse.prod.controller;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.dreamhouse.prod.model.*;
import com.dreamhouse.feat.model.*;
import com.dreamhouse.size.model.*;

@Controller
@RequestMapping("/prod")
public class ProdController {

	@Autowired
	private ProdService prodSvc;
	@Autowired
	private FeatService featSvc;
	@Autowired
	private SizeService sizeSvc;
	@Autowired
	private ProdSizeConnectService prodSizeConnectSvc;

	@ModelAttribute("prodListData")
	protected List<ProdVO> referenceListData() {
		return prodSvc.getAll();
	}
	

	// --- 商品清單頁面 ---
	@GetMapping("listAllProd")
	public String listAllProd(ModelMap model) {
		return "back-end/prod/product_listAll";
	}
	// --- 前台商品清單頁面(測試) ---
	@GetMapping("listAllProduct")
	public String listAllProduct(ModelMap model) {
		return "front-end/product_front_listAll";
	}
	// --- 前台商品清單頁面(測試) ---
	@GetMapping("getOne_For_Display_front") 
	public String getOneForDisplayFront(@RequestParam("productId") Integer productId, ModelMap model) {
	    ProdVO prodVO = prodSvc.getOneProd(productId);
	    
	    List<ProdSizeConnectVO> currentConfigs = prodSizeConnectSvc.findByProductId(productId);
	    Map<Integer, ProdSizeConnectVO> configMap = currentConfigs.stream()
	            .collect(java.util.stream.Collectors.toMap(
	                c -> c.getSizeVO().getSizeId(), 
	                c -> c, 
	                (existing, replacement) -> existing));
	    
	    model.addAttribute("prodVO", prodVO);
	    model.addAttribute("configMap", configMap);
	    return "front-end/product_front_listOne";
	}	
	

	@GetMapping("getOne_For_Display")
	public String getOneForDisplay(@RequestParam("productId") Integer productId, ModelMap model) {

		ProdVO prodVO = prodSvc.getOneProd(productId);

		List<ProdSizeConnectVO> configs = prodSizeConnectSvc.findByProductId(productId);

		Map<Integer, ProdSizeConnectVO> configMap = configs.stream()
				.collect(Collectors.toMap(c -> c.getSizeVO().getSizeId(), // 取得關聯的尺寸ID
						c -> c // 放進 Value
				));

		List<SizeVO> allSizes = sizeSvc.getAll();

		List<Integer> distinctWidths = allSizes.stream().map(s -> s.getWidth()) // 取得 SizeVO 裡的寬度
				.distinct().sorted().collect(Collectors.toList());

		if (distinctWidths.isEmpty()) {
			distinctWidths = allSizes.stream().map(SizeVO::getWidth).distinct().sorted().collect(Collectors.toList());
		}

		model.addAttribute("prodVO", prodVO);
		model.addAttribute("configMap", configMap);
		model.addAttribute("allSizes", allSizes);
		model.addAttribute("distinctWidths", distinctWidths);

		return "back-end/prod/product_listOne";
	}

	// --- 新增頁面 ---
	@GetMapping("addProd")
	public String addProd(ModelMap model) {
		ProdVO prodVO = new ProdVO();
		model.addAttribute("prodVO", prodVO);
		model.addAttribute("existingMaterials", prodSvc.getAllExistingMaterials());
		populateCommonData(model);
		return "back-end/prod/product_add";
	}

	// --- insert ---
	@PostMapping("insert")
	public String insert(@Valid ProdVO prodVO, BindingResult result, ModelMap model,
			@RequestParam(value = "images", required = false) MultipartFile[] parts) throws IOException {

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		// 1. 檢查日期邏輯：下架日期不為空，且必須遠於上架日期
	    if (prodVO.getOnDate() != null && prodVO.getOffDate() != null) {
	        if (!prodVO.getOffDate().isAfter(prodVO.getOnDate())) {
	            result.rejectValue("offDate", "error.prodVO", "下架日期必須晚於上架日期！");
	        }
	    }
	    // 2. 商品名稱重複驗證
	    if (prodSvc.isProductNameDuplicate(prodVO.getProductName())) {
	        result.rejectValue("productName", "error.productName", "商品名稱已經存在。");
	    }
		
		// 3. 上稿器空白時不會跳出訊息，有空標籤：<p><br></p>
	    if (prodVO.getDescription() != null) {
	        String desc = prodVO.getDescription();

	        String plainText = desc.replaceAll("<[^>]*>", "");

	        plainText = plainText.replaceAll("&[a-zA-Z0-9#]+;", "")
	                             .replaceAll("\\s", "")
	                             .trim();

	        if (plainText.isEmpty() || "<p><br></p>".equals(desc.trim())) {
	            result.rejectValue("description", "error.prodVO", "商品描述：請勿空白！");
	        }
	    }
		
		
		result = removeFieldError(prodVO, result, "images");

		if (result.hasErrors()) {
			if (parts != null && parts.length > 0 && !parts[0].isEmpty()) {
				byte[] bytes = parts[0].getBytes();
				String base64 = java.util.Base64.getEncoder().encodeToString(bytes);
				prodVO.setBase64Image("data:image/png;base64," + base64);
			}
			model.addAttribute("prodVO", prodVO);
			populateCommonData(model);
			return "back-end/prod/product_add";
		}
		/*************************** 2.開始新增資料 *****************************************/
		byte[] imageBytes = null;
		if (parts != null && parts.length > 0 && !parts[0].isEmpty()) {
			imageBytes = parts[0].getBytes();
		} else if (prodVO.getBase64Image() != null && !prodVO.getBase64Image().isEmpty()) {
			String base64Data = prodVO.getBase64Image().split(",")[1];
			imageBytes = java.util.Base64.getDecoder().decode(base64Data);
		}
		if (imageBytes != null) {
			prodVO.setImageData(imageBytes);
		}

		ProdVO savedVO = prodSvc.addProd(prodVO);
		/*************************** 3.新增完成,準備轉交(Send the Success view) **************/
		List<ProdVO> list = prodSvc.getAll();
		model.addAttribute("prodListData", list); // for listAllProd.html 第156行用
		model.addAttribute("success", "- (新增成功)");
		return "redirect:/prod/setSizes?productId=" + savedVO.getProductId();
	}

	// 新增商品後跳轉到庫存管理
	// 庫存管理入口
	@GetMapping("setSizes")
	public String setSizes(@RequestParam("productId") Integer productId, ModelMap model) {
		ProdVO prodVO = prodSvc.getOneProd(productId);
		model.addAttribute("prodVO", prodVO);

		if (prodVO != null && prodVO.getFeatures() != null) {
			prodVO.getFeatures().size();
		}
		List<SizeVO> allSizes = sizeSvc.getAll();
		model.addAttribute("allSizes", allSizes);

		List<ProdSizeConnectVO> currentConfigs = prodSizeConnectSvc.findByProductId(productId);

		Map<Integer, ProdSizeConnectVO> configMap = currentConfigs.stream().collect(java.util.stream.Collectors
				.toMap(c -> c.getSizeVO().getSizeId(), c -> c, (existing, replacement) -> existing));
		model.addAttribute("configMap", configMap);

		Set<Integer> distinctWidths = allSizes.stream().map(SizeVO::getWidth)
				.collect(java.util.stream.Collectors.toCollection(java.util.TreeSet::new));

		model.addAttribute("distinctWidths", distinctWidths);

		return "back-end/prod/product_size";
	}

	@PostMapping("updateSize")
	public String updateSize(@RequestParam("productId") Integer productId, @RequestParam("sizeId") Integer[] sizeIds,
			@RequestParam("stock") Integer[] stocks, @RequestParam("price") Integer[] prices, ModelMap model,
			RedirectAttributes redirectAttributes) {

		List<String> errorMsgs = new ArrayList<>();
		List<SizeVO> allSizes = sizeSvc.getAll();

		try {
			for (int i = 0; i < sizeIds.length; i++) {
				if (sizeIds[i] != null) {
					final Integer currentId = sizeIds[i]; // 為了在 Lambda 中使用
					SizeVO currentSize = allSizes.stream().filter(s -> s.getSizeId().equals(currentId)).findFirst()
							.orElse(null);

					String sizeName = (currentSize != null)
							? "規格 [寬" + currentSize.getWidth() + " * 長" + currentSize.getLength() + "]"
							: "規格ID: " + currentId;

					if (stocks[i] != null && stocks[i] < 0) {
						errorMsgs.add(sizeName + "：庫存不能為負數 (" + stocks[i] + ")");
					}

					if (prices[i] != null && prices[i] < 0) {
						errorMsgs.add(sizeName + "：價格不能為負數 (" + prices[i] + ")");
					}

					if (errorMsgs.isEmpty()) {
						if (stocks[i] != null && prices[i] != null) {
							prodSizeConnectSvc.saveOrUpdate(productId, sizeIds[i], stocks[i], prices[i]);
						}
					}
				}
			}

			if (!errorMsgs.isEmpty()) {
				ProdVO prodVO = prodSvc.getOneProd(productId);
				List<ProdSizeConnectVO> currentConfigs = prodSizeConnectSvc.findByProductId(productId);
				Map<Integer, ProdSizeConnectVO> configMap = currentConfigs.stream()
						.collect(Collectors.toMap(c -> c.getSizeVO().getSizeId(), c -> c, (e, r) -> e));
				Set<Integer> distinctWidths = allSizes.stream().map(SizeVO::getWidth)
						.collect(Collectors.toCollection(TreeSet::new));

				model.addAttribute("prodVO", prodVO);
				model.addAttribute("allSizes", allSizes);
				model.addAttribute("configMap", configMap);
				model.addAttribute("distinctWidths", distinctWidths);
				model.addAttribute("errorMsgs", errorMsgs);
				return "back-end/prod/product_size";
			}

			long totalActiveSizes = prodSizeConnectSvc.findByProductId(productId).size();

			if (totalActiveSizes > 0) {
				redirectAttributes.addFlashAttribute("successMessage",
						"儲存成功！商品編號 " + productId + " 目前共有 " + totalActiveSizes + " 筆規格資料。");
			} else {
				redirectAttributes.addFlashAttribute("errorMessage", "儲存完成，但目前沒有任何有效的規格內容。");
			}

		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", "系統儲存發生錯誤：" + e.getMessage());
			return "back-end/prod/product_size";
		}

		return "redirect:/prod/listAllProd";
	}

	// --- 修改頁面 ---
	@PostMapping("getOne_For_Update")
	public String getOne_For_Update(@RequestParam("productId") Integer productId, ModelMap model) {
		ProdVO prodVO = prodSvc.getOneProd(productId);
		model.addAttribute("prodVO", prodVO);
		populateCommonData(model);

		List<Integer> currentFeatureIds = new ArrayList<>();
		if (prodVO.getFeatures() != null) {
			for (FeatVO feat : prodVO.getFeatures()) {
				currentFeatureIds.add(feat.getFeatureId());
			}
		}

		model.addAttribute("currentFeatureIds", currentFeatureIds);

		return "back-end/prod/product_edit";
	}

	@PostMapping("update")
	public String update(@Valid ProdVO prodVO, BindingResult result, ModelMap model,
			@RequestParam("images") MultipartFile[] parts) throws IOException {

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		// 1. 檢查日期邏輯：下架日期不為空，且必須遠於上架日期
	    if (prodVO.getOnDate() != null && prodVO.getOffDate() != null) {
	        if (!prodVO.getOffDate().isAfter(prodVO.getOnDate())) {
	            result.rejectValue("offDate", "error.prodVO", "下架日期必須晚於上架日期！");
	        }
	    }
	    // 2. 商品名稱重複驗證，排除本身ID
	    if (prodVO.getProductName() != null && !prodVO.getProductName().isBlank()) {
	        if (prodSvc.isProductNameDuplicateForUpdate(prodVO.getProductName(), prodVO.getProductId())) {
	            result.rejectValue("productName", "error.productName", "此商品名稱已被其他商品使用。");
	        }
	    }
	    
	    // 3. 上稿器空白時不會跳出訊息，有空標籤：<p><br></p>
	    if (prodVO.getDescription() != null) {
	        String desc = prodVO.getDescription();

	        String plainText = desc.replaceAll("<[^>]*>", "");

	        plainText = plainText.replaceAll("&[a-zA-Z0-9#]+;", "")
	                             .replaceAll("\\s", "")
	                             .trim();

	        if (plainText.isEmpty() || "<p><br></p>".equals(desc.trim())) {
	            result.rejectValue("description", "error.prodVO", "商品描述：請勿空白！");
	        }
	    }

		System.out.println("DEBUG: 修改中的商品 ID = " + prodVO.getProductId());
		// 去除BindingResult中images欄位的FieldError紀錄 --> 見第256行
		result = removeFieldError(prodVO, result, "images");

		if (result.hasErrors()) {
			ProdVO dbProd = prodSvc.getOneProd(prodVO.getProductId());
			prodVO.setCreateTime(dbProd.getCreateTime());
			prodVO.setUpdateTime(dbProd.getUpdateTime());

			populateCommonData(model);
			return "back-end/prod/product_edit";
		}
		/*************************** 2.開始修改資料 *****************************************/
		ProdVO currentProd = prodSvc.getOneProd(prodVO.getProductId());

		currentProd.setProductName(prodVO.getProductName());
		currentProd.setProductType(prodVO.getProductType());
		currentProd.setStatus(prodVO.getStatus());
		currentProd.setMaterial(prodVO.getMaterial());
		currentProd.setDescription(prodVO.getDescription());
		currentProd.setOnDate(prodVO.getOnDate());
		currentProd.setOffDate(prodVO.getOffDate());
		currentProd.setOnDate(prodVO.getOnDate());

		if (parts[0].isEmpty()) {
		} else {
			currentProd.setImageData(parts[0].getBytes());
		}

		// EmpService empSvc = new EmpService();
		prodSvc.updateProd(currentProd);

		/*************************** 3.修改完成,準備轉交(Send the Success view) **************/
		model.addAttribute("success", "- (修改成功)");
		ProdVO latestProd = prodSvc.getOneProd(prodVO.getProductId());
		model.addAttribute("prodVO", latestProd);
		return "redirect:/prod/listAllProd"; // 修改成功後轉交product_listAll.html
	}

	// 去除BindingResult中某個欄位的FieldError紀錄
	public BindingResult removeFieldError(ProdVO prodVO, BindingResult result, String removedFieldname) {
		List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
				.filter(fieldname -> !fieldname.getField().equals(removedFieldname)).collect(Collectors.toList());
		result = new BeanPropertyBindingResult(prodVO, "prodVO");
		for (FieldError fieldError : errorsListToKeep) {
			result.addError(fieldError);
		}
		return result;
	}

	// --- 修改共通資料載入邏輯 ---
	private void populateCommonData(ModelMap model) {
		model.addAttribute("featListData", featSvc.getAll());

		// --- 加入動態材質清單 ---
		List<String> mattressMaterials = Arrays.asList("高支撐獨立筒彈簧", "獨立筒彈簧", "獨立筒彈簧+天然乳膠", "天然乳膠", "記憶棉");
		List<String> frameMaterials = Arrays.asList("實木", "木心板", "金屬");

		model.addAttribute("mattressMaterials", mattressMaterials);
		model.addAttribute("frameMaterials", frameMaterials);
		model.addAttribute("distinctWidths", Arrays.asList(90, 105, 120, 150, 180));
		// ------------------------

	}

	// 商品功能轉換器
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(java.util.List.class, "features",
				new org.springframework.beans.propertyeditors.CustomCollectionEditor(java.util.List.class) {
					@Override
					protected Object convertElement(Object element) {
						// 這裡就是關鍵：Spring 會把每一個勾選的 ID 丟進來
						if (element instanceof String text) {
							FeatVO feat = new FeatVO();
							feat.setFeatureId(Integer.parseInt(text));
							return feat;
						}
						return element;
					}
				});
	}
	

	@GetMapping("/api/allProdStatus")
	@ResponseBody
	public List<Map<String, Object>> getAllStatus() {
	    // 取得所有商品，並只提取 ID 與 狀態
	    return prodSvc.getAll().stream().map(p -> {
	        Map<String, Object> map = new HashMap<>();
	        map.put("id", p.getProductId());
	        map.put("status", p.getStatus()); 
	        return map;
	    }).collect(Collectors.toList());
	}

}