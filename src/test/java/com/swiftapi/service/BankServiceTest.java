package com.swiftapi.service;

import com.swiftapi.exception.InvalidISO2Exception;
import com.swiftapi.exception.InvalidSwiftCodeException;
import com.swiftapi.model.Bank;
import com.swiftapi.repository.BankRepository;
import com.swiftapi.repository.CountryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class BankServiceTest {

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
}
