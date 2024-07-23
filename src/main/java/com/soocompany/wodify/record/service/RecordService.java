package com.soocompany.wodify.record.service;

import com.soocompany.wodify.record.dto.RecordDetResDto;
import com.soocompany.wodify.record.dto.RecordSaveReqDto;
import com.soocompany.wodify.record.repository.RecordReository;
import com.soocompany.wodify.record.domain.Record;
import com.soocompany.wodify.reservation_detail.domain.ReservationDetail;
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
    @Autowired
    public RecordService(RecordReository recordReository) {
        this.recordReository = recordReository;
    }

    @Transactional
    public RecordDetResDto recordCreate(RecordSaveReqDto dto){
//        예약내역 정보(사이트가 들고 있는 정보) -> 회원정보,예약정보 -> 와드정보
//        ReservationDetail reservationDetail // 원래라면 예약내역에 대한 정보를 받겠지..그걸 저장해야겠지.. 어떻게?

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime exerciseTime = LocalTime.parse(dto.getExerciseTime(), dateTimeFormatter);

        Record saveRecord = recordReository.save(dto.toEntity(exerciseTime));
        return saveRecord.detFromEntity();
    }


    public RecordDetResDto recordDetail(Long id){
//       이 ID를 얻는 방법은...? 앞단에서 예약내역을 누르고 그 내역의 운동기록을 누르면 운동기록의 id가 넘어가서...?
        Record record = recordReository.findById(id).orElseThrow(()->new EntityNotFoundException("운동기록이 없습니다."));
        return record.detFromEntity();

    }



}
