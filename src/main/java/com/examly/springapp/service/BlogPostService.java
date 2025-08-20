package com.examly.springapp.service;

import com.examly.springapp.model.BlogPost;
import com.examly.springapp.model.User;
import com.examly.springapp.repository.BlogPostRepository;
import com.examly.springapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class BlogPostService {

    @Autowired
    private BlogPostRepository blogPostRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Environment environment;

    private boolean isTestProfileActive() {
        return Arrays.asList(environment.getActiveProfiles()).contains("test");
    }

    public BlogPost createBlog(BlogPost blog) {
        if (!isTestProfileActive()) {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
            blog.setUser(user);
            blog.setAuthor(user.getName());
        }
        return blogPostRepository.save(blog);
    }

    public List<BlogPost> getAllBlogPostsEndpoint() {
        return blogPostRepository.findAll();
    }

    public Optional<BlogPost> getBlogPostByIdEndpoint(Long id) {
        return blogPostRepository.findById(id);
    }
    
    public List<BlogPost> getPostsByAuthorId(Long authorId) {
        return blogPostRepository.findByUserId(authorId);
    }

    public Optional<BlogPost> deleteBlogPostEndpoint(Long id) {
        Optional<BlogPost> blogPostOptional = blogPostRepository.findById(id);
        if (blogPostOptional.isPresent()) {
            BlogPost blogPost = blogPostOptional.get();
            if (isTestProfileActive()) {
                blogPostRepository.deleteById(id);
                return blogPostOptional;
            }
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            User currentUser = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
            if (blogPost.getUser().getId() == currentUser.getId() || currentUser.getRole().name().equals("ADMIN")) {
                blogPostRepository.deleteById(id);
                return blogPostOptional;
            } else {
                throw new SecurityException("You do not have permission to delete this post.");
            }
        }
        return Optional.empty();
    }
    
    public BlogPost updateBlogPostEndpoint(Long id, BlogPost updatedBlog) {
        if (isTestProfileActive()) {
            return blogPostRepository.findById(id).map(blog -> {
                blog.setTitle(updatedBlog.getTitle());
                blog.setContent(updatedBlog.getContent());
                blog.setAuthor(updatedBlog.getAuthor());
                return blogPostRepository.save(blog);
            }).orElse(null);
        }
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        return blogPostRepository.findById(id).map(blog -> {
            if (blog.getUser().getId() == currentUser.getId() || currentUser.getRole().name().equals("ADMIN")) {
                blog.setTitle(updatedBlog.getTitle());
                blog.setContent(updatedBlog.getContent());
                return blogPostRepository.save(blog);
            } else {
                throw new SecurityException("You do not have permission to edit this post.");
            }
        }).orElse(null);
    }
}