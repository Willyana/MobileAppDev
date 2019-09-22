package myasn.com.example.inventory;
/*
 *****************************************************
 * @author Willyana - 12067867
 *****************************************************
 */

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import java.util.ArrayList;
import java.util.Iterator;

public class Inventory_list extends ListFragment {
    private ArrayAdapter<String> adapter;

    // Required empty public constructor
    public Inventory_list() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.inventory_list, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        //-----------------------------ListView Method--------------------------------
        ArrayList<String> inventoryArray = new ArrayList<>();//declare array list
        Iterator iterator = MainActivity.entries.iterator(); // iterate array from MainActivity
        while (iterator.hasNext()) {//get all the data in the array
            inventoryLogs inventory = (inventoryLogs) iterator.next();
            if (MainActivity.name.equals(inventory.getName())) {
                inventoryArray.add(inventory.toString());
            }
        }
        String[] myInventory = inventoryArray.toArray(new String[0]); //put the data in the String list
        //declare the adapter array
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, myInventory);
        setListAdapter(adapter);//show the data in the listView

        //------------------------Back to Add Invoices Button method---------------------------
        final Button back = (Button) getActivity().findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Invoice_fragment invoiceFragment = new Invoice_fragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.pagePlace, invoiceFragment);
                ft.commit();
            }
        });
    }//end onStart method
}//End inventory_list class
