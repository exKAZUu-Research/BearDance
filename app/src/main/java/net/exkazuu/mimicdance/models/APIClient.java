package net.exkazuu.mimicdance.models;

import org.apache.http.client.HttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class APIClient {

    private static final String SERVER_URL = "http://localhost:3000";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final SimpleDateFormat ISO8601 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

    /**
     *
     * @param id 端末ID
     * @param lesson プレイ中のレッスン
     * @return パートナーも同じレッスンをプレイ中かどうか
     */
    public static boolean connect(String id, String lesson) {
        try {
            String json = post(id, "connected", lesson);
            JSONObject obj = new JSONObject(json);
            String lesson2 = obj.getString("lesson");
            return lesson.equals(lesson2);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     *
     * @param id 端末ID
     * @param lesson プレイ中のレッスン
     * @return パートナーがreadyなら再生の開始時刻を返します。
     */
    public static Date ready(String id, String lesson) {
        try {
            String json = post(id, "ready", lesson);
            JSONObject obj = new JSONObject(json);
            String playAt = obj.getString("play_at");
            if (playAt != null) {
                return ISO8601.parse(playAt);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String post(String id, String state, String lesson) throws IOException, JSONException {
        JSONObject obj = new JSONObject();
        obj.put("id", id);
        obj.put("state", "ready");
        obj.put("lesson", lesson);
        String json = obj.toString();

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
            .url(SERVER_URL + "/state.json")
            .post(body)
            .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}
