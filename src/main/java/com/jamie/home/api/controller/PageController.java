package com.jamie.home.api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    Logger logger = LoggerFactory.getLogger(PageController.class);

    @GetMapping("/")
    public String main() {
        return "index";
    }
}