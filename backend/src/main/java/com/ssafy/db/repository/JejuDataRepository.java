package com.ssafy.db.repository;

import com.ssafy.db.entity.JejuData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JejuDataRepository extends JpaRepository<JejuData, Long> {
    Optional<JejuData> findById(Long id);
}
