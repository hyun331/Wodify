package com.soocompany.wodify.wod.service;

import com.soocompany.wodify.wod.repository.WodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WodService {
    private final WodRepository wodRepository;

    @Autowired
    public WodService(WodRepository wodRepository) {
        this.wodRepository = wodRepository;
    }
}
