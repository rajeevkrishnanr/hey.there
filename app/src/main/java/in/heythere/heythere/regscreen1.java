package in.heythere.heythere;

/**
 * Created by rajeev on 04-Jan-15.
 */
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class regscreen1 extends android.app.Fragment {

    public EditText new_user,new_pass;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_regscreen1, container, false);
        new_user = (EditText)rootView.findViewById(R.id.new_username);
        new_pass = (EditText) rootView.findViewById(R.id.new_password);



        String fragdbgtext = new_user.getText().toString();

        //String fragdbgtext = "This is Still Empty";


        Log.d("Datafrom Fragment infragment ", "Sending data: " + fragdbgtext);
/*
        ScreenSlideActivity activityPrototype =((ScreenSlideActivity)getActivity());
        activityPrototype.getDatafromFragment1(fragdbgtext);
  */      //((ScreenSlideActivity) getActivity()).getDatafromFragment1(fragdbgtext);


        Button next_enter = (Button)rootView.findViewById(R.id.next);
        next_enter.setOnClickListener(next_enterOnClickListener);
        return rootView;
    }

    View.OnClickListener next_enterOnClickListener
            = new View.OnClickListener(){

        @Override
        public void onClick(View arg0) {

            String TPusername = new_user.getText().toString();
            String TPpass =new_pass.getText().toString();


        /*String TabOfFragmentB = ((AndroidViewPagerActivity)getActivity()).getTabFragmentB();

        MyFragmentB fragmentB = (MyFragmentB)getActivity()
                .getSupportFragmentManager()
                .findFragmentByTag(TabOfFragmentB);

        fragmentB.b_updateText(textPassToB);

*/
            ((RegActivity) getActivity()).getDatafromFragment1(TPusername,TPpass);

            Toast.makeText(getActivity(),
                    "text sent to Fragment B:\n " + TPusername + "password is" + TPpass,
                    Toast.LENGTH_SHORT).show();
            ((RegActivity)getActivity()).actionNextPage();



        }};



}