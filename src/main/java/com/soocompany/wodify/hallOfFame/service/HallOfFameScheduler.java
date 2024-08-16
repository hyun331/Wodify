package com.soocompany.wodify.hallOfFame.service;

import com.soocompany.wodify.box.domain.Box;
import com.soocompany.wodify.box.repository.BoxRepository;
import com.soocompany.wodify.hallOfFame.domain.HallOfFame;
import com.soocompany.wodify.hallOfFame.repository.HallOfFameRepository;
import com.soocompany.wodify.member.domain.Member;
import com.soocompany.wodify.member.domain.Role;
import com.soocompany.wodify.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class HallOfFameScheduler {
    @Autowired
    private BoxRepository boxRepository;
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private HallOfFameRepository hallOfFameRepository;

    @Scheduled(cron = "0 0 0 * * *")  //자정
    // @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void updateHallOfFame(){
        for(Box box : boxRepository.findByDelYn("N")){
            List<Member> memberList = memberRepository.findByBoxAndRoleAndDelYn(box, Role.USER, "N");
            //상위 5명
            List<Member> topMember = memberList.stream()
                    .sorted((m1, m2)->{
                        BigDecimal total1 = m1.getDeadLift().add(m1.getSquat()).add(m1.getBenchPress());
                        BigDecimal total2 = m2.getDeadLift().add(m2.getSquat()).add(m2.getBenchPress());
                        return total2.compareTo(total1);
                    }).limit(5)
                    .collect(Collectors.toList());

            //순위 갱신
            hallOfFameRepository.deleteByBox(box);
            int rank = 1;
            for(Member member : topMember){
                hallOfFameRepository.save(HallOfFame.builder()
                                .member(member)
                                .box(box)
                                .rank(rank++)
                                .benchPress(member.getBenchPress())
                                .deadLift(member.getDeadLift())
                                .squat(member.getSquat())
                                .total(member.getBenchPress().add(member.getSquat()).add(member.getDeadLift()))
                        .build());
            }

        }
    }
}
