package com.levi.restboot.repository;

import com.levi.restboot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by a.petrovych on 15-Mar-16.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where u.name = :name")
    User findUserByName(@Param("name") String name);
}
