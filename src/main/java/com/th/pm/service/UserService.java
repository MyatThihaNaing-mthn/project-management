package com.th.pm.service;

import com.th.pm.dto.UserDto;
import com.th.pm.dto.UserRegister;

public interface UserService {
    UserDto createUser(UserRegister userRegister);
}
