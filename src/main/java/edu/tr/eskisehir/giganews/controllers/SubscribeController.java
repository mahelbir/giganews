package edu.tr.eskisehir.giganews.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Pattern;

@Controller
class SubscribeController {
    @GetMapping("/subscribe")
    String subscribe() {
        return "subscribe";
    }

    @PostMapping("/subscribe")
    @ResponseBody
    String subscribe(@RequestParam String email) {
        if (validEmail(email)) {
            System.out.println("User subscribed: " + email);
            return "success";
        } else {
            return "error";
        }
    }

    private static boolean validEmail(String email) {
        return Pattern.compile("[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$")
                .matcher(email)
                .matches();
    }
}