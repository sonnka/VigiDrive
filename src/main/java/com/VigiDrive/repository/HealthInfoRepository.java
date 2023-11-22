package com.VigiDrive.repository;

import com.VigiDrive.model.entity.Driver;
import com.VigiDrive.model.entity.HealthInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HealthInfoRepository extends JpaRepository<HealthInfo, Long> {

    Optional<HealthInfo> findFirstByDriverOrderByTimeDesc(Driver driver);
}
