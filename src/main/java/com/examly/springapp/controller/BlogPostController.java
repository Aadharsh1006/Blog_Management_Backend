package com.examly.springapp.controller;

import com.examly.springapp.model.BlogPost;
import com.examly.springapp.service.BlogPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/posts")
public class BlogPostController {

    @Autowired
    private BlogPostService blogPostService;

    @PostMapping
    public ResponseEntity<BlogPost> createBlogPostEndpoint(@RequestBody BlogPost blogPost) {
        if (blogPost.getTitle() == null || blogPost.getTitle().isEmpty() ||
            blogPost.getContent() == null || blogPost.getContent().isEmpty() ||
            blogPost.getAuthor() == null || blogPost.getAuthor().isEmpty()) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Required fields missing");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        BlogPost createdPost = blogPostService.createBlog(blogPost);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<BlogPost>> getAllBlogPostsEndpoint() {
        return ResponseEntity.ok(blogPostService.getAllBlogPostsEndpoint());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBlogPostByIdEndpoint(@PathVariable Long id) {
        return blogPostService.getBlogPostByIdEndpoint(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> {
                    Map<String, String> error = new HashMap<>();
                    error.put("message", "Blog post not found with id: " + id);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
                });
    }

    @GetMapping("/author/{authorId}")
    public ResponseEntity<List<BlogPost>> getPostsByAuthor(@PathVariable Long authorId) {
        return ResponseEntity.ok(blogPostService.getPostsByAuthorId(authorId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBlogPostEndpoint(@PathVariable Long id, @RequestBody BlogPost updatedBlog) {
        BlogPost result = blogPostService.updateBlogPostEndpoint(id, updatedBlog);
        if (result != null) {
            return ResponseEntity.ok(result);
        } else {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Blog post not found with id: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBlogPostEndpoint(@PathVariable Long id) {
        return blogPostService.deleteBlogPostEndpoint(id)
                .<ResponseEntity<?>>map(post -> {
                    Map<String, String> response = new HashMap<>();
                    response.put("message", "Blog post deleted successfully");
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    Map<String, String> error = new HashMap<>();
                    error.put("message", "Blog post not found with id: " + id);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
                });
    }
}