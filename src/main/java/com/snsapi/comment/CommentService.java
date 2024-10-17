package com.snsapi.comment;

import com.snsapi.like.LikeDTO;
import com.snsapi.post.Post;
import com.snsapi.post.PostRepository;
import com.snsapi.user.User;
import com.snsapi.user.UserDTO;
import com.snsapi.user.UserRepository;
import com.snsapi.utils.DateConverter;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public List<CommentDTO> getAllComments() {
        return commentRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public CommentDTO saveComment(Integer userId, Integer postId, String content) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Người dùng không tồn tại."));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Bài viết không tồn tại."));

        Comment comment = new Comment();
        comment.setUser(user);
        comment.setPost(post);
        comment.setContent(content);

        return convertToDTO(commentRepository.save(comment));
    }

    public CommentDTO updateComment(Integer commentId, String content) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Bình luận không tồn tại."));

        comment.setContent(content);

        Comment updateComment = commentRepository.save(comment);


        return convertToDTO(updateComment);
    }

    public void deleteComment(Integer commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Bình luận không tồn tại."));
        commentRepository.delete(comment);
    }

    public CommentDTO saveReply(Integer userId, Integer postId, Integer commentId, String content) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Người dùng không tồn tại."));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Bài viết không tồn tại."));

        Comment parentComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Bình luận không tồn tại."));

        Comment reply = new Comment();
        reply.setUser(user);
        reply.setPost(post);
        reply.setContent(content);
        reply.setParentComment(parentComment);

        Comment savedReply = commentRepository.save(reply);

        parentComment.getReplies().add(savedReply);
        commentRepository.save(parentComment);

        return convertToDTO(savedReply);
    }

    public void toggleLikeComment(Integer commentId, Integer userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Bình luận không tồn tại."));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Người dùng không tồn tại."));

        if (comment.getLikeUsers().contains(user)) {
            comment.getLikeUsers().remove(user);
        } else {
            comment.getLikeUsers().add(user);
        }

        commentRepository.save(comment);
    }

    public int countLikes(Integer commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Bình luận không tồn tại."));
        return comment.getLikeUsers().size();
    }

    public List<User> getUsersWhoLikedComment(Integer commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Bình luận không tồn tại."));
        return new ArrayList<>(comment.getLikeUsers());
    }

    private CommentDTO convertToDTO(Comment comment) {
        CommentDTO dto = new CommentDTO();
        dto.setId(comment.getId());
        dto.setPostId(comment.getPost().getId());
        dto.setUserId(comment.getUser().getId());
        dto.setContent(comment.getContent());
        dto.setCreatedAt(DateConverter.localDateTimeToDateWithSlash(comment.getCreatedAt()));

        List<CommentDTO> repliesDTO = comment.getReplies() != null ?
                comment.getReplies().stream()
                        .map(this::convertToDTO)
                        .collect(Collectors.toList()) : new ArrayList<>();
        dto.setReplies(repliesDTO);

        LikeDTO likeDTO = new LikeDTO();
        likeDTO.setLikeCount(comment.getLikeUsers().size());
        likeDTO.setLikeByUsers(comment.getLikeUsers().stream()
                .map(user -> {
                    UserDTO userDTO = new UserDTO();
                    userDTO.setId(user.getId());
                    userDTO.setName(user.getName());
                    userDTO.setProfilePicture(user.getProfilePicture());
                    return userDTO;
                })
                .collect(Collectors.toList()));

        dto.setLikes(likeDTO);
        return dto;
    }
}
