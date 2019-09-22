package myasn.com.example.inventory;
/*
 *****************************************************
 * @author Willyana - 12067867
 *****************************************************
 */

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Home_fragment extends Fragment {

    // Required empty public constructor
    public Home_fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.home_fragment, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        //------------------------Next Button method---------------------------
        final Button next = (Button) getActivity().findViewById(R.id.btnNext); //declare next method
        next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final EditText a_user = (EditText) getActivity().findViewById(R.id.username); //declare the username edit text
                String name = a_user.getText().toString(); //get the username
                if (name.isEmpty()) { //if there is empty username
                    Toast.makeText(getActivity(), "Must enter user name before you can process.", //toast the string for empty username
                            Toast.LENGTH_LONG).show();
                } else {
                    MainActivity.name = name; //declare String name in the main activity
                    Invoice_fragment invoiceFragment = new Invoice_fragment(); //change to Invoice_fragment
                    FragmentTransaction ft = getFragmentManager().beginTransaction(); //begin transaction
                    ft.replace(R.id.pagePlace, invoiceFragment); //replace the fragment
                    ft.commit(); //commit the transaction
                }
            }
        });
    }//End OnStart method
}//End Home_fragment class
