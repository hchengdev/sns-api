package com.snsapi.media;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MediaDTO {
    private Integer id;
    private Integer postId;
    private String url;

    // Các phương thức getter và setter đã được tạo tự động nhờ vào @Data từ Lombok
}
