package org.example;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.URI;

public class FreegeoipTest {
    String key = "5e0e83dca90497afbd3692867075614b";

//    @Test
    public void freegeoipTest() {
        String site = "freegeoip.io";
        String format = "json";
        String checkAddress = "ru.wikipedia.org";
        String api = site + "/" + format + "/" + checkAddress;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://freegeoip.io"))
                .build();
        try {
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
            System.out.println("======= RESPONSE START ======");
            System.out.println(response.statusCode());
            System.out.println(response.body());
            System.out.println("======= RESPONSE END ======");
        } catch(IOException e) {
            System.out.println("IOException : ");
            e.printStackTrace();
        } catch(InterruptedException e) {
            System.out.println("InterruptedException : ");
            e.printStackTrace();
        }
    }

}
