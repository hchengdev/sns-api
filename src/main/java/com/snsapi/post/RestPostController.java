package com.snsapi.post;

import com.snsapi.comment.CommentDTO;
import com.snsapi.like.LikeDTO;
import com.snsapi.media.MediaDTO;
import com.snsapi.user.User;
import com.snsapi.user.UserDTO;
import com.snsapi.user.UserServices;
import com.snsapi.utils.DateConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/posts")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class RestPostController {

    private final PostService postService;
    private final UserServices userServices;

    @GetMapping
    public ResponseEntity<List<PostDTO>> findAllPosts() {
        List<Post> posts = postService.getAllPosts();

        List<PostDTO> postDTOs = posts.stream()
                .map(post -> {
                    PostDTO postDTO = new PostDTO();
                    postDTO.setId(post.getId());
                    postDTO.setUserId(post.getUser() != null ? post.getUser().getId() : null);
                    postDTO.setContent(post.getContent());
                    postDTO.setVisibility(post.getVisibility());
                    postDTO.setCreatedAt(DateConverter.localDateTimeToDateWithSlash(post.getCreatedAt()));
                    postDTO.setUpdatedAt(DateConverter.localDateTimeToDateWithSlash(post.getUpdatedAt()));

                    List<MediaDTO> mediaDTOs = post.getMedia() != null ?
                            post.getMedia().stream()
                                    .map(media -> {
                                        MediaDTO mediaDTO = new MediaDTO();
                                        mediaDTO.setId(media.getId());
                                        mediaDTO.setUrl(media.getUrl());
                                        return mediaDTO;
                                    }).collect(Collectors.toList()) : new ArrayList<>();

                    postDTO.setMedia(mediaDTOs);

                    List<CommentDTO> commentDTOs = post.getComments() != null ?
                            post.getComments().stream()
                                    .map(comment -> postService.convertToCommentDTO(comment))
                                    .collect(Collectors.toList()) : new ArrayList<>();

                    postDTO.setComments(commentDTOs);

                    int likeCount = post.getLikeUsers() != null ? post.getLikeUsers().size() : 0;
                    List<UserDTO> likeByUsers = post.getLikeUsers() != null ?
                            post.getLikeUsers().stream()
                                    .map(user -> new UserDTO(user.getId(), user.getName(), user.getProfilePicture()))
                                    .collect(Collectors.toList()) : new ArrayList<>();

                    LikeDTO likeDTO = new LikeDTO(likeCount, likeByUsers);
                    postDTO.setLikes(likeDTO);

                    return postDTO;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(postDTOs);
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestParam(value = "file", required = false) MultipartFile[] files,
                                  @RequestParam("content") String content,
                                  @RequestParam("userId") Integer userId,
                                  @RequestParam("visibility") Post.VisibilityEnum visibility) {
        if (files != null && files.length == 0) {
            return ResponseEntity.badRequest().body("File không hợp lệ.");
        }

        if (content == null || content.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Nội dung không được để trống.");
        }

        try {
            Post savedPost = postService.save(userId, content, visibility, files);

            PostDTO postDTO = new PostDTO();
            postDTO.setId(savedPost.getId());
            postDTO.setUserId(savedPost.getUser().getId());
            postDTO.setContent(savedPost.getContent());
            postDTO.setVisibility(savedPost.getVisibility());
            postDTO.setCreatedAt(DateConverter.localDateTimeToDateWithSlash(savedPost.getCreatedAt()));
            postDTO.setUpdatedAt(DateConverter.localDateTimeToDateWithSlash(savedPost.getUpdatedAt()));
            postDTO.setMedia(savedPost.getMedia().stream().map(media -> {
                MediaDTO mediaDTO = new MediaDTO();
                mediaDTO.setId(media.getId());
                mediaDTO.setUrl(media.getUrl());
                return mediaDTO;
            }).collect(Collectors.toList()));

            int likeCount = savedPost.getLikeUsers().size();
            List<UserDTO> likeByUsers = savedPost.getLikeUsers().stream()
                    .map(user -> new UserDTO(user.getId(), user.getName(), user.getProfilePicture()))
                    .collect(Collectors.toList());

            LikeDTO likeDTO = new LikeDTO(likeCount, likeByUsers);
            postDTO.setLikes(likeDTO);

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
        if (content == null || content.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Nội dung không được để trống.");
        }

        try {
            Post updatedPost = postService.updatePost(postId, content, visibility, file);

            if (updatedPost != null) {
                PostDTO postDTO = new PostDTO();
                postDTO.setId(updatedPost.getId());
                postDTO.setUserId(updatedPost.getUser().getId());
                postDTO.setContent(updatedPost.getContent());
                postDTO.setVisibility(updatedPost.getVisibility());
                postDTO.setCreatedAt(DateConverter.localDateTimeToDateWithSlash(updatedPost.getCreatedAt()));
                postDTO.setUpdatedAt(DateConverter.localDateTimeToDateWithSlash(updatedPost.getUpdatedAt()));
                postDTO.setMedia(updatedPost.getMedia().stream().map(media -> {
                    MediaDTO mediaDTO = new MediaDTO();
                    mediaDTO.setId(media.getId());
                    mediaDTO.setUrl(media.getUrl());
                    return mediaDTO;
                }).collect(Collectors.toList()));

                int likeCount = updatedPost.getLikeUsers().size();
                List<UserDTO> likeByUsers = updatedPost.getLikeUsers().stream()
                        .map(user -> new UserDTO(user.getId(), user.getName(), user.getProfilePicture()))
                        .collect(Collectors.toList());

                LikeDTO likeDTO = new LikeDTO(likeCount, likeByUsers);
                postDTO.setLikes(likeDTO);

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
    public ResponseEntity<LikeDTO> toggleLikePost(@PathVariable Integer id, Principal principal) {
        String email = principal.getName();
        Optional<User> user = userServices.findByEmail(email);

        if (user.isPresent()) {
            postService.toggleLikePost(id, user.get().getId());

            int likeCount = postService.countLikes(id);
            List<User> likedUsers = postService.getUsersWhoLikedPost(id);

            List<UserDTO> userDTOs = likedUsers.stream()
                    .map(likedUser -> new UserDTO(likedUser.getId(), likedUser.getName(), likedUser.getProfilePicture()))
                    .collect(Collectors.toList());

            LikeDTO response = new LikeDTO(likeCount, userDTOs);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
