package com.snsapi.post;

import com.snsapi.like.LikeDTO;
import com.snsapi.media.MediaDTO;
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
            postDTO.setCreatedAt(post.getCreatedAt());
            postDTO.setUpdatedAt(post.getUpdatedAt());

            List<MediaDTO> mediaDTOs = post.getMedia().stream().map(media -> {
                MediaDTO mediaDTO = new MediaDTO();
                mediaDTO.setId(media.getId());
                mediaDTO.setPostId(media.getPost().getId());
                mediaDTO.setUrl(media.getUrl());
                return mediaDTO;
            }).collect(Collectors.toList());

            postDTO.setMedia(mediaDTOs);

            List<LikeDTO> likeDTOs = post.getLikeUsers().stream().map(like -> {
                LikeDTO likeDTO = new LikeDTO();
                likeDTO.setId(like.getId());
                likeDTO.setFirstName(like.getName());
                return likeDTO;
            }).collect(Collectors.toList());

            postDTO.setLikes(likeDTOs);

            return postDTO;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(postDTOs);
    }
}
