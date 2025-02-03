package com.swiftapi;

import com.swiftapi.model.Bank;
import com.swiftapi.model.Country;
import com.swiftapi.model.SwiftCodeRequest;
import com.swiftapi.repository.BankRepository;
import com.swiftapi.repository.CountryRepository;
import com.swiftapi.service.BankService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class Controller {

    BankRepository br;
    CountryRepository cr;
    BankService bs;

    // SHOW BANK INFO
    @GetMapping("/v1/swift-codes/{swift-code}")
    public Bank getSwiftCode(@PathVariable("swift-code") String swiftCode){
        Optional<Bank> opt = br.findBySWIFTCode(swiftCode);

        if (opt.isEmpty()) return null;

        Bank bank = opt.get();

        if (bank.isHeadquarter()){
            System.out.println();
            return bank;

        } else {
            return bank;
        }

        //return "SWIFT CODE: " + swiftCode;
    }

    // SHOW BANKS FOR COUNTRY
    @GetMapping("/v1/swift-codes/country/{countryISO2code}")
    public String getISO2Code(@PathVariable("countryISO2code") String ISO2Code){
        // TODO
        return "COUNTRY ISO2 CODE: " + ISO2Code;
    }

    // ADD NEW BANK
    @PostMapping
    public ResponseEntity<?> addSwiftCode(@RequestBody SwiftCodeRequest scr) {
        boolean success = bs.saveBank(scr);

        if (success) {
            return ResponseEntity.ok("SWIFT code successfully added.");
        } else {
            return ResponseEntity.status(500).body("Error adding SWIFT code.");
        }
    }
/*
    @PostMapping("/v1/swift-codes")
    public void addSwiftCode(@RequestBody Bank bank, @RequestBody Country country){
        // TODO
        System.out.println("SWIFT code added successfully");
    }
*/
    // DELETE BANK
    @DeleteMapping("/v1/swift-codes/{swift-code}")
    public String deleteSwiftCode(@PathVariable("swift-code") String swiftCode){
        // TODO
        return swiftCode + " DELETED";
    }
}
