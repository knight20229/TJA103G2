package com.dreamhouse.returns.controller;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dreamhouse.orderproductsize.model.OrderProductSizeService;
import com.dreamhouse.orderproductsize.model.OrderProductSizeVO;
import com.dreamhouse.orders.model.OrdersService;
import com.dreamhouse.orders.model.OrdersVO;
import com.dreamhouse.prod.model.ProdSizeConnectVO;

@Controller
@RequestMapping("/returns")
public class ReturnController {
	@Autowired
	OrdersService ordersSvc;

	@Autowired
	OrderProductSizeService orderProductSizeSvc;

	// 訂單列表頁面（含訂單明細）
	@GetMapping("/list")
	public String listAllOrders(ModelMap model) {
		List<OrdersVO> orders = ordersSvc.getAllReturn();

		// 為每個訂單組裝明細資料
		List<Map<String, Object>> orderListWithDetails = new ArrayList<>();

		for (OrdersVO order : orders) {
			try {
				Map<String, Object> orderData = new HashMap<>();

				// 訂單基本資訊
				orderData.put("order", order);

				// 查詢並組裝訂單明細（使用 JOIN FETCH 一次查詢）
				List<OrderProductSizeVO> items = orderProductSizeSvc.getByOrderIdWithDetails(order.getOrderId());
				List<Map<String, Object>> details = new ArrayList<>();

				for (OrderProductSizeVO item : items) {
					Map<String, Object> detail = new HashMap<>();

					// 基本資訊（所有明細都有）
					detail.put("quantity", item.getQuantity());
					detail.put("price", item.getPrice());
					detail.put("isCustom", item.getIsCustom());

					// 初始化所有可能用到的鍵（避免 Thymeleaf 找不到屬性）
					detail.put("productName", null);
					detail.put("productImageBase64", null);
					detail.put("width", null);
					detail.put("length", null);
					detail.put("customProductId", null);

					if (!item.getIsCustom()) {
						// 透過 JPA 關聯獲取商品和尺寸資訊
						ProdSizeConnectVO psc = item.getProdSizeConnect();
						if (psc != null && psc.getProdVO() != null && psc.getSizeVO() != null) {
							detail.put("productName", psc.getProdVO().getProductName());
							detail.put("productImageBase64", convertBlobToBase64(psc.getProdVO().getImageData()));
							detail.put("width", psc.getSizeVO().getWidth());
							detail.put("length", psc.getSizeVO().getLength());
						} else {
							detail.put("productName", "商品資訊不存在");
						}
					} else {
						detail.put("productName", "客製化商品");
						detail.put("customProductId", item.getCustomProductId());
					}

					details.add(detail);
				}

				orderData.put("details", details);
				orderListWithDetails.add(orderData);

			} catch (Exception e) {
				// 如果某個訂單處理失敗，記錄錯誤但繼續處理其他訂單
				System.err.println("處理訂單 ID " + order.getOrderId() + " 時發生錯誤: " + e.getMessage());
				e.printStackTrace();
			}
		}

		model.addAttribute("orderListWithDetails", orderListWithDetails);
		return "back-end/return/listAllReturn";
	}

	/**
	 * 將 BLOB 圖片轉換為 Base64 字串
	 */
	private String convertBlobToBase64(byte[] imageData) {
		if (imageData == null || imageData.length == 0) {
			return null;
		}
		return "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(imageData);
	}
}
