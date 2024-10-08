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

    @Value("${file-upload}")
    private String fileUpload;

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Post save(PostRequest postRequest, MultipartFile file) {
        Post post = new Post();
        post.setUser(userService.findById(postRequest.getUserId())); // Lấy người dùng từ service
        post.setContent(postRequest.getContent());
        post.setVisibility(postRequest.getVisibility());

        // Kiểm tra file và thêm vào media
        if (file != null && !file.isEmpty()) {
            Media media = new Media(); // Giả sử bạn có lớp Media
            // Xử lý file và thiết lập các thuộc tính cho media
            media.setFileName(file.getOriginalFilename()); // Ví dụ, nếu bạn có thuộc tính tên tệp
            // Thiết lập thêm các thuộc tính khác cho media nếu cần

            post.addMedia(media); // Thêm vào danh sách media
        }

        // Lưu bài viết vào cơ sở dữ liệu
        return postRepository.save(post);
    }


    public void likePost(Integer postId, Integer userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post không tồn tại."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Người dùng không tồn tại."));

        post.getLikeUsers().add(user);
        postRepository.save(post);
    }

    public void unlikePost(Integer postId, Integer userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post không tồn tại."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Người dùng không tồn tại."));

        post.getLikeUsers().remove(user);
        postRepository.save(post);
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

    public Post updatePost(Integer postId, PostRequest postRequest, MultipartFile file) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Bài viết không tồn tại."));

        post.setContent(postRequest.getContent());
        post.setVisibility(postRequest.getVisibility());

        if (postRequest.getFile() != null && !postRequest.getFile().isEmpty()) {
            String imageFileName = saveFile(postRequest.getFile());
            if (imageFileName != null) {
                Media media = Media.builder()
                        .fileName(imageFileName)
                        .mediaType(postRequest.getFile().getContentType())
                        .post(post)
                        .build();
                mediaRepository.save(media);
                post.getMedia().add(media);
            }
        }
        return postRepository.save(post);
    }

    public void deletePost(Integer postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Bài viết không tồn tại."));
        postRepository.delete(post);
    }
}
