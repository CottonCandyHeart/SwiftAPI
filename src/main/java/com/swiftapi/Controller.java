package com.swiftapi;

import com.swiftapi.model.Bank;
import com.swiftapi.model.Country;
import com.swiftapi.repository.BankRepository;
import com.swiftapi.repository.CountryRepository;
import org.springframework.web.bind.annotation.*;

@RestController
public class Controller {

    BankRepository bankRepo;
    CountryRepository countryRepo;

    @GetMapping("/v1/swift-codes/{swift-code}")
    public String getSwiftCode(@PathVariable("swift-code") String swiftCode){
        // TODO


        return "SWIFT CODE: " + swiftCode;
    }

    @GetMapping("/v1/swift-codes/country/{countryISO2code}")
    public String getISO2Code(@PathVariable("countryISO2code") String ISO2Code){
        // TODO
        return "COUNTRY ISO2 CODE: " + ISO2Code;
    }

    @PostMapping("/v1/swift-codes")
    public void addSwiftCode(@RequestBody Bank bank, @RequestBody Country country){
        // TODO
        System.out.println("SWIFT code added successfully");
    }

    @DeleteMapping("/v1/swift-codes/{swift-code}")
    public String deleteSwiftCode(@PathVariable("swift-code") String swiftCode){
        // TODO
        return swiftCode + " DELETED";
    }
}
