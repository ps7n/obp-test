package obp.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Scanner;

/**
 * Created by pstepaniak on 2016-08-23.
 */
public class Application {

    public static void main(String[] args) {
        System.out.println("OBP Test app");

        Scanner in = new Scanner(System.in);
        ConsumerThread ct = new ConsumerThread("localhost:9092");
        ct.start();

        System.out.println("----------------------------------------------------------------------------");
        System.out.println("" + (new Date()) );

        System.out.println("\nInsert the GET resource url for OBP API:");
        String url = in.nextLine();

        System.out.println("\nInsert the response that will be inserted into Response kafka queue:");
        String response = in.nextLine();
        ct.setResponse(response);

        String content = getContent(url);
        System.out.println("\nHTTP Response:\n" + content);

        ct.interrupt();
    }

    private static String getContent(String urlToRead) {
        StringBuilder result = new StringBuilder();
        HttpURLConnection conn = null;
        try {
            URL url = new URL(urlToRead);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            result.append(readStream(conn.getInputStream()));
        } catch (IOException e) {
            System.out.println("Error while processing the HTTP response: " + e.getMessage());
            try {
                result.append(readStream(conn.getErrorStream()));
            } catch (Exception ex) {
                System.out.println("Fatal error: " + ex.getMessage());
            }
        }
        return result.toString();
    }

    private static String readStream(InputStream inputStream) throws IOException {
        StringBuilder result = new StringBuilder();
        BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
            result.append("\n");
        }
        rd.close();
        return result.toString();
    }


}
