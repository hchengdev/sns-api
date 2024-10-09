//package com.snsapi.post;
//
//import com.snsapi.comment.CommentRepository;
//import com.snsapi.media.Media;
//import com.snsapi.media.MediaRepository;
//import com.snsapi.user.User;
//import com.snsapi.user.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//@Transactional
//public class PostService {
//    private final PostRepository postRepository;
//    private final UserRepository userRepository;
//    private final MediaRepository mediaRepository;
//    private final CommentRepository commentRepository;
//
//    @Value("${file-upload}")
//    private String fileUpload;
//
//    public List<Post> getAllPosts() {
//        return postRepository.findAll();
//    }
//
//    public Post save(PostRequest postRequest, MultipartFile file) {
//        User user = userRepository.findById(postRequest.getUserId())
//                .orElseThrow(() -> new IllegalArgumentException("Người dùng không tồn tại."));
//        Post post = Post.builder()
//                .content(postRequest.getContent())
//                .visibility(postRequest.getVisibility())
//                .user(user)
//                .build();
//
//        postRepository.save(post);
//
//        if (postRequest.getFile() != null && !postRequest.getFile().isEmpty()) {
//            String imageFileName = saveFile(postRequest.getFile());
//
//            Media media = Media.builder()
//                    .fileName(imageFileName)
//                    .mediaType(postRequest.getFile().getContentType())
//                    .post(post)
//                    .build();
//
//            mediaRepository.save(media);
//            post.getMedia().add(media);
//        }
//        return post;
//    }
//
//    public void likePost(Long postId, Long userId) {
//        Post post = postRepository.findById(postId)
//                .orElseThrow(() -> new IllegalArgumentException("Post không tồn tại."));
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new IllegalArgumentException("Người dùng không tồn tại."));
//
//        post.getLikeUsers().add(user);
//        postRepository.save(post);
//    }
//
//    public void unlikePost(Long postId, Long userId) {
//        Post post = postRepository.findById(postId)
//                .orElseThrow(() -> new IllegalArgumentException("Post không tồn tại."));
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new IllegalArgumentException("Người dùng không tồn tại."));
//
//        post.getLikeUsers().remove(user);
//        postRepository.save(post);
//    }
//
//    private String saveFile(MultipartFile file) {
//        if (file.isEmpty()) {
//            return null;
//        }
//        try {
//            Path path = Paths.get(fileUpload + File.separator + file.getOriginalFilename());
//            Files.copy(file.getInputStream(), path);
//            return file.getOriginalFilename();
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    public Post updatePost(Long postId, PostRequest postRequest, MultipartFile file) {
//        Post post = postRepository.findById(postId)
//                .orElseThrow(() -> new IllegalArgumentException("Bài viết không tồn tại."));
//
//        post.setContent(postRequest.getContent());
//        post.setVisibility(postRequest.getVisibility());
//
//        if (postRequest.getFile() != null && !postRequest.getFile().isEmpty()) {
//            String imageFileName = saveFile(postRequest.getFile());
//            if (imageFileName != null) {
//                Media media = Media.builder()
//                        .fileName(imageFileName)
//                        .mediaType(postRequest.getFile().getContentType())
//                        .post(post)
//                        .build();
//                mediaRepository.save(media);
//                post.getMedia().add(media);
//            }
//        }
//        return postRepository.save(post);
//    }
//
//    public void deletePost(Long postId) {
//        Post post = postRepository.findById(postId)
//                .orElseThrow(() -> new IllegalArgumentException("Bài viết không tồn tại."));
//
//        postRepository.delete(post);
//    }
//}
