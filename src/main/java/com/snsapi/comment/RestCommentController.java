package com.snsapi.comment;

import com.snsapi.like.LikeDTO;
import com.snsapi.post.Post;
import com.snsapi.post.PostRepository;
import com.snsapi.user.User;
import com.snsapi.user.UserServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/posts/{postId}/comments")
@CrossOrigin("*")
@RequiredArgsConstructor
public class RestCommentController {

    private final CommentService commentService;
    private final PostRepository postRepository;
    private final UserServices userServices;

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<CommentResponse> getAllCommentsByPostId(@PathVariable Integer postId) {
        List<Comment> comments = commentService.getAllCommentsByPostId(postId);

        List<CommentDTO> commentDTOs = comments.stream()
                .map(comment -> new CommentDTO(
                        comment.getId(),
                        comment.getUser().getId(),
                        comment.getPost().getId(),
                        comment.getContent(),
                        comment.getCreatedAt(),
                        comment.getUpdatedAt(),
                        comment.getLikeUsers().stream()
                                .map(user -> new LikeDTO(
                                        user.getId(),
                                        user.getName()
                                )).collect(Collectors.toList())
                ))
                .collect(Collectors.toList());

        CommentResponse response = new CommentResponse(commentDTOs);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<CommentDTO> createComment(@PathVariable Integer postId,
                                                    @RequestParam Integer userId,
                                                    @RequestBody CommentRequest commentRequest) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Bài viết không tồn tại."));

        if (post.getVisibility() == Post.VisibilityEnum.PRIVATE) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        Comment comment = commentService.createComment(postId, userId, commentRequest.getContent());

        CommentDTO commentDTO = new CommentDTO(
                comment.getId(),
                userId,
                postId,
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getUpdatedAt(),
                null
        );

        return ResponseEntity.ok(commentDTO);
    }

    @PutMapping("/{commentId}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<CommentDTO> updateComment(
            @PathVariable("commentId") Integer commentId,
            @RequestBody CommentRequest commentRequest) {
        Comment updatedComment = commentService.updateComment(commentId, commentRequest.getContent());

        CommentDTO commentDTO = new CommentDTO(
                updatedComment.getId(),
                updatedComment.getUser().getId(),
                updatedComment.getPost().getId(),
                updatedComment.getContent(),
                updatedComment.getCreatedAt(),
                updatedComment.getUpdatedAt(),
                updatedComment.getLikeUsers().stream()
                        .map(user -> new LikeDTO(user.getId()))
                        .collect(Collectors.toList())
        );
        return ResponseEntity.ok(commentDTO);
    }

    @DeleteMapping("/{commentId}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<Void> deleteComment(@PathVariable Integer commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{commentId}/likes")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<Void> likeComment(@PathVariable Integer commentId,
                                            Principal principal) {
        String email = principal.getName();
        Optional<User> user = userServices.findByEmail(email);
        commentService.likeComment(commentId, user.get().getId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{commentId}/likes")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<Void> unlikeComment(@PathVariable Integer commentId,
                                              Principal principal) {
        String email = principal.getName();
        Optional<User> user = userServices.findByEmail(email);
        commentService.unlikeComment(commentId, user.get().getId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{commentId}/likes/count")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<Integer> countLikes(@PathVariable Integer commentId) {
        int likeCount = commentService.countLikes(commentId);
        return ResponseEntity.ok(likeCount);
    }
}
