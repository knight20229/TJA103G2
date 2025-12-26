package com.dreamhouse.size.model;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SizeService {

    @Autowired
    private SizeRepository repository;

    public List<SizeVO> getAll() {
        return repository.findAll();
    }

    public SizeVO getOneSize(Integer sizeId) {
        return repository.findById(sizeId).orElse(null);
    }
}