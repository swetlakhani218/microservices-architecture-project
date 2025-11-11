package com.project.user.service.Service;

import com.project.user.service.Entity.User;

import java.util.List;

public interface UserService {
    //user operations

    // create
    User createUser(User user);

    // get all user
    List<User> getAllUser();

    // get user by id
    User getUserById(String userId);

    // delete user
    void deleteUserById(String userId);

    // update user
    void updateUser(User user);


}
