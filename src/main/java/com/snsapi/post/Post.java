package com.snsapi.post;

import com.snsapi.comment.Comment;
import com.snsapi.media.Media;
import com.snsapi.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @NotBlank(message = "Nội dung không được để trống.")
    @Column(name = "content")
    private String content;

    @Column(name = "image")
    private String image;

    @Enumerated(EnumType.STRING)
    @Column(name = "visibility")
    private VisibilityEnum visibility;

    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    @Column(name = "created_at")
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    @Column(name = "updated_at")
    private Date updatedAt;

    @Size(max = 4, message = "Tối đa 4 ảnh.")
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "post")
    private List<Media> media = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "post_likes",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> likeUsers = new HashSet<>();

    public enum VisibilityEnum {
        PRIVATE,
        FRIENDS_ONLY,
        PUBLIC
    }

    public void addMedia(Media mediaItem) {
        if (mediaItem != null) {
            mediaItem.setPost(this);
            this.media.add(mediaItem);
        }
    }

    public void addComment(Comment comment) {
        if (comment != null) {
            comment.setPost(this);
            this.comments.add(comment);
        }
    }

    public void likePost(User user) {
        this.likeUsers.add(user);
    }

    public void unlikePost(User user) {
        this.likeUsers.remove(user);
    }
}
