package com.snsapi.comment;

import com.snsapi.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @NotEmpty
    @NotBlank
    private String content;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

//    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    @JoinColumn(name = "post_id")
//    private Post post;

//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    private List<User> likes;

//    public void likeComment (User user) {
//        if(!likes.contains(user)) {
//            likes.add(user);
//        }
//    }

//    public void unLikeComment (User user) {
//        likes.remove(user);
//    }
}
