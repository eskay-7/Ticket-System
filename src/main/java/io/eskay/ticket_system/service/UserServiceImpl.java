package io.eskay.ticket_system.service;

import io.eskay.ticket_system.dto.request.UpdateUserInfoRequest;
import io.eskay.ticket_system.dto.response.UserDto;
import io.eskay.ticket_system.mapper.UserDtoMapper;
import io.eskay.ticket_system.repository.UserRepository;
import io.eskay.ticket_system.util.AuthenticatedUser;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserDtoMapper userDtoMapper;
    private final UserRepository userRepository;

    public UserServiceImpl(
            UserDtoMapper userDtoMapper,
            UserRepository userRepository
    ) {
        this.userDtoMapper = userDtoMapper;
        this.userRepository = userRepository;
    }

    @Override
    public UserDto getUserInfo() {
        return userDtoMapper.apply(AuthenticatedUser.get());
    }

    @Override
    public UserDto updateUserInfo(UpdateUserInfoRequest request) {
        var user = AuthenticatedUser.get();
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        return userDtoMapper.apply(userRepository.save(user));
    }
}
