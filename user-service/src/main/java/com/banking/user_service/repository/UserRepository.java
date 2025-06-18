package com.banking.user_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banking.user_service.entity.User;



public interface UserRepository extends JpaRepository<User,Integer>{

	Optional<User> findByUserName(String userName);
    Optional<User> findById(Long id);
	User findByCif(String cif);

}
