package com.akshay.anfapp;

import android.webkit.URLUtil;

import org.junit.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by akshaythalakoti on 2/7/18.
 */

public class HttpUrlConnectionTest {

    private String getUrl() {
        return "https://www.abercrombie.com/anf/nativeapp/qa/codetest/codeTest_exploreData.json";
    }

    @Test
    public void testURL() throws Exception {
        String strUrl = getUrl();
        HttpURLConnection urlConn = null;
        try {
            URL url = new URL(strUrl);
            urlConn = (HttpURLConnection) url.openConnection();
            urlConn.connect();
            assertEquals(HttpURLConnection.HTTP_OK, urlConn.getResponseCode());
        } catch (IOException e) {
            System.err.println("Error creating HTTP connection");
            e.printStackTrace();
            throw e;
        } finally {
            if (urlConn != null) {
                urlConn.disconnect();
            }
        }
    }

    @Test
    public void checkUrlIsValid() throws IOException {
        assertNotNull(URLUtil.isValidUrl(getUrl()));
    }
}
