package com.jamie.home.api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PageController {

    Logger logger = LoggerFactory.getLogger(PageController.class);

    @GetMapping("/")
    public String main() {
        return "index";
    }

    @PostMapping("/main/test")
    public String test() {
        return "test";
    }
}