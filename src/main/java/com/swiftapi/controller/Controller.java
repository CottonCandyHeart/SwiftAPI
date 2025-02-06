package com.swiftapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.swiftapi.exception.InvalidISO2Exception;
import com.swiftapi.exception.InvalidSwiftCodeException;
import com.swiftapi.model.Bank;
import com.swiftapi.model.Country;
import com.swiftapi.model.CountryISO2Request;
import com.swiftapi.model.SwiftCodeRequest;
import com.swiftapi.repository.BankRepository;
import com.swiftapi.repository.CountryRepository;
import com.swiftapi.service.BankService;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class Controller {

    @Autowired
    BankRepository br;
    @Autowired
    CountryRepository cr;
    @Autowired
    BankService bs;

    // SHOW BANK INFO
    @GetMapping("/v1/swift-codes/{swift-code}")
    public ResponseEntity<?> getSwiftCode(@PathVariable("swift-code") String swiftCode){
        try{
            Optional<Bank> opt = br.findBySWIFTCode(swiftCode);

            if (opt.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Bank not found");

            Bank bank = opt.get();

            if (bank.isHeadquarter()){
                SwiftCodeRequest scr = new SwiftCodeRequest();
                scr.setSwiftCode(bank.getSWIFTCode());
                scr.setAddress(bank.getAddress());
                scr.setBankName(bank.getBankName());
                scr.setCountryISO2(bank.getCountry().getISO2());
                scr.setCountryName(bank.getCountry().getCountryName());
                scr.setHeadquarter(true);

                List<SwiftCodeRequest.Branch> branchList = br.findBySWIFTCodeStartingWithAndIsHeadquarterFalse(bank.getSWIFTCode().substring(0,8))
                        .stream()
                        .map(SwiftCodeRequest.Branch::new)
                        .toList();

                scr.setBranches(branchList);

                return ResponseEntity.ok(scr);

            } else {
                SwiftCodeRequest.SingleBranch branch = new SwiftCodeRequest.SingleBranch(bank);
                return ResponseEntity.ok(branch);
            }
        } catch (DataAccessException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Database error", e);
        }
    }

    // SHOW BANKS FOR COUNTRY
    @GetMapping("/v1/swift-codes/country/{countryISO2code}")
    public ResponseEntity<?> getISO2Code(@PathVariable("countryISO2code") String ISO2Code){
        try{
            Optional<Country> opt = cr.findByISO2(ISO2Code);
            if (opt.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Country not found");
            Country country = opt.get();

            List<SwiftCodeRequest.Branch> branchList = br.findByCountryISO2(ISO2Code)
                    .stream()
                    .map(SwiftCodeRequest.Branch::new)
                    .toList();

            CountryISO2Request ciso2r = new CountryISO2Request();
            ciso2r.setCountryISO2(country.getISO2());
            ciso2r.setCountryName(country.getCountryName());
            ciso2r.setSwiftCodes(branchList);


            return ResponseEntity.ok(ciso2r);
        } catch (DataAccessException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Database error", e);
        }

    }

    // ADD NEW BANK
    @PostMapping("/v1/swift-codes")
    public ResponseEntity<String> addSwiftCode(@RequestBody SwiftCodeRequest scr) {
        try{
            ObjectMapper mapper = new ObjectMapper();
            System.out.println("Received JSON: " + mapper.writeValueAsString(scr));
            System.out.println("isHeadquarter: " + scr.isHeadquarter());

            bs.saveBank(scr);
            return ResponseEntity.status(HttpStatus.CREATED).body("SWIFT code successfully added.");
        } catch (DataAccessException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Database error", e);
        } catch (InvalidSwiftCodeException | InvalidISO2Exception | JsonProcessingException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // DELETE BANK
    @DeleteMapping("/v1/swift-codes/{swift-code}")
    @Transactional
    public ResponseEntity<String> deleteSwiftCode(@PathVariable("swift-code") String swiftCode){
        try{
            Optional<Bank> bank = br.findBySWIFTCode(swiftCode);

            if (bank.isPresent()) {
                br.deleteBySWIFTCode(swiftCode);
                return ResponseEntity.ok(swiftCode + " DELETED");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("SWIFT Code not found: " + swiftCode);
            }
        } catch (DataAccessException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Database error", e);
        }
    }
}
