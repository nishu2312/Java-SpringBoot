package com.taskmanager.repo;


import org.springframework.data.jpa.repository.JpaRepository;

import com.taskmanager.model.User;

public interface UserRepository extends JpaRepository<User, Long>
{

}
