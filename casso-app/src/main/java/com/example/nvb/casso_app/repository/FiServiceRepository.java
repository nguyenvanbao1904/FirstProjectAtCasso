package com.example.nvb.casso_app.repository;

import com.example.nvb.casso_app.entity.FiService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FiServiceRepository extends JpaRepository<FiService,String> {
    Optional<FiService> findById(String id);

    @Query("SELECT f FROM FiService f WHERE f.id NOT IN (SELECT t.fiService.id FROM Token t)")
    List<FiService> findAllNotLinked();

    @Query("SELECT f FROM FiService f WHERE f.id IN (SELECT t.fiService.id FROM Token t WHERE t.accessToken IS NOT NULL AND t.accessToken <> '')")
    List<FiService> findAllLinked();
}
