package com.example.chat_app_backend.appUser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    @Query("SELECT" +
            " case when count(u)>0" +
            " then true else false end " +
            "FROM AppUser u " +
            "where u.email = ?1")
    boolean findUserByEmail(String email);

    @Query("select u from AppUser u where u.email =?1")
    AppUser findAppUserByEmail(String email);

}
