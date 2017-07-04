package com.msfc.dapple;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by James on 6/29/2017.
 */

public class ListenableRequest extends StringRequest {
    CompletedListener listener;
    Map<String,String> params;
    String cookies;
    public ListenableRequest(int method, String url, Response.Listener<String> l2, Response.ErrorListener l3,CompletedListener listener){
        super(method,url,l2,l3);
        this.listener=listener;
    }
    @Override
    protected Map<String,String> getParams() {
        return params;
    }
    public void setParams(Map<String,String> map){
        params=map;
    }
    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        // since we don't know which of the two underlying network vehicles
        // will Volley use, we have to handle and store session cookies manually
        Map<String, String> responseHeaders = response.headers;
        String rawCookies = responseHeaders.get("Set-Cookie");
        String[] splitCookies=rawCookies.split("[=;]");
        cookies=splitCookies[1];
        return super.parseNetworkResponse(response);
    }
    public String getCookies(){
        return cookies;
    }
}
