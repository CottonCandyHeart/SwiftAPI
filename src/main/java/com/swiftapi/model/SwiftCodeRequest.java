package com.swiftapi.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SwiftCodeRequest {
    private String address;
    private String bankName;
    private String countryISO2;
    private String countryName;
    private boolean isHeadquarter;
    private String swiftCode;
    private List<Branch> branches;

    @Getter
    @Setter
    public static class Branch {
        private String address;
        private String bankName;
        private String countryISO2;
        private boolean isHeadquarter;
        private String swiftCode;

        public Branch(Bank bank) {
            this.address = bank.getAddress();
            this.bankName = bank.getBankName();
            this.countryISO2 = bank.getCountry().getISO2();
            this.isHeadquarter = bank.isHeadquarter();
            this.swiftCode = bank.getSWIFTCode();
        }
    }

    @Getter
    @Setter
    public static class SingleBranch extends Branch {
        private String countryName;
        public SingleBranch(Bank bank){
            super(bank);
            this.countryName = bank.getCountry().getCountryName();
        }

    }
}

