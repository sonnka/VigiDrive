package com.VigiDrive.repository;

import com.VigiDrive.model.entity.Access;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccessRepository extends JpaRepository<Access, Long> {

    List<Access> findAllByDriverIdAndIsActive(Long driverId, Boolean isActive);

    List<Access> findAllByManagerIdAndIsActive(Long managerId, Boolean isActive);

    List<Access> findAllByManagerIdAndIsExpiring(Long managerId, Boolean isExpiring);
}
