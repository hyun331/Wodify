package com.soocompany.wodify.hallOfFame.service;

import com.soocompany.wodify.box.domain.Box;
import com.soocompany.wodify.hallOfFame.domain.HallOfFame;
import com.soocompany.wodify.hallOfFame.dto.HallOfFameAttendanceRateResDto;
import com.soocompany.wodify.hallOfFame.dto.HallOfFameListResDto;
import com.soocompany.wodify.hallOfFame.dto.HallOfFameResDto;
import com.soocompany.wodify.hallOfFame.repository.HallOfFameRepository;
import com.soocompany.wodify.member.domain.Member;
import com.soocompany.wodify.member.domain.Role;
import com.soocompany.wodify.member.repository.MemberRepository;
import com.soocompany.wodify.record.domain.Record;
import com.soocompany.wodify.record.repository.RecordDetailRepository;
import com.soocompany.wodify.record.repository.RecordReository;
import com.soocompany.wodify.reservation.domain.Reservation;
import com.soocompany.wodify.reservation.repository.ReservationRepository;
import com.soocompany.wodify.reservation_detail.domain.ReservationDetail;
import com.soocompany.wodify.reservation_detail.repository.ReservationDetailRepository;
import com.soocompany.wodify.wod.domain.Wod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class HallOfFameService {

    private final HallOfFameRepository hallOfFameRepository;
    private final MemberRepository memberRepository;
    private final ReservationRepository reservationRepository;
    private final RecordReository recordReository;
    private final ReservationDetailRepository reservationDetailRepository;

    @Autowired
    public HallOfFameService(HallOfFameRepository hallOfFameRepository, MemberRepository memberRepository, ReservationRepository reservationRepository, RecordReository recordReository, ReservationDetailRepository reservationDetailRepository) {
        this.hallOfFameRepository = hallOfFameRepository;
        this.memberRepository = memberRepository;
        this.reservationRepository = reservationRepository;
        this.recordReository = recordReository;
        this.reservationDetailRepository = reservationDetailRepository;
    }

    public HallOfFameListResDto getHallOfFame(){
        Member loginMember = memberRepository.findByIdAndDelYn(Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName()),"N").orElseThrow(()->{
            log.error("getHallOfFame() : id에 맞는 회원이 존재하지 않습니다.");
            throw new EntityNotFoundException(" id에 맞는 회원이 존재하지 않습니다.");
        });

        Box box = loginMember.getBox();
        if(box==null){
            log.error("getHallOfFame() : 로그인한 회원은 박스가 존재하지 않습니다.");
            throw new EntityNotFoundException("박스에 등록되지 않았습니다. 박스에 가입해주세요");
        }

        List<HallOfFame> hallOfFameList= hallOfFameRepository.findByBox(box);
        List<HallOfFameResDto> hallOfFameResDtoList = new ArrayList<>();
        for(HallOfFame hallOfFame : hallOfFameList){
            hallOfFameResDtoList.add(hallOfFame.fromEntity());
        }

        //해당 박스의 이전날 와드
        LocalDate yesterday = LocalDate.now().minusDays(1);
        List<Reservation> reservationList = reservationRepository.findAllByBoxAndDateAndDelYn(box, yesterday, "N");
        if(reservationList.isEmpty()){
            log.error("getHallOfFame() : 예약이 존재하지 않습니다.");
            throw new EntityNotFoundException("운동기록이 존재하지 않습니다.");
//            return new HallOfFameListResDto(hallOfFameResDtoList, null);

        }else{
            Reservation reservation = reservationList.get(0);
            Long wodId = wodId = reservation.getWod().getId();
            HallOfFameListResDto hallOfFameListResDto = new HallOfFameListResDto(hallOfFameResDtoList, wodId);

            return hallOfFameListResDto;


        }

    }

    public List<HallOfFameAttendanceRateResDto> getHallOfFameAttendanceRate() {
        Member loginMember = memberRepository.findByIdAndDelYn(Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName()),"N").orElseThrow(()->{
            log.error("getHallOfFameAttendanceRate() : id에 맞는 회원이 존재하지 않습니다.");
            throw new EntityNotFoundException(" id에 맞는 회원이 존재하지 않습니다.");
        });

        Box box = loginMember.getBox();
        if(box==null){
            log.error("getHallOfFameAttendanceRate() : 로그인한 회원은 박스가 존재하지 않습니다.");
            throw new EntityNotFoundException("박스에 등록되지 않았습니다. 박스에 가입해주세요");
        }

        List<HallOfFameAttendanceRateResDto> hallOfFameAttendanceRateResDtoList = new ArrayList<>();
        TreeMap<String, Integer> treeMap = new TreeMap<>();

        List<Member> userList = memberRepository.findByBoxAndRoleAndDelYn(box, Role.USER, "N");
        for(Member m : userList){
            treeMap.put(m.getEmail(),0);
//            returnHashMap.add(new HallOfFameAttendanceRateResDto(m.getName(), 0));
        }
        LocalDate today = LocalDate.now();
        LocalDate firstDayOfMonth = today.withDayOfMonth(1);

        // Get the last day of the current month
        LocalDate lastDayOfMonth = today.with(TemporalAdjusters.lastDayOfMonth());
        List<Reservation> reservationList = reservationRepository.findByBoxAndDateBetweenAndDelYn(box, firstDayOfMonth, lastDayOfMonth, "N", Pageable.unpaged()).getContent();
        for(Reservation r : reservationList){
            List<ReservationDetail> reservationDetailList = reservationDetailRepository.findByReservationAndDelYn(r, "N");
            for(ReservationDetail rd : reservationDetailList){
                if(rd.getRecord() != null && rd.getRecord().getSnf().equals("S")){
                    int value = treeMap.get(rd.getMember().getEmail())+1;
                    treeMap.replace(rd.getMember().getEmail(), value);
                }
            }
        }
        List<String> keySet = new ArrayList<>(treeMap.keySet());
        keySet.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return treeMap.get(o2).compareTo(treeMap.get(o1));
            }
        });

        List<HallOfFameAttendanceRateResDto> returnList = new ArrayList<>();
        int i=0;
        for(String key : keySet){
            if(i>=5)
                break;

            Member member = memberRepository.findByEmailAndDelYn(key, "N").orElseThrow(()->{
                log.error("email에 맞는 멤버를 찾을 수 없습니다");
                throw new EntityNotFoundException("email에 맞는 멤버를 찾을 수 없습니다.");
            });
            returnList.add(HallOfFameAttendanceRateResDto.builder()
                            .name(member.getName())
                            .email(key)
                            .attendanceCount(treeMap.get(key))
                            .build());
            i++;
        }

        return returnList;


    }
}
