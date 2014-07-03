import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import spark.Spark;
import spark.utils.IOUtils;

public class HelloWorldTest {

    private static final String HELLOPATH = "/hello";
    public static final int PORT = 4567;

    @AfterClass
    public static void tearDown() {
        Spark.stop();
    }

    @BeforeClass
    public static void setUp() {
        HelloWorld.main(null);
        try {
            Thread.sleep(500);
        } catch (Exception e) {
        }
    }

    @Test
    public void sayHello() {
        UrlResponse response = doMethod("GET", HELLOPATH, null);
        assertNotNull(response);
        assertNotNull(response.body);
        assertEquals(200, response.status);
        assertTrue(response.body.contains("Hello World"));
    }

    private static UrlResponse doMethod(String requestMethod, String path, String body) {
        UrlResponse response = new UrlResponse();

        try {
            getResponse(requestMethod, path, response);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

    private static void getResponse(String requestMethod, String path, UrlResponse response)
            throws IOException {
        URL url = new URL("http://localhost:" + PORT + path);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(requestMethod);
        connection.connect();
        String res = IOUtils.toString(connection.getInputStream());
        response.body = res;
        response.status = connection.getResponseCode();
        response.headers = connection.getHeaderFields();
    }

    private static class UrlResponse {
        public Map<String, List<String>> headers;
        private String body;
        private int status;
    }
}
