package edu.tr.eskisehir.giganews.controllers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.LinkedList;

@Controller
class HomeController {
    @GetMapping("/")
    public String index(Model model) throws IOException {
        LinkedList<Dictionary<String, String>> allNews = getAllNews("");
        model.addAttribute("primaryItem", allNews.get(0));
        allNews.remove(0);
        model.addAttribute("data", allNews);
        model.addAttribute("isIndex", true);
        return "home";
    }

    @GetMapping("/category/{category}")
    public String category(@PathVariable(value = "category") String category, Model model) throws IOException {
        model.addAttribute("data", getAllNews(category));
        model.addAttribute("isHome", false);
        return "home";
    }

    private static LinkedList<Dictionary<String, String>> getAllNews(String category) throws IOException {
        Document doc = Jsoup.connect("https://www.bbc.com/news/" + category).get();
        LinkedList<Dictionary<String, String>> allNewsList = new LinkedList<>();
        Elements allNews;
        if (category.isEmpty()) {
            Elements container = doc.selectXpath("//*[@id=\"news-top-stories-container\"]");
            allNews = container.select(".gel-layout--no-flex > .nw-c-top-stories__secondary-item");
            Elements primaryItem = container.select(".gel-layout--no-flex > .nw-c-top-stories__primary-item");
            allNewsList.add(0, getNewsMeta(primaryItem.get(0)));
        } else {
            allNews = doc.selectXpath("//*[@id=\"topos-component\"]/div[4]/div/div[1]/div/div[3]").select(".gel-layout--equal .gel-layout__item");
        }
        for (Element news : allNews) {
            Dictionary<String, String> newsMeta = getNewsMeta(news);
            if (newsMeta != null)
                allNewsList.add(newsMeta);
        }
        return allNewsList;
    }

    private static Dictionary<String, String> getNewsMeta(Element news) {
        Elements img = news.select("img");
        String time = news.select("time .gs-u-vh").text();
        if (img.hasAttr("src") && !time.isEmpty()) {
            Dictionary<String, String> newsMeta = new Hashtable<>();
            String[] parts;
            if (img.hasAttr("data-src")) {
                newsMeta.put("img", img.attr("data-src").replace("{width}", "700"));
            } else {
                newsMeta.put("img", "");
                parts = img.attr("src").split("/");
                parts[4] = "850";
                newsMeta.put("img", String.join("/", parts));
            }
            if (time.contains(" ago")) {
                parts = time.split("ago");
                newsMeta.put("time", parts[0] + "ago");
            } else {
                newsMeta.put("time", time);
            }
            newsMeta.put("text", news.select(".gs-c-promo-summary").text());
            Elements heading = news.select(".gs-c-promo-body .gs-c-promo-heading");
            newsMeta.put("title", heading.text());
            newsMeta.put("url", heading.attr("href"));
            return newsMeta;
        }
        return null;
    }
}