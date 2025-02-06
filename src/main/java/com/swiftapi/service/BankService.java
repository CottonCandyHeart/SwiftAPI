package com.swiftapi.service;

import com.swiftapi.exception.InvalidISO2Exception;
import com.swiftapi.exception.InvalidSwiftCodeException;
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

    public void saveBank(SwiftCodeRequest scr) throws InvalidSwiftCodeException, InvalidISO2Exception {
        try{
            // FORM DATA
            String ISO2 = scr.getCountryISO2().toUpperCase();
            String SWIFTCode = scr.getSwiftCode().toUpperCase();
            String codeType = "BIC" + SWIFTCode.length();
            String bankName = scr.getBankName().toUpperCase();
            String address = scr.getAddress().toUpperCase();
            String townName = "";
            String countryName = scr.getCountryName().toUpperCase();
            String timeZone = "";
            boolean isHeadquarter = scr.isHeadquarter();
            System.out.println("SAVE BANK: " + isHeadquarter);

            // CHECK ERRORS
            checkBank(ISO2, SWIFTCode, isHeadquarter);

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
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public boolean saveBank(List<List<String>> data){

            for (List<String> row : data){
                try {
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
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }

            return true;
    }

    public void showAll(){
        try{
            List<Country> allCountries = cr.findAll();
            List<Bank> allBanks = br.findAll();

            allCountries.forEach(country -> System.out.println(country));
            System.out.println();
            allBanks.forEach(bank -> System.out.println(bank));
        } catch (Exception e) {
            System.err.println("Cannot read file");
            e.printStackTrace();
        }
    }

    public boolean isBankEmpty(){
        try{
            long count = br.count();
            return (count == 0);
        } catch (Exception e) {
            System.err.println("Database error");
            return false;
        }

    }

    public void checkBank(String ISO2, String SWIFTCode, boolean isHeadquarter) throws InvalidISO2Exception, InvalidSwiftCodeException {
        Optional<Bank> opt = br.findBySWIFTCode(SWIFTCode);
        if (opt.isPresent()) throw new InvalidSwiftCodeException("SWIFT Code already exists");

        if (ISO2.length() != 2) throw new InvalidISO2Exception("Too many characters in ISO2: " + ISO2);
        if (containsDigits(ISO2)) throw new InvalidISO2Exception("ISO Contains digits: " + ISO2);
        if (hasSpecialCharacters(ISO2)) throw new InvalidISO2Exception("ISO contains special characters: " + ISO2);
        if (SWIFTCode.length() != 11 && SWIFTCode.length() != 8) throw new InvalidSwiftCodeException("Invalid SWIFT Code length: " + SWIFTCode);
        if (containsDigits(SWIFTCode.substring(0,6))) throw new InvalidSwiftCodeException("SWIFT Code contains digits: " + SWIFTCode);
        if (hasSpecialCharacters(SWIFTCode)) throw new InvalidSwiftCodeException("SWIFT Code contains special characters: " + SWIFTCode);
        if (!isHeadquarter && (SWIFTCode.substring(SWIFTCode.length()-3).equals("XXX") || SWIFTCode.length() == 8)) throw new InvalidSwiftCodeException("Invalid Bank type for the SWIFT Code: " + SWIFTCode + " and Bank type: " + isHeadquarter);
    }

    public boolean containsDigits(String text) {
        return text.matches(".*\\d.*");
    }

    public boolean hasSpecialCharacters(String text){
        return text.matches(".*[-!@#$%^&*(),.?\":{}|<>].*");
    }
}
