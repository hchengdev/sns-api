
package com.snsapi.post;

import com.snsapi.comment.CommentRepository;
import com.snsapi.media.Media;
import com.snsapi.media.MediaRepository;
import com.snsapi.user.User;
import com.snsapi.user.UserRepository;
import com.snsapi.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final MediaRepository mediaRepository;
    private final CommentRepository commentRepository;
    private final UserService userService;

    @Value("${upload.image}")
    private String fileUpload;

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Post save(Integer userId, String content, Post.VisibilityEnum visibility, MultipartFile file) {
        Post post = new Post();
        post.setUser(userService.findById(userId));
        post.setContent(content);
        post.setVisibility(visibility);

        if (file != null && !file.isEmpty()) {
            String savedFileName = saveFile(file);
            if (savedFileName != null) {
                Media media = new Media();
                media.setFileName(savedFileName);
                media.setMediaType(file.getContentType());
                post.addMedia(media);
            } else {
                throw new RuntimeException("File upload failed");
            }
        }

        return postRepository.save(post);
    }

    public Post updatePost(Integer postId, String content, Post.VisibilityEnum visibility, MultipartFile file) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Bài viết không tồn tại."));

        post.setContent(content);
        post.setVisibility(visibility);

        if (file != null && !file.isEmpty()) {
            Media media = new Media();
            media.setFileName(saveFile(file));
            media.setMediaType(file.getContentType());
            media.setPost(post);
            mediaRepository.save(media);
            post.addMedia(media);
        }

        return postRepository.save(post);
    }

    public void deletePost(Integer postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Bài viết không tồn tại."));
        postRepository.delete(post);
    }

    private String saveFile(MultipartFile file) {
        if (file.isEmpty()) {
            return null;
        }
        try {
            Path path = Paths.get(fileUpload + File.separator + file.getOriginalFilename());
            Files.copy(file.getInputStream(), path);
            return file.getOriginalFilename();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Post> searchPostByContent(String content) {
        return postRepository.findByContent(content);
    }

    public List<Post> findAllPostsByUserId(Integer userId) {
        return postRepository.findByUserId(userId);
    }
}
