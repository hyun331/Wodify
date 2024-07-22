package com.soocompany.wodify.box.service;

import com.soocompany.wodify.box.domain.Box;
import com.soocompany.wodify.box.dto.BoxSaveReqDto;
import com.soocompany.wodify.box.dto.BoxUpdateReqDto;
import com.soocompany.wodify.box.repository.BoxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BoxService {

    private final BoxRepository boxRepository;
    @Autowired
    public BoxService(BoxRepository boxRepository) {
        this.boxRepository = boxRepository;
    }


    public Box boxCreate(BoxSaveReqDto dto) {
        Box box = dto.toEntity();
        return boxRepository.save(box);
    }


    public Box boxUpdate(Long id, BoxUpdateReqDto dto) {
        Optional<Box> optionalBox = boxRepository.findById(id);
        if (optionalBox.isPresent()) {
            Box box = optionalBox.get();
            box.setName(dto.getName());
            return boxRepository.save(box);
        } else {
            throw new RuntimeException("Box not found with id " + id);
        }
    }


    public Box boxDelete(Long id) {
        Optional<Box> optionalBox = boxRepository.findById(id);
        if (optionalBox.isPresent()) {
            Box box = optionalBox.get();
            box.markAsDeleted();
            return boxRepository.save(box);
        } else {
            throw new RuntimeException("Box not found with id " + id);
        }
    }
}
