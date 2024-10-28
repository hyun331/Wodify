package com.soocompany.wodify.holding_info.repository;

import com.soocompany.wodify.box.domain.Box;
import com.soocompany.wodify.holding_info.domain.HoldingInfo;
import com.soocompany.wodify.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HoldingInfoRepository extends JpaRepository<HoldingInfo,Long> {
    Optional<HoldingInfo> findByIdAndDelYn(Long id, String delYn);
    List<HoldingInfo> findByMemberAndBoxAndDelYn(Member member, Box box, String delYn);
}
