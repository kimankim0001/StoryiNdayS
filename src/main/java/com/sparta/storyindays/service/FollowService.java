package com.sparta.storyindays.service;

import com.sparta.storyindays.dto.user.ProfileUpdateResDto;
import com.sparta.storyindays.entity.Follow;
import com.sparta.storyindays.entity.User;
import com.sparta.storyindays.exception.BusinessLogicException;
import com.sparta.storyindays.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final UserService userService;
    private final MessageSource messageSource;

    @Transactional
    public ProfileUpdateResDto followUser(long followeeId, User user) {


        User followeeUser = userService.findById(followeeId);
        if(followeeUser.getUsername().equals(user.getUsername())) {
            throw new BusinessLogicException(messageSource.getMessage(
                    "no.follow.me",
                    null,
                    "Wrong Request",
                    Locale.getDefault()
            ));
        }

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
        new BusinessLogicException(messageSource.getMessage(
                "no.exist.follow",
                null,
                "Wrong Request",
                Locale.getDefault()
        )));

        if (cancleFollow.isFollow()) {
            cancleFollow.changeFollow(false);
        } else {
            throw new BusinessLogicException(messageSource.getMessage(
                    "no.follow.user",
                    null,
                    "Wrong Request",
                    Locale.getDefault()
            ));
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
                throw new BusinessLogicException(messageSource.getMessage(
                        "already.follow.user",
                        null,
                        "Wrong Request",
                        Locale.getDefault()
                ));
            } else {
                curFollow.get().changeFollow(true);
                return true;
            }
        }
        return false;
    }
}
