package com.ssafy.db.repository;

import com.ssafy.db.entity.SurveyFavorCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SurveyFavorCategoryRepository extends JpaRepository<SurveyFavorCategory, Integer> {

}
