package com.ssafy.db.repository;

import com.ssafy.db.entity.ScheduleItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleItemsRepository extends JpaRepository<ScheduleItems, Long> {
    Optional<List<ScheduleItems>> findAllByScheduleId(Long Id);
}
