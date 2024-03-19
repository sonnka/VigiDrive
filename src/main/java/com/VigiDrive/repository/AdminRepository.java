package com.VigiDrive.repository;

import com.VigiDrive.model.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    Optional<Admin> findByEmailIgnoreCase(String email);

    List<Admin> findAllByApproved(boolean isApproved);

    boolean existsByEmailIgnoreCase(String email);
}
