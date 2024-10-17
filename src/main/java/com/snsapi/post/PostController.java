package com.snsapi.post;

import com.snsapi.comment.CommentDTO;
import com.snsapi.like.LikeDTO;
import com.snsapi.media.MediaDTO;
import com.snsapi.user.UserDTO;
import com.snsapi.utils.DateConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/me")
@CrossOrigin("*")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/posts")
    public ResponseEntity<List<PostDTO>> searchPosts(@RequestParam String content) {
        List<Post> posts = postService.searchPostByContent(content);

        List<PostDTO> postDTOs = posts.stream().map(post -> {
            PostDTO postDTO = new PostDTO();
            postDTO.setId(post.getId());
            postDTO.setUserId(post.getUser().getId());
            postDTO.setContent(post.getContent());
            postDTO.setVisibility(post.getVisibility());
            postDTO.setCreatedAt(DateConverter.localDateTimeToDateWithSlash(post.getCreatedAt()));
            postDTO.setUpdatedAt(DateConverter.localDateTimeToDateWithSlash(post.getUpdatedAt()));

            List<MediaDTO> mediaDTOs = post.getMedia().stream().map(media -> {
                MediaDTO mediaDTO = new MediaDTO();
                mediaDTO.setId(media.getId());
                mediaDTO.setPostId(media.getPost().getId());
                mediaDTO.setUrl(media.getUrl());
                return mediaDTO;
            }).collect(Collectors.toList());

            postDTO.setMedia(mediaDTOs);

            List<UserDTO> likeUsers = post.getLikeUsers().stream()
                    .map(user -> {
                        UserDTO userDTO = new UserDTO();
                        userDTO.setId(user.getId());
                        userDTO.setName(user.getName());
                        return userDTO;
                    }).collect(Collectors.toList());

            List<CommentDTO> commentDTOs = post.getComments().stream().map(comment -> {
                CommentDTO commentDTO = new CommentDTO();
                commentDTO.setId(comment.getId());
                commentDTO.setUserId(comment.getUser().getId());
                commentDTO.setContent(comment.getContent());
                commentDTO.setCreatedAt(DateConverter.localDateTimeToDateWithSlash(comment.getCreatedAt()));
                return commentDTO;
            }).collect(Collectors.toList());

            postDTO.setComments(commentDTOs);

            LikeDTO likeDTO = new LikeDTO();
            likeDTO.setLikeCount(likeUsers.size());
            likeDTO.setLikeByUsers(likeUsers);
            postDTO.setLikes(likeDTO);

            return postDTO;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(postDTOs);
    }
}
