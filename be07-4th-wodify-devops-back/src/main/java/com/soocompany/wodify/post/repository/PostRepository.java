package com.soocompany.wodify.post.repository;

import com.soocompany.wodify.box.domain.Box;
import com.soocompany.wodify.post.domain.Post;
import com.soocompany.wodify.post.domain.Type;
import com.soocompany.wodify.post.dto.PostRecordResDto;
import com.soocompany.wodify.record.domain.Record;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.Tuple;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByDelYnOrderByIdDesc(String yn);
    List<Post> findAllByTypeAndBoxAndDelYnOrderByIdDesc(Type type, Box box, String yn);
//    Page<Post> findAllByTypeAndBoxAndDelYnOrderByIdDesc(Pageable pageable, Type type, Box box, String yn);
    Page<Post> findAll(Specification<Post> specification, Pageable pageable);

    @Query(value = "SELECT r.exercise_time as exerciseTime, r.snf as snf, r.comments as comments " +
            "FROM record r " +
            "WHERE r.id IN ( " +
            "    SELECT rd.record_id " +
            "    FROM reservation_detail rd " +
            "    WHERE rd.reservation_id IN ( " +
            "        SELECT res.id " +
            "        FROM reservation res " +
            "        WHERE res.date = :date " +
            "        AND res.del_yn = 'N' " +
            "        AND res.box_id = :boxId " +
            "    ) " +
            "    AND rd.del_yn = 'N' " +
            "    AND rd.member_id = :memberId " +
            ") " +
            "LIMIT 1", nativeQuery = true)
    Optional<Tuple> findTopRecordByDateAndBoxIdAndMemberId(
            @Param("date") LocalDate date,
            @Param("boxId") Long boxId,
            @Param("memberId") Long memberId
    );
}
