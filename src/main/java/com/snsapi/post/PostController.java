package com.snsapi.post;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/me")
@CrossOrigin("*")
@RequiredArgsConstructor
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping("/posts")
    public ResponseEntity<List<Post>> searchPosts(@RequestParam String content) {
        List<Post> posts = postService.searchPostByContent(content);
        return ResponseEntity.ok(posts);
    }
}
