package io.proj3ct.pogodnikSl;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    private static Document getPage() throws IOException {
        String url = "https://world-weather.ru/pogoda/russia/yaroslavl/7days/";
        Document page = Jsoup.parse(new URL(url), 3000);
        return page;
    }

    //Среда, 15 марта
    // 15 марта
    // \d{2}\ \d{5}
    private static Pattern pattern = Pattern.compile("\\d{2}\\s\\D{5}");

    private static String getDateFromString(String stringDate) throws Exception {
        Matcher matcher = pattern.matcher(stringDate);
        if (matcher.find()) {
            return matcher.group();
        }
        throw new Exception("Can't extract date from string");
    }

    private static int printPartValues(Elements values, int index) {
        int interationCount = 4;
        if (index == 0) {
            Element valueLn = values.get(3);
            boolean isMorning = valueLn.text().contains("Ночь");
            if (isMorning) {
                interationCount = 3;
            }
        }
        for (int i = 0; i < interationCount; i++) {
            Element valueLine = values.get(index + i);
            for (Element tr : valueLine.select("tr")) {
                System.out.println(tr.text() + "   ");
            }
            System.out.println();
        }

        return interationCount;
    }

    public static void main(String[] args) throws Exception {
        Document page = getPage();
        Element divWrapper = page.select("div[id=content-left]").first();
        Elements names = divWrapper.select("div[class=weather-short]");
        Elements values = divWrapper.select("tr[class]");
        int index = 0;
        for (Element name : names) {
            String dateString = name.select("div[class=dates short-d],[class=dates short-d red]").text();
            String date = getDateFromString(dateString);
            System.out.println(date + " Темп. Осад. Дав.  Ветер Влаж.\n");
            int interationCount = printPartValues(values, index);
            index = index + interationCount;
        }
    }
}
