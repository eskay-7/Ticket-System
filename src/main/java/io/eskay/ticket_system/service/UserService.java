package io.eskay.ticket_system.service;

import io.eskay.ticket_system.dto.request.UpdateUserInfoRequest;
import io.eskay.ticket_system.dto.response.UserDto;
import jakarta.validation.Valid;

public interface UserService {
    UserDto getUserInfo();

    UserDto updateUserInfo(UpdateUserInfoRequest request);
}
