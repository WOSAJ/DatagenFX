package tk.wosaj.datagenfx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

public class UpdateManager {
    public static void checkUpdates() throws URISyntaxException, IOException {
        URL url = new URI("https://wosaj.github.io/DatagenFX/update.json").toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        var reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String temp;
        StringBuilder out = new StringBuilder();
        while((temp = reader.readLine()) != null) {
            out.append(temp);
        }
        System.out.println(out);
    }

    public static void main(String[] args) throws URISyntaxException, IOException {
        checkUpdates();
    }
}
