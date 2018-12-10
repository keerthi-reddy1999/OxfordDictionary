package com.example.keerthireddyade.volleydictionary;

import android.app.DownloadManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private Button button;
    private EditText editText;
    private TextView textView;
    String serverurl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.button);
        editText = findViewById(R.id.edit_text);
        textView = findViewById(R.id.text_view);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                serverurl=dictionaryEntries();
                performreq(serverurl);
            }
        });
    }
    private String dictionaryEntries()
    {
        final String language = "en";
        final String word = editText.getText().toString();
        final String word_id = word.toLowerCase();
        return "https://od-api.oxforddictionaries.com:443/api/v1/entries/" + language + "/" + word_id;
    }
        public void performreq(final String serverurl)
        {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, serverurl,
                    new com.android.volley.Response.Listener<String>() {
                String def;
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject je=new JSONObject(response);
                                JSONArray results=je.getJSONArray("results");

                                JSONObject lEntries=results.getJSONObject(0);
                                JSONArray laArray=lEntries.getJSONArray("lexicalEntries");

                                JSONObject entries=laArray.getJSONObject(0);
                                JSONArray e=entries.getJSONArray("entries");

                                JSONObject jsonObject=e.getJSONObject(0);
                                JSONArray sensesArray=jsonObject.getJSONArray( "senses");

                                JSONObject d=sensesArray.getJSONObject(0);
                                JSONArray de=d.getJSONArray("definitions");

                                def=de.getString(0);
                                textView.setText(def);
                            }catch(Exception e){}
                           //The string response contains the response you get back from the request it could be json, xml or just string
                        }
                    },
                    new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<String, String>();

                    //Add headers here as headers.put("key","value");
                    headers.put("Accept","application/json");
                    headers.put("app_id","75c8d060") ;
                    headers.put("app_key","38d95c38ec2fa21c221a9f6acf2a8dc6");

                    return headers;
                }
                @Override
                public Map<String, String> getParams() {
                    Map<String, String> mparams = new HashMap<>();
                    //Add your parameters here as mparams.put("key","value"); you will most probably use this method when doing a post request
                    return mparams;
                }
                @Override
                public String getBodyContentType() {
                    return "application/json";
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }
    }
