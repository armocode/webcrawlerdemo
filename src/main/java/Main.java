import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        //starting seed url
        String url = "https://en.wikipedia.org/wiki/Elon_Musk";
        crawl(1, url, new ArrayList<>());
    }

    public static void crawl(int level, String url, ArrayList<String> visited) {
        if (level < 8) {
            Document document = request(url, visited);

            if(document != null) {
                Elements links = document.select("a[href]");
                for (Element link : links) {
                    String next_link = link.attr("abs:href");
                    if (visited.contains(next_link) == false) {
                        //crawl(level++, next_link, visited);
                    }
                }
            }
        }
    }

    public static Document request(String url, ArrayList<String> v) {
        try {
            Connection connection = Jsoup.connect(url);
            Document document = connection.get();

            if (connection.response().statusCode() == 200) {
                System.out.println("Link:" + url);
                System.out.println(document.title());
                Elements text = document.select("p");
                checkTerms(String.valueOf(text));
                v.add(url);

                return document;
            }
            return null;
        }
        catch (IOException e) {
            return null;
        }
    }

    public static void checkTerms(String text) throws FileNotFoundException {
        String term1 = "Tesla";
        String term2 = "Musk";
        String term3 = "manufacturer";
        String term4 = "Elon Mask";

        int counter1 = 0;
        int counter2 = 0;
        int counter3 = 0;
        int counter4 = 0;
        String check = "";

        //go through text and check out terms
        for (int i = 0; i < text.length() - 1; i++) {
            check = "";
            for (int j = 0; j < 13; j++) {
                if (i + 15 > text.length()){
                    break;
                }
                check = check + text.charAt(i+j);

                //sum up how many times specified terms are found in text
                if (check.equals(term1)) {
                    counter1++;
                }
                if (check.equals(term2)) {
                    counter2++;
                }
                if (check.equals(term3)) {
                    counter3++;
                }
                if (check.equals(term4)) {
                    counter4++;
                }
            }
        }

        //write to csv file the results
        int total = counter1 + counter2 + counter3 + counter4;
        File csvFile = new File("results.csv");
        PrintWriter out = new PrintWriter(csvFile);
        out.println(term1 + ": " + counter1);
        out.println(term2 + ": " + counter2);
        out.println(term3 + ": " + counter3);
        out.println(term4 + ": " + counter4);
        out.println("Total: " + total);
        out.close();
    }
}
