package com.swiftapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;

@Controller
public class SwiftCodeController {
    // SHOW FORM
    @GetMapping("/v1/swift-codes")
    public String showForm() {
        return "addSWIFTForm";
    }
}
