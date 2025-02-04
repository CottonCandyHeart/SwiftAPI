package com.swiftapi.controller;

import com.swiftapi.model.Bank;
import com.swiftapi.model.Country;
import com.swiftapi.model.CountryISO2Request;
import com.swiftapi.model.SwiftCodeRequest;
import com.swiftapi.repository.BankRepository;
import com.swiftapi.repository.CountryRepository;
import com.swiftapi.service.BankService;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
        Optional<Bank> opt = br.findBySWIFTCode(swiftCode);

        if (opt.isEmpty()) return ResponseEntity.ok("Bank not found");

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
    }

    // SHOW BANKS FOR COUNTRY
    @GetMapping("/v1/swift-codes/country/{countryISO2code}")
    public ResponseEntity<?> getISO2Code(@PathVariable("countryISO2code") String ISO2Code){
        Optional<Country> opt = cr.findByISO2(ISO2Code);
        if (opt.isEmpty()) return ResponseEntity.ok("Country not found");
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
    }

    // ADD NEW BANK
    @PostMapping("/v1/swift-codes")
    public String addSwiftCode(@RequestBody SwiftCodeRequest scr) {
        boolean success = bs.saveBank(scr);

        if (success) {
            return "SWIFT code successfully added.";
        } else {
            return "Error adding SWIFT code.";
        }
    }

    // DELETE BANK
    @DeleteMapping("/v1/swift-codes/{swift-code}")
    @Transactional
    public String deleteSwiftCode(@PathVariable("swift-code") String swiftCode){
        Optional<Bank> bank = br.findBySWIFTCode(swiftCode);

        if (bank.isPresent()) {
            br.deleteBySWIFTCode(swiftCode);
            return swiftCode + " DELETED";
        } else {
            return "SWIFT Code not found: " + swiftCode;
        }
    }
}
