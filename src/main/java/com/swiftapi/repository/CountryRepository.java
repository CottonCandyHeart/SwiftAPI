package com.swiftapi.repository;

import com.swiftapi.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource
public interface CountryRepository extends JpaRepository<Country, String> {
    Optional<Country> findByISO2(String countryISO2);


}
