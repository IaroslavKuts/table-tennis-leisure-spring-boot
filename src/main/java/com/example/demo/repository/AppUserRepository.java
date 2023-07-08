package com.example.demo.repository;

import com.example.demo.domain.entity.AppUser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.web.bind.annotation.CrossOrigin;


import java.util.List;
import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "users",
                        path="users")
//@CrossOrigin(origins = {"http://localhost:3000","http://localhost:3000/","http://localhost:8080"},allowCredentials = "true")
public interface AppUserRepository extends JpaRepository<AppUser,Long>, JpaSpecificationExecutor {

    @RestResource(path = "byEmail", rel = "byEmail")
    Optional<AppUser> findByEmail(@Param("email") String email);






}
