package com.energysistem.testapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import java.io.File;
import java.util.Date;
import java.util.Locale;

import jxl.*;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;


public class MainActivity extends Activity {
private int tamanoLista;
    // Log tag
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String url = "https://gist.githubusercontent.com/anonymous/e5dcf0c22f563a05de06/raw/aaa592b23268f34ec75894602612b3e20fd78091/appList.json";
    private ProgressDialog pDialog;
    private List<AppInfo> appList = new ArrayList<AppInfo>();
    private ListView listView;
    private CustomListAdapter adapter;
    private Button botonExportar;

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
        botonExportar = (Button) findViewById(R.id.boton_exportar);

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

                        botonExportar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                exportarExcel(adapter.getAppInfoList());
                            }
                        });
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

    private final WritableCellFormat getCellFormat(Colour colour) throws WriteException {

        WritableFont cellFonts = new WritableFont(WritableFont.TIMES, 12, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLACK);
        WritableCellFormat cellFormats = new WritableCellFormat(cellFonts);
        if(colour!=null)
            cellFormats.setBackground(colour);
        return cellFormats;
    }
    public void exportarExcel(List<AppInfo> listaAPP)
    {
        final String fileName = Build.DEVICE+"_AppCompatibility_Test.xls";
        final String directoryName = "/AppTesting";

        //Saving file in external storage
        File sdCard = Environment.getExternalStorageDirectory();
        File directory = new File(sdCard.getAbsolutePath() + directoryName);
       // File directory = new File(sdCard.getAbsolutePath());

        //create directory if not exist
        if(!directory.isDirectory()){
            directory.mkdirs();
        }

        //file path
        File file = new File(directory, fileName);

        WorkbookSettings wbSettings = new WorkbookSettings();
        wbSettings.setLocale(new Locale("en", "EN"));
        WritableWorkbook workbook;

        try {
            workbook = Workbook.createWorkbook(file, wbSettings);
            //Excel sheet name. 0 represents first sheet
            WritableSheet sheet = workbook.createSheet(Build.DEVICE, 0); //Creamos un sheet con el nombre del device

            try {

                sheet.addCell(new Label(0, 0, Build.DEVICE,getCellFormat(Colour.LIGHT_BLUE))); // column and row
                sheet.addCell(new Label(1, 0, "Aplicación",getCellFormat(Colour.LIGHT_BLUE))); // column and row
                sheet.addCell(new Label(2, 0, "Aparece",getCellFormat(Colour.LIGHT_BLUE)));
                sheet.addCell(new Label(3, 0, "Instala",getCellFormat(Colour.LIGHT_BLUE)));
                sheet.addCell(new Label(4, 0, "Funciona",getCellFormat(Colour.LIGHT_BLUE)));
                int i = 1;
                int highestGrande = 15;
                int highestPequenio = 8;
                for (final AppInfo app : listaAPP) {

                    String title = app.getAppName();
                    String aparece = app.isAparece() ? "Si" : "No";
                    String instala = app.isInstala() ? "Si" : "No";
                    String funciona = app.isFunciona() ? "Si" : "No";


                    sheet.addCell(new Label(1, i, title,getCellFormat(null)));
                    sheet.addCell(new Label(2, i, aparece,aparece.equals("Si") ? getCellFormat(Colour.LIGHT_GREEN) : getCellFormat(Colour.RED)));
                    sheet.addCell(new Label(3, i, instala,instala.equals("Si") ? getCellFormat(Colour.LIGHT_GREEN) : getCellFormat(Colour.RED)));
                    sheet.addCell(new Label(4, i, funciona,funciona.equals("Si") ? getCellFormat(Colour.LIGHT_GREEN) : getCellFormat(Colour.RED)));
                    i++;
                }
                for(int s = 0; s < sheet.getColumns(); s++)
                {
                    CellView cell=sheet.getColumnView(s);
                    //cell.setAutosize(true);
                    if(s==0 || s==1) //Para la columna "Dispositivo" y "Titulo aplicación" le ponemos un tamaño
                        cell.setSize((highestGrande + ((highestGrande/2) + (highestGrande/4))) * 256);

                    else
                        cell.setSize((highestPequenio + ((highestPequenio/2) + (highestPequenio/4))) * 256);
                    sheet.setColumnView(s, cell);
                }

                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                shareIntent.setType("application/vnd.ms-excel");
                startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.send_to)));

            } catch (RowsExceededException e) {
                e.printStackTrace();
            } catch (WriteException e) {
                e.printStackTrace();
            }
            workbook.write();
            try {
                workbook.close();
            } catch (WriteException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
