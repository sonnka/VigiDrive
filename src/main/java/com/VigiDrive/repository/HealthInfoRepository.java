package com.VigiDrive.repository;

import com.VigiDrive.model.entity.HealthInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HealthInfoRepository extends JpaRepository<HealthInfo, Long> {
}
