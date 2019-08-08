package com.example.apptoerpn;

import androidx.appcompat.app.AppCompatActivity;

//MPRP Commmented some imports which shows error
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
//import android.support.v4.app.DialogFragment;
import android.os.Bundle;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.textclassifier.TextLinks;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.example.apptoerpn.Constants;
import static android.content.Context.MODE_PRIVATE;



import com.example.apptoerpn.R;
//import com.example.apptoerpn.implementation.ServiceLocatorImpl;


/* import in.epochconsulting.erpnext.mprp.R;
import in.epochconsulting.erpnext.mprp.activity.Home;
import in.epochconsulting.erpnext.mprp.common.BasicActivity;
import in.epochconsulting.erpnext.mprp.fragments.DatePickerFragment;
import in.epochconsulting.erpnext.mprp.model.pojo.SingletonData;
import in.epochconsulting.erpnext.mprp.request_items.adapter.RequestItemsAdapter;
import in.epochconsulting.erpnext.mprp.request_items.model.RequestItemList;
import in.epochconsulting.erpnext.mprp.request_items.model.RequestedItemDetailsModel;
import in.epochconsulting.erpnext.mprp.request_items.pojo.RequestItemsDetailsList;
import in.epochconsulting.erpnext.mprp.request_items.pojo.RequestedItemServerData;
import in.epochconsulting.erpnext.mprp.request_items.view.CustomRequestedItemsViewHolder;
import in.epochconsulting.erpnext.mprp.utils.AlertDialogHandler;
import in.epochconsulting.erpnext.mprp.utils.Constants;
import in.epochconsulting.erpnext.mprp.utils.CustomUrl;
import in.epochconsulting.erpnext.mprp.utils.Utility;

import static in.epochconsulting.erpnext.mprp.request_items.model.RequestItemsDataFactory.makeRequestItemsList; */

//MPRP

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    String username = "administrator";
    JSONObject jsonObject;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button ConnectButton  = (Button)findViewById(R.id.btnConnect);


        ConnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("****************************Suresh Connect Button clicked**************************************");
                requestLogin();

                CookieManager cookieManager = new CookieManager(new PersistentCookieStoreManager(MainActivity.this), CookiePolicy.ACCEPT_ORIGINAL_SERVER);
                CookieHandler.setDefault(cookieManager);

            }
        });
    }

    //from MPrp login
    private void requestLogin() {
        final RequestQueue requestQueue1 = Volley.newRequestQueue(MainActivity.this);

        //String  myUrl = "http://192.168.0.62:8000/api/method/login"; //developer lap url
        String  myUrl = "http://192.168.0.15/api/method/login";  //localhost url

        System.out.println("Suresh ************ From requestLogin "+myUrl);

        StringRequest stringRequest = new StringRequest(Request.Method.POST,myUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String loginResponse = null;


                try {
                    if(response!=null){
                        jsonObject = new JSONObject(response);
                    }
                    loginResponse = jsonObject.get("message").toString();
                    if (loginResponse!=null && loginResponse.equalsIgnoreCase(Constants.LOGIN_RESPONSE) ) {
                        String loggedUser = jsonObject.get("full" +
                                "_name").toString();
                        System.out.println("Suresh ************From requestLogin ************************ : "+loggedUser);

                        //added on 25th Oct 2017 to tie user down to a warehouse
                        getLoggedInUserData();

                    } else {
                        Toast.makeText(getApplicationContext(),"loginResponse is null" , Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    Log.e("ERROR",e.toString());
                }
            }


        }//end of sucess response
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Suresh ************ From requestLogin Error in login connection ");

                Toast.makeText(getApplicationContext(),"Error in login connection" , Toast.LENGTH_LONG).show();


            }
        })
                //end of error response and stringRequest params
        {
            //This is for providing body for Post request@Override
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> mapObject = new HashMap<>();
                mapObject.put(Constants.KEY_NAME, "administrator");
                mapObject.put(Constants.KEY_PASS, "password");
                return mapObject;

            }
        };


        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        requestQueue1.add(stringRequest);

    }

    private void getLoggedInUserData() {
        RequestQueue requestQueue2 = Volley.newRequestQueue(this);

        //StringRequest stringRequest1 = new StringRequest()

        //String myUrl2 = "http://192.168.0.62:8000/api/method/frappe.auth.get_logged_user"; //dev lap url
        String myUrl2 = "http://192.168.0.15/api/method/frappe.auth.get_logged_user";//localhost url
       // String myUrl2 = "http://192.168.0.109:8000/api/method/login?usr=Administrator&pwd=password";

        System.out.println("Suresh ************ From Home getLoggedInUserData() : " );


        StringRequest stringRequest = new StringRequest(Request.Method.PUT, myUrl2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject object = new JSONObject(response);
                    //String loggedInUser = object.getString("message");
                    System.out.println("Suresh ************ From getLoggedInUserData Response came for this sec login JSONObject object "+object );
                    System.out.println("Suresh ************ From getLoggedInUserData Response came for this sec login JSONObject object "+object.getString("message") );

                    System.out.println("Suresh ************ From getLoggedInUserData Response came for this sec login  ");

                    System.out.println("Suresh ************ From getLoggedInUserData Response came for this sec login  ");
                    populatetheDataModel(username);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }//end of sucess responseP
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                System.out.println("Suresh ************ From getLoggedInUserData fragments Errorrrr in login connection ");

                Toast.makeText(getApplicationContext(),"Error in getLoggedInUserData connection" , Toast.LENGTH_LONG).show();


            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                return MainActivity.this.getHeaders();
            }

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue2.add(stringRequest);


    }
    public Map<String, String> getHeaders () {
        Map<String, String> headers = new HashMap<>();
        SharedPreferences prefs = this.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        String userId = prefs.getString(Constants.USER_ID, null);
        String sid = prefs.getString(Constants.SESSION_ID, null);
        headers.put("user_id", userId);
        headers.put("sid", sid);

        System.out.println("Suresh ************ From Home userId : "+ userId);
        System.out.println("Suresh ************ From Home sid : "+ sid);

        return headers;
    }

    //from MPrp logins


    private void populatetheDataModel(String username) throws JSONException {

        System.out.println("****************************Suresh from populatetheDataModel**************************************");
        String new_url ="http://192.168.0.15/api/resource/Serial%20No/10000" ;

        JSONObject rfid_data = new JSONObject();

        // putting data to JSONObject
        rfid_data.put("pch_rfid_tag2", "90");

        System.out.println("****************************Suresh from populatetheDataModel rfid_data**************************************"+ rfid_data);




        String url = "http://192.168.0.15/api/method/nhance.api.android_api_test"; //lap


        final RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

        //Json object request

        // prepare the Request
        JsonObjectRequest JsonRequest = new JsonObjectRequest(Request.Method.PUT, new_url,rfid_data,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        System.out.println("****************************JSON Object Response came **************************************"+response.toString());

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("****************************JSON Object Erro responce came **************************************");
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                return MainActivity.this.getHeaders_one();
            }

        };

// add it to the RequestQueue
        requestQueue.add(JsonRequest);

        //Json object request

    }

    public Map<String, String> getHeaders_one () {
        Map<String, String> headers = new HashMap<>();
        SharedPreferences prefs = this.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        String userId = prefs.getString(Constants.USER_ID, null);
        String sid = prefs.getString(Constants.SESSION_ID, null);
        headers.put("user_id", userId);
        headers.put("sid", sid);
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json");
        return headers;
    }
}
