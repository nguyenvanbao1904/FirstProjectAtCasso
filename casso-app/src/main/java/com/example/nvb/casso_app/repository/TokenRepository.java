package com.example.nvb.casso_app.repository;

import com.example.nvb.casso_app.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, String> {
    Optional<Token> findByFiService_Id(String fiServiceId);
    void deleteByFiService_Id(String fiServiceId);
}
