package com.example.project1.repository;

import com.example.project1.entity.AssetHistory;
import com.example.project1.entity.CompanyProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface AssetHistoryRepository extends JpaRepository<AssetHistory, Long> {
    Optional<AssetHistory> findByDateAndCompany(LocalDate date, CompanyProfile companyProfile);
}
