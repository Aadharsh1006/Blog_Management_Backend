package com.examly.springapp.controller;

import com.examly.springapp.dto.CommentDto;
import com.examly.springapp.model.Comment;
import com.examly.springapp.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
public class CommentController {

    @Autowired
    private CommentService commentService; // Correctly uses CommentService

    @GetMapping
    public ResponseEntity<List<Comment>> getCommentsByPost(@PathVariable Long postId) {
        List<Comment> comments = commentService.getCommentsByPostId(postId);
        return ResponseEntity.ok(comments);
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()") // Only logged-in users can comment
    public ResponseEntity<Comment> createComment(@PathVariable Long postId, @RequestBody CommentDto commentDto) {
        Comment newComment = commentService.createComment(postId, commentDto.getContent());
        return new ResponseEntity<>(newComment, HttpStatus.CREATED);
    }
}