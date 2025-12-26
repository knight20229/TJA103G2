package com.dreamhouse.prod.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.feat.model.*;
import com.size.model.*;
import com.dreamhouse.prod.model.*;
import com.emp.model.EmpVO;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/prod")
public class ProdController {

	@Autowired
	private ProdService prodSvc;
	@Autowired
	private FeatService featSvc;
	@Autowired
	private SizeService sizeSvc;

	// --- 2. 修改新增頁面 ---
	@GetMapping("addProd")
	public String addProd(ModelMap model) {
		ProdVO prodVO = new ProdVO();
		model.addAttribute("prodVO", prodVO);
		model.addAttribute("existingMaterials", prodSvc.getAllExistingMaterials());

		// 呼叫更新後的共通資料載入
		populateCommonData(model);
		return "back-end/prod/product_add";
	}

	// --- 修改共通資料載入邏輯 ---
	private void populateCommonData(ModelMap model) {
		model.addAttribute("featListData", featSvc.getAll());

		// --- 1. 加入動態材質清單 ---
		List<String> mattressMaterials = Arrays.asList("高支撐獨立筒彈簧", "獨立筒彈簧", "獨立筒彈簧+天然乳膠", "天然乳膠", "記憶棉");
		List<String> frameMaterials = Arrays.asList("實木", "木心板", "金屬");

		model.addAttribute("mattressMaterials", mattressMaterials);
		model.addAttribute("frameMaterials", frameMaterials);
		// ------------------------

		// 2. 取得所有尺寸物件 
		List<SizeVO> allSizes = sizeSvc.getAll();

		// 3. 過濾出「不重複的寬度」 
		Set<Integer> distinctWidths = new TreeSet<>();
		for (SizeVO s : allSizes) {
			distinctWidths.add(s.getWidth());
		}
		model.addAttribute("distinctWidths", distinctWidths);

		// 4. 將所有尺寸轉成 JSON 字串 
		try {
			ObjectMapper mapper = new ObjectMapper();
			String allSizesJson = mapper.writeValueAsString(allSizes);
			model.addAttribute("allSizesJson", allSizesJson);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("allSizesJson", "[]");
		}
	}
	
	@PostMapping("insert")
	public String insert(@Valid ProdVO prodVO, BindingResult result, ModelMap model,
			@RequestParam("upFiles") MultipartFile[] parts) throws IOException {

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		// 去除BindingResult中upFiles欄位的FieldError紀錄 --> 見第172行
		result = removeFieldError(prodVO, result, "upFiles");

		if (parts[0].isEmpty()) { // 使用者未選擇要上傳的圖片時
			model.addAttribute("errorMessage", "商品圖片: 請上傳圖片");
		} else {
			for (MultipartFile multipartFile : parts) {
				byte[] buf = multipartFile.getBytes();
				prodVO.setUpFiles(buf);
			}
		}
		if (result.hasErrors() || parts[0].isEmpty()) {
			return "back-end/prod/product_add";
		}
		/*************************** 2.開始新增資料 *****************************************/
		prodSvc.addProd(prodVO);
		/*************************** 3.新增完成,準備轉交(Send the Success view) **************/
		List<ProdVO> list = prodSvc.getAll();
		model.addAttribute("prodListData", list); // for listAllEmp.html 第85行用
		model.addAttribute("success", "- (新增成功)");
		return "redirect:back-end/prod/product_list"; // 新增成功後重導至IndexController_inSpringBoot.java的第58行@GetMapping("/emp/listAllEmp")
	}

	
	
}