package com.snsapi.comment;

import com.snsapi.like.LikeDTO;
import com.snsapi.post.PostRepository;
import com.snsapi.user.User;
import com.snsapi.user.UserDTO;
import com.snsapi.user.UserServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
public class RestCommentController {
    private final CommentService commentService;
    private final PostRepository postRepository;
    private final UserServices userServices;

    @GetMapping
    public ResponseEntity<List<CommentDTO>> getAllComments() {
        List<CommentDTO> comments = commentService.getAllComments();
        return ResponseEntity.ok(comments);
    }

    @PostMapping
    public ResponseEntity<CommentDTO> createComment(@RequestBody CommentDTO commentDTO) {
        if (commentDTO.getContent() == null || commentDTO.getContent().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        CommentDTO savedComment = commentService.saveComment(commentDTO.getUserId(), commentDTO.getPostId(), commentDTO.getContent());

        CommentDTO responseComment = new CommentDTO();
        responseComment.setId(savedComment.getId());
        responseComment.setUserId(savedComment.getUserId());
        responseComment.setPostId(savedComment.getPostId());
        responseComment.setContent(savedComment.getContent());
        responseComment.setCreatedAt(savedComment.getCreatedAt());
        responseComment.setReplies(savedComment.getReplies());
        responseComment.setLikes(savedComment.getLikes());

        return ResponseEntity.status(HttpStatus.CREATED).body(responseComment);
    }

    @PostMapping("/{commentId}/replies")
    public ResponseEntity<CommentDTO> createReply(@PathVariable Integer commentId, @RequestBody CommentDTO replyDTO) {
        if (replyDTO.getContent() == null || replyDTO.getContent().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        CommentDTO savedReply = commentService.saveReply(replyDTO.getUserId(), replyDTO.getPostId(), commentId, replyDTO.getContent());

        return ResponseEntity.status(HttpStatus.CREATED).body(savedReply);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentDTO> updateComment(@PathVariable Integer commentId,
                                                    @RequestBody CommentDTO commentDTO) {
        CommentDTO updatedComment = commentService.updateComment(commentId, commentDTO.getContent());
        return ResponseEntity.ok(updatedComment);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Integer commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{commentId}/likes")
    public ResponseEntity<LikeDTO> toggleLikeComment(@PathVariable Integer commentId, Principal principal) {
        String email = principal.getName();
        Optional<User> user = userServices.findByEmail(email);

        if (user.isPresent()) {
            commentService.toggleLikeComment(commentId, user.get().getId());

            int likeCount = commentService.countLikes(commentId);
            List<User> likedUsers = commentService.getUsersWhoLikedComment(commentId);

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
