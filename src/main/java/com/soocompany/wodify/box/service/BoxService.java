package com.soocompany.wodify.box.service;

import com.soocompany.wodify.box.domain.Box;
import com.soocompany.wodify.box.dto.BoxSaveReqDto;
import com.soocompany.wodify.box.dto.BoxUpdateReqDto;
import com.soocompany.wodify.box.repository.BoxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class BoxService {

    private final BoxRepository boxRepository;
    @Autowired
    public BoxService(BoxRepository boxRepository) {
        this.boxRepository = boxRepository;
    }

    @PersistenceContext
    private EntityManager entityManager;



    @Transactional
    public Box boxCreate(BoxSaveReqDto dto) {
        Box box = dto.toEntity();
        return boxRepository.save(box);
    }



    @Transactional
    public Box boxUpdate(Long id, BoxUpdateReqDto dto) {
        Optional<Box> optionalBox = boxRepository.findById(id);
        if (optionalBox.isPresent()) {
            Box box = optionalBox.get();
            entityManager.detach(box);
            box = Box.builder()
                    .id(box.getId())
                    .name(dto.getName() != null ? dto.getName() : box.getName())
                    .logo(dto.getLogo() != null ? dto.getLogo() : box.getLogo())
                    .operatingHours(box.getOperatingHours())
                    .fee(box.getFee())
                    .intro(box.getIntro())
//                    .code(dto.getCode() != null ? dto.getCode() : box.getCode())
                    .member(box.getMember())
                    .build();
            return boxRepository.save(box);
        } else {
            throw new RuntimeException("Box not found with id " + id);
        }
    }



    @Transactional
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
