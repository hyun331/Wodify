package com.soocompany.wodify.record.service;

import com.soocompany.wodify.member.domain.Member;
import com.soocompany.wodify.member.repository.MemberRepository;
import com.soocompany.wodify.record.domain.RecordDetail;
import com.soocompany.wodify.record.dto.*;
import com.soocompany.wodify.record.repository.RecordReository;
import com.soocompany.wodify.record.domain.Record;
import com.soocompany.wodify.reservation_detail.domain.ReservationDetail;
import com.soocompany.wodify.reservation_detail.repository.ReservationDetailRepository;
import com.soocompany.wodify.wod.domain.WodDetail;
import com.soocompany.wodify.wod.repository.WodDetailRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@Slf4j
public class RecordService {

    private final RecordReository recordReository;
    private final ReservationDetailRepository reservationDetailRepository;
    private final WodDetailRepository wodDetailRepository;
    private final MemberRepository memberRepository;
    @Autowired
    public RecordService(RecordReository recordReository, ReservationDetailRepository reservationDetailRepository, WodDetailRepository wodDetailRepository, MemberRepository memberRepository) {
        this.recordReository = recordReository;
        this.reservationDetailRepository = reservationDetailRepository;
        this.wodDetailRepository = wodDetailRepository;
        this.memberRepository = memberRepository;
    }


    @Transactional
    public RecordResDto recordCreate(RecordSaveReqDto dto){

        // 인증 정보에서 현재 로그인한 사용자의 이름(아이디) 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String memberIdStr = authentication.getName();

        Long memberId = Long.parseLong(memberIdStr);

        // dto에 memberId 설정
        dto.setMemberId(memberId);

        ReservationDetail reservationDetail = reservationDetailRepository.findByIdAndDelYn(dto.getReservationDetailId(), "N").orElseThrow(()->{
            log.error("recordCreate() : EntityNotFoundException:reservationDetail");
            return new EntityNotFoundException("예약내역이 없습니다"); }
        );


        /* 예약내역에 대한 운동기록의 delYN이 Y일때 */
        if(recordReository.findByReservationDetailIdAndDelYn(dto.getReservationDetailId(), "Y").isPresent()){
            log.info("recordCreate() : 이미 한번이상 생성되었다가 삭제된 운동기록입니다. 새로 생성하는 것이 아니라 있는 기록을 수정합니다.");
            Record record = recordReository.findByReservationDetailIdAndDelYn(dto.getReservationDetailId(), "Y").orElseThrow(()->{
                log.error("recordCreate() : EntityNotFoundException:record");
                return new EntityNotFoundException("운동기록이 없습니다.");
            });

//            if(dto.getExerciseTime() == null){
//                log.error("recordCreate() : IllegalArgumentException");
//                throw new IllegalArgumentException("운동수행시간이 입력되지 않았습니다.");
//            }

            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            LocalTime exerciseTime = LocalTime.parse(dto.getExerciseTime(), dateTimeFormatter);

            record.existedRecordUpdateEntity(dto, exerciseTime);

            int i = 0;
            System.out.println(dto);
            for(RecordSaveReqDetDto rd : dto.getRecordSaveReqDetDtoList()){
                record.getRecordDetails().get(i).recordDetailUpdateEntity(rd.getDetailComments());
                i++;
            }
            recordReository.save(record);

            reservationDetail.updateRecord(record);
            return record.detFromEntity();
        }


//        if(recordReository.findByReservationDetailId(dto.getReservationDetailId()).isPresent()){
//            log.error("recordCreate() : IllegalArgumentException");
//            throw new IllegalArgumentException("예약내역에 대한 운동기록이 존재합니다.");
//        } // 애초에 운동기록이 있으면 앞단 화면이 다르겠지만. 뒷단에서도 확인 // 근데 필요한가..

//        if(dto.getExerciseTime() == null){
//            log.error("recordCreate() : IllegalArgumentException");
//            throw new IllegalArgumentException("운동수행시간이 입력되지 않았습니다.");
//        } // 디폴트값없고 NOT NULL인 컬럼값에 대해 한번 더 확인 // 앞단에서 처리


        /* 예약내역에 대한 운동기록의 delYN이 N일때 */
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalTime exerciseTime = LocalTime.parse(dto.getExerciseTime(), dateTimeFormatter); // 현재 받아오는 운동수행시간(string) // 앞단에서 무슨값으로(string? localtime?) 들어올지 몰라서 string으로 생각하고 처리해서 넣는다.

        Member member = memberRepository.findByIdAndDelYn(memberId, "N").orElseThrow(()->{
            log.error("recordCreate() : EntityNotFoundException:wodDetail");
            return new EntityNotFoundException("멤버가 없습니다");
        });

        Record record = dto.toEntity(reservationDetail, exerciseTime, member);
        for(RecordSaveReqDetDto recordSaveReqDetDto : dto.getRecordSaveReqDetDtoList()){
            WodDetail wodDetail = wodDetailRepository.findByIdAndDelYn(recordSaveReqDetDto.getWodDetailId(), "N").orElseThrow(()->{
                log.error("recordCreate() : EntityNotFoundException:wodDetail");
                return new EntityNotFoundException("와드디테일이 없습니다");
            });
            record.getRecordDetails().add(recordSaveReqDetDto.toEntity(record, wodDetail));
        }
        recordReository.save(record);
        reservationDetail.updateRecord(record);
        return record.detFromEntity();
    }


    public RecordResDto recordDetail(Long id){ // 회원 예약내역리스트 -> 예약내역 하나 선택 -> 운동기록 확인
        Record record = recordReository.findByIdAndDelYn(id, "N").orElseThrow(()->{
            log.error("recordDetail() : EntityNotFoundException");
            return new EntityNotFoundException("운동기록이 없습니다");
        });
        RecordResDto recordResDto = record.detFromEntity();
        for(RecordDetail rd : record.getRecordDetails()){
            String name = rd.getWodDetail().getName();
            String content = rd.getWodDetail().getContents();
            recordResDto.getRecordResDetDtoList().add(rd.fromEntity(name, content));
        }
        return recordResDto;
    }

    public Page<RecordResDto> recordList(Pageable pageable){
        Page<Record> records = recordReository.findByDelYn("N", pageable);
        return records.map(Record::detFromEntity);
    } // 예외처리, 누구 기준으로 보는 리스트인가. 운동기록은 예약내역을 통해 보기때문에 리스트업할 일이 없다.


    public Page<RecordResDto> recordListByMemberId(Long memberId, Pageable pageable) {
        return recordReository.findByMemberId(memberId, pageable)
                .map(record -> new RecordResDto(record));
    }

    @Transactional
    public RecordResDto recordUpdate(Long id, RecordUpdateReqDto dto){
        Record record = recordReository.findByIdAndDelYn(id, "N").orElseThrow(()->{
            log.error("recordUpdate() : EntityNotFoundException");
            return new EntityNotFoundException("운동기록이 없습니다.");
        });

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalTime exerciseTime = LocalTime.parse(dto.getExerciseTime(), dateTimeFormatter);

        record.recordUpdateEntity(dto,exerciseTime);
        int i = 0;
        for(RecordUpdateReqDetDto rd : dto.getRecordUpdateReqDtoList()){
            record.getRecordDetails().get(i).recordDetailUpdateEntity(rd.getDetailComments());
            i++;
        }

        recordReository.save(record); // 해도되고 안해도 된다.
        return record.detFromEntity();
    }


    @Transactional
    public RecordResDto recordDelete(Long id){
        Record record = recordReository.findByIdAndDelYn(id, "N").orElseThrow(()->{
            log.error("recordDelete() : EntityNotFoundException");
            return new EntityNotFoundException("운동기록이 없습니다.");
        });
        record.recordDelete();
        return record.detFromEntity();
    }

}
