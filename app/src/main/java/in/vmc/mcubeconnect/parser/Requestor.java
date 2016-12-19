package in.vmc.mcubeconnect.parser;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import in.vmc.mcubeconnect.utils.TAG;


/**
 * Created by gousebabjan on 29/6/16.
 */
public class Requestor implements TAG{

    public static JSONObject requestLogin(RequestQueue requestQueue, String url, final String email, final String password) {
        JSONObject response = null;
        String resp;
        RequestFuture<String> requestFuture = RequestFuture.newFuture();
        StringRequest request = new StringRequest(Request.Method.POST, url, requestFuture, requestFuture) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(EMAIL, email);
                params.put(PASSWORD, password);

                return params;
            }
        };

        requestQueue.add(request);
        try {
            resp = requestFuture.get(30000, TimeUnit.MILLISECONDS);
            response = new JSONObject(resp);
        } catch (InterruptedException e) {
            //L.m(e + "");
        } catch (ExecutionException e) {
            // L.m(e + "");
        } catch (TimeoutException e) {
            //L.m(e + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return response;
    }

    public static JSONObject requestCheckVisitJASON(RequestQueue requestQueue, String url, final String authkey, final String beaconId) {
        JSONObject response = null;
        String resp;
        RequestFuture<String> requestFuture = RequestFuture.newFuture();
        StringRequest request = new StringRequest(Request.Method.POST, url, requestFuture, requestFuture) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(AUTHKEY, authkey);
                params.put(BEACONID, beaconId);

                return params;
            }
        };

        requestQueue.add(request);
        try {
            resp = requestFuture.get(30000, TimeUnit.MILLISECONDS);
            response = new JSONObject(resp);
        } catch (InterruptedException e) {
            //L.m(e + "");
        } catch (ExecutionException e) {
            // L.m(e + "");
        } catch (TimeoutException e) {
            //L.m(e + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return response;
    }

    public static JSONObject requestGetlikeunlike(RequestQueue requestQueue, String url,final String authkey, final String siteid, final String bid) {
        JSONObject response = null;
        String resp;
        RequestFuture<String> requestFuture = RequestFuture.newFuture();
        StringRequest request = new StringRequest(Request.Method.POST, url, requestFuture, requestFuture) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(AUTHKEY, authkey);
                params.put(SITEID, siteid);
                params.put(BID, bid);
                return params;
            }
        };

        requestQueue.add(request);
        try {
            resp = requestFuture.get(30000, TimeUnit.MILLISECONDS);
            response = new JSONObject(resp);
        } catch (InterruptedException e) {
            //L.m(e + "");
        } catch (ExecutionException e) {
            // L.m(e + "");
        } catch (TimeoutException e) {
            //L.m(e + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return response;
    }

    public static JSONObject requestReferQuery(RequestQueue requestQueue, String url,final String authkey,final String siteid, final String name, final String message, final String phone, final String email) {
        JSONObject response = null;
        String resp;
        RequestFuture<String> requestFuture = RequestFuture.newFuture();
        StringRequest request = new StringRequest(Request.Method.POST, url, requestFuture, requestFuture) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(AUTHKEY, authkey);
                params.put(SITEID, siteid);
                params.put(NAME, name);
                params.put("message", message);
                params.put(NUMBER, phone);
                params.put(EMAIL, email);
                return params;
            }
        };

        requestQueue.add(request);
        try {
            resp = requestFuture.get(30000, TimeUnit.MILLISECONDS);
            response = new JSONObject(resp);
        } catch (InterruptedException e) {
            //L.m(e + "");
        } catch (ExecutionException e) {
            // L.m(e + "");
        } catch (TimeoutException e) {
            //L.m(e + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return response;
    }

    public static JSONObject requestGetProfileDetail(RequestQueue requestQueue, String url, final String authkey) {
        JSONObject response = null;
        String resp;
        RequestFuture<String> requestFuture = RequestFuture.newFuture();
        StringRequest request = new StringRequest(Request.Method.POST, url, requestFuture, requestFuture) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(AUTHKEY, authkey);
                return params;
            }
        };

        requestQueue.add(request);
        try {
            resp = requestFuture.get(30000, TimeUnit.MILLISECONDS);
            response = new JSONObject(resp);
        } catch (InterruptedException e) {
            //L.m(e + "");
        } catch (ExecutionException e) {
            // L.m(e + "");
        } catch (TimeoutException e) {
            //L.m(e + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return response;
    }

    public static JSONObject requestSubmitQuery(RequestQueue requestQueue, String url, final String authkey,final String interest, final String query,final String siteid,final String beaconId) {
        JSONObject response = null;
        String resp;
        RequestFuture<String> requestFuture = RequestFuture.newFuture();
        StringRequest request = new StringRequest(Request.Method.POST, url, requestFuture, requestFuture) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(AUTHKEY, authkey);
                params.put(SITEID, siteid);
                params.put(INTERESTEDIN, interest);
                params.put(QUERY, query);
                params.put(BEACONID, beaconId);

                return params;
            }
        };

        requestQueue.add(request);
        try {
            resp = requestFuture.get(30000, TimeUnit.MILLISECONDS);
            response = new JSONObject(resp);
        } catch (InterruptedException e) {
            //L.m(e + "");
        } catch (ExecutionException e) {
            // L.m(e + "");
        } catch (TimeoutException e) {
            //L.m(e + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return response;
    }

    public static JSONObject requestRegister(RequestQueue requestQueue, String url, final String phone,final String name, final String email,final String password) {
        JSONObject response = null;
        String resp;
        RequestFuture<String> requestFuture = RequestFuture.newFuture();
        StringRequest request = new StringRequest(Request.Method.POST, url, requestFuture, requestFuture) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(NAME, name);
                params.put(EMAIL, email);
                params.put(NUMBER, phone);
                params.put(PASSWORD, password);
                return params;
            }
        };

        requestQueue.add(request);
        try {
            resp = requestFuture.get(30000, TimeUnit.MILLISECONDS);
            response = new JSONObject(resp);
        } catch (InterruptedException e) {
            //L.m(e + "");
        } catch (ExecutionException e) {
            // L.m(e + "");
        } catch (TimeoutException e) {
            //L.m(e + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return response;
    }

    public static JSONObject requestFeedback(RequestQueue requestQueue, String url, final String authkey, final String message) {
        JSONObject response = null;
        String resp;
        RequestFuture<String> requestFuture = RequestFuture.newFuture();
        StringRequest request = new StringRequest(Request.Method.POST, url, requestFuture, requestFuture) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(AUTHKEY, authkey);
                params.put(FEEDBACK, message);
                return params;
            }
        };

        requestQueue.add(request);
        try {
            resp = requestFuture.get(30000, TimeUnit.MILLISECONDS);
            response = new JSONObject(resp);
        } catch (InterruptedException e) {
            //L.m(e + "");
        } catch (ExecutionException e) {
            // L.m(e + "");
        } catch (TimeoutException e) {
            //L.m(e + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return response;
    }

    public static JSONObject requestgetOTP(RequestQueue requestQueue, String url, final String phone) {
        JSONObject response = null;
        String resp;
        RequestFuture<String> requestFuture = RequestFuture.newFuture();
        StringRequest request = new StringRequest(Request.Method.POST, url, requestFuture, requestFuture) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(PHONE, phone);
                return params;
            }
        };

        requestQueue.add(request);
        try {
            resp = requestFuture.get(30000, TimeUnit.MILLISECONDS);
            response = new JSONObject(resp);
        } catch (InterruptedException e) {
            //L.m(e + "");
        } catch (ExecutionException e) {
            // L.m(e + "");
        } catch (TimeoutException e) {
            //L.m(e + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return response;
    }

    public static JSONObject requestGetImages(RequestQueue requestQueue, String url, final String sietId) {
        JSONObject response = null;
        String resp;
        RequestFuture<String> requestFuture = RequestFuture.newFuture();
        StringRequest request = new StringRequest(Request.Method.POST, url, requestFuture, requestFuture) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(SITEID, sietId);
                return params;
            }
        };

        requestQueue.add(request);
        try {
            resp = requestFuture.get(30000, TimeUnit.MILLISECONDS);
            response = new JSONObject(resp);
        } catch (InterruptedException e) {
            //L.m(e + "");
        } catch (ExecutionException e) {
            // L.m(e + "");
        } catch (TimeoutException e) {
            //L.m(e + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return response;
    }

    public static JSONObject requestDeleteSite(RequestQueue requestQueue, String url, final String sietId,  final String authkey) {
        JSONObject response = null;
        String resp;
        RequestFuture<String> requestFuture = RequestFuture.newFuture();
        StringRequest request = new StringRequest(Request.Method.POST, url, requestFuture, requestFuture) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(SITEID, sietId);
                params.put(AUTHKEY, authkey);
                return params;
            }
        };

        requestQueue.add(request);
        try {
            resp = requestFuture.get(30000, TimeUnit.MILLISECONDS);
            response = new JSONObject(resp);
        } catch (InterruptedException e) {
            //L.m(e + "");
        } catch (ExecutionException e) {
            // L.m(e + "");
        } catch (TimeoutException e) {
            //L.m(e + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return response;
    }

    public static JSONObject requestChangePass(RequestQueue requestQueue, String url, final String phone ,final String otp,final  String password) {
        JSONObject response = null;
        String resp;
        RequestFuture<String> requestFuture = RequestFuture.newFuture();
        StringRequest request = new StringRequest(Request.Method.POST, url, requestFuture, requestFuture) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(OTP, otp);
                // Log.d("OTP", otp);
                params.put(NUMBER, phone);
                //  Log.d("OTP",phone);
                params.put(PASSWORD, password);
                return params;
            }
        };

        requestQueue.add(request);
        try {
            resp = requestFuture.get(30000, TimeUnit.MILLISECONDS);
            response = new JSONObject(resp);
        } catch (InterruptedException e) {
            //L.m(e + "");
        } catch (ExecutionException e) {
            // L.m(e + "");
        } catch (TimeoutException e) {
            //L.m(e + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return response;
    }

    public static JSONObject requestUpdateUserProfile(RequestQueue requestQueue, String url, final String authkey,final String name, final String email, final String dob,final String gender,final String password, final String image) {
        JSONObject response = null;
        String resp;
        RequestFuture<String> requestFuture = RequestFuture.newFuture();
        StringRequest request = new StringRequest(Request.Method.POST, url, requestFuture, requestFuture) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(AUTHKEY, authkey);
                params.put(NAME, name);
                params.put(EMAIL, email);
                params.put(DOB, dob);
                params.put(GENDER, gender);
                params.put(PASSWORD, password);
                params.put("image", image);
                return params;
            }
        };

        requestQueue.add(request);
        try {
            resp = requestFuture.get(30000, TimeUnit.MILLISECONDS);
            response = new JSONObject(resp);
        } catch (InterruptedException e) {
            //L.m(e + "");
        } catch (ExecutionException e) {
            // L.m(e + "");
        } catch (TimeoutException e) {
            //L.m(e + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return response;
    }

    public static JSONObject requestGetlocationDetail(RequestQueue requestQueue, String url, final String bid, final String beaconId,final String authkey) {
        JSONObject response = null;
        String resp;
        RequestFuture<String> requestFuture = RequestFuture.newFuture();
        StringRequest request = new StringRequest(Request.Method.POST, url, requestFuture, requestFuture) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ID, bid);
                params.put(BEACONID, beaconId);
                params.put(AUTHKEY, authkey);

                return params;
            }
        };

        requestQueue.add(request);
        try {
            resp = requestFuture.get(30000, TimeUnit.MILLISECONDS);
            response = new JSONObject(resp);
        } catch (InterruptedException e) {
            //L.m(e + "");
        } catch (ExecutionException e) {
            // L.m(e + "");
        } catch (TimeoutException e) {
            //L.m(e + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return response;
    }

    public static JSONObject requestGetSiteDetail(RequestQueue requestQueue, String url, final String authkey, final String siteId) {
        JSONObject response = null;
        String resp;
        RequestFuture<String> requestFuture = RequestFuture.newFuture();
        StringRequest request = new StringRequest(Request.Method.POST, url, requestFuture, requestFuture) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(AUTHKEY, authkey);
                params.put(SITEID, siteId);
                return params;
            }
        };

        requestQueue.add(request);
        try {
            resp = requestFuture.get(30000, TimeUnit.MILLISECONDS);
            response = new JSONObject(resp);
        } catch (InterruptedException e) {
            //L.m(e + "");
        } catch (ExecutionException e) {
            // L.m(e + "");
        } catch (TimeoutException e) {
            //L.m(e + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return response;
    }

    public static JSONObject requestGetAllSites(RequestQueue requestQueue, String url, final String authkey, final String offset, final String limit) {
        JSONObject response = null;
        String resp;
        RequestFuture<String> requestFuture = RequestFuture.newFuture();
        StringRequest request = new StringRequest(Request.Method.POST, url, requestFuture, requestFuture) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(AUTHKEY, authkey);
                params.put(OFFSET, offset);
                params.put(LIMIT, limit);

                return params;
            }
        };

        requestQueue.add(request);
        try {
            resp = requestFuture.get(30000, TimeUnit.MILLISECONDS);
            response = new JSONObject(resp);
        } catch (InterruptedException e) {
            //L.m(e + "");
        } catch (ExecutionException e) {
            // L.m(e + "");
        } catch (TimeoutException e) {
            //L.m(e + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return response;
    }

    public static JSONObject requestVistDetail(RequestQueue requestQueue, String url, final String authkey) {
        JSONObject response = null;
        String resp;
        RequestFuture<String> requestFuture = RequestFuture.newFuture();
        StringRequest request = new StringRequest(Request.Method.POST, url, requestFuture, requestFuture) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(AUTHKEY, authkey);

                return params;
            }
        };

        requestQueue.add(request);
        try {
            resp = requestFuture.get(30000, TimeUnit.MILLISECONDS);
            response = new JSONObject(resp);
        } catch (InterruptedException e) {
            //L.m(e + "");
        } catch (ExecutionException e) {
            // L.m(e + "");
        } catch (TimeoutException e) {
            //L.m(e + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return response;
    }
}

