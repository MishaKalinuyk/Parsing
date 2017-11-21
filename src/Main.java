import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) throws IOException
    {
        System.out.println("Введите исполнителя или название песни:");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        String reqName = reader.readLine();
        String url = "http://zaycev.net/search.html?query_search=" + reqName;
        String useragent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
                "AppleWebKit/537.36 (KHTML, like Gecko) " +
                "Chrome/52.0.2743.116 Safari/537.36";

        Elements nextPage;
        int number = 0;

        do {

            Document doc = Jsoup.connect(url).userAgent(useragent).get();
            nextPage = doc.select("[class$=pager__item_last]");
            Elements resultOfSearch = doc.getElementsByClass("search-page__tracks");
            Elements elements = resultOfSearch.select("div[data-dkey]");



            for(Element element : elements) {
                String artName = element.select(".musicset-track__artist").text();
                String trackName = element.select(".musicset-track__track-name").text();
                String trackDur = element.select(".musicset-track__duration").text();
                try {
                    Document link = Jsoup
                            .connect(element.select(".musicset-track__download-link")
                                    .attr("abs:href"))
                            .userAgent(useragent)
                            .get();

                    String trackLin = link.select("#audiotrack-download-link").attr("abs:href");

                    number++;

                    System.out.print(String.format("%s)Artist: %s, Track: %s, Duration: %s, Link: %s\n",
                            String.valueOf(number), artName, trackName, trackDur, trackLin));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            url = nextPage.attr("abs:href");

        } while (nextPage.hasAttr("href"));
    }
}