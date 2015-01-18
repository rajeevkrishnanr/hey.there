package in.heythere.heythere;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class login extends ActionBarActivity implements View.OnClickListener {

    private EditText user, pass;
    private Button loginbut,regbut;
    private ProgressDialog pDialog;

    /* JSON parser*/

    JSONParser jsonParser = new JSONParser();

    private static final String LOGIN_URL = "http://server.heyteam.me/login_exec.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_ID = "id";
    private  String user_uid= "NULL";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        user = (EditText) findViewById(R.id.userid);
        pass = (EditText) findViewById(R.id.password);
        loginbut = (Button) findViewById(R.id.but_signin);
        regbut = (Button) findViewById(R.id.but_reg);
        loginbut.setOnClickListener(this);
        regbut.setOnClickListener(this);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("HeyThere_pref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();

        editor.putBoolean("key", true);
        boolean auth_status = pref.getBoolean("Auth_status",false);
if (auth_status == true) {
    Intent i = new Intent(login.this, MainScreen.class);
    finish();
    startActivity(i);
}
    }


    @Override public void onClick(View v)
    {

     switch (v.getId())
     {
      /*here we have used, switch case, because on login activity you may
      also want to show registration button, so if the user is new ! we can go the
       registration activity , other than this we could also do this without switch
      case.*/
         case R.id.but_signin: new AttemptLogin().execute();break;

         case R.id.but_reg:
             Intent regIntent = new Intent(login.this,RegActivity.class);
             startActivity(regIntent);break;
         default: break;

     }
    }

    class AttemptLogin extends AsyncTask<String, String, String>
    {
        @Override protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(login.this);
            pDialog.setMessage("Attempting for login...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show(); }
        @Override protected String doInBackground(String... args)
        {

        /*  here Check for success tag */

            int success;
            String msg = "0";
            String username = user.getText().toString();
            String password = pass.getText().toString();




            try { List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("username", username));
                params.add(new BasicNameValuePair("password", password));
                Log.d("request!", "starting");
                JSONObject json = jsonParser.makeHttpRequest( LOGIN_URL, "POST", params);

                // success tag for json
                success = json.getInt(TAG_SUCCESS);

                if (success == 1)
                {
                    //Log.d("Successfully Login!", json.toString());
                    msg= "1";
                    user_uid =json.getString(TAG_ID);
                    String user_fullname=json.getString("fullname");
                    String user_email=json.getString("email");
                    String user_dp=json.getString("dp");
                    Log.d("Successfully Login!", user_fullname +" and "+user_email);




                    SharedPreferences pref_new = getApplicationContext().getSharedPreferences("HeyThere_pref", 0); // 0 - for private mode
                    SharedPreferences.Editor editor_new = pref_new.edit();
                    editor_new.putBoolean("Auth_status", true);
                    editor_new.putString("id",user_uid);
                    editor_new.putString("name",user_fullname);
                    editor_new.putString("email",user_email);
                    editor_new.putString("dp_encode",user_dp);
                    editor_new.putString("username",username);
                    editor_new.commit();


                    Intent start_app = new Intent(login.this,MainScreen.class);



                    // this finish() method is used to tell android os that we are done with current
                    // activity now! Moving to other activity
                    startActivity(start_app);
                    finish();
                    //return json.getString(TAG_MESSAGE);
                    return msg;}
                else{
                    //return json.getString(TAG_MESSAGE);
                    msg = "0";
                    return msg;
                    }

            }
            catch (JSONException e)
            {
                e.printStackTrace();

            }
            return msg;
        }


        @Override protected void onPostExecute(String result){
            int status = Integer.parseInt(result) ;
            String disp_text="Try Again";
            switch (status){
                case 0: disp_text =  "Login Failed";

                    break;
                case 1:disp_text= "Login Successful!";break;
                    default:break;
            }
            pDialog.dismiss();
            Toast.makeText(getApplicationContext(),disp_text , Toast.LENGTH_LONG).show();

        }






    }
}

