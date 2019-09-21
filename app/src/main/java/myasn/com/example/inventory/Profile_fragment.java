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

public class Profile_fragment extends Fragment {
    private String user; //declare the username
    private String pass1; //declare the password
    private String pass2; //declare the repeat password
    public static String userProfile; //declare data user profile

    // Required empty public constructor
    public Profile_fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.profile_fragment, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        //------------------------Save Profile Button method---------------------------
        Button save = (Button) getActivity().findViewById(R.id.saveButton);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //declare the EditText for profile fragment
                EditText userName = (EditText) getActivity().findViewById(R.id.userProf);
                user = userName.getText().toString();

                EditText password1 = (EditText) getActivity().findViewById(R.id.passProf1);
                pass1 = password1.getText().toString();

                EditText password2 = (EditText) getActivity().findViewById(R.id.passProf2);
                pass2 = password2.getText().toString();

                if (user.isEmpty()) { // if the user name empty
                    Toast.makeText(getContext(), "Passwords must be the same or null. "
                            + "Username cannot be null.", Toast.LENGTH_LONG).show();
                } else if (pass1.equals(pass2)) { // if the password and repeat password the same
                    //go to home fragment
                    userProfile = user + pass1;
                    Home_fragment home_fragment = new Home_fragment();
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.pagePlace, home_fragment);
                    ft.commit();
                } else { //if the password and repeat password different
                    Toast.makeText(getContext(), "Passwords must be the same or null. "
                            + "Username cannot be null.", Toast.LENGTH_LONG).show();
                }
            }
        });

        //------------------------Cancel Profile Button method---------------------------
        Button cancel = (Button) getActivity().findViewById(R.id.cancelButton);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go to invoice fragment
                Home_fragment home_fragment = new Home_fragment();//change fragment to Home_fragment
                FragmentTransaction ft = getFragmentManager().beginTransaction(); //begin transaction
                ft.replace(R.id.pagePlace, home_fragment); //replace fragment
                ft.commit(); //commit transaction
            }
        });
    } //End onStart Method
} //End Profile_fragment class
