package in.heythere.heythere;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.NameValuePair;

import java.util.ArrayList;


public class nearbyList extends ActionBarActivity {

    MyLocationService gps;
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    private static final String FIND_FRIEND_URL = "http://192.168.0.11/heyserver/server/find_people.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    String id;
    double latitude,longitude;
    private Bitmap dpImage_temp = null;
    //String[] nearbyNames;
    //ArrayList<Bitmap> dpImageArry = new ArrayList<Bitmap>();
    NameValuePair[] nearbyDetails;
    ListView peopleList;
    ArrayList<String> names_local;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_list);
        Bundle b=this.getIntent().getExtras();
        final ArrayList<String> nearbyNames = b.getStringArrayList("names");
        final ArrayList<Bitmap> dpImageArry = b.getParcelableArrayList("dps");
        final ArrayList<String> nearbyIds = b.getStringArrayList("ids");
        final ArrayList<String> nearbyDistance = b.getStringArrayList("dists");
        names_local =nearbyNames;


        peopleList=(ListView)findViewById(R.id.nearList);
        CustomPeopleList listAdapter = new CustomPeopleList(nearbyList.this,nearbyNames,dpImageArry);
        peopleList.setAdapter(listAdapter);
        peopleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(nearbyList.this, "You Clicked at "+nearbyNames.get(position), Toast.LENGTH_SHORT).show();


                Bundle profile=new Bundle();
                profile.putString("name", nearbyNames.get(position));
                profile.putParcelable("dps", dpImageArry.get(position));
                profile.putString("ids", nearbyIds.get(position));
                profile.putString("dists",nearbyDistance.get(position));
                Intent new_profile=new Intent(nearbyList.this, People_Profile.class);
                new_profile.putExtras(profile);
                startActivity(new_profile);

/*
                LayoutInflater profileLayoutInflater = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                View profilePopupView = profileLayoutInflater.inflate(R.layout.profile_popup, null);
                final PopupWindow profilePopupWindow = new PopupWindow(profilePopupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            Button Btn_popupDismiss = (Button)profilePopupView.findViewById(R.id.dismiss_popup);
            Btn_popupDismiss.setOnClickListener(new Button.OnClickListener(){

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    //profilePopupWindow.dismiss();
                }});*/

                }
        });



        gps = new MyLocationService(nearbyList.this);


        // check if GPS enabled
        if(gps.canGetLocation()){

            latitude = gps.getLatitude();
            longitude = gps.getLongitude();

            // \n is for new line
            //findNearby();
            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        }
        else
        {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }

    }








}
