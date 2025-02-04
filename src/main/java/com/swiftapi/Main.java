package com.swiftapi;

import com.swiftapi.repository.BankRepository;
import com.swiftapi.repository.CountryRepository;
import com.swiftapi.service.BankService;
import com.swiftapi.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.List;

@SpringBootApplication
public class Main  implements CommandLineRunner {
    private final BankRepository br;
    private final CountryRepository cr;
    private final BankService bs;

    @Autowired
    public Main(BankRepository br, CountryRepository cr, BankService bs) {
        this.br = br;
        this.cr = cr;
        this.bs = bs;
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        if (bs.isBankEmpty()){
            try {
                FileService fs = new FileService();

                List<List<String>> data = fs.readFile("Interns_2025_SWIFT_CODES.xlsx");
                System.out.println("File data read successfully!");

                bs.saveBank(data);
                System.out.println("Data saved successfully!");

            } catch (IOException e) {
                System.err.println("Error reading file: " + e.getMessage());
            }
        }

        //bs.showAll();
    }
}