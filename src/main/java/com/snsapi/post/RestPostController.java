package com.snsapi.post;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@CrossOrigin("*")
@RequiredArgsConstructor

public class RestPostController {
    private final PostService postService;

    @GetMapping
    public ResponseEntity<List<Post>> findAllPosts() {
        List<Post> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestParam("file") MultipartFile file,
                                  @RequestParam("content") String content,
                                  @RequestParam("userId") Integer userId,
                                  @RequestParam("visibility") Post.VisibilityEnum visibility) {
        PostRequest postRequest = new PostRequest(userId, content, visibility, file);
        postService.save(postRequest, file);
        return ResponseEntity.accepted().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePosts(@PathVariable("id") Integer postId,
                                         @RequestParam(value = "file", required = false) MultipartFile file,
                                         @RequestParam("content") String content,
                                         @RequestParam("visibility") Post.VisibilityEnum visibility) {
        PostRequest postRequest = new PostRequest(null, content, visibility, file);
        Post updatedPost = postService.updatePost(postId, postRequest, file);
        if (updatedPost != null) {
            return ResponseEntity.ok(updatedPost);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable("id") Integer postId) {
        Post deletedPost = postService.deletePost(postId);
        if (deletedPost != null) {
            return ResponseEntity.ok(deletedPost);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
