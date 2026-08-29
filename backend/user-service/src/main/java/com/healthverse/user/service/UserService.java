package com.healthverse.user.service;

import com.healthverse.user.dto.UpdateUserProfileRequest;
import com.healthverse.user.dto.UserProfileDto;

public interface UserService {

    UserProfileDto getUserProfile(String email);

    UserProfileDto updateUserProfile(String email, UpdateUserProfileRequest request);
}
