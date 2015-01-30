package in.heythere.heythere;

/**
 * Created by rajeev on 04-Jan-15.
 */
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class regscreen1 extends android.app.Fragment {
    JSONParser jsonParser = new JSONParser();
    public static final String USER_CHK_URL="http://server.heyteam.me/user_availability.php";
    public static final String TAG_AVAILABLE="available";
    public EditText new_user,new_pass,new_pass_verify;
    public TextView user_err;
    private ProgressBar spinner;
    int available=0;
    String result="1";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_regscreen1, container, false);
        new_user = (EditText)rootView.findViewById(R.id.new_username);
        new_pass = (EditText) rootView.findViewById(R.id.new_password);
        new_pass_verify = (EditText) rootView.findViewById(R.id.new_password_verify);
        user_err =(TextView)rootView.findViewById(R.id.user_error_text);
        spinner = (ProgressBar)rootView.findViewById(R.id.progressBar1);
        user_err.setVisibility(View.GONE);
        spinner.setVisibility(View.GONE);
        spinner.setIndeterminate(true);

        new_user.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                new Chk_user().execute(new_user.getText().toString());

            }
        });





        Button next_enter = (Button)rootView.findViewById(R.id.next);
        next_enter.setOnClickListener(next_enterOnClickListener);

        return rootView;
    }



    View.OnClickListener next_enterOnClickListener
            = new View.OnClickListener(){

        @Override
        public void onClick(View arg0) {

            String TPusername = new_user.getText().toString();
            String TPpass = new_pass.getText().toString();
            String TPpass_verify =new_pass_verify.getText().toString();
            Log.d("Pass1 and Pass 2: ",TPpass +" and "+TPpass_verify);


            if( (TPpass).isEmpty() || (TPpass_verify).isEmpty() ||(TPusername).isEmpty())
            {

                Toast.makeText(getActivity(),"Please Fill in all the fields !",Toast.LENGTH_SHORT).show();
            }
            else
            {


                if (TPpass.equals(TPpass_verify)) {


                    new_pass.setError(null);
                    ((RegActivity) getActivity()).getDatafromFragment1(TPusername, TPpass, TPpass_verify);
                    ((RegActivity) getActivity()).actionNextPage();
                } else {
                    new_pass.setError("Passwords Do not Match !");
                }
            }
        }};

    public void updateTextView(String value_to_set) {
        new_user.setText(value_to_set);
    }


    /**************************************************************************************************************/

    class Chk_user extends AsyncTask<String, String, String>
    {
        @Override protected void onPreExecute() {
            super.onPreExecute();
            spinner.setVisibility(View.VISIBLE);

        }


        @Override protected String doInBackground(String... args)
        {

            String usename_check_Asyncdata = args[0];

            try {
                Log.d("AttemptReg() Background", "DP encoded!");


                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("username", usename_check_Asyncdata));
                 Log.d("JSON request!", "starting");
                JSONObject json = jsonParser.makeHttpRequest( USER_CHK_URL, "POST", params);
                available = json.getInt(TAG_AVAILABLE);

                }
            catch (JSONException jexp)
            {
                jexp.printStackTrace();
            }
        return Integer.toString(available);
        }
        @Override protected void onPostExecute(String result){

            spinner.setVisibility(View.GONE);
            int status = Integer.parseInt(result) ;
            switch (status){
                case 0:new_user.setError("Username Not Available");
                    user_err.setVisibility(View.GONE);break;

                case 1:user_err.setVisibility(View.GONE);new_user.setError(null);
                    break;

            }


            }
}







    /***************************************************************************************************************/






}