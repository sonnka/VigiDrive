package com.VigiDrive.repository;

import com.VigiDrive.model.entity.Manager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ManagerRepository extends JpaRepository<Manager, Long> {

    Optional<Manager> findByEmailIgnoreCase(String email);
}
