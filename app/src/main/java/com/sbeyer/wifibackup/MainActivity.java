package com.sbeyer.wifibackup;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import static com.sbeyer.wifibackup.R.id.editText_SSID;
import static com.sbeyer.wifibackup.R.id.textView_showPassword;

public class MainActivity extends AppCompatActivity {

    DbHelper mydb;

    String dataList = "";
    String selectedFromList;
    String[] selectedFromListArray;
    String[] emptyArray;


    String inputStringSSID = "";
    String inputStringPassword = "";
    String inputStringOwner = "";
    String inputStringComment = "";
    String inputStringLocation = "";
    String inputStringProvider = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mydb = new DbHelper(this);



        dataList = mydb.getDataSortedBySsid();
        //Log.d("TESTLISTDA", dataList);

         if (!dataList.isEmpty()) {

            final ListAdapter WifiList_Adapter = new CustomListAdapter(this, getDataListArray(dataList));
            final ListView wifiListView = (ListView) findViewById(R.id.listView_datalist);

            wifiListView.setAdapter(WifiList_Adapter);

            wifiListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                    Log.d("onItemClick", "detected");

                    selectedFromList = (wifiListView.getItemAtPosition(i).toString());
                    selectedFromListArray = selectedFromList.split("\t");

                    final PopupMenu popup = new PopupMenu(MainActivity.this, view);
                    popup.getMenuInflater().inflate(R.menu.menu_popup, popup.getMenu());

                    //registering popup with OnMenuItemClickListener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {

                            performPopupAction((String) item.getTitle(), selectedFromListArray);

                            return true;
                        }
                    });

                    popup.show();//showing popup menu

                }
            });
        }
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // Open Settings
                return true;

            case R.id.action_syncSavedWifi:
                mydb.setData("WLAN_SJ_2.4GHz", "130504112303SB", "WPA2", "Simon & Joana", "erweitert WLAN_F", "Saarbrücker Straße 15, 33613 Bielefeld", "1", "25k", "tmp", "tmp", "2,4");
                mydb.setData("WLAN_F", "JonasSimonMicha", "WPA2", "Frye", "leer", "Saarbrücker Straße 15", "1", "25k", "tmp", "tmp", "2,4");
                mydb.setData("WLAN-TEST789", "testpwd", "TEST", "Dr. Nothdurft", "TESTKOMMENTAR3", "Bielefeld", "2", "25k", "tmp", "tmp", "2,4");
                mydb.setData("ZZZZ", "testpwd", "TEST", "Dr. Nothdurft", "TESTKOMMENTAR3", "Bielefeld", "3", "25k", "tmp", "tmp", "2,4");
                mydb.setData("WLAN-TEST782349", "testpwd", "TEST", "Dr. Brandt", "TESTKOMMENTAR3", "Bielefeld", "4", "25k", "tmp", "tmp", "2,4");
                mydb.setData("WLAN-TEST782349", "testpwd", "TEST", "Dr. Bond", "TESTKOMMENTAR3", "Bielefeld", "5", "25k", "tmp", "tmp", "2,4");
                return true;


            case R.id.action_add:
                // Open "Add Wifi" Dialog
                openDialog("Add", emptyArray);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults) {

        switch (permsRequestCode) {

            case 200:

                boolean accessWifiStateAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                break;

        }
    }


    public void openViewDialog(String Case, String[] selectedFromListArray){
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);

        View subViewPwdView = inflater.inflate(R.layout.view_details, null);

        final TextView showPasswordTextView = (TextView) subViewPwdView.findViewById(textView_showPassword);

        showPasswordTextView.setText(selectedFromListArray[2]);

        if (Case.equals("Show")) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(selectedFromListArray[1]);
            builder.setMessage(selectedFromListArray[3]);
            builder.setView(subViewPwdView);
            AlertDialog alertDialog = builder.create();


            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(MainActivity.this, "OK", Toast.LENGTH_SHORT).show();
                }
            });
            builder.show();
        }
    }

    public void openDialog(String Case, String[] selectedFromListArray) {
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);

        View subView = inflater.inflate(R.layout.change_data, null);

        final EditText ssidEditText = (EditText) subView.findViewById(editText_SSID);
        final EditText passwordEditText = (EditText) subView.findViewById(R.id.editText_Password);
        passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        final EditText ownerEditText = (EditText) subView.findViewById(R.id.editText_Owner);
        final EditText commentEditText = (EditText) subView.findViewById(R.id.editText_comment);
        final EditText locationEditText = (EditText) subView.findViewById(R.id.editText_location);


        if ((passwordEditText.hasFocus() || ownerEditText.hasFocus() || commentEditText.hasFocus()) && ssidEditText.getText().toString().trim().equals("")){
            ssidEditText.setError("SSID REQUIRED");
        }


        final Spinner providerSpinner = (Spinner) subView.findViewById(R.id.spinner_Provider);
        SimpleImageArrayAdapter adapter = new SimpleImageArrayAdapter(this,
                new Integer[]{R.drawable.spinnericon_otherprovider, R.drawable.spinnericon_telekom, R.drawable.spinnericon_unitymedia, R.drawable.spinnericon_vodafone, R.drawable.spinnericon_otwo, R.drawable.spinnericon_einsundeins});
        providerSpinner.setAdapter(adapter);


        //[0] = unknown
        //[1] = telekom
        //[2] = unitymedia
        //[3] = vodafone
        //[4] = O2
        //[5] = 1und1

        providerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                checkSpinner(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        if (Case.equals("Add")) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getResources().getString(R.string.add));
            builder.setMessage(getResources().getString(R.string.addWifiMessage));
            builder.setView(subView);
            AlertDialog alertDialog = builder.create();


            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    inputStringSSID = ssidEditText.getText().toString();
                    inputStringPassword = passwordEditText.getText().toString();
                    passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    inputStringOwner = ownerEditText.getText().toString();
                    inputStringComment = commentEditText.getText().toString();
                    inputStringLocation = locationEditText.getText().toString();

                    if (inputStringProvider.equals("")){
                        inputStringProvider = "0";
                    }

                    mydb.setData(inputStringSSID, inputStringPassword, "tmp", inputStringOwner, inputStringComment, inputStringLocation, inputStringProvider,  "tmp", "tmp", "tmp", "tmp");

                    reloadListView();

                    Toast.makeText(MainActivity.this, "saved", Toast.LENGTH_SHORT).show();
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(MainActivity.this, "Cancel", Toast.LENGTH_SHORT).show();
                }
            });

            builder.show();

        } else if (Case.equals("Edit")) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getResources().getString(R.string.edit));
            builder.setMessage(getResources().getString(R.string.editWifiMessage));
            builder.setView(subView);
            AlertDialog alertDialog = builder.create();

            final String selectedElementId = selectedFromListArray[0];

            //Log.d("selectedElementId", selectedElementId.toString());
            String selectedElementString = mydb.getDataById(selectedElementId);
            //Log.d("TEST", selectedElementString);

            final String[] selectedElementArray = selectedElementString.split("\t");

            //[0] = leid
            //[1] = ssid
            //[2] = password
            //[3] = security
            //[4] = owner
            //[5] = comment
            //[6] = location
            //[7] = provider

            ssidEditText.setText(selectedElementArray[1]);
            passwordEditText.setText(selectedElementArray[2]);
            ownerEditText.setText(selectedElementArray[4]);
            commentEditText.setText(selectedElementArray[5]);
            locationEditText.setText(selectedElementArray[6]);

            //checkSpinner(selectedElementArray, providerSpinner);
            //Log.d("PROVIDER", selectedElementArray[6]);
            if (selectedElementArray[7].contains("0")){
                //Log.d("PROVIDER", selectedElementArray[6]);
                providerSpinner.setSelection(0);
            }else if (selectedElementArray[7].contains("1")){
                //Log.d("PROVIDER", selectedElementArray[6]);
                providerSpinner.setSelection(1);
            }else if (selectedElementArray[7].contains("2")){
                //Log.d("PROVIDER", selectedElementArray[6]);
                providerSpinner.setSelection(2);
            }else if (selectedElementArray[7].contains("3")){
                //Log.d("PROVIDER", selectedElementArray[6]);
                providerSpinner.setSelection(3);
            }else if (selectedElementArray[7].contains("4")){
                //Log.d("PROVIDER", selectedElementArray[6]);
                providerSpinner.setSelection(4);
            }else if (selectedElementArray[7].contains("5")){
                //Log.d("PROVIDER", selectedElementArray[6]);
                providerSpinner.setSelection(5);
            }

            //providerSpinner.setSelection(Integer.parseInt(selectedElementArray[3]));

            builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    inputStringSSID = ssidEditText.getText().toString();
                    inputStringPassword = passwordEditText.getText().toString();
                    inputStringOwner = ownerEditText.getText().toString();
                    inputStringComment = commentEditText.getText().toString();
                    inputStringLocation = locationEditText.getText().toString();

                    if (inputStringProvider.equals("")){
                        inputStringProvider = "0";
                    }
                    mydb.deleteDataById(selectedElementArray[0]);
                    mydb.setData(inputStringSSID, inputStringPassword, "", inputStringOwner, inputStringComment, inputStringLocation, inputStringProvider, "tmp", "tmp", "tmp", "tmp");

                    reloadListView();

                    Toast.makeText(MainActivity.this, "saved", Toast.LENGTH_SHORT).show();
                }
            });

            builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(MainActivity.this, "Cancel", Toast.LENGTH_SHORT).show();
                }
            });

            builder.setNeutralButton(getResources().getString(R.string.delete), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mydb.deleteDataById(selectedElementId);

                    Toast.makeText(MainActivity.this, selectedElementArray[2] + " deleted", Toast.LENGTH_SHORT).show();

                    reloadListView();
                }
            });
            builder.show();
        } else if (Case.equals("Edit")) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getResources().getString(R.string.edit));
            builder.setMessage(getResources().getString(R.string.editWifiMessage));
            builder.setView(subView);
            AlertDialog alertDialog = builder.create();

            final String selectedElementId = selectedFromListArray[0];

            //Log.d("selectedElementId", selectedElementId.toString());
            String selectedElementString = mydb.getDataById(selectedElementId);
            //Log.d("TEST", selectedElementString);

            final String[] selectedElementArray = selectedElementString.split("\t");

            //[0] = leid
            //[1] = ssid
            //[2] = password
            //[3] = security
            //[4] = owner
            //[5] = comment
            //[6] = location
            //[7] = provider

            ssidEditText.setText(selectedElementArray[1]);
            passwordEditText.setText(selectedElementArray[2]);
            ownerEditText.setText(selectedElementArray[4]);
            commentEditText.setText(selectedElementArray[5]);
            locationEditText.setText(selectedElementArray[6]);

            //checkSpinner(selectedElementArray, providerSpinner);
            //Log.d("PROVIDER", selectedElementArray[6]);
            if (selectedElementArray[6].contains("0")){
                //Log.d("PROVIDER", selectedElementArray[6]);
                providerSpinner.setSelection(0);
            }else if (selectedElementArray[6].contains("1")){
                //Log.d("PROVIDER", selectedElementArray[6]);
                providerSpinner.setSelection(1);
            }else if (selectedElementArray[6].contains("2")){
                //Log.d("PROVIDER", selectedElementArray[6]);
                providerSpinner.setSelection(2);
            }else if (selectedElementArray[6].contains("3")){
                //Log.d("PROVIDER", selectedElementArray[6]);
                providerSpinner.setSelection(3);
            }else if (selectedElementArray[6].contains("4")){
                //Log.d("PROVIDER", selectedElementArray[6]);
                providerSpinner.setSelection(4);
            }else if (selectedElementArray[6].contains("5")){
                //Log.d("PROVIDER", selectedElementArray[6]);
                providerSpinner.setSelection(5);
            }

            //providerSpinner.setSelection(Integer.parseInt(selectedElementArray[3]));

            builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    inputStringSSID = ssidEditText.getText().toString();
                    inputStringPassword = passwordEditText.getText().toString();
                    inputStringOwner = ownerEditText.getText().toString();
                    inputStringComment = commentEditText.getText().toString();
                    inputStringLocation = locationEditText.getText().toString();

                    if (inputStringProvider.equals("")){
                        inputStringProvider = "0";
                    }
                    mydb.deleteDataById(selectedElementArray[0]);
                    mydb.setData(inputStringSSID, inputStringPassword, "", inputStringOwner, inputStringComment, inputStringLocation, inputStringProvider, "tmp", "tmp", "tmp", "tmp");

                    reloadListView();

                    Toast.makeText(MainActivity.this, "saved", Toast.LENGTH_SHORT).show();
                }
            });

            builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(MainActivity.this, "Cancel", Toast.LENGTH_SHORT).show();
                }
            });

            builder.setNeutralButton(getResources().getString(R.string.delete), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mydb.deleteDataById(selectedElementId);

                    Toast.makeText(MainActivity.this, selectedElementArray[2] + " deleted", Toast.LENGTH_SHORT).show();

                    reloadListView();
                }
            });
            builder.show();
        }
    }

    public void reloadListView() {
        dataList = mydb.getDataSortedBySsid();
        final String[] dataListArray = dataList.split("\n");

        ListAdapter WifiList_Adapter = new CustomListAdapter(this, dataListArray);
        ListView wifiListView = (ListView) findViewById(R.id.listView_datalist);

        wifiListView.setAdapter(WifiList_Adapter);
    }

    public String[] getDataListArray(String dataListString) {
        final String[] dataListArray = dataList.split("\n");
        return dataListArray;
    }

    private void checkSpinner(int position) {
        if (position == 0) {
            inputStringProvider = "0";
        } else if (position == 1) {
            inputStringProvider = "1";
        } else if (position == 2) {
            inputStringProvider = "2";
        } else if (position == 3) {
            inputStringProvider = "3";
        } else if (position == 4) {
            inputStringProvider = "4";
        } else if (position == 5) {
            inputStringProvider = "5";
        }
    }

    private void performPopupAction(String title, String[] selectedArray){
        if (title.equals(getResources().getString(R.string.popup_edit))){
            openDialog(getResources().getString(R.string.edit), selectedArray);

        }else if(title.equals(getResources().getString(R.string.popup_showDetails))){
            openViewDialog(getResources().getString(R.string.show), selectedArray);

        }else if(title.equals(getResources().getString(R.string.popup_delete))){
            mydb.deleteDataById(selectedArray[0]);
            reloadListView();
        }

    }


}