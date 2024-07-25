package com.soocompany.wodify.wodDetail.controller;

import com.soocompany.wodify.wodDetail.service.WodDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WodDetailController {

    private final WodDetailService wodDetailService;

    @Autowired
    public WodDetailController(WodDetailService wodDetailService) {
        this.wodDetailService = wodDetailService;
    }
}
