package com.snsapi.post;

import com.snsapi.like.LikeDTO;
import com.snsapi.media.MediaDTO;
import com.snsapi.user.User;
import com.snsapi.user.UserServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/posts")
@CrossOrigin(origins = "http://localhost:3002")
@RequiredArgsConstructor
public class RestPostController {

    private final PostService postService;
    private final UserServices userServices;

    @GetMapping
    public ResponseEntity<List<PostDTO>> findAllPosts() {
        List<Post> posts = postService.getAllPostsSortedByCreatedAt();

        List<PostDTO> postDTOs = posts.stream()
                .distinct()
                .map(post -> {
                    PostDTO postDTO = new PostDTO();
                    postDTO.setId(post.getId());
                    postDTO.setUserId(post.getUser().getId());
                    postDTO.setContent(post.getContent());
                    postDTO.setVisibility(post.getVisibility());
                    postDTO.setCreatedAt(post.getCreatedAt());
                    postDTO.setUpdatedAt(post.getUpdatedAt());
                    postDTO.setMedia(post.getMedia().stream().map(media -> {
                        MediaDTO mediaDTO = new MediaDTO();
                        mediaDTO.setId(media.getId());
                        mediaDTO.setUrl(media.getUrl());
                        return mediaDTO;
                    }).collect(Collectors.toList()));
                    postDTO.setLikes(post.getLikeUsers().stream().map(user -> {
                        LikeDTO likeDTO = new LikeDTO();
                        likeDTO.setId(user.getId());
                        likeDTO.setName(user.getName());
                        return likeDTO;
                    }).collect(Collectors.toList()));

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

        try {
            Post savedPost = postService.save(userId, content, visibility, file);

            PostDTO postDTO = new PostDTO();
            postDTO.setId(savedPost.getId());
            postDTO.setUserId(savedPost.getUser().getId());
            postDTO.setContent(savedPost.getContent());
            postDTO.setVisibility(savedPost.getVisibility());
            postDTO.setCreatedAt(savedPost.getCreatedAt());
            postDTO.setUpdatedAt(savedPost.getUpdatedAt());
            postDTO.setMedia(savedPost.getMedia().stream().map(media -> {
                MediaDTO mediaDTO = new MediaDTO();
                mediaDTO.setId(media.getId());
                mediaDTO.setUrl(media.getUrl());
                return mediaDTO;
            }).collect(Collectors.toList()));
            postDTO.setLikes(savedPost.getLikeUsers().stream().map(user -> {
                LikeDTO likeDTO = new LikeDTO();
                likeDTO.setId(user.getId());
                likeDTO.setName(user.getName());
                return likeDTO;
            }).collect(Collectors.toList()));

            return ResponseEntity.created(URI.create("/api/v1/posts/" + savedPost.getId())).body(postDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Đã xảy ra lỗi khi lưu bài post: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePost(@PathVariable("id") Integer postId,
                                        @RequestParam(value = "file", required = false) MultipartFile file,
                                        @RequestParam("content") String content,
                                        @RequestParam(value = "visibility", required = false) Post.VisibilityEnum visibility) {
        // Kiểm tra xem nội dung có hợp lệ không
        if (content == null || content.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Nội dung không được để trống.");
        }

        try {
            // Cập nhật bài viết với nội dung và có thể có ảnh
            Post updatedPost = postService.updatePost(postId, content, visibility, file);

            if (updatedPost != null) {
                PostDTO postDTO = new PostDTO();
                // Ánh xạ thuộc tính từ updatedPost
                postDTO.setId(updatedPost.getId());
                postDTO.setUserId(updatedPost.getUser().getId());
                postDTO.setContent(updatedPost.getContent());
                postDTO.setVisibility(updatedPost.getVisibility());
                postDTO.setCreatedAt(updatedPost.getCreatedAt());
                postDTO.setUpdatedAt(updatedPost.getUpdatedAt());
                postDTO.setMedia(updatedPost.getMedia().stream().map(media -> {
                    MediaDTO mediaDTO = new MediaDTO();
                    mediaDTO.setId(media.getId());
                    mediaDTO.setUrl(media.getUrl());
                    return mediaDTO;
                }).collect(Collectors.toList()));
                postDTO.setLikes(updatedPost.getLikeUsers().stream().map(user -> {
                    LikeDTO likeDTO = new LikeDTO();
                    likeDTO.setId(user.getId());
                    likeDTO.setName(user.getName());
                    return likeDTO;
                }).collect(Collectors.toList()));

                return ResponseEntity.ok(postDTO);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Đã xảy ra lỗi khi cập nhật bài post: " + e.getMessage());
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

    @PutMapping("/{id}/likes")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<Void> likePost(@PathVariable Integer id,
                                         Principal principal) {
        String email = principal.getName();
        Optional<User> user = userServices.findByEmail(email);
        postService.likePost(id, user.get().getId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/likes")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<Void> unlikePost(@PathVariable Integer id,
                                           Principal principal) {
        String email = principal.getName();
        Optional<User> user = userServices.findByEmail(email);
        postService.unlikePost(id, user.get().getId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/likes/count")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<Integer> countLikes(@PathVariable Integer id) {
        int likeCount = postService.countLikes(id);
        return ResponseEntity.ok(likeCount);
    }
}
