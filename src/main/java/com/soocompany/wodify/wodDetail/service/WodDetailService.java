package com.soocompany.wodify.wodDetail.service;

import com.soocompany.wodify.wodDetail.repository.WodDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WodDetailService {
    private final WodDetailRepository wodDetailRepository;

    @Autowired
    public WodDetailService(WodDetailRepository wodDetailRepository) {
        this.wodDetailRepository = wodDetailRepository;
    }
}
