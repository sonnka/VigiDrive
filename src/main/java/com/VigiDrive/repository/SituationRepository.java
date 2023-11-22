package com.VigiDrive.repository;

import com.VigiDrive.model.entity.Driver;
import com.VigiDrive.model.entity.Situation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SituationRepository extends JpaRepository<Situation, Long> {

    List<Situation> findAllByDriver(Driver driver);

    Optional<Situation> findByDriver(Driver driver);
}