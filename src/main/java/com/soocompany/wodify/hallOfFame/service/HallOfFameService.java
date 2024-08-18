package com.soocompany.wodify.hallOfFame.service;

import com.soocompany.wodify.box.domain.Box;
import com.soocompany.wodify.hallOfFame.domain.HallOfFame;
import com.soocompany.wodify.hallOfFame.dto.HallOfFameListResDto;
import com.soocompany.wodify.hallOfFame.dto.HallOfFameResDto;
import com.soocompany.wodify.hallOfFame.repository.HallOfFameRepository;
import com.soocompany.wodify.member.domain.Member;
import com.soocompany.wodify.member.repository.MemberRepository;
import com.soocompany.wodify.reservation.domain.Reservation;
import com.soocompany.wodify.reservation.repository.ReservationRepository;
import com.soocompany.wodify.wod.domain.Wod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class HallOfFameService {

    private final HallOfFameRepository hallOfFameRepository;
    private final MemberRepository memberRepository;
    private final ReservationRepository reservationRepository;
    public HallOfFameService(HallOfFameRepository hallOfFameRepository, MemberRepository memberRepository, ReservationRepository reservationRepository) {
        this.hallOfFameRepository = hallOfFameRepository;
        this.memberRepository = memberRepository;
        this.reservationRepository = reservationRepository;
    }

    public HallOfFameListResDto getHallOfFame(){
        Member loginMember = memberRepository.findByIdAndDelYn(Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName()),"N").orElseThrow(()->{
            log.error("getHallOfFame() : id에 맞는 회원이 존재하지 않습니다.");
            throw new EntityNotFoundException(" id에 맞는 회원이 존재하지 않습니다.");
        });

        Box box = loginMember.getBox();
        if(box==null){
            log.error("getHallOfFame() : 로그인한 회원은 박스가 존재하지 않습니다.");
            throw new EntityNotFoundException("로그인한 회원은 박스가 존재하지 않습니다.");
        }

        List<HallOfFame> hallOfFameList= hallOfFameRepository.findByBox(box);
        List<HallOfFameResDto> hallOfFameResDtoList = new ArrayList<>();
        for(HallOfFame hallOfFame : hallOfFameList){
            hallOfFameResDtoList.add(hallOfFame.fromEntity());
        }

        //해당 박스의 이전날 와드
        LocalDate yesterday = LocalDate.now().minusDays(1);
        Reservation reservation = reservationRepository.findAllByBoxAndDateAndDelYn(box, yesterday, "N").get(0);

        Long wodId = null;
        if(reservation != null){
            wodId = reservation.getWod().getId();
        }else{
            log.error("getHallOfFame() : 예약이 존재하지 않습니다.");
            throw new EntityNotFoundException("예약이 존재하지 않습니다.");
        }


        HallOfFameListResDto hallOfFameListResDto = new HallOfFameListResDto(hallOfFameResDtoList, wodId);

        return hallOfFameListResDto;

    }
}
