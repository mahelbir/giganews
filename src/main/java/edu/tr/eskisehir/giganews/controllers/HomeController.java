package edu.tr.eskisehir.giganews.controllers;

import edu.tr.eskisehir.giganews.libraries.Crawler;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.ui.Model;

import java.util.Dictionary;
import java.util.LinkedList;


@Controller
class HomeController {
    @GetMapping("/")
    public String listAllNews(Model model) {
        LinkedList<Dictionary<String, String>> allNews = Crawler.scanAllNews("");
        if (allNews.get(0) != null) {
            model.addAttribute("primaryItem", allNews.get(0));
            allNews.remove(0);
        }
        model.addAttribute("news", allNews);
        return "home";
    }

    @GetMapping("/category/{category}")
    public String selectCategory(@PathVariable(value = "category") String category, Model model) {
        model.addAttribute("news", Crawler.scanAllNews(category));
        return "category";
    }
}