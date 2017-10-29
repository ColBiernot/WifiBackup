package com.sbeyer.wifibackup;
////////////////////////////////////////////////////////////////////////////////////////////////////
//  CustomListAdapter.java
//
//
//  Autor: Simon Beyer
//  Letzte Änderung: 20.11.2015
////////////////////////////////////////////////////////////////////////////////////////////////////
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.sbeyer.wifibackup.R;

import java.security.acl.Owner;
import java.util.regex.Pattern;

import static android.R.attr.label;

class CustomListAdapter extends ArrayAdapter<String> {

    final Context context = getContext();

    String id;
    String ssid;
    String password;
    String owner;
    String comment;
    String location;
    String provider;

    String bulletChar = "\u2022";
    String passwordToShow;


    public CustomListAdapter(Context context, String[] dataListFinal) {
        super(context, R.layout.listitem_wifilist ,dataListFinal);
    }
    public String allElementsAdapter = "";

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        LayoutInflater iteminflater = LayoutInflater.from(getContext());
        View customView = iteminflater.inflate(R.layout.listitem_wifilist, parent, false);

        ImageView wifiImage = (ImageView) customView.findViewById(R.id.list_icon_wifi);
        ImageView providerImage = (ImageView) customView.findViewById(R.id.list_icon_provider);
        TextView textViewSsid = (TextView) customView.findViewById(R.id.list_item_datalist_textview_ssid);
        final TextView textViewPassword = (TextView) customView.findViewById(R.id.list_item_datalist_textview_password);
        TextView textViewOwner = (TextView) customView.findViewById(R.id.list_item_datalist_textview_owner);

        final String singleListItem = getItem(position);
        final String[] singleListItemArray = singleListItem.split("\t");
        id = singleListItemArray[0];
        //Log.d("0", singleListItemArray[0]);
        ssid = singleListItemArray[1];
        //Log.d("1", singleListItemArray[1]);
        password = singleListItemArray[2];
        //Log.d("2", singleListItemArray[2]);
        owner = singleListItemArray[4];
        //Log.d("3", singleListItemArray[3]);
        comment = singleListItemArray[5];
        //Log.d("4", singleListItemArray[4]);
        location = singleListItemArray[6];
        //Log.d("5", singleListItemArray[5]);
        provider = singleListItemArray[7];
        //Log.d("6", singleListItemArray[6]);


        allElementsAdapter = id + ssid + password + owner + comment + location + provider;

        wifiImage.setImageResource(R.drawable.wifi_black);

        switch (provider) {
            case "0":
                providerImage.setImageResource(R.drawable.providericon_otherprovider);
                break;
            case "1":
                providerImage.setImageResource(R.drawable.providericon_telekom);
                break;
            case "2":
                providerImage.setImageResource(R.drawable.providericon_unitymedia);
                break;
            case "3":
                providerImage.setImageResource(R.drawable.providericon_vodafone);
                break;
            case "4":
                providerImage.setImageResource(R.drawable.providericon_einsundeins);
                break;
            case "5":
                providerImage.setImageResource(R.drawable.providericon_einsundeins);
                break;
        }

        String ownerString = owner;

        textViewSsid.setText(ssid);

        textViewPassword.setText(setPasswordInvisibile(password));
        textViewOwner.setText(ownerString);

/*
        imagebuttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String [] tmpDataArray = singleListItem.split("\t");
                ((MainActivity)context).openDialog("Edit", tmpDataArray);
            }
        });

        imagebuttonSetVisibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                imagebuttonSetVisibility.setVisibility(View.GONE);
                imagebuttonSetVisibility2.setVisibility(View.VISIBLE);

                String [] tmpDataArray = singleListItem.split("\t");
                textViewPassword.setText(setPasswordInvisibile(tmpDataArray[2]));

            }
        });

        imagebuttonSetVisibility2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                imagebuttonSetVisibility2.setVisibility(View.GONE);
                imagebuttonSetVisibility.setVisibility(View.VISIBLE);

                String [] tmpDataArray = singleListItem.split("\t");
                textViewPassword.setText(tmpDataArray[2]);

            }
        });
*/
        return customView;
    }

    private String setPasswordInvisibile(String pwd){
        int passwordLength = pwd.length();

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < passwordLength; i++) {
            builder.append(bulletChar);
            passwordToShow = builder.toString();
        }

        return passwordToShow;
    }
}
