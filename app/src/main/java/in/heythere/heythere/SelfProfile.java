package in.heythere.heythere;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class SelfProfile extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_profile);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("HeyThere_pref", 0);
        SharedPreferences.Editor editor = pref.edit();

         String fullName = pref.getString("name",null);
         String email = pref.getString("email",null);
         String username = pref.getString("username",null);

         String dp_encoded = pref.getString("dp_encode",null);
        Log.d("The Profile Values:",fullName+" email "+email+" name"+username );
         Bitmap dpImage;

        byte[] dp_decoded_String= Base64.decode(dp_encoded, Base64.DEFAULT);
        dpImage = BitmapFactory.decodeByteArray(dp_decoded_String, 0, dp_decoded_String.length);




        TextView display_name = (TextView) findViewById(R.id.profile_fullname);
        TextView display_email = (TextView) findViewById(R.id.profile_email);
        TextView display_username = (TextView) findViewById(R.id.profile_username);
        ImageView display_dp = (ImageView) findViewById(R.id.profile_dp);

     display_name.setText(fullName);
        display_email.setText(email);
        display_username.setText(username);
        display_dp.setImageBitmap(dpImage);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }




    }
