//
//package com.snsapi.post;
//
//import com.snsapi.like.LikeDTO;
//import com.snsapi.media.MediaDTO;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.net.URI;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//@RestController
//@RequestMapping("/api/v1/posts")
//@CrossOrigin(origins = "http://localhost:8081")
//@RequiredArgsConstructor
//public class RestPostController {
//
//    private final PostService postService;
//
//    @GetMapping
//    public ResponseEntity<List<PostDTO>> findAllPosts() {
//        List<Post> posts = postService.getAllPosts();
//
//        List<PostDTO> postDTOs = posts.stream()
//                .distinct()
//                .map(post -> {
//                    PostDTO postDTO = new PostDTO();
//                    postDTO.setId(post.getId());
//                    postDTO.setUserId(post.getUser().getId());  // Thêm trường userId
//                    postDTO.setContent(post.getContent());
//                    postDTO.setVisibility(post.getVisibility());
//                    postDTO.setCreatedAt(post.getCreatedAt());
//                    postDTO.setUpdatedAt(post.getUpdatedAt());
//                    postDTO.setMedia(post.getMedia().stream().map(media -> {
//                        MediaDTO mediaDTO = new MediaDTO();
//                        mediaDTO.setId(media.getId());
//                        mediaDTO.setUrl(media.getUrl());
//                        return mediaDTO;
//                    }).collect(Collectors.toList()));
//                    postDTO.setLike(post.getLikeUsers().stream().map(user -> {
//                        LikeDTO likeDTO = new LikeDTO();
//                        likeDTO.setId(user.getId());
//                        likeDTO.setFirstName(user.getFirstName());
//                        likeDTO.setLastName(user.getLastName());
//                        return likeDTO;
//                    }).collect(Collectors.toList()));
//
//                    return postDTO;
//                })
//                .collect(Collectors.toList());
//
//        return ResponseEntity.ok(postDTOs);
//    }
//
//    @PostMapping
//    public ResponseEntity<?> save(@RequestParam(value = "file", required = false) MultipartFile file,
//                                  @RequestParam("content") String content,
//                                  @RequestParam("userId") Integer userId,
//                                  @RequestParam("visibility") Post.VisibilityEnum visibility) {
//        if (file != null && file.isEmpty()) {
//            return ResponseEntity.badRequest().body("File không hợp lệ.");
//        }
//
//        if (content == null || content.trim().isEmpty()) {
//            return ResponseEntity.badRequest().body("Nội dung không được để trống.");
//        }
//
//        try {
//            Post savedPost = postService.save(userId, content, visibility, file);
//
//            PostDTO postDTO = new PostDTO();
//            postDTO.setId(savedPost.getId());
//            postDTO.setUserId(savedPost.getUser().getId());
//            postDTO.setContent(savedPost.getContent());
//            postDTO.setVisibility(savedPost.getVisibility());
//            postDTO.setCreatedAt(savedPost.getCreatedAt());
//            postDTO.setUpdatedAt(savedPost.getUpdatedAt());
//            postDTO.setMedia(savedPost.getMedia().stream().map(media -> {
//                MediaDTO mediaDTO = new MediaDTO();
//                mediaDTO.setId(media.getId());
//                mediaDTO.setUrl(media.getUrl());
//                return mediaDTO;
//            }).collect(Collectors.toList()));
//            postDTO.setLike(savedPost.getLikeUsers().stream().map(user -> {
//                LikeDTO likeDTO = new LikeDTO();
//                likeDTO.setId(user.getId());
//                likeDTO.setFirstName(user.getFirstName());
//                likeDTO.setLastName(user.getLastName());
//                return likeDTO;
//            }).collect(Collectors.toList()));
//
//            return ResponseEntity.created(URI.create("/api/v1/posts/" + savedPost.getId())).body(postDTO);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Đã xảy ra lỗi khi lưu bài post: " + e.getMessage());
//        }
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<?> updatePost(@PathVariable("id") Integer postId,
//                                        @RequestParam(value = "file", required = false) MultipartFile file,
//                                        @RequestParam("content") String content,
//                                        @RequestParam("visibility") Post.VisibilityEnum visibility) {
//        try {
//            Post updatedPost = postService.updatePost(postId, content, visibility, file);
//
//            if (updatedPost != null) {
//                PostDTO postDTO = new PostDTO();
//                postDTO.setId(updatedPost.getId());
//                postDTO.setUserId(updatedPost.getUser().getId());
//                postDTO.setContent(updatedPost.getContent());
//                postDTO.setVisibility(updatedPost.getVisibility());
//                postDTO.setCreatedAt(updatedPost.getCreatedAt());
//                postDTO.setUpdatedAt(updatedPost.getUpdatedAt());
//                postDTO.setMedia(updatedPost.getMedia().stream().map(media -> {
//                    MediaDTO mediaDTO = new MediaDTO();
//                    mediaDTO.setId(media.getId());
//                    mediaDTO.setUrl(media.getUrl());
//                    return mediaDTO;
//                }).collect(Collectors.toList()));
//                postDTO.setLike(updatedPost.getLikeUsers().stream().map(user -> {
//                    LikeDTO likeDTO = new LikeDTO();
//                    likeDTO.setId(user.getId());
//                    likeDTO.setFirstName(user.getFirstName());
//                    likeDTO.setLastName(user.getLastName());
//                    return likeDTO;
//                }).collect(Collectors.toList()));
//
//                return ResponseEntity.ok(postDTO);
//            } else {
//                return ResponseEntity.notFound().build();
//            }
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Đã xảy ra lỗi khi cập nhật bài post: " + e.getMessage());
//        }
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<?> deletePost(@PathVariable("id") Integer postId) {
//        try {
//            postService.deletePost(postId);
//            return ResponseEntity.ok().build();
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//    // GET: Tìm kiếm bài post gần đúng theo content
//    @GetMapping("/me/posts")
//    public ResponseEntity<Map<String, List<PostDTO>>> searchPosts(
//            @RequestParam("content") String content,
//            @RequestHeader("Authorization") String token) {
//
//        List<Post> posts = postService.searchPostByContent(content);
//
//        List<PostDTO> postDTOs = posts.stream()
//                .map(post -> {
//                    PostDTO postDTO = new PostDTO();
//                    postDTO.setId(post.getId());
//                    postDTO.setUserId(post.getUser().getId());
//                    postDTO.setContent(post.getContent());
//                    postDTO.setVisibility(post.getVisibility());
//                    postDTO.setCreatedAt(post.getCreatedAt());
//                    postDTO.setUpdatedAt(post.getUpdatedAt());
//
//                    List<MediaDTO> mediaDTOs = post.getMedia().stream()
//                            .map(media -> {
//                                MediaDTO mediaDTO = new MediaDTO();
//                                mediaDTO.setId(media.getId());
//                                mediaDTO.setPostId(post.getId());
//                                mediaDTO.setUrl(media.getUrl());
//                                return mediaDTO;
//                            })
//                            .collect(Collectors.toList());
//                    postDTO.setMedia(mediaDTOs);
//
//                    List<LikeDTO> likeDTOs = post.getLikeUsers().stream()
//                            .map(user -> {
//                                LikeDTO likeDTO = new LikeDTO();
//                                likeDTO.setId(user.getId());
//                                likeDTO.setFirstName(user.getFirstName());
//                                likeDTO.setLastName(user.getLastName());
//                                return likeDTO;
//                            })
//                            .collect(Collectors.toList());
//                    postDTO.setLike(likeDTOs);
//
//                    return postDTO;
//                })
//                .collect(Collectors.toList());
//
//        Map<String, List<PostDTO>> response = new HashMap<>();
//        response.put("posts", postDTOs);
//
//        return ResponseEntity.ok(response);
//    }
//}
