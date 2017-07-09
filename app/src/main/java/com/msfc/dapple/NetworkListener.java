package com.msfc.dapple;

import com.android.volley.NetworkResponse;
import com.android.volley.toolbox.StringRequest;

/**
 * Created by James on 6/28/2017.
 */

public interface NetworkListener {
    public void onNetworkingResponse(NetworkResponse response);
}
