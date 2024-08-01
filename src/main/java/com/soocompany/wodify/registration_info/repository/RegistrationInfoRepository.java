package com.soocompany.wodify.registration_info.repository;

import com.soocompany.wodify.box.domain.Box;
import com.soocompany.wodify.member.domain.Member;
import com.soocompany.wodify.registration_info.domain.RegistrationInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegistrationInfoRepository extends JpaRepository<RegistrationInfo, Long> {
    List<RegistrationInfo> findByMemberAndBoxAndDelYnOrderByRegistrationDateDesc(Member member, Box box, String delYn);
}
