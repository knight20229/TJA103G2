package com.dreamhouse.cart.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dreamhouse.cart.model.CartItemDTO;
import com.dreamhouse.cart.model.CartService;
import com.dreamhouse.cart.model.CustomProdCartService;
import com.dreamhouse.cart.model.StandardProdCartService;
import com.dreamhouse.customcloth.model.CustomClothService;
import com.dreamhouse.customcloth.model.CustomClothVO;
import com.dreamhouse.customfeature.model.CustomFeatureService;
import com.dreamhouse.customfeature.model.CustomFeatureVO;
import com.dreamhouse.custommaterial.model.CustomMaterialService;
import com.dreamhouse.custommaterial.model.CustomMaterialVO;
import com.dreamhouse.customprod.model.CustomProductService;
import com.dreamhouse.customprod.model.CustomProductVO;
import com.dreamhouse.customsize.model.CustomSizeService;
import com.dreamhouse.customsize.model.CustomSizeVO;
import com.dreamhouse.mem.model.MemService;
import com.dreamhouse.mem.model.MemVO;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/cart")
public class CartController {
	
	
	@Autowired
    private StandardProdCartService stdSer;
	
	@Autowired
	private CustomProdCartService custSer;
	
    @Autowired
    private CartService cartSer;
    
    @Autowired
	private CustomProductService customProdSer;

	@Autowired
	private CustomClothService customClothSer;

	@Autowired
	private CustomFeatureService customFeatSer;

	@Autowired
	private CustomSizeService customSizeSer;

	@Autowired
	private CustomMaterialService customMaterSer;

	@Autowired
	private MemService memSer;
    
	
	@PostMapping("addSTD")
	public String addSTD(@RequestParam("productId") Integer productId, @RequestParam("sizeId") Integer sizeId, @RequestParam("quantity") Integer quantity, @RequestParam("size") Integer sizePrice, RedirectAttributes ra, HttpSession session, ModelMap model) {
		if (session.getAttribute("memberId") == null) {
			return "redirect:/mem/login";
			
		} 
			
		Integer memberId = (Integer)session.getAttribute("memberId");
		stdSer.addToCart(productId, sizeId, quantity, sizePrice, memberId);
		
		
		ra.addFlashAttribute("message", "商品已成功加入購物車！");
		return "redirect:/product/" + productId;
	}
	
	@PostMapping("addCustom")
	public String addCustom(@RequestParam("customProductId") Integer customProductId, @RequestParam("quantity") Integer quantity, @RequestParam("cloth") Integer customClothId, @RequestParam("size") Integer customSizeId, @RequestParam("material") Integer customMaterialId, @RequestParam("feature") Integer customFeatureId, @RequestParam(value = "bed_frame", defaultValue = "true") Boolean bed_frame, HttpSession session, ModelMap model) {
		System.out.println("前端傳過來的值是：" + bed_frame);

		if (session.getAttribute("memberId") == null) {
			return "redirect:/mem/login";
			
		} 
		
		// 在mysql新增一筆客製化商品
		CustomClothVO customClothVO = customClothSer.getOneById(customClothId);
		CustomFeatureVO customFeatureVO = customFeatSer.getOneById(customFeatureId);
		CustomSizeVO customSizeVO = customSizeSer.getOneById(customSizeId);
		CustomMaterialVO customMaterialVO = customMaterSer.getOneById(customMaterialId);
		
		Integer memberId = (Integer)session.getAttribute("memberId");
		MemVO memVO = memSer.findById(memberId);
		
		
		Integer totalPrice = customClothVO.getCustomClothPrice() + customFeatureVO.getCustomFeaturePrice() + customSizeVO.getCustomSizePrice() + customMaterialVO.getCustomMaterialPrice();
		if (bed_frame == true) {
			totalPrice = totalPrice + 5000;
		}
		
		
		CustomProductVO customProd = new CustomProductVO();
		customProd.setHasBedFrame(bed_frame);
		customProd.setClothVO(customClothVO);
		customProd.setFeatureVO(customFeatureVO);
		customProd.setMaterialVO(customMaterialVO);
		customProd.setSizeVO(customSizeVO);
		customProd.setCustomPrice(totalPrice);
		customProd.setMemVO(memVO);
		
		customProdSer.addCustomProd(customProd);

		model.addAttribute("customProdPrice", customProd.getCustomPrice());

		//  加入購物車
		custSer.addToCart(customProd.getCustomProductId(), customSizeId, quantity, totalPrice, memberId);
		return "redirect:/cart/getCart" + "?memberId=" + memberId;
	}
	
	@GetMapping("getCart")
	public String getAllCartItem(@RequestParam("memberId") String memberId, HttpSession session, ModelMap model) {
		
		if (session.getAttribute("memberId") == null) {
			return "redirect:/mem/login";
		} 
		
		List<CartItemDTO> cartItems = cartSer.getAllCartItems(Integer.valueOf(memberId));

		// 計算購物車總計
		double cartTotal = cartItems.stream()
			.mapToDouble(item -> item.getPrice() * item.getQuantity())
			.sum();

		model.addAttribute("cartItems", cartItems);
		model.addAttribute("cartTotal", cartTotal);

		return "front-end/cart/cart";
	}
	
	@GetMapping("removeItem")
	public String removeItem(@RequestParam("memberId") String memberId, @RequestParam("itemKey") String itemKey, ModelMap model) {
		cartSer.removeItem(itemKey, Integer.valueOf(memberId));
		List<CartItemDTO> cartItems = cartSer.getAllCartItems(Integer.valueOf(memberId));
		model.addAttribute("CartItem", cartItems);
		return "redirect:/cart/getCart" + "?memberId=" + memberId;
	}
	
}
