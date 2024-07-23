package com.soocompany.wodify.wod.controller;

import com.soocompany.wodify.wod.service.WodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WodController {

    private final WodService wodService;

    @Autowired
    public WodController(WodService wodService) {
        this.wodService = wodService;
    }
}
