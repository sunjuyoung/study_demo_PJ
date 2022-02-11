package com.project.study.repository;

import com.project.study.domain.Zone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ZoneRepository extends JpaRepository<Zone,Long>{
    Optional<Zone> findByCity(String cityName);
}
