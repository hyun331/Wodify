package com.soocompany.wodify.post.repository;
import com.soocompany.wodify.post.domain.Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Likes, Long> {
    Optional<Likes> findByPostIdAndMemberId(Long PostId, Long MemberId);
}
