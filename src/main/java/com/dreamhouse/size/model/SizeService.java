package com.dreamhouse.size.model;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SizeService {

    @Autowired
    private SizeRepository sizeRepository;

    public List<SizeVO> getAll() {
        return sizeRepository.findAll();
    }

    public SizeVO getOneSize(Integer sizeId) {
        return sizeRepository.findById(sizeId).orElse(null);
    }
}