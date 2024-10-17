package com.snsapi.like;

import com.snsapi.user.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikeDTO {
    private Integer likeCount;
    private List<UserDTO> likeByUsers;
}
