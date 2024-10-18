package com.snsapi.comment;

import com.snsapi.post.Post;
import com.snsapi.post.PostRepository;
import com.snsapi.user.User;
import com.snsapi.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    public List<Comment> getAllCommentsByPostId(Integer postId) {
        return commentRepository.findByPostId(postId);
    }

    public Comment createComment(Integer postId, Integer userId, String content) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Bài viết không tồn tại."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Người dùng không tồn tại."));

        Comment comment = Comment.builder()
                .post(post)
                .user(user)
                .content(content)
                .build();

        return commentRepository.save(comment);
    }

    public Comment updateComment(Integer commentId, String content) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Bình luận không tồn tại!!"));
        comment.setContent(content);
        return commentRepository.save(comment);
    }

    public void deleteComment(Integer commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment không tồn tại."));
        commentRepository.delete(comment);
    }

    public void likeComment(Integer commentId, Integer userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment không tồn tại."));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Người dùng không tồn tại."));

        if (comment.getLikeUsers().contains(user)) {
            throw new IllegalArgumentException("Bạn đã thích bình luận này rồi.");
        }
        comment.getLikeUsers().add(user);
        commentRepository.save(comment);
    }

    public void unlikeComment(Integer commentId, Integer userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment không tồn tại."));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Người dùng không tồn tại."));

        if (!comment.getLikeUsers().contains(user)) {
            throw new IllegalArgumentException("Bạn chưa thích bình luận này.");
        }
        comment.getLikeUsers().remove(user);
        commentRepository.save(comment);
    }

    public int countLikes(Integer commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Bài viết không tồn tại!!"));
        return comment.getLikeUsers().size();
    }
}
