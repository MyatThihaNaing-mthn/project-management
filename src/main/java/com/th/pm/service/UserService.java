package com.th.pm.service;

import com.th.pm.dto.UserDto;
import com.th.pm.dto.UserRegister;
import com.th.pm.model.User;

public interface UserService {
    UserDto createUser(UserRegister userRegister);
    UserDto findUserByEmail(String email);
    User findUserByEmailForAuth(String email);
}
