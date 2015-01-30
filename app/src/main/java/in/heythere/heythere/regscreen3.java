package in.heythere.heythere;

/**
 * Created by rajeev on 04-Jan-15.
 */

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.Fragment;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class regscreen3 extends Fragment {
    ImageView dpPreview;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView3 = inflater.inflate(R.layout.fragment_regscreen3, container, false);
        Button back_enter = (Button)rootView3.findViewById(R.id.back_button);
        dpPreview = (ImageView)rootView3.findViewById(R.id.dp_preview);
        Button finish = (Button)rootView3.findViewById(R.id.finish_reg);
        back_enter.setOnClickListener(back_enterOnClickListener);
        finish.setOnClickListener(uploadOnClickListener);
        dpPreview.setImageBitmap(null);




        return rootView3;
    }

    View.OnClickListener back_enterOnClickListener
            = new View.OnClickListener(){

        @Override
        public void onClick(View arg0) {


            ((RegActivity)getActivity()).actionBackPage();



        }};

    View.OnClickListener uploadOnClickListener
            = new View.OnClickListener(){

        @Override
        public void onClick(View arg0) {



boolean dp_set = (dpPreview.getDrawable() != null);
            if(dp_set) {

                ((RegActivity) getActivity()).finishPage(arg0);
            }
            else
            {
                Toast.makeText(getActivity(),"Please upload a Display Picture!",Toast.LENGTH_SHORT).show();
            }


        }};

}
