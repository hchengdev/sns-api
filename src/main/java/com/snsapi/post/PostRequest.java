package com.snsapi.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostRequest {
    private Integer userId;
    private String content;
    private Post.VisibilityEnum visibility;
}
