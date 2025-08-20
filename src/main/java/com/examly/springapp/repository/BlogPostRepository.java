package com.examly.springapp.repository;

import com.examly.springapp.model.BlogPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {
    // Add this method to find all posts by a user's ID
    List<BlogPost> findByUserId(Long userId);
}