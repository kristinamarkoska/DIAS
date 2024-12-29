package com.example.project1.service;

import com.example.project1.model.CompanyIssuerModel;
import com.example.project1.repository.CompanyIssuerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyIssuerService {

    private final CompanyIssuerRepository companyIssuerRepository;

    public List<CompanyIssuerModel> findAll() {
        return companyIssuerRepository.findAllByOrderByIdAsc();
    }

    public CompanyIssuerModel findById(Long id) throws Exception {
        return companyIssuerRepository.findById(id).orElseThrow(Exception::new);
    }

}
