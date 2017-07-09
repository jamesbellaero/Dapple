package com.msfc.dapple;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by James on 6/29/2017.
 */

public class ListenableRequest extends StringRequest {
    ArrayList<NetworkListener> listeners;
    Map<String,String> params;
    public ListenableRequest(int method, String url, Response.Listener<String> l2, Response.ErrorListener l3){
        super(method,url,l2,l3);
        listeners=new ArrayList<>();
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

        for(NetworkListener listener: listeners) {
            listener.onNetworkingResponse(response);
        }
        return super.parseNetworkResponse(response);
    }
    public void addListener(NetworkListener nl){
        listeners.add(nl);
    }
}
