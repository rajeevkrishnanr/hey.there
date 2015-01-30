package in.heythere.heythere;

/**
 * Created by rajeev on 04-Jan-15.
 */

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;



/**
 * Created by rajeev on 04-Jan-15.
 */


public class regscreen2 extends Fragment {

    public EditText new_name,new_email;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_regscreen2, container, false);
        new_name = (EditText)rootView.findViewById(R.id.new_fullname);
        new_email = (EditText) rootView.findViewById(R.id.new_email);
        Button next_enter = (Button)rootView.findViewById(R.id.next);
        Button back_enter = (Button)rootView.findViewById(R.id.back_button);
        next_enter.setOnClickListener(next_enterOnClickListener);
        back_enter.setOnClickListener(back_enterOnClickListener);
        return rootView;
    }


    View.OnClickListener next_enterOnClickListener
            = new View.OnClickListener(){

        @Override
        public void onClick(View arg0) {

            String TPfullname = new_name.getText().toString();
            String TPemail =new_email.getText().toString();
            if( (TPfullname).isEmpty() || (TPemail).isEmpty())
            {

                Toast.makeText(getActivity(),"Please Fill in all the fields !",Toast.LENGTH_SHORT).show();
            }
            else {


                ((RegActivity) getActivity()).getDatafromFragment2(TPfullname, TPemail);

                Toast.makeText(getActivity(),
                        "text sent to Fragment B: Full name: " + TPfullname + "\n password is: " + TPemail,
                        Toast.LENGTH_SHORT).show();
                ((RegActivity) getActivity()).actionNextPage();
            }



        }};

    View.OnClickListener back_enterOnClickListener
            = new View.OnClickListener(){

        @Override
        public void onClick(View arg0) {


            ((RegActivity)getActivity()).actionBackPage();



        }};




}