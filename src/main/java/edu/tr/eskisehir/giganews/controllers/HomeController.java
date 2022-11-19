package edu.tr.eskisehir.giganews.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
class HomeController {
    @GetMapping("/")
    String index() {
        return "home";
    }
}