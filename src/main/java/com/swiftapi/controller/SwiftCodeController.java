package com.swiftapi.controller;

import com.swiftapi.model.SwiftCodeRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.stereotype.Controller;

@Controller
public class SwiftCodeController {

    // SHOW FORM
    @GetMapping("/v1/swift-codes")
    public String showForm() {
        return "addSWIFTForm";
    }

    //@PostMapping("/v1/swift-codes")
    //public SwiftCodeRequest handleSubmit(@ModelAttribute SwiftCodeRequest swiftCodeRequest) {
     //   return swiftCodeRequest;
    //}
}
