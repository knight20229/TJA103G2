package com.dreamhouse.ecpay.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ECPayService {

	@Value("${ecpay.merchant-id}")
	private String merchantId;

	@Value("${ecpay.hash-key}")
	private String hashKey;

	@Value("${ecpay.hash-iv}")
	private String hashIV;

	@Value("${ecpay.api-url}")
	private String apiUrl;

	@Value("${ecpay.return-url}")
	private String returnUrl;

	@Value("${ecpay.order-result-url}")
	private String orderResultUrl;

	/**
	 * 生成綠界付款表單參數
	 */
	public Map<String, String> generatePaymentParams(Integer orderId, Integer amount, String itemName) {
		Map<String, String> params = new TreeMap<>();

		// 基本參數（按照綠界 AIO 規範）
		params.put("MerchantID", merchantId);
		params.put("MerchantTradeNo", generateTradeNo(orderId));
		params.put("MerchantTradeDate", getCurrentDateTime());
		params.put("PaymentType", "aio");
		params.put("TotalAmount", String.valueOf(amount));
		params.put("TradeDesc", "DreamHouse Order");
		params.put("ItemName", itemName);
		params.put("ReturnURL", returnUrl);              // Server 端背景通知
		params.put("ChoosePayment", "Credit");
		params.put("EncryptType", "1");

		// 可選參數
		params.put("OrderResultURL", orderResultUrl);    // Client 端接收付款結果（自動導向）
		params.put("NeedExtraPaidInfo", "N");

		// 計算檢查碼
		String checkMacValue = generateCheckMacValue(params);
		params.put("CheckMacValue", checkMacValue);

		return params;
	}

	/**
	 * 生成交易編號（格式：DH + 時間戳 + 訂單ID取餘數，限制20字元）
	 * 使用取餘數方式，支援無限訂單數量
	 * 例如：DH20260110022817009（19字元）
	 * 訂單ID 1009 → 1009 % 1000 = 009
	 * 注意：無法從 MerchantTradeNo 直接反推訂單ID，需透過資料庫記錄對應關係
	 */
	private String generateTradeNo(Integer orderId) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String timestamp = sdf.format(new Date());
		// 訂單ID對1000取餘數，得到0-999的值，補0到3位數
		int suffix = orderId % 1000;
		String suffixPadded = String.format("%03d", suffix);
		return "DH" + timestamp + suffixPadded;
	}

	/**
	 * 取得當前時間（yyyy/MM/dd HH:mm:ss）
	 */
	private String getCurrentDateTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		return sdf.format(new Date());
	}

	/**
	 * 計算 CheckMacValue 步驟： 1. 參數依 key 排序（已使用 TreeMap 自動排序） 2. 串接成 key1=value1&key2=value2
	 * 格式 3. 前加 HashKey、後加 HashIV 4. URL Encode 5. 轉小寫 6. SHA256 加密 7. 轉大寫
	 */
	public String generateCheckMacValue(Map<String, String> params) {
		try {
			// 移除 CheckMacValue（如果存在）
			Map<String, String> sortedParams = new TreeMap<>(params);
			sortedParams.remove("CheckMacValue");

			// 串接參數（不編碼）
			StringBuilder sb = new StringBuilder();
			sb.append("HashKey=").append(hashKey);

			for (Map.Entry<String, String> entry : sortedParams.entrySet()) {
				sb.append("&").append(entry.getKey()).append("=").append(entry.getValue());
			}

			sb.append("&HashIV=").append(hashIV);

			System.out.println("Step 1 - 原始字串: " + sb.toString());

			// 對整個字串進行 URL Encode（已經在函數內轉小寫並還原字元）
			String encoded = urlEncodeForCheckMac(sb.toString());
			System.out.println("Step 2 - URL Encode + 轉小寫 + 還原字元: " + encoded);

			// SHA256 加密（不需再轉小寫，urlEncodeForCheckMac 已處理）
			String hashed = sha256(encoded);
			System.out.println("Step 3 - SHA256: " + hashed);

			// 轉大寫
			String result = hashed.toUpperCase();
			System.out.println("Step 4 - 轉大寫: " + result);

			return result;

		} catch (Exception e) {
			throw new RuntimeException("CheckMacValue 計算失敗", e);
		}
	}

	/**
	 * CheckMacValue 專用的 URL Encode
	 * 根據綠界官方範例：
	 * - 空格保持為 + （不轉換成 %20）
	 * - 先轉小寫再還原特定字元
	 */
	private String urlEncodeForCheckMac(String value) throws UnsupportedEncodingException {
		// 先進行標準 URL Encode
		String encoded = URLEncoder.encode(value, "UTF-8");

		// 重要：空格在 URLEncoder 中是 +，綠界要求保持 +，不要改成 %20！

		// 轉小寫（URLEncoder 產生的是大寫如 %3D，需轉成 %3d）
		encoded = encoded.toLowerCase();

		System.out.println("URL Encode Debug - 輸入: " + value);
		System.out.println("URL Encode Debug - 輸出: " + encoded);

		return encoded;
	}

	/**
	 * SHA256 加密
	 */
	private String sha256(String input) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(input.getBytes("UTF-8"));

			StringBuilder hexString = new StringBuilder();
			for (byte b : hash) {
				String hex = Integer.toHexString(0xff & b);
				if (hex.length() == 1) {
					hexString.append('0');
				}
				hexString.append(hex);
			}
			return hexString.toString();

		} catch (Exception e) {
			throw new RuntimeException("SHA256 加密失敗", e);
		}
	}

	/**
	 * 驗證回傳的 CheckMacValue
	 */
	public boolean verifyCheckMacValue(Map<String, String> params) {
		String receivedCheckMacValue = params.get("CheckMacValue");
		String calculatedCheckMacValue = generateCheckMacValue(params);
		return calculatedCheckMacValue.equals(receivedCheckMacValue);
	}

	public String getApiUrl() {
		return apiUrl;
	}
}
