package com.dreamhouse.feat.model;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FeatService {

    @Autowired
    private FeatRepository featRepository;

    public List<FeatVO> getAll() {
        return featRepository.findAll();
    }

    public FeatVO getOneFeat(Integer featureId) {
        return featRepository.findById(featureId).orElse(null);
    }
}