package com.snsapi.like;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikeDTO {
    private Integer id;
    private String name;

    public LikeDTO(Integer id) {
        this.id = id;
    }
}
