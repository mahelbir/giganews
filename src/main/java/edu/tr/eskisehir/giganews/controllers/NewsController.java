package edu.tr.eskisehir.giganews.controllers;

import edu.tr.eskisehir.giganews.libraries.Crawler;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
class NewsController {
    @GetMapping("/read")
    String readNews(@RequestParam String id, Model model) {
        System.out.println("User is reading: " + id);
        model.addAttribute("id", id);
        model.addAttribute("result", Crawler.fetchNews(id));
        return "news";
    }

    @PostMapping("/react")
    @ResponseBody
    String reactWithEmoji(@RequestParam String emoji, @RequestParam String id) {
        if (!emoji.isEmpty() && !id.isEmpty()) {
            System.out.println("User reacted with '" + emoji + "': " + id);
            return "success";
        } else {
            System.out.println("Reaction error");
            return "error";
        }
    }
}