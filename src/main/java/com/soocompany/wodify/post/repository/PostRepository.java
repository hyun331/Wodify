package com.soocompany.wodify.post.repository;

import com.soocompany.wodify.post.domain.Post;
import com.soocompany.wodify.post.domain.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByTypeAndDelYnOrderByCreatedTimeDesc(Type type, String yn);
    Page<Post> findAllByTypeAndDelYnOrderByCreatedTimeDesc(Pageable pageable, Type type, String yn);
}
