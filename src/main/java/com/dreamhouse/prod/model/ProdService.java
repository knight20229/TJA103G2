package com.dreamhouse.prod.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.ServletContext;

@Service
public class ProdService {

    @Autowired
    private ProdRepository prodRepository;

    @Transactional
    public ProdVO addProd(ProdVO prodVO) {
        return prodRepository.save(prodVO);
    }

    @Transactional
    public void updateProd(ProdVO prodVO) {
        prodRepository.save(prodVO);
    }

    public void deleteProd(Integer productId) {
        if (prodRepository.existsById(productId)) {
            prodRepository.deleteById(productId);
        }
    }

    public ProdVO getOneProd(Integer productId) {
        Optional<ProdVO> optional = prodRepository.findById(productId);
        return optional.orElse(null); 
    }

    public List<ProdVO> getAll() {
        return prodRepository.findAll();
    }
    
    public List<String> getAllExistingMaterials() {
        return prodRepository.findDistinctMaterials();
    }
    // 新增商品時，已存在的名稱
    public boolean isProductNameDuplicate(String name) {
        return prodRepository.existsByProductName(name);
    }
    // 編輯商品時，排除本身的已存在名稱
    public boolean isProductNameDuplicateForUpdate(String name, Integer productId) {
        return prodRepository.existsByProductNameAndProductIdNot(name, productId);
    }
    
    // 每分鐘執行一次：0秒 開始，每分鐘執行
    // Cron 格式：秒 分 時 日 月 週
    @Autowired
    private ServletContext servletContext;
    
//    @Scheduled(cron = "0/30 * * * * *")
    @Scheduled(fixedRate = 20000)
    @Transactional
    public void autoUpdateProductStatus() {
        int activatedCount = prodRepository.updateStatusToActive();
        int deactivatedCount = prodRepository.updateStatusToInactive();

        if (activatedCount > 0 || deactivatedCount > 0) {
            String timeStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
            String msg = String.format("系統通知：於 %s 自動上架 %d 件、下架 %d 件商品。", 
                            timeStr, activatedCount, deactivatedCount);            
            servletContext.setAttribute("productUpdateNotice", msg);
            
            System.out.println("DEBUG >>> " + msg);
        }
    }
}