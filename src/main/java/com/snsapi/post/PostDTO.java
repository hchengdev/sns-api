package com.snsapi.post;

import com.snsapi.like.LikeDTO;
import com.snsapi.media.MediaDTO;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class PostDTO {
    private Integer id;
    private Integer userId;
    private String content;
    private Post.VisibilityEnum visibility;
    private List<MediaDTO> media;
    private List<LikeDTO> likes;
    private String createdAt;
    private String updatedAt;

    public PostDTO() {
    }

    public PostDTO(Integer id, Integer userId, String content, Post.VisibilityEnum visibility,
                   List<MediaDTO> media, List<LikeDTO> likes, String createdAt, String updatedAt) {
        this.id = id;
        this.userId = userId;
        this.content = content;
        this.visibility = visibility;
        this.media = media;
        this.likes = likes; // Sửa tên biến ở đây
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Post.VisibilityEnum getVisibility() {
        return visibility;
    }

    public void setVisibility(Post.VisibilityEnum visibility) {
        this.visibility = visibility;
    }

    public List<MediaDTO> getMedia() {
        return media;
    }

    public void setMedia(List<MediaDTO> media) {
        this.media = media;
    }

    public List<LikeDTO> getLikes() { // Sửa getter để trả về "likes"
        return likes;
    }

    public void setLikes(List<LikeDTO> likes) { // Sửa setter để nhận "likes"
        this.likes = likes;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
