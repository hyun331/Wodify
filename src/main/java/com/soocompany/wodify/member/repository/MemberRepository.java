package com.soocompany.wodify.member.repository;

import com.soocompany.wodify.box.domain.Box;
import com.soocompany.wodify.member.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmailAndDelYn(String email, String delYn);

    Page<Member> findAll(Pageable pageable);

    Page<Member> findAllByDelYn(Pageable pageable, String delYn);

    Optional<Member> findByIdAndDelYn(Long id, String delYn);
}
