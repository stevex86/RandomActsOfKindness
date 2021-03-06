package networking;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ConnectionHandler {

    private static final String baseURL = "http://raok.stevex86.com/";
    private static final String phoneURL = baseURL + "phone";
    private static final String msgURL = baseURL + "msg_to";
    private static final String donateURL = baseURL + "donate";
    private static final String mailURL = baseURL + "mail";
    private static final String registerURL = baseURL + "register";
    private static final String checkUserStats = baseURL + "user_click_data";

    private static HttpURLConnection buildGetRequest(String url, HashMap<String, String> params) throws IOException {
        int count = 0;
        StringBuilder urlParametersBuilder = new StringBuilder();

        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (count > 0) {
                urlParametersBuilder.append("&");
            }
            urlParametersBuilder.append(key).append("=").append(value);
            count++;
        }

        url = url + urlParametersBuilder.toString();
        URL requestURL = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) requestURL.openConnection();
        connection.setDoInput(true);
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("charset", "utf-8");
        connection.setUseCaches(false);

        return connection;
    }

    private static HttpURLConnection buildPostRequest(String url, String content) throws IOException {
        URL requestURL = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) requestURL.openConnection();
        connection.setDoInput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("charset", "utf-8");

        connection.setRequestProperty("Content-Length", "" + content.getBytes().length);
        connection.setUseCaches (false);
        OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
        writer.write(content);
        writer.flush();
        writer.close();

        return connection;
    }

    private static String buildResponse(HttpURLConnection connection) throws IOException {
        int responseCode = connection.getResponseCode();
        if (responseCode == 200 || responseCode == 201 || responseCode >= 400) {
            BufferedReader in;
            if (responseCode >= 400) {
                in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            }
            else {
                in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            }
            String inputLine;
            StringBuilder response = new StringBuilder();
            response.append(responseCode);
            response.append(":");

            while((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            Log.d("ConnectionHandler", response.toString());
            return response.toString();
        }
        else {
            throw new IOException("Received bad response from server.");
        }
    }

    public static String addMinutes(String id, int seconds) throws IOException, JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        jsonObject.put("call-time", seconds);

        HttpURLConnection connection = buildPostRequest(phoneURL, jsonObject.toString());
        return buildResponse(connection);
    }


    public static String incrementMsg(String id) throws IOException, JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);

        HttpURLConnection connection = buildPostRequest(msgURL, jsonObject.toString());
        return buildResponse(connection);
    }

    public static String addDonation(String id, int amount) throws IOException, JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        jsonObject.put("donated", amount);

        HttpURLConnection connection = buildPostRequest(donateURL, jsonObject.toString());
        return buildResponse(connection);
    }

    public static String incrementCard(String id) throws IOException, JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);

        HttpURLConnection connection = buildPostRequest(mailURL, jsonObject.toString());
        return buildResponse(connection);
    }

    public static String register(String guid) throws IOException, JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("guid", guid);

        HttpURLConnection connection = buildPostRequest(registerURL, jsonObject.toString());
        return buildResponse(connection);
    }

    public static String getUserStats(String id) throws IOException, JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);

        HttpURLConnection connection = buildPostRequest(checkUserStats, jsonObject.toString());
        return buildResponse(connection);
    }

}
