package com.bridgelabz.userregistration.repository;

import com.bridgelabz.userregistration.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    @Query(value = "SELECT * FROM userregist.user_details Where email=:email", nativeQuery = true)
    Optional<User> findByEmailid(String email);

    Optional<User> findByToken(String token);
}
