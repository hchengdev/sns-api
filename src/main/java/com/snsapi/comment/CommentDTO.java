package com.snsapi.comment;

import com.snsapi.like.LikeDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    private Integer id;
    private Integer userId;
    private Integer postId;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<LikeDTO> likes;
}