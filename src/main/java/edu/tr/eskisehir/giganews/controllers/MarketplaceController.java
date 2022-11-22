package edu.tr.eskisehir.giganews.controllers;

import edu.tr.eskisehir.giganews.libraries.Crawler;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
class MarketplaceController {
    @GetMapping("/marketplace")
    String marketplace(Model model) {
        System.out.println("User is checking marketplace");
        model.addAttribute("result", Crawler.scanMarketplace());
        return "marketplace";
    }
}