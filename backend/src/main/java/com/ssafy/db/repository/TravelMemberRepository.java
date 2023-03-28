package com.ssafy.db.repository;

import com.ssafy.db.entity.TravelMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TravelMemberRepository extends JpaRepository<TravelMember, Integer> {
    Optional<TravelMember> findByType(String type);
}
