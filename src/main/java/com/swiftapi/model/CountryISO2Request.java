package com.swiftapi.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CountryISO2Request {
    private String countryISO2;
    private String countryName;
    List<SwiftCodeRequest.Branch> swiftCodes;
}
