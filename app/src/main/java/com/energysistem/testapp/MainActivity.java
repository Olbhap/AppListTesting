package com.energysistem.testapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.energysistem.testapp.adapter.CustomListAdapter;
import com.energysistem.testapp.app.AppController;
import com.energysistem.testapp.model.AppInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {
private int tamanoLista;
    // Log tag
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String url = "https://gist.githubusercontent.com/Olbhap/0dad229103cd1d06171a/raw/84307604a3e3d63134b4ac1dbcd1d9717e636a07/appTest";
    private ProgressDialog pDialog;
    private List<AppInfo> appList = new ArrayList<AppInfo>();
    private ListView listView;
    private CustomListAdapter adapter;
    @Override
    protected void onStart() {
        super.onStart();
        Log.e("Life Cycle","onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("Life Cycle","onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("Life Cycle","onResume");
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Log.e("Life Cycle","onPostResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("Life Cycle","onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("Life Cycle","onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hidePDialog();
        Log.e("Life Cycle","onDestroy");
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        listView = (ListView) findViewById(R.id.list);
        adapter = new CustomListAdapter(this, appList);
        listView.setAdapter(adapter);

        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();

        // Creating volley request obj
        JsonArrayRequest movieReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        hidePDialog();

                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                AppInfo app = new AppInfo();
                                app.setAppName(obj.getString("appName"));
                                app.setPackageName(obj.getString("packageName"));
                                app.setAparece(obj.getBoolean("aparece"));

                                // adding movie to movies array
                                appList.add(app);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hidePDialog();

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);



        /*final ArrayList<String> listPackages = new ArrayList<String>();
        listPackages.add("com.facebook.katana");
        listPackages.add("com.amazon.windowshop");
        listPackages.add("com.amazon.mShop.android.shopping");
        listPackages.add("com.shazam.android");
        listPackages.add("com.supercell.clashofclans");
        listPackages.add("com.venticake.retrica");
        listPackages.add("com.soundcloud.android");
        tamanoLista = listPackages.size();

        b_comprueba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(tamanoLista > 0) {
                    final String appPackageName = listPackages.get(tamanoLista-1); // getPackageName() from Context or Activity object
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(
                                "market://details?id=" + appPackageName)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(
                                "http://play.google.com/store/apps/details?id=" + appPackageName)));
                    }
                    tamanoLista--;
                }
            }
        });*/





    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
