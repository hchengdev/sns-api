package com.snsapi.post;

import lombok.Data;
import java.util.Date;

@Data
public class PostDTO {
    private Integer id;
    private String content;
    private Post.VisibilityEnum visibility;
    private Date createdAt;
    private Date updatedAt;
}
