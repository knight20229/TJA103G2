package com.dreamhouse.checkout.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dreamhouse.cart.model.CartItemDTO;
import com.dreamhouse.cart.model.CartService;
import com.dreamhouse.coupon.model.CouponService;
import com.dreamhouse.coupon.model.CouponVO;
import com.dreamhouse.ecpay.service.ECPayService;
import com.dreamhouse.mem.model.MemService;
import com.dreamhouse.mem.model.MemVO;
import com.dreamhouse.memcoupon.model.MemCouponCompositeKey;
import com.dreamhouse.memcoupon.model.MemCouponRepository;
import com.dreamhouse.memcoupon.model.MemCouponVO;
import com.dreamhouse.orderproductsize.model.OrderProductSizeService;
import com.dreamhouse.orderproductsize.model.OrderProductSizeVO;
import com.dreamhouse.orders.model.OrderStatus;
import com.dreamhouse.orders.model.OrdersService;
import com.dreamhouse.orders.model.OrdersVO;
import com.dreamhouse.orders.model.PaymentStatus;
import com.dreamhouse.prod.model.ProdSizeConnectRepository;
import com.dreamhouse.prod.model.ProdSizeConnectVO;
import com.dreamhouse.promotions.model.PromotionsService;
import com.dreamhouse.promotions.model.PromotionsVO;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {

	@Autowired
	private ProdSizeConnectRepository prodSizeConnectRepo;

	@Autowired
	private MemService memService;

	@Autowired
	private CouponService couponService;

	@Autowired
	private MemCouponRepository memCouponRepo;

	@Autowired
	private OrdersService ordersService;

	@Autowired
	private OrderProductSizeService orderProductSizeService;

	@Autowired
	private ECPayService ecPayService;

	@Autowired
	private CartService cartService;

	@Autowired
	private PromotionsService promotionsService;

	// =========================
	// 結帳頁面（從購物車）
	// =========================
	@GetMapping("")
	public String checkoutPage(HttpSession session, Model model, RedirectAttributes redirectAttributes) {

		// 檢查登入
		Integer memberId = (Integer) session.getAttribute("memberId");
		if (memberId == null) {
			return "redirect:/mem/login";
		}

		// 從購物車獲取商品
		List<CartItemDTO> cartItems = cartService.getAllCartItems(memberId);
		if (cartItems == null || cartItems.isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage", "購物車是空的");
			return "redirect:/cart/getCart?memberId=" + memberId;
		}

		// 查詢會員資訊
		MemVO member = memService.findById(memberId);

		// 計算總金額
		double subtotal = cartItems.stream().mapToDouble(item -> item.getPrice() * item.getQuantity()).sum();

		// 查詢可用優惠券
		List<MemCouponVO> coupons = findAvailableCoupons(memberId);

		// 查詢當前有效促銷
		List<PromotionsVO> activePromotions = promotionsService.getActivePromotions();

		// 計算最佳促銷折扣
		PromotionsVO bestPromotion = null;
		double promotionDiscount = 0;
		if (!activePromotions.isEmpty()) {
			for (PromotionsVO promo : activePromotions) {
				double discount = calculatePromotionDiscount(promo, subtotal);
				if (discount > promotionDiscount) {
					promotionDiscount = discount;
					bestPromotion = promo;
				}
			}
		}

		// 計算促銷後金額
		double afterPromotion = subtotal - promotionDiscount;

		model.addAttribute("member", member);
		model.addAttribute("cartItems", cartItems);
		model.addAttribute("subtotal", subtotal);
		model.addAttribute("activePromotions", activePromotions);
		model.addAttribute("bestPromotion", bestPromotion);
		model.addAttribute("promotionDiscount", promotionDiscount);
		model.addAttribute("afterPromotion", afterPromotion);
		model.addAttribute("discount", 0);
		model.addAttribute("finalTotal", afterPromotion);
		model.addAttribute("coupons", coupons);

		return "front-end/checkout";
	}

	// =========================
	// 確認下單（從購物車）
	// =========================
	@PostMapping("")
	public String checkoutSubmit(@RequestParam String receivingName, @RequestParam String receivingPhone,
			@RequestParam String receivingAddress, @RequestParam String paymentMethod,
			@RequestParam(required = false) Integer couponId, HttpSession session, Model model,
			RedirectAttributes redirectAttributes) {

		try {
			// 檢查登入
			Integer memberId = (Integer) session.getAttribute("memberId");
			if (memberId == null) {
				return "redirect:/mem/login";
			}

			// 收件人姓名驗證
			if (receivingName == null || receivingName.trim().isEmpty()) {
				redirectAttributes.addFlashAttribute("errorMessage", "收件人姓名不可為空");
				return "redirect:/checkout";
			}
			if (receivingName.length() < 2 || receivingName.length() > 50) {
				redirectAttributes.addFlashAttribute("errorMessage", "收件人姓名長度需介於 2~50 字");
				return "redirect:/checkout";
			}

			// 收件人電話驗證
			if (receivingPhone == null || receivingPhone.trim().isEmpty()) {
				redirectAttributes.addFlashAttribute("errorMessage", "收件人電話不可為空");
				return "redirect:/checkout";
			}
			// 手機號碼格式驗證 (09開頭 + 8碼)
			if (!receivingPhone.matches("^09\\d{8}$")) {
				redirectAttributes.addFlashAttribute("errorMessage", "收件人電話格式錯誤，應為09xxxxxxxx");
				return "redirect:/checkout";
			}

			// 收件人地址驗證
			if (receivingAddress == null || receivingAddress.trim().isEmpty()) {
				redirectAttributes.addFlashAttribute("errorMessage", "收件人地址不可為空");
				return "redirect:/checkout";
			}

			// 從購物車獲取商品
			List<CartItemDTO> cartItems = cartService.getAllCartItems(memberId);
			if (cartItems == null || cartItems.isEmpty()) {
				throw new RuntimeException("購物車是空的");
			}

			// 計算商品總金額
			double amount = cartItems.stream().mapToDouble(item -> item.getPrice() * item.getQuantity()).sum();

			// 1. 先計算促銷折扣
			List<PromotionsVO> activePromotions = promotionsService.getActivePromotions();
			PromotionsVO bestPromotion = null;
			double promotionDiscount = 0;
			if (!activePromotions.isEmpty()) {
				for (PromotionsVO promo : activePromotions) {
					double discount = calculatePromotionDiscount(promo, amount);
					if (discount > promotionDiscount) {
						promotionDiscount = discount;
						bestPromotion = promo;
					}
				}
			}

			// 促銷後金額
			double afterPromotion = amount - promotionDiscount;

			// 2. 再計算優惠券折扣（基於促銷後金額）
			double couponDiscount = 0;
			if (couponId != null) {
				CouponVO coupon = validateCoupon(couponId, memberId, afterPromotion);
				couponDiscount = calculateDiscount(coupon, afterPromotion);
			}

			// 3. 總折扣和最終付款金額
			double totalDiscount = promotionDiscount + couponDiscount;
			double payment = amount - totalDiscount;

			// 建立訂單
			OrdersVO order = new OrdersVO();
			order.setMemberId(memberId);
			order.setAmount(amount);
			order.setDiscount(totalDiscount);
			order.setPayment(payment);
			order.setOrderStatus(OrderStatus.PENDING_PAYMENT.getLabel());
			order.setPaymentStatus(PaymentStatus.UNPAID.getLabel());
			order.setReceivingName(receivingName);
			order.setReceivingPhone(receivingPhone);
			order.setReceivingAddress(receivingAddress);
			order.setPaymentMethod(paymentMethod);
			order.setPromotionId(bestPromotion != null ? bestPromotion.getPromotionsId() : null);
			order.setCouponId(couponId);

			ordersService.addOrder(order);

			// 批量建立訂單明細
			for (CartItemDTO cartItem : cartItems) {
				OrderProductSizeVO detail = new OrderProductSizeVO();
				detail.setOrders(order);
				detail.setQuantity(cartItem.getQuantity());
				detail.setPrice((double) cartItem.getPrice());

				// 判斷是標準商品還是客製化商品
				if (cartItem.getItemKey().startsWith("STD:")) {
					// 標準商品
					ProdSizeConnectVO psc = prodSizeConnectRepo.findById(cartItem.getProductSizeId()).orElse(null);
					if (psc == null) {
						throw new RuntimeException("商品尺寸不存在：" + cartItem.getProductName());
					}
					detail.setProdSizeConnect(psc);
					detail.setIsCustom(false);
				} else if (cartItem.getItemKey().startsWith("CUST:")) {
					// 客製化商品
					detail.setCustomProductId(cartItem.getCustomProductId());
					detail.setIsCustom(true);
				}

				orderProductSizeService.add(detail);
			}

			// 更新優惠券使用狀態
			if (couponId != null) {
				markCouponAsUsed(memberId, couponId);
			}

			// 生成綠界付款參數
			String itemName = generateItemNames(cartItems); // 組合商品名稱
			Map<String, String> ecpayParams = ecPayService.generatePaymentParams(order.getOrderId(),
					(int) Math.round(payment), itemName);

			// 儲存 MerchantTradeNo
			String merchantTradeNo = ecpayParams.get("MerchantTradeNo");
			order.setMerchantTradeNo(merchantTradeNo);
			ordersService.updateOrder(order);

			// 將參數傳到付款頁面
			model.addAttribute("ecpayUrl", ecPayService.getApiUrl());
			model.addAttribute("ecpayParams", ecpayParams);

			return "front-end/ecpay-submit";

		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return "redirect:/cart/getCart?memberId=" + session.getAttribute("memberId");
		}
	}

	// =========================
	// 輔助方法
	// =========================

	/**
	 * 查詢會員可用的優惠券
	 */
	private List<MemCouponVO> findAvailableCoupons(Integer memberId) {
		LocalDate today = LocalDate.now();
		return memCouponRepo.findAll().stream().filter(mc -> mc.getMemCouponKey().getMemberId().equals(memberId))
				.filter(mc -> mc.getUseStatus() == 0).filter(mc -> {
					CouponVO coupon = mc.getCouponVO();
					return coupon != null && coupon.getState() == 1 && !today.isBefore(coupon.getStartDt())
							&& !today.isAfter(coupon.getEndDt());
				}).toList();
	}

	/**
	 * 驗證優惠券
	 */
	private CouponVO validateCoupon(Integer couponId, Integer memberId, double orderAmount) {
		// 查詢優惠券
		CouponVO coupon = couponService.getOneById(couponId);
		if (coupon == null || coupon.getState() != 1) {
			throw new RuntimeException("優惠券不存在或已停用");
		}

		// 檢查日期
		LocalDate today = LocalDate.now();
		if (today.isBefore(coupon.getStartDt()) || today.isAfter(coupon.getEndDt())) {
			throw new RuntimeException("優惠券已過期或尚未開始");
		}

		// 檢查使用門檻
		if (orderAmount < coupon.getStandard()) {
			throw new RuntimeException("訂單金額未達優惠券使用門檻 $" + coupon.getStandard());
		}

		return coupon;
	}

	/**
	 * 計算優惠券折扣金額
	 */
	private double calculateDiscount(CouponVO coupon, double orderAmount) {
		if ("AMOUNT".equals(coupon.getType())) {
			return coupon.getCouponValue();
		} else if ("PERCENT".equals(coupon.getType())) {
			return orderAmount * coupon.getCouponValue() / 100.0;
		}
		return 0;
	}

	/**
	 * 計算促銷活動折扣金額
	 */
	private double calculatePromotionDiscount(PromotionsVO promotion, double orderAmount) {
		if ("AMOUNT".equals(promotion.getType())) {
			return promotion.getPromotionsValue();
		} else if ("PERCENT".equals(promotion.getType())) {
			return orderAmount * promotion.getPromotionsValue() / 100.0;
		}
		return 0;
	}

	/**
	 * 標記優惠券為已使用
	 */
	private void markCouponAsUsed(Integer memberId, Integer couponId) {
		MemCouponCompositeKey key = new MemCouponCompositeKey(couponId, memberId);
		MemCouponVO memCoupon = memCouponRepo.findById(key).orElse(null);
		if (memCoupon != null) {
			memCoupon.setUseStatus(1);
			memCoupon.setCouponUpdateTime(LocalDateTime.now());
			memCouponRepo.save(memCoupon);
		}
	}

	/**
	 * 組合商品名稱（用於 ECPay itemName）
	 */
	private String generateItemNames(List<CartItemDTO> cartItems) {
		if (cartItems == null || cartItems.isEmpty()) {
			return "商品";
		}
		if (cartItems.size() == 1) {
			return cartItems.get(0).getProductName();
		}
		// 多個商品：商品A等N件商品
		return cartItems.get(0).getProductName() + "等" + cartItems.size() + "件商品";
	}

	// =========================
	// 綠界金流回調
	// =========================

	/**
	 * 綠界付款結果通知（Server 端背景執行） 對應綠界參數：ReturnURL 用途：接收綠界背景通知，更新訂單狀態 回應：必須返回 "1|OK"
	 */
	@PostMapping("/ecpay/callback")
	@ResponseBody
	public String ecpayCallback(@RequestParam Map<String, String> params) {
		try {
			System.out.println("=== 收到綠界回調 ===");
			params.forEach((key, value) -> System.out.println(key + " = " + value));

			// 驗證 CheckMacValue
			if (!ecPayService.verifyCheckMacValue(params)) {
				System.err.println("CheckMacValue 驗證失敗");
				return "0|CheckMacValue Error";
			}

			// 取得付款結果
			String rtnCode = params.get("RtnCode");
			String merchantTradeNo = params.get("MerchantTradeNo");

			// 解析訂單編號（移除前綴 "DH" 和時間戳）
			Integer orderId = parseOrderId(merchantTradeNo);

			if ("1".equals(rtnCode)) {
				// 付款成功，更新訂單狀態
				ordersService.updatePaymentStatus(orderId, PaymentStatus.PAID.getLabel(),
						OrderStatus.PENDING_SHIPMENT.getLabel());

				// 清空購物車
				OrdersVO order = ordersService.getByOrderID(orderId);
				if (order != null && order.getMemberId() != null) {
					cartService.clearCart(order.getMemberId());
					System.out.println("訂單 " + orderId + " 付款成功，已清空購物車");
				} else {
					System.out.println("訂單 " + orderId + " 付款成功");
				}
			} else {
				// 付款失敗
				ordersService.updatePaymentStatus(orderId, PaymentStatus.FAILED.getLabel(),
						OrderStatus.PENDING_PAYMENT.getLabel());
				System.out.println("訂單 " + orderId + " 付款失敗，RtnCode: " + rtnCode);
			}

			return "1|OK";

		} catch (Exception e) {
			e.printStackTrace();
			return "0|Error";
		}
	}

	/**
	 * 綠界付款結果回傳（Client 端前端接收） 對應綠界參數：OrderResultURL 用途：用戶付款完成後，綠界以 POST 方式回傳付款結果到前端
	 * 回應：導向訂單詳情頁（自動跳轉，提升用戶體驗）
	 */
	@PostMapping("/ecpay/return")
	public String ecpayReturn(@RequestParam Map<String, String> params, HttpSession session,
			RedirectAttributes redirectAttributes) {
		try {
			System.out.println("=== 收到綠界前端回傳（OrderResultURL）===");
			System.out.println("Session ID: " + session.getId());
			System.out.println("Session 中的 memberId: " + session.getAttribute("memberId"));

			// 驗證 CheckMacValue
			if (!ecPayService.verifyCheckMacValue(params)) {
				redirectAttributes.addFlashAttribute("errorMessage", "付款驗證失敗");
				return "redirect:/front/orders/myOrders";
			}

			// 取得付款結果
			String rtnCode = params.get("RtnCode");
			String rtnMsg = params.get("RtnMsg");
			String merchantTradeNo = params.get("MerchantTradeNo");
			Integer orderId = parseOrderId(merchantTradeNo);

			// 從訂單取得會員ID，恢復 Session（解決跨域 Cookie 丟失問題）
			OrdersVO order = ordersService.getByOrderID(orderId);
			if (order != null && order.getMemberId() != null) {
				session.setAttribute("memberId", order.getMemberId());
				System.out.println("恢復 Session: memberId = " + order.getMemberId());
			}

			// 根據付款結果顯示訊息
			if ("1".equals(rtnCode)) {
				redirectAttributes.addFlashAttribute("successMessage", "付款成功！" + rtnMsg);
			} else {
				redirectAttributes.addFlashAttribute("errorMessage", "付款失敗：" + rtnMsg);
			}

			// 導向訂單詳情頁（無論成功或失敗）
			return "redirect:/front/orders/detail?orderId=" + orderId;

		} catch (Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("errorMessage", "系統錯誤");
			return "redirect:/front/orders/myOrders";
		}
	}

	/**
	 * 從 MerchantTradeNo 查詢訂單 ID 格式：DH{timestamp 14位}{orderId%1000 補3位}getCouponId
	 * 由於使用取餘數，無法直接反推，需透過資料庫查詢
	 */
	private Integer parseOrderId(String merchantTradeNo) {
		// 透過資料庫查詢取得對應的訂單（因為使用取餘數，無法直接反推）
		OrdersVO order = ordersService.getByMerchantTradeNo(merchantTradeNo);
		if (order == null) {
			throw new RuntimeException("找不到對應的訂單：" + merchantTradeNo);
		}
		return order.getOrderId();
	}
}
