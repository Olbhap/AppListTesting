package com.energysistem.testapp.adapter;

/**
 * Created by pgc on 08/05/2015.
 */
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.energysistem.testapp.R;
import com.energysistem.testapp.model.AppInfo;

public class CustomListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;

    public List<AppInfo> getAppInfoList() {
        return appInfoList;
    }

    private List<AppInfo> appInfoList;

    public CustomListAdapter(Activity activity, List<AppInfo> appInfoList) {
        this.activity = activity;
        this.appInfoList = appInfoList;
    }

    @Override
    public int getCount() {
        return appInfoList.size();
    }

    @Override
    public Object getItem(int location) {
        return appInfoList.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_row, null);

        TextView title = (TextView) convertView.findViewById(R.id.titulo_app);
        CheckBox check_aparece = (CheckBox) convertView.findViewById(R.id.check_aparece);
        CheckBox check_instala = (CheckBox) convertView.findViewById(R.id.check_instala);
        CheckBox check_funciona = (CheckBox) convertView.findViewById(R.id.check_funciona);




        // getting movie data for the row
        final AppInfo m = appInfoList.get(position);
        // title
        title.setText(m.getAppName());

        check_aparece.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                m.setAparece(isChecked);

            }
        });check_funciona.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                m.setFunciona(isChecked);
            }
        });check_instala.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               m.setInstala(isChecked);
            }
        });


        Button botonPlay = (Button) convertView.findViewById(R.id.boton_comprobar);
        botonPlay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                try {
                    activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(
                            "market://details?id=" + m.getPackageName())));
                } catch (android.content.ActivityNotFoundException anfe) {
                    activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(
                            "http://play.google.com/store/apps/details?id=" + m.getPackageName())));
                }
                arg0.setBackgroundColor(activity.getResources().getColor(R.color.verdeChecked));
            }
        });

        return convertView;
    }

}