package org.example;

import org.junit.jupiter.api.Test;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;

public class FreegeoipTest {

    //That's not a good way to keep credentials.
    //You should use more sophisticated secrets providing mechanisms in production.
    private String key = "5e0e83dca90497afbd3692867075614b";
    private Logger logger = LoggerFactory.getLogger(FreegeoipTest.class);

    @Test
    public void freegeoipTest() throws IOException, InterruptedException {
        //FreeGeoIP API has been deprecated and moved to ipstack.com
        //https://github.com/fiorix/freegeoip/blob/master/README.md
        //https://github.com/apilayer/freegeoip/blob/master/README.md
        String site = "http://api.ipstack.com";
        String format = "json";
        String checkAddress = "ru.wikipedia.org";
        String api = site + "/" + checkAddress + "?access_key=" + key + "&output=" + format;
        int expectedResponseCode = 200;
        double expectedLatitude = 38.98371887207031;
        double expectedLongitude = -77.38275909423828;
        double tolerance = 0.01;

        HttpResponse<String> response = getStringHttpResponse(api);

        logger.info("Response code: " + response.statusCode());
        assertEquals(expectedResponseCode, response.statusCode());

        JSONObject parsedResponse = getParsedResponse(response);
        double actualLatitude = parsedResponse.getDouble("latitude");
        double actualLongitude = parsedResponse.getDouble("longitude");
        logger.info("Latitude: " + actualLatitude);
        logger.info("Longitude: " + actualLongitude);
        assertTrue(Math.abs(expectedLatitude - actualLatitude) <= tolerance);
        assertTrue(Math.abs(expectedLongitude - actualLongitude) <= tolerance);
    }

    private HttpResponse<String> getStringHttpResponse(String api) throws IOException, InterruptedException {
        HttpResponse<String> response;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(api))
                .build();
        try {
            response = client.send(request, BodyHandlers.ofString());
        } catch(IOException e) {
            logger.error("Got IOException on calling " + api.replace(key, "***"));
            throw e;
        } catch(InterruptedException e) {
            logger.error("Got InterruptedException on calling " + api.replace(key, "***"));
            throw e;
        }
        return response;
    }

    private JSONObject getParsedResponse(HttpResponse<String> response) {
        String responseBody = response.body();
        return new JSONObject(responseBody);
    }

}
