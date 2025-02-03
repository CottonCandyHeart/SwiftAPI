package com.swiftapi.service;

import com.swiftapi.model.Bank;
import com.swiftapi.model.Country;
import com.swiftapi.model.SwiftCodeRequest;
import com.swiftapi.repository.BankRepository;
import com.swiftapi.repository.CountryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BankService {
    private final BankRepository br;
    private final CountryRepository cr;

    public BankService(BankRepository br, CountryRepository cr){
        this.br = br;
        this.cr = cr;
    }

    public boolean saveBank(SwiftCodeRequest scr){
        try{
            // FORM DATA
            String ISO2 = scr.getCountryISO2();
            String SWIFTCode = scr.getSwiftCode();
            String codeType = "BIC11";
            String bankName = scr.getBankName();
            String address = scr.getAddress();
            String townName = "";
            String countryName = scr.getCountryName();
            String timeZone = "";
            boolean isHeadquarter = scr.isHeadquarter();

            //if (isHeadquarter) SWIFTCode += "XXX";

            // TODO errors

            Optional<Country> countryExist = cr.findByISO2(ISO2);

            // CHOOSE COUNTRY OR ADD IT IF DOESN'T EXISTS
            Country country;
            if (countryExist.isPresent()){
                country = countryExist.get();
            } else {
                country = new Country();
                country.setISO2(ISO2);
                country.setCountryName(countryName);
                country.setTimeZone(timeZone);
                cr.save(country);
            }

            // ADD BANK
            Bank bank = new Bank();
            bank.setSWIFTCode(SWIFTCode);
            bank.setCodeType(codeType);
            bank.setBankName(bankName);
            bank.setAddress(address);
            bank.setTownName(townName);
            bank.setHeadquarter(isHeadquarter);
            bank.setCountry(country);

            br.save(bank);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void saveBank(List<List<String>> data){
        for (List<String> row : data){
            // READ DATA
            String ISO2 = row.get(0).toUpperCase();
            String SWIFTCode = row.get(1).toUpperCase();
            String codeType = row.get(2);
            String bankName = row.get(3);
            String address = row.get(4);
            String townName = row.get(5);
            String countryName = row.get(6).toUpperCase();
            String timeZone = row.get(7);

            Optional<Country> countryExist = cr.findByISO2(ISO2);

            // CHOOSE COUNTRY OR ADD IT IF DOESN'T EXISTS
            Country country;
            if (countryExist.isPresent()){
                country = countryExist.get();
            } else {
                country = new Country();
                country.setISO2(ISO2);
                country.setCountryName(countryName);
                country.setTimeZone(timeZone);
                cr.save(country);
            }

            // ADD BANK
            Bank bank = new Bank();
            bank.setSWIFTCode(SWIFTCode);
            bank.setCodeType(codeType);
            bank.setBankName(bankName);
            bank.setAddress(address);
            bank.setTownName(townName);
            bank.setHeadquarter(SWIFTCode.substring(SWIFTCode.length() - 3).equals("XXX"));
            bank.setCountry(country);

            br.save(bank);
        }
    }

    public void showAll(){
        List<Country> allCountries = cr.findAll();
        List<Bank> allBanks = br.findAll();

        allCountries.forEach(country -> System.out.println(country));
        System.out.println();
        allBanks.forEach(bank -> System.out.println(bank));
    }

    public boolean isBankEmpty(){
        long count = br.count();
        return (count == 0);
    }
}
