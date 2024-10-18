package com.snsapi.videoCall;

import com.snsapi.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VideoCallService {
    private final IVideoCallRepository videoCallRepository;

    public void videoCall (Integer userId, Integer friendId) {
        VideoCall videoCall = VideoCall.builder()
                .user(User.builder().id(userId).build())
                .friend(User.builder().id(friendId).build())
                .videoCallStatus(VideoCallStatus.PENDING)
                .build();
//        videoCallRepository.save(videoCall);
    }

    public void acceptVideoCall(Integer friendId, Integer userId) {
        VideoCall videoCall = videoCallRepository.findById(friendId)
                .orElseThrow(() -> new IllegalArgumentException("Video call không tồn tại!!"));
        videoCall.setVideoCallStatus(VideoCallStatus.ACCEPTED);
        videoCallRepository.save(videoCall);
    }


}
