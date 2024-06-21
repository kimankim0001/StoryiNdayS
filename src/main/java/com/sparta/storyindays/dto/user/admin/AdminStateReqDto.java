package com.sparta.storyindays.dto.user.admin;

import com.sparta.storyindays.enums.user.State;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminStateReqDto {
    private State state;
}
