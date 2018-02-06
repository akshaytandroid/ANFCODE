package com.akshay.anfapp;

import android.app.Activity;
import android.os.Bundle;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import static org.mockito.Mockito.verify;

/**
 * Created by akshaythalakoti on 2/5/18.
 */
@Config(manifest = Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class MainActivityTest {

    @Mock
    HttpURLConnection mockHttpConnection;

    HttpUrlActivity activity;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        activity = new HttpUrlActivity(mockHttpConnection);
    }

    @Test
    public void itCanTestHttpURLConnectionStuff() throws ProtocolException {
        Shadows.shadowOf(activity);
        verify(mockHttpConnection).setRequestMethod("GET");
    }

    public class HttpUrlActivity extends Activity {

        private HttpURLConnection httpConnection;

        public HttpUrlActivity() throws MalformedURLException, IOException {
            this((HttpURLConnection) new URL("https://www.abercrombie.com/anf/nativeapp/qa/codetest/codeTest_exploreData.json").openConnection());
        }

        public HttpUrlActivity(final HttpURLConnection httpConnection) {
            this.httpConnection = httpConnection;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            try {
                httpConnection.setRequestMethod("GET");
            } catch (ProtocolException e) {
                throw new RuntimeException(e);
            }
            super.onCreate(savedInstanceState);
        }

    }

}
