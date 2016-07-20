package net.exkazuu.mimicdance.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import net.exkazuu.mimicdance.BuildConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class APIClient {

    private static final String TAG = "APIClient";
    private static final String CLIENT_PREFERENCE = "CLIENT_PREFERENCE";
    private static final String CLIENT_ID = "CLIENT_ID";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final SimpleDateFormat ISO8601 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    private static final String SERVER_URL = "https://mimic-dance.herokuapp.com";

    private static final boolean USE_MOCK = false;

    // region 内部クラス

    public enum ClientType {
        NONE, A, B;
    }

    public static class PartnerState {

        public static final PartnerState NONE = new PartnerState(false, null, null);
        public static final PartnerState CONNECTED = new PartnerState(true, null, null);

        public final boolean isConnected;
        public final Date playAt;
        public final String program;

        private PartnerState(boolean isConnected, Date playAt, String program) {
            this.isConnected = isConnected;
            this.playAt = playAt;
            this.program = program;
        }

        public boolean isNone() {
            return !isConnected;
        }

        public boolean isConnected() {
            return isConnected && playAt == null;
        }

        public boolean isReady() {
            return playAt != null;
        }

        @Override
        public String toString() {
            if (isNone()) {
                return "None";
            }
            if (isConnected()) {
                return "Connected";
            }
            return "Ready(" + playAt + ")";
        }

        public static PartnerState createReady(Date date, String program) {
            if (date == null || program == null) {
                return CONNECTED;
            }
            return new PartnerState(true, date, program);
        }
    }

    // endregion

    public static ClientType getClientType(Context context) {
        return getClientType(getClientId((context)));
    }

    @Nullable
    public static ClientType getClientType(String clientId) {
        if (clientId == null || clientId.length() == 0) {
            return ClientType.NONE;
        }
        if (clientId.endsWith("A")) {
            return ClientType.A;
        }
        if (clientId.endsWith("B")) {
            return ClientType.B;
        }
        return null;
    }

    public static String getClientId(Context context) {
        SharedPreferences pref = context.getSharedPreferences(CLIENT_PREFERENCE, Context.MODE_PRIVATE);
        return pref.getString(CLIENT_ID, "");
    }

    public static boolean setClientId(Context context, String clientId) {
        SharedPreferences pref = context.getSharedPreferences(CLIENT_PREFERENCE, Context.MODE_PRIVATE);
        ClientType type = getClientType(clientId);
        if (type == null) {
            return false;
        }
        switch (type) {
            case A:
            case B:
                pref.edit()
                    .putString(CLIENT_ID, clientId)
                    .apply();
                break;
            case NONE:
                pref.edit()
                    .remove(CLIENT_ID)
                    .apply();
                break;
        }
        return true;
    }

    /**
     * @param context
     * @param lesson  プレイ中のレッスン
     * @return パートナーも同じレッスンをプレイ中かどうか
     */
    public static PartnerState connect(Context context, String lesson) {
        if (USE_MOCK && BuildConfig.DEBUG) {
            Log.v("Mimic", "connect USE MOCK!");
            return PartnerState.CONNECTED;
        }

        String id = getClientId(context);
        try {
            String json = post(id, "connected", lesson, null);
            JSONObject obj = new JSONObject(json);
            if (!obj.has("lesson")) {
                return PartnerState.NONE;
            }
            String lesson2 = obj.getString("lesson");
            return lesson.equals(lesson2)
                ? PartnerState.CONNECTED
                : PartnerState.NONE;
        } catch (JSONException e) {
            Log.w(TAG, e.getMessage());
        } catch (IOException e) {
            Log.w(TAG, e.getMessage());
        }
        return PartnerState.NONE;
    }

    /**
     * @param context
     * @param lesson  プレイ中のレッスン
     * @return パートナーがreadyなら再生の開始時刻を返します。
     */
    public static PartnerState ready(Context context, String lesson, String myProgram) {
        if (USE_MOCK && BuildConfig.DEBUG) {
            Log.v("Mimic", "ready USE MOCK");
            return PartnerState.createReady(new Date(System.currentTimeMillis() + 2 * 1000), "");
        }

        String id = getClientId(context);
        try {
            String json = post(id, "ready", lesson, myProgram);
            JSONObject obj = new JSONObject(json);
            if (!obj.has("lesson")) {
                return PartnerState.NONE;
            }
            String lesson2 = obj.getString("lesson");
            if (!lesson.equals(lesson2)) {
                return PartnerState.NONE;
            }
            if (obj.has("program") && obj.has("play_at")) {
                String program = obj.getString("program");
                String strPlayAt = obj.getString("play_at");
                Date playAt = ISO8601.parse(strPlayAt);
                return PartnerState.createReady(playAt, program);
            } else {
                return PartnerState.CONNECTED;
            }
        } catch (JSONException e) {
            Log.w(TAG, e.getMessage());
        } catch (ParseException e) {
            Log.w(TAG, e.getMessage());
        } catch (IOException e) {
            Log.w(TAG, e.getMessage());
        }
        return PartnerState.NONE;
    }

    private static String post(String id, String state, String lesson, String program) throws IOException, JSONException {
        JSONObject obj = new JSONObject();
        obj.put("id", id);
        obj.put("state", state);
        obj.put("lesson", lesson);
        obj.put("program", program);
        String json = obj.toString();

        Log.v(TAG, "request: " + json);

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
            .url(SERVER_URL + "/state.json")
            .post(body)
            .build();
        Response response = client.newCall(request).execute();
        String resBody = response.body().string();
        Log.v(TAG, "response body: " + resBody);
        return resBody;
    }
}
