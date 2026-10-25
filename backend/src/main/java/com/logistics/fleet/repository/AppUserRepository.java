package com.logistics.fleet.repository;

import com.logistics.fleet.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("SELECT COALESCE(MAX(u.userId), 0) FROM AppUser u")
    Long findMaxUserId();
}
