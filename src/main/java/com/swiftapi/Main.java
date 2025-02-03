package com.swiftapi;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class Main  implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            FileService fs = new FileService();

            fs.readFile("Interns_2025_SWIFT_CODES.xlsx");
            System.out.println("File data read successfully!");


        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }
}