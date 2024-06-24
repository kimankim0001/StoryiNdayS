package com.sparta.storyindays.service;

import com.sparta.storyindays.dto.user.ProfileUpdateResDto;
import com.sparta.storyindays.entity.Follow;
import com.sparta.storyindays.entity.User;
import com.sparta.storyindays.exception.BusinessLogicException;
import com.sparta.storyindays.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final UserService userService;

    @Transactional
    public ProfileUpdateResDto followUser(long followeeId, User user) {

        User followeeUser = userService.findById(followeeId);

        Follow newFollow = new Follow(true, user.getUsername(), followeeUser);

        if (!isAleadyFollow(followeeId, user)) {
            followRepository.save(newFollow);
        }

        return new ProfileUpdateResDto(followeeUser);
    }

    @Transactional
    public ProfileUpdateResDto followCancle(long followeeId, User user) {

        User followeeUser = userService.findById(followeeId);
        Follow cancleFollow = followRepository.findByFollowUserIdAndFolloweeUser(user.getUsername(), followeeUser).orElseThrow(() ->
                new BusinessLogicException("찾을수 없는 Follow ID 입니다"));

        if (cancleFollow.isFollow()) {
            cancleFollow.changeFollow(false);
        } else {
            throw new BusinessLogicException("팔로우 되어있지않은 유저입니다!");
        }

        return new ProfileUpdateResDto(cancleFollow.getFolloweeUser());
    }

    public List<Follow> getFolloweeList(String userName) {
        return followRepository.findAllByFollowUserId(userName);
    }

    public boolean isAleadyFollow(long followeeId, User user) {
        User followeeUser = userService.findById(followeeId);
        Optional<Follow> curFollow = followRepository.findByFollowUserIdAndFolloweeUser(user.getUsername(), followeeUser);
        if (curFollow.isPresent()) {
            if (curFollow.get().isFollow()) {
                throw new BusinessLogicException("이미 팔로우 하고있는 유저입니다!");
            } else {
                curFollow.get().changeFollow(true);
                return true;
            }
        }
        return false;
    }
}
