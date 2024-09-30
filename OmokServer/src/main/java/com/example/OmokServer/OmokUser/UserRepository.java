package com.example.OmokServer.OmokUser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<OmokUser, Long> {
    OmokUser findByUsername(String username);
}
