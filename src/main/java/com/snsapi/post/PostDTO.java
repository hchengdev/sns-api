package com.snsapi.post;

import com.snsapi.like.LikeDTO;
import com.snsapi.media.MediaDTO;

import java.util.Date;
import java.util.List;

public class PostDTO {
    private Integer id;
    private Integer userId;
    private String content;
    private Post.VisibilityEnum visibility;
    private String image;
    private List<MediaDTO> media;
    private List<LikeDTO> like;
    private Date createdAt;
    private Date updatedAt;

    public PostDTO() {
    }

    public PostDTO(Integer id, Integer userId, String content, Post.VisibilityEnum visibility, String image, List<MediaDTO> media, List<LikeDTO> like, Date createdAt, Date updatedAt) {
        this.id = id;
        this.userId = userId;
        this.content = content;
        this.visibility = visibility;
        this.image = image;
        this.media = media;
        this.like = like;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<MediaDTO> getMedia() {
        return media;
    }

    public void setMedia(List<MediaDTO> media) {
        this.media = media;
    }

    public List<LikeDTO> getLike() {
        return like;
    }

    public void setLike(List<LikeDTO> like) {
        this.like = like;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
