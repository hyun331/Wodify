package com.soocompany.wodify.post.repository;

import com.soocompany.wodify.post.domain.Comment;
import com.soocompany.wodify.post.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
