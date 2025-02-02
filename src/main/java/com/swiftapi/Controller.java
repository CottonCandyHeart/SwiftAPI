package com.swiftapi;

import org.springframework.web.bind.annotation.*;

@RestController
public class Controller {
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
    public String postAllSwiftCodes(){
        // TODO
        return "SWIFT CODES";
    }

    @DeleteMapping("/v1/swift-codes/{swift-code}")
    public String deleteSwiftCode(@PathVariable("swift-code") String swiftCode){
        // TODO
        return swiftCode + " DELETED";
    }
}
