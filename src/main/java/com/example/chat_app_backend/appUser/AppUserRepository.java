package com.example.chat_app_backend.appUser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

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


    @Query("select u from AppUser u where u.id <> ?1")
    List<AppUser> findAllOtherUsers(Long userId);

}
