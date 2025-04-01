package com.cenkc.digitalwallet.service;

import com.cenkc.digitalwallet.entity.User;
import com.cenkc.digitalwallet.entity.dto.UserRequestDTO;
import com.cenkc.digitalwallet.entity.dto.UserResponseDTO;

public interface UserService {

    User getCurrentUser();
    UserResponseDTO addUser(UserRequestDTO userRequestDTO);
//    User findByUsername(String username);
//    User findByEmail(String email);
//    User findByTckn(String tckn);
//    User save(User user);
//    void delete(Long id);
//    List<User> findAll();
//    List<User> findAllActive();
//    List<User> findAllDeleted();{
}
