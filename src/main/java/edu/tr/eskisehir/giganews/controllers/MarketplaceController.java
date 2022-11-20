package edu.tr.eskisehir.giganews.controllers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.util.LinkedList;

@Controller
class MarketplaceController {
    @GetMapping("/marketplace")
    String marketplace(Model model) throws IOException {
        model.addAttribute("data", getData());
        return "marketplace";
    }

    private static LinkedList<String> getData() throws IOException {
        Document doc = Jsoup.connect("https://www.bbc.com/news/business/market-data").get();
        Elements currencies = doc.select("[aria-labelledby=nw-c-md-region--currencies] table");
        LinkedList<String> data = new LinkedList<>();
        for (Element currency : currencies) {
            Elements tbody = currency.select("tbody tr");
            for (Element tr : tbody)
                data.add(tr.text());
        }
        return data;
    }
}