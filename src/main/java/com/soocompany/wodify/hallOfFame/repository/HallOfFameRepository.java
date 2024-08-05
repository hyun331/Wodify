package com.soocompany.wodify.hallOfFame.repository;

import com.soocompany.wodify.box.domain.Box;
import com.soocompany.wodify.hallOfFame.domain.HallOfFame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HallOfFameRepository extends JpaRepository<HallOfFame, Long> {
    void deleteByBox(Box box);

    List<HallOfFame> findByBoxOrderByRank(Box box);
}
