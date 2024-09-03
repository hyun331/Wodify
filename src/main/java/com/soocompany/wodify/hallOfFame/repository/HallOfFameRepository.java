package com.soocompany.wodify.hallOfFame.repository;

import com.soocompany.wodify.box.domain.Box;
import com.soocompany.wodify.hallOfFame.domain.HallOfFame;
import com.soocompany.wodify.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HallOfFameRepository extends JpaRepository<HallOfFame, Long> {
    void deleteByBox(Box box);

//    List<HallOfFame> findByBoxOrderByRank(Box box);

    List<HallOfFame> findByBox(Box box);

    Optional<HallOfFame> findByMember(Member member);
}
