package com.swiftapi.service;

import com.swiftapi.exception.InvalidISO2Exception;
import com.swiftapi.exception.InvalidSwiftCodeException;
import com.swiftapi.model.Bank;
import com.swiftapi.model.Country;
import com.swiftapi.repository.BankRepository;
import com.swiftapi.repository.CountryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class BankServiceUnitTest {

    private BankRepository br;
    private CountryRepository cr;
    private BankService bs;

    @BeforeEach
    void setUp() {
        br = Mockito.mock(BankRepository.class);
        cr = Mockito.mock(CountryRepository.class);
        bs = new BankService(br, cr);
    }

    @Test
    void testCheckBank_SaveBank_CorrectData_CountryDoesntExists(){

        List<List<String>> data = new ArrayList<>();
        List<String> row = new ArrayList<>();

        row.add("TS");
        row.add("AKBKMTMTXXX");
        row.add("BIC11");
        row.add("Test Bank");
        row.add("Test address");
        row.add("Test town");
        row.add("Test country");
        row.add("Test zone");

        data.add(row);

        assertTrue(bs.saveBank(data));
    }

    @Test
    void testCheckBank_SaveBank_CorrectData_CountryExists(){
        List<List<String>> data = new ArrayList<>();
        List<String> row = new ArrayList<>();

        row.add("PL");
        row.add("AKBKMTMTXXX");
        row.add("BIC11");
        row.add("Test Bank");
        row.add("Test address");
        row.add("Test town");
        row.add("Poland");
        row.add("Warsaw");

        data.add(row);


        Country country = new Country();
        country.setISO2("PL");
        country.setCountryName("Poland");
        country.setTimeZone("Warsaw");
        cr.save(country);

        assertTrue(bs.saveBank(data));
    }

    @Test
    void testCheckBank_SwiftCodeAlreadyExists() {
        when(br.findBySWIFTCode("AKBKMTMTXXX")).thenReturn(Optional.of(new Bank()));

        Exception exception = assertThrows(InvalidSwiftCodeException.class, () ->
                bs.checkBank("PL", "AKBKMTMTXXX", true)
        );

        assertEquals("SWIFT Code already exists", exception.getMessage());
    }


    @Test
    void testCheckBank_ValidData() {
        when(br.findBySWIFTCode("AKBKMTMTXXX")).thenReturn(Optional.empty());

        assertDoesNotThrow(() ->
                bs.checkBank("PL", "AKBKMTMTXXX", true)
        );
    }

    @Test
    void testCheckBank_InvalidISO2_TooLong() {
        Exception exception = assertThrows(InvalidISO2Exception.class, () ->
                bs.checkBank("PLX", "AKBKMTMTXXX", true)
        );
        assertEquals("Too many characters in ISO2: PLX", exception.getMessage());
    }

    @Test
    void testCheckBank_InvalidISO2_SpecialCharacters() {
        Exception exception = assertThrows(InvalidISO2Exception.class, () ->
                bs.checkBank("P!", "AKBKMTMTXXX", true)
        );
        assertEquals("ISO contains special characters: P!", exception.getMessage());
    }

    @Test
    void testCheckBank_InvalidISO2_ContainsNumbers() {
        Exception exception = assertThrows(InvalidISO2Exception.class, () ->
                bs.checkBank("P1", "AKBKMTMTXXX", true)
        );
        assertEquals("ISO Contains digits: P1", exception.getMessage());
    }

    @Test
    void testCheckBank_InvalidSwiftCode_TooShort() {
        Exception exception = assertThrows(InvalidSwiftCodeException.class, () ->
                bs.checkBank("PL", "AKBK", true)
        );
        assertEquals("Invalid SWIFT Code length: AKBK", exception.getMessage());
    }

    @Test
    void testCheckBank_InvalidSwiftCode_TooLong() {
        Exception exception = assertThrows(InvalidSwiftCodeException.class, () ->
                bs.checkBank("PL", "AKBKMTMTAAAXXX", true)
        );
        assertEquals("Invalid SWIFT Code length: AKBKMTMTAAAXXX", exception.getMessage());
    }

    @Test
    void testCheckBank_InvalidSwiftCode_SpecialCharacters() {
        Exception exception = assertThrows(InvalidSwiftCodeException.class, () ->
                bs.checkBank("PL", "A-BKMTMTXXX", true)
        );
        assertEquals("SWIFT Code contains special characters: A-BKMTMTXXX", exception.getMessage());
    }

    @Test
    void testCheckBank_InvalidSwiftCode_ContainsNumbers() {
        Exception exception = assertThrows(InvalidSwiftCodeException.class, () ->
                bs.checkBank("PL", "A3BKMTMTXXX", true)
        );
        assertEquals("SWIFT Code contains digits: A3BKMTMTXXX", exception.getMessage());
    }

    @Test
    void testCheckBank_InvalidHeadquarter_NoHeadquarterMark() {
        Exception exception = assertThrows(InvalidSwiftCodeException.class, () ->
                bs.checkBank("PL", "ADBKMTMTXXX", false)
        );
        assertEquals("Invalid Bank type for the SWIFT Code: ADBKMTMTXXX and Bank type: false", exception.getMessage());
    }

    @Test
    void testIsBankEmpty_EmptyDatabase() {
        when(br.count()).thenReturn(0L);

        assertTrue(bs.isBankEmpty());
    }

    @Test
    void testIsBankEmpty_NonEmptyDatabase() {
        when(br.count()).thenReturn(5L);

        assertFalse(bs.isBankEmpty());
    }

    @Test
    void testIsBankEmpty_DatabaseError() {
        when(br.count()).thenThrow(new RuntimeException("Database error"));

        assertFalse(bs.isBankEmpty());
    }
}
