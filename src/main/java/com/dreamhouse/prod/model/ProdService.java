package com.dreamhouse.prod.model;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProdService {

    @Autowired
    private ProdRepository repository;

    @Transactional
    public void addProd(ProdVO prodVO) {
        repository.save(prodVO);
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
}