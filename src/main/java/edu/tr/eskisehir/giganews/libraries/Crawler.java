package edu.tr.eskisehir.giganews.libraries;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.LinkedList;

public class Crawler {

    protected static final String SOURCE = "https://www.bbc.com/";

    public static LinkedList<Dictionary<String, String>> scanAllNews(String category) {
        LinkedList<Dictionary<String, String>> allNewsList = new LinkedList<>();
        try {
            Document doc = Jsoup.connect(SOURCE + "news/" + category).get();
            Elements allNews;
            if (category.isEmpty()) {
                Elements container = doc.selectXpath("//*[@id=\"news-top-stories-container\"]");
                allNews = container.select(".gel-layout--no-flex > .nw-c-top-stories__secondary-item");
                Elements primaryItem = container.select(".gel-layout--no-flex > .nw-c-top-stories__primary-item");
                allNewsList.add(0, formatNewsMeta(primaryItem.get(0)));
            } else {
                allNews = doc.selectXpath("//*[@id=\"topos-component\"]/div[4]/div/div[1]/div/div[3]").select(".gel-layout--equal .gel-layout__item");
            }
            for (Element news : allNews) {
                Dictionary<String, String> newsMeta = formatNewsMeta(news);
                if (newsMeta != null)
                    allNewsList.add(newsMeta);
            }
        } catch (IOException e) {
            System.out.println("Error: Crawler.scanAllNews()");
        }
        return allNewsList;
    }

    protected static Dictionary<String, String> formatNewsMeta(Element news) {
        Elements img = news.select("img");
        Elements heading = news.select(".gs-c-promo-body .gs-c-promo-heading");
        String time = news.select("time .gs-u-vh").text();
        String link = heading.attr("href");
        if (img.hasAttr("src") && !time.isEmpty() && link.contains("/news/")) {
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
            newsMeta.put("title", heading.text());
            newsMeta.put("url", link);
            return newsMeta;
        }
        return null;
    }

    public static Dictionary<String, String> fetchNews(String path) {
        Dictionary<String, String> result = new Hashtable<>();
        try {
            Document doc = Jsoup.connect(SOURCE + path).get();
            Elements article = doc.selectXpath("//*[@id=\"main-content\"]/div[5]/div/div[1]");
            result.put("img", article.select("figure div span picture img").attr("src"));
            result.put("time", article.select("time").text());
            result.put("title", doc.selectXpath("//*[@id=\"main-heading\"]").text());
            result.put("topic", doc.selectXpath("//*[@id=\"header-content\"]/div[3]/div/nav/div[2]/div/div/ul/li[1]").text());
            LinkedList<String> body = new LinkedList<>();
            for (Element text : article.select("div[data-component=text-block]"))
                body.add("<p class=\"fs-5 mb-4\">" + text.text() + "</p>");
            result.put("body", String.join("", body));
        } catch (IOException e) {
            System.out.println("Error: Crawler.fetchNews()");
        }
        return result;
    }

    public static LinkedList<String> scanMarketplace() {
        LinkedList<String> data = new LinkedList<>();
        try {
            Document doc = Jsoup.connect(SOURCE + "news/business/market-data").get();
            Elements currencies = doc.select("[aria-labelledby=nw-c-md-region--currencies] table");
            for (Element currency : currencies) {
                Elements tbody = currency.select("tbody tr");
                for (Element tr : tbody)
                    data.add(tr.text());
            }
        } catch (IOException e) {
            System.out.println("Error: Crawler.scanMarketplace()");
        }
        return data;
    }
}
