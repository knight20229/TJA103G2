package com.dreamhouse.blob.reader; 

import java.io.IOException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dreamhouse.prod.model.ProdService;
import com.dreamhouse.prod.model.ProdVO;

@Controller
@RequestMapping("/prod") // 對應 HTML 的寫法
public class DBImgReaderController {

    @Autowired
    private ProdService prodSvc; 

    @GetMapping("/DBImgReader")
    public void dBImgReader(@RequestParam("productId") Integer productId, HttpServletResponse res) throws IOException {
        
        res.setContentType("image/gif, image/jpeg, image/png");
        ServletOutputStream out = res.getOutputStream();
        
        try {
            ProdVO prodVO = prodSvc.getOneProd(productId);
            
            if (prodVO != null) {
                byte[] pic = prodVO.getImageData();
                
                if (pic != null && pic.length > 0) {
                    out.write(pic); 
                }
            }
        } catch (Exception e) {
            System.out.println("圖片讀取錯誤: " + e.getMessage());
        } finally {
            out.close();
        }
    }
}