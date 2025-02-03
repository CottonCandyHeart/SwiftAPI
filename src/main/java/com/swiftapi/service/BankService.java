package com.swiftapi.service;

import com.swiftapi.model.Bank;
import com.swiftapi.model.Country;
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

    public void saveBank(List<List<String>> data){
        for (List<String> row : data){
            String ISO2 = row.get(0).toUpperCase();
            String SWIFTCode = row.get(1).toUpperCase();
            String codeType = row.get(2).toUpperCase();
            String bankName = row.get(3).toUpperCase();
            String address = row.get(4).toUpperCase();
            String townName = row.get(5).toUpperCase();
            String countryName = row.get(6).toUpperCase();
            String timeZone = row.get(7);

            Optional<Country> countryExist = cr.findByISO2(ISO2);

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
