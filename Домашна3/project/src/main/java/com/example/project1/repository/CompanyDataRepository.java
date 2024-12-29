package com.example.project1.repository;

import com.example.project1.model.CompanyIssuerModel;
import com.example.project1.model.CompanyDataModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyDataRepository extends JpaRepository<CompanyDataModel, Long> {
    Optional<CompanyDataModel> findByDateAndCompany(LocalDate date, CompanyIssuerModel company);
    List<CompanyDataModel> findByCompanyIdAndDateBetween(Long companyId, LocalDate from, LocalDate to);
    List<CompanyDataModel> findByCompanyId(Long companyId);
}
