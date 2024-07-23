package com.soocompany.wodify.record.contrlloer;

import com.soocompany.wodify.record.dto.RecordDetResDto;
import com.soocompany.wodify.record.dto.RecordSaveReqDto;
import com.soocompany.wodify.record.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class RecordController {

    private final RecordService recordService;
    @Autowired
    public RecordController(RecordService recordService) {
        this.recordService = recordService;
    }

    @PostMapping("/record/create")
    public RecordDetResDto recordCreate(@RequestBody RecordSaveReqDto dto){
        return recordService.recordCreate(dto);
    }

    @GetMapping("/record/detail/{id}")
    public RecordDetResDto recordDetail(@PathVariable Long id){
        return recordService.recordDetail(id);
    }
}
