package net.herobrine.gamecore;

import lombok.SneakyThrows;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SkinSettings {

    public String get(String url,String name) throws IOException {
        HttpsURLConnection connection = (HttpsURLConnection) new URL(String.format(url, name)).openConnection();

        if (connection.getResponseCode() != 200) {
            System.out.println("An error has occurred: RESPONSE_CODE (" + connection.getResponseCode() + ")");
        } else {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String output;

            while ((output = bufferedReader.readLine()) != null) {
                builder.append(output);
            }
            return builder.toString();
        }
        return "";
    }

    public String getSig(String string){
        Pattern pattern = Pattern.compile("\"signature\" : \"(.*?)\"");
        Matcher matcher = pattern.matcher(string);
        matcher.find();
        return matcher.group(1);
    }
    public String getData(String string){
        Pattern pattern = Pattern.compile("\"value\" : \"(.*?)\"");
        Matcher matcher = pattern.matcher(string);
        matcher.find();
        return matcher.group(1);
    }


    private String insertChar(String string, char character, int index) {
        StringBuilder builder = new StringBuilder(string);
        builder.insert(index, character);

        return builder.toString();
    }

    private String formatUUID(String input) {
        input = insertChar(input, '-', 8);
        input = insertChar(input, '-', 13);
        input = insertChar(input, '-', 18);
        input = insertChar(input, '-', 23);

        return input;
    }

    public String getUUID(String output) {
        Pattern pattern = Pattern.compile("\"id\" : \"(.*?)\"");
        Matcher matcher = pattern.matcher(output);
        matcher.find();

        String id = matcher.group(1);
        return id;
    }
}
