package com.ssafy.db.repository;

import com.ssafy.db.entity.ScheduleThumbnail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleThumbnailRepository extends JpaRepository<ScheduleThumbnail, Integer> {
    List<ScheduleThumbnail> findAll();
}
