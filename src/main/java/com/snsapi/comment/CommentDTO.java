package com.snsapi.comment;

import com.snsapi.like.LikeDTO;
import com.snsapi.user.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    private Integer id;
    private Integer postId;
    private Integer userId;
    private String content;
    private String createdAt;
    private List<CommentDTO> replies;
    private LikeDTO likes;
    private UserDTO createdBy;
}