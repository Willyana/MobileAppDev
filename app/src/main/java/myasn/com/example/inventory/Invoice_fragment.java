package myasn.com.example.inventory;
/*
 *****************************************************
 * @author Willyana - 12067867
 *****************************************************
 */

import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;

public class Invoice_fragment extends Fragment {
    private String qualitySpin;
    private TrackGPS gps;

    // Required empty public constructor
    public Invoice_fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.invoice_fragment, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        gps = new TrackGPS(getContext());

        //-------------------------- Spinner method ------------------------
        final String[] quality; //declare the spinner list of string
        quality = getResources().getStringArray(R.array.quality_array);
        final Spinner s1 = (Spinner) getActivity().findViewById(R.id.qualitySpin);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, quality);
        s1.setAdapter(adapter);
        //get selected item on Spinner
        s1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//set on selected item
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                int index = arg0.getSelectedItemPosition();//set the selected position
                qualitySpin = quality[index]; //get the index
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) { //method for nothing selected
            }
        });

        //----------------Method for show item button---------------------
        Button btnShow = (Button) getActivity().findViewById(R.id.btnShow); //declare show button
        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //-------------Go to Invoice list fragment----------------
                Inventory_list invoiceList = new Inventory_list(); //change fragment to Inventory_list
                FragmentTransaction ft = getFragmentManager().beginTransaction(); //begin transaction
                ft.replace(R.id.pagePlace, invoiceList);//replace fragment
                ft.commit(); //commit transaction
            }
        });

        //-----------------Method for add item button---------------------
        Button btnAdd = (Button) getActivity().findViewById(R.id.btnAdd); //declare add button
        btnAdd.setOnClickListener(new View.OnClickListener() {
            String invoice; //declare string invoice

            @Override
            public void onClick(View v) {
                //---create string for inventory logs
                inventoryLogs inventoryLog = new inventoryLogs(); //new object for inventoryLogs

                //---Name---
                String ns = MainActivity.name; //call the name from MainActivity

                //---Time---
                Calendar cal = Calendar.getInstance(); //set the time
                String day = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
                String month = String.valueOf(cal.get(Calendar.MONTH));
                String year = String.valueOf(cal.get(Calendar.YEAR));
                String hour = String.valueOf(cal.get(Calendar.HOUR));
                String minute = String.valueOf(cal.get(Calendar.MINUTE));
                String nowTime = day + "/" + month + "/" + year + " " + hour + ":" + minute;

                //---location---
                String longitude = null;
                String latitude = null;

                //check if location enabled
                if (gps.canGetLocation()==true){
                    double gpsLongitude = gps.getLongitude(); //get the longitude
                    double gpsLatitude = gps.getLatitude(); //get the latitude

                    longitude = Double.toString(gpsLongitude); //set string longitude
                    latitude = Double.toString(gpsLatitude); //set string latitude
                }else {
                    gps.showSettingsAlert(); //show gps alert
                }

                //---referenceNum---
                EditText refNum = (EditText) getActivity().findViewById(R.id.invNum);
                invoice = refNum.getText().toString();

                if (invoice.isEmpty() || qualitySpin.isEmpty()) {
                    Toast.makeText(getActivity(), "Entry not saved as not all data entered. "
                                    + "Complete all entries and try again.",
                            Toast.LENGTH_LONG).show();
                } else {
                    inventoryLog.setName(ns); //set name to inventory object
                    inventoryLog.setTime(nowTime); //set time to inventory object
                    inventoryLog.setLongitude(longitude); //set longitude to inventory object
                    inventoryLog.setLatitude(latitude); //set latitude to inventory object
                    inventoryLog.setReferenceNum(invoice); //set reference number to inventory object
                    inventoryLog.setDescription(qualitySpin); //set quality to inventory object
                    MainActivity.entries.add(inventoryLog); //add inventory object to array list entries in MainActivity

                    Toast.makeText(getContext(), inventoryLog.toString() + " is added to list.",
                            Toast.LENGTH_LONG).show(); //show the item has been added

                    refNum.setText(""); //set the invoice number edit text to empty
                    s1.setSelection(0); //set the quality spinner to

                }
            }
        });
    }//End onStart method

    public void onDestroy(){ //on destroy method
        super.onDestroy();
        gps.stopUsingGPS(); //stop using gps
    }
} //End Invoice_fragment class



