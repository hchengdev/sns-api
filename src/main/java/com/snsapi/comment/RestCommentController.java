//package com.snsapi.comment;
//
//import com.snsapi.post.Post;
//import com.snsapi.post.PostRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/v1/posts/{postId}/comments")
//@CrossOrigin("*")
//@RequiredArgsConstructor
//public class RestCommentController {
//
//    private final CommentService commentService;
//    private final PostRepository postRepository;
//
//    @GetMapping
//    @PreAuthorize("hasAuthority('ROLE_USER')")
//    public ResponseEntity<List<Comment>> getAllCommentsByPostId(@PathVariable Integer postId) {
//        List<Comment> comments = commentService.getAllCommentsByPostId(postId);
//        return ResponseEntity.ok(comments);
//    }
//
//    @PostMapping
//    @PreAuthorize("hasAuthority('ROLE_USER')")
//    public ResponseEntity<Comment> createComment(@PathVariable Integer postId,
//                                                 @RequestParam Integer userId,
//                                                 @RequestBody CommentRequest commentRequest) {
//        Post post = postRepository.findById(postId)
//                .orElseThrow(() -> new IllegalArgumentException("Bài viết không tồn tại."));
//
//        if (post.getVisibility() == Post.VisibilityEnum.PRIVATE) {
//            return ResponseEntity.status(403).body(null);
//        }
//        Comment comment = commentService.createComment(postId, userId, commentRequest.getContent());
//        return ResponseEntity.ok(comment);
//    }
//
//    @PutMapping("/{commentId}")
//    @PreAuthorize("hasAuthority('ROLE_USER')") // Kiểm tra quyền truy cập
//    public ResponseEntity<Comment> updateComment(@PathVariable Integer postId,
//                                                 @PathVariable Integer commentId,
//                                                 @RequestBody CommentRequest commentRequest) {
//        Comment updatedComment = commentService.updateComment(commentId, commentRequest.getContent());
//        return ResponseEntity.ok(updatedComment);
//    }
//}
