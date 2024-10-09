package com.snsapi.media;

public class MediaDTO {
    private Integer id;
    private Integer postId;
    private String url;

    public MediaDTO() {
    }

    public MediaDTO(Integer id, Integer postId, String url) {
        this.id = id;
        this.postId = postId;
        this.url = url;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
