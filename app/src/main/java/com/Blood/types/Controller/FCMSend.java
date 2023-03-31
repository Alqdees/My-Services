package com.Blood.types.Controller;


import android.content.Context;
import android.os.Build;
import android.os.StrictMode;
import androidx.annotation.RequiresApi;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class FCMSend {
    private static String BASE_URL = "https://fcm.googleapis.com/fcm/send";
    private static String APi_KEY = "AIzaSyDwLmR_6r0CPKZrlzVyPA-_ZD4aLnhNXr0";

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void pushNotification(Context context, String token, String title, String message)
    {
        StrictMode.ThreadPolicy policy = new  StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        RequestQueue queue = Volley.newRequestQueue(context);
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("to",token);
            JSONObject notification = new JSONObject();
            notification.put("title",title);
            notification.put("body",message);
            jsonObject.put("notification",notification);


            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    BASE_URL,
                    jsonObject,
                    new Response.Listener<JSONObject>() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onResponse(JSONObject response) {

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }
            )
            {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {

                    Map<String ,String> params = new HashMap<>();
                    params.put("Content-Type","application/json");
                    params.put("Authorization",APi_KEY);
                    return params;
                }
            };
            queue.add(request);

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

}