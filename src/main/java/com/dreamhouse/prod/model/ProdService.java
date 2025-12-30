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
    private ProdRepository repository;

    @Transactional
    public ProdVO addProd(ProdVO prodVO) {
        return repository.save(prodVO);
    }

    @Transactional
    public void updateProd(ProdVO prodVO) {
        repository.save(prodVO);
    }

    public void deleteProd(Integer productId) {
        if (repository.existsById(productId)) {
            repository.deleteById(productId);
        }
    }

    public ProdVO getOneProd(Integer productId) {
        Optional<ProdVO> optional = repository.findById(productId);
        return optional.orElse(null); // 如果沒找到就回傳 null
    }

    public List<ProdVO> getAll() {
        return repository.findAll();
    }
    
    public List<String> getAllExistingMaterials() {
        return repository.findDistinctMaterials();
    }
    
    // 每分鐘執行一次：0秒 開始，每分鐘執行
    // Cron 格式：秒 分 時 日 月 週
    @Autowired
    private ServletContext servletContext;
    
    @Scheduled(cron = "0 */30 * * * *")
    @Transactional
    public void autoUpdateProductStatus() {
    	LocalDateTime now = LocalDateTime.now();
        int activatedCount = repository.updateStatusToActive(now);
        int deactivatedCount = repository.updateStatusToInactive(now);

        if (activatedCount > 0 || deactivatedCount > 0) {
            String msg = String.format("系統通知：於 %s 自動上架 %d 件、下架 %d 件商品。", 
                            now.format(DateTimeFormatter.ofPattern("HH:mm")), 
                            activatedCount, deactivatedCount);            
            servletContext.setAttribute("productUpdateNotice", msg);
        }
    }
}