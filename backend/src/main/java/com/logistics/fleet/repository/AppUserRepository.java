package com.logistics.fleet.repository;

import com.logistics.fleet.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByEmail(String email);

    @Query(
        value = "SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END " +
                "FROM APP_USERS WHERE LOWER(EMAIL) = LOWER(:email)",
        nativeQuery = true
    )
    int countByEmail(@org.springframework.data.repository.query.Param("email") String email);

    default boolean existsByEmail(String email) {
        return countByEmail(email) > 0;
    }

    @Query("SELECT COALESCE(MAX(u.userId), 0) FROM AppUser u")
    Long findMaxUserId();
}
