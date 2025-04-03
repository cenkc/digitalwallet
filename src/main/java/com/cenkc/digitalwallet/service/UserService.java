package com.cenkc.digitalwallet.service;

import com.cenkc.digitalwallet.entity.User;
import com.cenkc.digitalwallet.entity.dto.UserRequestDTO;
import com.cenkc.digitalwallet.entity.dto.UserResponseDTO;

public interface UserService {

    User getCurrentUser();
    UserResponseDTO addUser(UserRequestDTO userRequestDTO);
}
