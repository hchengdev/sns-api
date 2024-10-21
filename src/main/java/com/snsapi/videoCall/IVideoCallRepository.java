package com.snsapi.videoCall;

import com.snsapi.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IVideoCallRepository extends JpaRepository<User, Integer> {

}
