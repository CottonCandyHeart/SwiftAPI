package com.swiftapi.repository;

import com.swiftapi.model.Bank;
import com.swiftapi.model.SwiftCodeRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource
public interface BankRepository extends JpaRepository<Bank, String> {
    Optional<Bank> findBySWIFTCode(String swiftCode);
    List<Bank> findBySWIFTCodeStartingWithAndIsHeadquarterFalse(String swiftCode);
    List<Bank> findByCountryISO2(String ISO2Code);

}
