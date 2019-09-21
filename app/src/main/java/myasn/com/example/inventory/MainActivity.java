package myasn.com.example.inventory;
/*
 *****************************************************
 * @author Willyana - 12067867
 *****************************************************
 */

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.database.*;
import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.content.Context;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {
    public static ArrayList<inventoryLogs> entries; //declare array list
    public static String name; //declare String user name for data entry
    DBAdapter db = new DBAdapter(this); //initialise DBAdapter

    // Required empty public constructor
    public MainActivity() {
        entries = new ArrayList<inventoryLogs>();
    }

    //----------------onCreate Method---------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) { //when the class is created
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //declare the content
        //get home fragment as first fragment
        Home_fragment frag = new Home_fragment(); //set a fragment for home_fragment
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.pagePlace, frag); //replace the fragment
        ft.commit(); //commit the change

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); //bring in the toolbar
        getMenuInflater(); //inflate main menu.xml
        db.open(); //open db adapter
        Cursor c = db.getAllEntries(); //use cursor method to get data from database
        //---------------------------check the database----------------------------
        try { //copy data base
            String destPath = "/data/data/" + getPackageName() + "/databases";
            File f = new File(destPath);
            if (!f.exists()) {   // create dir and then copy db
                f.mkdir();
                f.createNewFile();
                //---copy the db from the assets folder into
                // the databases folder---
                CopyDB(getBaseContext().getAssets().open("logs"),
                        new FileOutputStream(destPath + "/logs"));
            }
        } catch (FileNotFoundException e) { //catch errors
            e.printStackTrace();
        } catch (IOException e) { //catch errors
            e.printStackTrace();
        }
        //-----------------get a entries data from database------------
        if (c.moveToFirst()) {//check first data in database
            do {
                String nameDB = c.getString(1); //get username
                String timeDB = c.getString(2); //get time
                String longitudeDB = c.getString(3); //get longitude
                String latitudeDB = c.getString(4); //get latitude
                String referenceNumDB = c.getString(5); //get reference
                String descriptionDB = c.getString(6); //get description
                inventoryLogs inventoryLog = new inventoryLogs(); //new object
                inventoryLog.setName(nameDB); //save name to new object
                inventoryLog.setTime(timeDB); //save time to new object
                inventoryLog.setLongitude(longitudeDB); //save longitude to new object
                inventoryLog.setLatitude(latitudeDB); //save latitude to new object
                inventoryLog.setReferenceNum(referenceNumDB); //save reference to new object
                inventoryLog.setDescription(descriptionDB); //save description to new object
                entries.add(inventoryLog); //add new object to array list
            } while (c.moveToNext()); //check next data in the database, if any rows
        }
        db.close(); //close db adapter
    }

    //--------------------------Copy data from database----------------------------
    private void CopyDB(InputStream inputStream, OutputStream outputStream) throws IOException {
        //---copy 1K bytes at a time---
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) { //while there is an input
            outputStream.write(buffer, 0, length); //write input
        }
        inputStream.close(); //close input stream
        outputStream.flush(); // flush output stream
        outputStream.close(); // close output stream
    }

    //----------------Method for create menu item---------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //----------------Method for selected menu item---------------------
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will automatically handle clicks on
        // the Home/Up button, so long as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //Simplifiable If Statement for menu item
        if (id == R.id.Send) {//go to send menu
            return true;
        } else if (id == R.id.Save) {//go to save menu
            return true;
        } else if (id == R.id.Profile) {//go to profile menu
            return true;
        } else {
        }
        return super.onOptionsItemSelected(item); //return selected item
    }


    //----------------Method for send menu item---------------------
    public void sendMenu(MenuItem item) {
        final Context ctx = this; //declare context
        AlertDialog.Builder builder = new AlertDialog.Builder(this); //create alert dialog builder
        builder.setTitle("Are you sure? This will delete all entries.") //set title
                .setIcon(R.mipmap.ic_launcher)
                .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) { //user click Ok
                        MyAsync myAsync = new MyAsync(); //call myAsync method
                        myAsync.execute();
                    }
                })
                .setNegativeButton(R.string.CANCEL, new DialogInterface.OnClickListener() { //user click cancel
                    public void onClick(DialogInterface dialog, int id) { //do nothing
                    }
                });
        // Create the AlertDialog object and show it
        AlertDialog alertDialog = builder.create(); //create alert dialog
        alertDialog.show(); //show
    }//End Send menu method

    //----------------Method for save menu item---------------------
    public void saveMenu(MenuItem item) {
        db.open(); //open database
        db.deleteAllEntries(); //delete all data in database
        Iterator it = entries.iterator(); //iterator
        String names = null; //declare string name
        String time = null; //declare string time
        String longitude = null; //declare string longitude
        String latitude = null; //declare string latitude
        String ref = null; //declare string ref
        String def = null; //declare string def

        while (it.hasNext()) { //if there is next row
            inventoryLogs inventory = (inventoryLogs) it.next();

            if (inventory.getName() != null) { //call the object
                names = inventory.getName(); //set string name
                time = inventory.getTime();//set string time
                longitude = inventory.getLongitude();//set string longitude
                latitude = inventory.getLatitude();//set string latitude
                ref = inventory.getReferenceNum();//set string ref
                def = inventory.getDescription();//set string def
                db.insertEntries(names, time, longitude, latitude, ref, def); //insert string to database
            }
        }
        Toast.makeText(this, "Data saved to database", Toast.LENGTH_SHORT).show(); //toast message
        db.close(); //close database
    }//End save menu method

    //----------------Method for profile menu item---------------------
    public void profileMenu(MenuItem item) {
        Profile_fragment profile_fragment = new Profile_fragment(); //set a fragment for home_fragment
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.pagePlace, profile_fragment); //replace fragment
        ft.commit(); //commit the change
    }//End Profile menu method

    //--------------------Method for onBackPressed---------------------
    @Override
    public void onBackPressed() {
        final Context context = this;
        AlertDialog.Builder builder = new AlertDialog.Builder(context); //Build AlertDialog
        builder.setTitle("Save entries to DB first?")
                .setIcon(R.mipmap.ic_launcher)
                .setPositiveButton(R.string.NO, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {//when user choose NO button
                        //close the application
                        finish();
                    }
                })
                .setNegativeButton(R.string.OK, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {//when user choose OK button
                        // User save the data in the database and close the application
                        db.open(); //open database
                        db.deleteAllEntries(); //delete all data in database
                        Iterator iterator = entries.iterator(); //iterator
                        String names = null; //declare string name
                        String time = null; //declare string time
                        String longitude = null; //declare string longitude
                        String latitude = null; //declare string latitude
                        String ref = null; //declare string ref
                        String def = null; //declare string def
                        while (iterator.hasNext()) {
                            inventoryLogs inventory = (inventoryLogs) iterator.next();
                            if (inventory.getName() != null) { //call the object
                                names = inventory.getName(); //set string name
                                time = inventory.getTime();//set string time
                                longitude = inventory.getLongitude();//set string longitude
                                latitude = inventory.getLatitude();//set string latitude
                                ref = inventory.getReferenceNum();//set string ref
                                def = inventory.getDescription();//set string def
                                db.insertEntries(names, time, longitude, latitude, ref, def); //insert string to database
                            }
                        }
                        db.close(); //close database
                        entries.clear(); //clear array list
                        finish(); //finish the method
                    }
                });
        // Create the AlertDialog object and show it
        AlertDialog alertDialog = builder.create();
        alertDialog.show(); //show alert dialog
    }//End onBackPressed Method

    //----------------Method for MyAsync---------------------
    private class MyAsync extends AsyncTask<String, String, String>{

        private String response;
        private ProgressDialog progressDialog;

        //----------------Method for do in background---------------------
        @Override
        protected String doInBackground(String... params) {
            publishProgress("Sending data..."); //onProgressUpdate();
            try{
                Thread.sleep(3000); //thread sleep method for 3 second
                StringBuilder out=new StringBuilder(); //create string builder
                for(inventoryLogs data: entries) { //for loop method
                    out.append(data.getName()+" "+data.getReferenceNum()+" "
                            +data.getTime()+" \nLongitude: " + data.getLongitude()+" "
                            + " Latitude: "+data.getLatitude()+ " "+data.getDescription()+"\n"); //append the data
                }
                response=out.toString();
            }catch(InterruptedException ie){}//catch errors
            catch(Exception e){}//catch errors

            return response;
        }
        //----------------Method for send email---------------------
        private void sendEmail(String[] emailAddresses, String[] carbonCopies, String subject, String message)
        {
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setData(Uri.parse("mailto:"));
            String[] to = emailAddresses; //set string to
            String[] cc = carbonCopies;//set string cc
            emailIntent.putExtra(Intent.EXTRA_EMAIL, to); //set intent for email
            emailIntent.putExtra(Intent.EXTRA_CC, cc); //set cc
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject); //set subject
            emailIntent.putExtra(Intent.EXTRA_TEXT, message); //set message
            emailIntent.setType("message/rfc822"); //set type
            startActivity(Intent.createChooser(emailIntent, "Email")); //start activity
        }

        //----------------Method for on post execute---------------------
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                DBAdapter db = new DBAdapter(MainActivity.this);
                db.open();
                String[] to = {"s12067867@gmail.com"}; //set email address for to email
                String[] cc = {"s12067867@gmail.com"}; //set email address for cc
                sendEmail(to, cc, "New logger data", name + "\n" + response);
                db.deleteAllEntries(); //delete database
                db.close(); //close database
                entries.clear(); //clear array list
            } catch (Exception e) {//catch errors
                Log.e("tag", e.getMessage());
            }
            progressDialog.dismiss(); //close progress dialog
        }

        //----------------Method for on pre execute---------------------
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(MainActivity.this,
                    "Processing",
                    "Please wait for "+(3000/1000)+ " seconds.",true
            );
        }

        //----------------Method for on progress update---------------------
        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            Home_fragment homeFragment = new Home_fragment(); //go to home_fragment
            FragmentTransaction ft = getFragmentManager().beginTransaction(); //begin transaction
            ft.replace(R.id.pagePlace, homeFragment);//replace fragment
            ft.commit();//commit transaction
            //Toast.makeText(getBaseContext(),values[0],Toast.LENGTH_SHORT).show();
        }
    }
}//End MainActivity class
