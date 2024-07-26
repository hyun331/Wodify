package com.soocompany.wodify.record.service;

import com.soocompany.wodify.record.dto.RecordDetResDto;
import com.soocompany.wodify.record.dto.RecordSaveReqDto;
import com.soocompany.wodify.record.dto.RecordUpdateReqDto;
import com.soocompany.wodify.record.repository.RecordReository;
import com.soocompany.wodify.record.domain.Record;
import com.soocompany.wodify.reservation_detail.domain.ReservationDetail;
import com.soocompany.wodify.reservation_detail.repository.ReservationDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class RecordService {

    private final RecordReository recordReository;
    private final ReservationDetailRepository reservationDetailRepository;
    @Autowired
    public RecordService(RecordReository recordReository, ReservationDetailRepository reservationDetailRepository) {
        this.recordReository = recordReository;
        this.reservationDetailRepository = reservationDetailRepository;
    }

    @Transactional
    public RecordDetResDto recordCreate(RecordSaveReqDto dto){
//        생길 수 있는 예외를 더 생각해보자.
        ReservationDetail reservationDetail = reservationDetailRepository.findById(dto.getReservationDetailId()).orElseThrow(()->new EntityNotFoundException("recordCreate(RecordSaveReqDto dto) : 예약내역이 없습닌다.")); // 예약내역 레포 말고 서비스에서 가져올 수 있도록 하자.

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime exerciseTime = LocalTime.parse(dto.getExerciseTime(), dateTimeFormatter); // 현재 받아오는 운동수행시간(string) // 앞단에서 무슨값으로(string? localtime?) 들어올지 몰라서 string으로 생각하고 처리해서 넣는다.

        Record record = recordReository.save(dto.toEntity(reservationDetail, exerciseTime));
        return record.detFromEntity();
    }

    public RecordDetResDto recordDetail(Long id){
//        생길 수 있는 예외를 더 생각해보자.
        Record record = recordReository.findById(id).orElseThrow(()->new EntityNotFoundException("recordDetail(Long id) : 운동기록이 없습니다."));
        return record.detFromEntity();
    }

    @Transactional
    public RecordDetResDto recordUpdate(Long id, RecordUpdateReqDto dto){
//        생길 수 있는 예외를 더 생각해보자.
        Record record = recordReository.findById(id).orElseThrow(()->new EntityNotFoundException("recordUpdate(RecordUpdateReqDto dto) : 운동기록이 없습니다."));
        record.recordUpdateEntity(dto);
        recordReository.save(record);
        return record.detFromEntity();
    }

    @Transactional
    public RecordDetResDto recordDelete(Long id){
//        생길 수 있는 예외를 더 생각해보자.
        Record record = recordReository.findById(id).orElseThrow(()->new EntityNotFoundException("recordDelete(Long id) : 운동기록이 없습니다."));
        record.updateDelYn();
        return record.detFromEntity();
    }

}