package com.swiftapi.repository;

import com.swiftapi.model.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface BankRepository extends JpaRepository<Bank, String> {
}
