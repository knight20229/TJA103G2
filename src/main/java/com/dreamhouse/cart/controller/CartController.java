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
import com.dreamhouse.cart.model.StandardProdCartService;

@Controller
@RequestMapping("/cart")
public class CartController {
	
	
	@Autowired
    private StandardProdCartService stdSer;
	
    
    @Autowired
    private CartService cartSer;
    
	
	@PostMapping("addSTD")
	public String addSTD(@RequestParam("productId") Integer productId, @RequestParam("sizeId") Integer sizeId, @RequestParam("memberId") Integer memberId, @RequestParam("quantity") Integer quantity,@RequestParam("price") Integer price, RedirectAttributes ra, ModelMap model) {
		// sizeId先寫死，之後串祥陞那邊頁面的資料再改
		stdSer.addToCart(productId, sizeId, quantity, price, memberId);
		
		
		ra.addFlashAttribute("message", "商品已成功加入購物車！");
		return "redirect:/prod/getOne_For_Display_front" + "?productId=" + productId;
	}
	
	@PostMapping("addCustom")
	public String addCustom(@RequestParam("productId") Integer productId, @RequestParam("sizeId") Integer sizeId, @RequestParam("productSizeId") Integer productSizeId, @RequestParam("memberId") Integer memberId, ModelMap model) {
		// 待補客製化商品方法
		return "redirect:/prod/getOne_For_Display_front" + "?productId=" + productId;
	}
	
	@GetMapping("getCart")
	public String getAllCartItem(@RequestParam("memberId") String memberId, ModelMap model) {
		List<CartItemDTO> cartItems = cartSer.getAllCartItems(Integer.valueOf(memberId));
		model.addAttribute("cartItems", cartItems);
		
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
