package com.swiftapi;

import com.swiftapi.model.Bank;
import com.swiftapi.model.Country;
import com.swiftapi.model.SwiftCodeRequest;
import com.swiftapi.repository.BankRepository;
import com.swiftapi.repository.CountryRepository;
import com.swiftapi.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
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

        if (opt.isEmpty()) return null;

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

    // DELETE BANK
    @DeleteMapping("/v1/swift-codes/{swift-code}")
    public String deleteSwiftCode(@PathVariable("swift-code") String swiftCode){
        // TODO
        return swiftCode + " DELETED";
    }
}
