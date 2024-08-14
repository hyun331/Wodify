package com.soocompany.wodify.post.repository;

import com.soocompany.wodify.box.domain.Box;
import com.soocompany.wodify.post.domain.Post;
import com.soocompany.wodify.post.domain.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByTypeAndBoxAndDelYnOrderByCreatedTimeDesc(Type type, Box box, String yn);
    Page<Post> findAllByTypeAndBoxAndDelYnOrderByCreatedTimeDesc(Pageable pageable, Type type, Box box, String yn);
}
