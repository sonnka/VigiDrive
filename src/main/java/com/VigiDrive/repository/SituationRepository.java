package com.VigiDrive.repository;

import com.VigiDrive.model.entity.Driver;
import com.VigiDrive.model.entity.Situation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SituationRepository extends JpaRepository<Situation, Long> {

    List<Situation> findAllByDriver(Driver driver);

    List<Situation> findAllByDriverAndStartGreaterThanOrderByStartAsc(Driver driver, LocalDateTime startOfWeek);
}