package com.soocompany.wodify.hallOfFame.service;

import com.soocompany.wodify.box.domain.Box;
import com.soocompany.wodify.hallOfFame.domain.HallOfFame;
import com.soocompany.wodify.hallOfFame.dto.HallOfFameResDto;
import com.soocompany.wodify.hallOfFame.repository.HallOfFameRepository;
import com.soocompany.wodify.member.domain.Member;
import com.soocompany.wodify.member.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class HallOfFameService {

    private final HallOfFameRepository hallOfFameRepository;
    private final MemberRepository memberRepository;
    public HallOfFameService(HallOfFameRepository hallOfFameRepository, MemberRepository memberRepository) {
        this.hallOfFameRepository = hallOfFameRepository;
        this.memberRepository = memberRepository;
    }

    public List<HallOfFameResDto> getHallOfFame(){
        Member loginMember = memberRepository.findByIdAndDelYn(Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName()),"N").orElseThrow(()->{
            log.error("getHallOfFame() : id에 맞는 회원이 존재하지 않습니다.");
            throw new EntityNotFoundException(" id에 맞는 회원이 존재하지 않습니다.");
        });

        Box box = loginMember.getBox();
        if(box==null){
            log.error("getHallOfFame() : 로그인한 회원은 박스가 존재하지 않습니다.");
            throw new EntityNotFoundException("로그인한 회원은 박스가 존재하지 않습니다.");
        }

        List<HallOfFame> hallOfFameList= hallOfFameRepository.findByBoxOrderByRank(box);

        List<HallOfFameResDto> hallOfFameResDtoList = new ArrayList<>();

        for(HallOfFame hallOfFame : hallOfFameList){
            hallOfFameResDtoList.add(hallOfFame.fromEntity());
        }
        return hallOfFameResDtoList;

    }
}
