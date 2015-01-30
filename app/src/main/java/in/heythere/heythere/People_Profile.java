package in.heythere.heythere;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class People_Profile extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people__profile);
        Bundle b=this.getIntent().getExtras();
        final String fullName = b.getString("name");
        final Bitmap dpImage = b.getParcelable("dps");
        final String id = b.getString("ids");
        final String dist_str = b.getString("dists");
        float dist_float=Float.parseFloat(dist_str);
        String dist = "Unknown";

        if(dist_float <= 0.5)
        {
        dist="Nearby (a few meters)";
        }
        else
        {
        dist=Integer.toString(Math.round(dist_float));

        }


        TextView display_name = (TextView) findViewById(R.id.people_fullname);
        TextView display_distance = (TextView) findViewById(R.id.people_distance);
        Button contact_btn = (Button) findViewById(R.id.people_contact);
        ImageView display_dp = (ImageView) findViewById(R.id.people_dp);
        Log.d("The People Profile data","Name: "+fullName+" , distance: "+dist);
        display_name.setText(fullName);
        display_distance.setText(dist +" Km");
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
