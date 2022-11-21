package edu.tr.eskisehir.giganews.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.regex.Pattern;


@Controller
class SubscriptionController {
    @GetMapping("/subscription")
    public String subscribe() {
        return "subscription";
    }

    @PostMapping("/subscription")
    @ResponseBody
    public String subscribeResponse(@RequestParam String email, @RequestParam String category) {
        if (isValidEmail(email)) { // check email
            if (!category.isEmpty())
                System.out.println("User subscribed to '" + category + "' category feed: " + email);
            else
                System.out.println("User subscribed to all news feed : " + email);
            return "success";
        } else {
            return "error";
        }
    }

    private static boolean isValidEmail(String email) {
        return Pattern.compile("[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$")
                .matcher(email.toLowerCase())
                .matches();
    }
}