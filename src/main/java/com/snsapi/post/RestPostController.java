package com.snsapi.post;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/posts")
@CrossOrigin("*")
@RequiredArgsConstructor

public class RestPostController {
    private final PostService postService;

    @GetMapping
    public ResponseEntity<List<PostDTO>> findAllPosts() {
        List<Post> posts = postService.getAllPosts();

        List<PostDTO> postDTOs = posts.stream()
                .distinct()
                .map(post -> {
                    PostDTO postDTO = new PostDTO();
                    postDTO.setId(post.getId());
                    postDTO.setContent(post.getContent());
                    postDTO.setVisibility(post.getVisibility());
                    postDTO.setCreatedAt(post.getCreatedAt());
                    postDTO.setUpdatedAt(post.getUpdatedAt());

                    return postDTO;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(postDTOs);
    }


    @PostMapping
    public ResponseEntity<?> save(@RequestParam(value = "file", required = false) MultipartFile file,
                                  @RequestParam("content") String content,
                                  @RequestParam("userId") Integer userId,
                                  @RequestParam("visibility") Post.VisibilityEnum visibility) {
        if (file != null && file.isEmpty()) {
            return ResponseEntity.badRequest().body("File không hợp lệ.");
        }

        if (content == null || content.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Nội dung không được để trống.");
        }

        PostRequest postRequest = new PostRequest(userId, content, visibility, file);

        try {
            Post savedPost = postService.save(postRequest, file);

            PostDTO postDTO = new PostDTO();
            postDTO.setId(savedPost.getId());
            postDTO.setContent(savedPost.getContent());
            postDTO.setVisibility(savedPost.getVisibility());
            postDTO.setCreatedAt(savedPost.getCreatedAt());
            postDTO.setUpdatedAt(savedPost.getUpdatedAt());

            return ResponseEntity.created(URI.create("/api/v1/posts/" + savedPost.getId())).body(postDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Đã xảy ra lỗi khi lưu bài post: " + e.getMessage());
        }
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
        try {
            postService.deletePost(postId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
