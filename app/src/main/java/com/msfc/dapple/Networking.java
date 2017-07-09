package com.msfc.dapple;

import android.app.Activity;
import android.app.Instrumentation;
import android.media.Image;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jtb20 on 7/8/2017.
 */

public class Networking{
    private static String url;
    protected static String cookie;
    public static void setupNetworking(String address){
        url=address;
    }
    public static void login(String username, String password, Activity activity){
        RequestQueue queue=Volley.newRequestQueue(activity);

        ListenableRequest request = new ListenableRequest( Request.Method.POST,url+"/api/login",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        //add listeners
        request.addListener(new LoginHandler());
        request.addListener((NetworkListener)activity);
        //add login data
        Map<String,String> parameters=new HashMap<>();
        parameters.put("username",username);
        parameters.put("password",password);
        request.setParams(parameters);
        // Add the request to the RequestQueue.
        queue.add(request);
    }






    public static void postImage(byte[] bytes, Activity activity){
        RequestQueue queue=Volley.newRequestQueue(activity);

        ListenableRequest request = new ListenableRequest( Request.Method.POST,url+"/api/image",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            System.out.println("Submitted Image Successfully");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                int a = error.networkResponse.statusCode;
            }
        });
        //add listener
        request.addListener(((NetworkListener)activity));
        //add image data
        Map<String,String> parameters=new HashMap<>();
        char[] chars = new char[bytes.length/2+bytes.length%2];
        for(int i=0;i<chars.length;i++){

            chars[i]=(char)(ByteOperations.bytesToShort(bytes[i*2],i*2+1<bytes.length?bytes[i*2+1]:0));
        }
        String toSend = new String(chars);
        System.out.println(toSend.getBytes().length);
        parameters.put("data",toSend);
        System.out.println(toSend.length());
        request.setParams(parameters);
        // Add the request to the RequestQueue.
        queue.add(request);
    }






    protected static void setCookie(String c){
        cookie=c;
    }

}
class LoginHandler implements NetworkListener{
    public LoginHandler(){
    }
    public void onNetworkingResponse(NetworkResponse response){
        Map<String, String> responseHeaders = response.headers;
        String rawCookies = responseHeaders.get("Set-Cookie");
        String[] splitCookies=rawCookies.split("[=;]");
        Networking.setCookie(splitCookies[1]);
    }
}
