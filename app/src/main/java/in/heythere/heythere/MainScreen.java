package in.heythere.heythere;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;

import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainScreen extends ActionBarActivity implements OnMapReadyCallback {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mDrawerItems;
    public static int DrawerArrowToggle_spinBars;
    MyLocationService gps;
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    private static final String FIND_FRIEND_URL = "http://server.heyteam.me/find_people.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    String id,searchRange,myName;
    double latitude,longitude,lat_p,lng_p;
    private Bitmap dpImage_temp = null;
    public MapFragment mapFragment;
    GoogleMap mapp;
    int circleRange;
    Circle rangeCircle;
    CircleOptions circleOptions;
    ArrayList<Bitmap> dpImageArry = new ArrayList<Bitmap>();
    ArrayList<String> nearbyNames= new ArrayList<String>();
    ArrayList<String> nearbyIds=new ArrayList<String>();
    ArrayList<String>nearbyDist = new ArrayList<String>();
    ArrayList<LatLng>nearbyCoordinates=new ArrayList<LatLng>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
//
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("HeyThere_pref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        id=pref.getString("id",null);
        myName=pref.getString("name","ME");


        mTitle = mDrawerTitle = getTitle();
        mDrawerItems = getResources().getStringArray(R.array.drawer_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mDrawerItems));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerToggle.setDrawerIndicatorEnabled(true);

        mDrawerLayout.setDrawerListener(mDrawerToggle);


//TODO Map Setup

        mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        getTheLocation();



        SeekBar rangeBar =(SeekBar)findViewById(R.id.rangeBar);
        final TextView rangeKm=(TextView)findViewById(R.id.rangeSet);
        rangeBar.setMax(100);
        //Defualt Values for SeekBar
        rangeBar.setProgress(5);
        rangeKm.setText("2" +" Km");
        searchRange=Integer.toString(rangeBar.getProgress());
        circleOptions = new CircleOptions()
                .center(new LatLng(latitude, longitude))
                .radius( Integer.parseInt(searchRange)).fillColor(0x4D3399cc).strokeColor(Color.BLUE).strokeWidth(1);

        rangeCircle = (mapFragment.getMap()).addCircle(circleOptions);








        rangeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress == 0)
                {
                    progress++;
                }

                rangeKm.setText(progress +" Km");

                searchRange=Integer.toString(progress);
                circleRange = progress*1000;
                rangeCircle.setRadius(circleRange);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {



            }
        });
        Button btnShowLocation = (Button) findViewById(R.id.btnShowLocation);
        // show location button click event
        btnShowLocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // create class object
                getTheLocation();
                findNearby();



            }
        });



    }

    public void getTheLocation(){
        gps = new MyLocationService(MainScreen.this);

        // check if GPS enabled
        if(gps.canGetLocation()){

            latitude = gps.getLatitude();
            longitude = gps.getLongitude();


            // \n is for new line
            //Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
    }

    //TODO Map Works
    @Override
    public final void onMapReady(GoogleMap map){
        map.setMyLocationEnabled(false);
        getTheLocation();
        map.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title(myName).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 11),2000,null);
    }

    public void putOnMap(LatLng marker_coordinate,String marker_name,String marker_distance){
        mapp = mapFragment.getMap();
        Log.d("PutonMap:","Marker Making");
        mapp.addMarker(new MarkerOptions().position(marker_coordinate).title(marker_name).snippet("Estimated Distance :" + marker_distance.substring(0, 4) + " Km"));

    }
    public void putMeOnMap()
    {
        mapp = mapFragment.getMap();
        mapp.clear();
        mapp.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title(myName).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        //mapp.addCircle(circleOptions);
        rangeCircle.setCenter(new LatLng(latitude,longitude));
        //rangeCircle.setRadius(circleRange);
    }

    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        switch (position){
            case 0://Home

                break;
            case 1://Profile
                Intent profile = new Intent(MainScreen.this, SelfProfile.class);
                startActivity(profile);
               break;
            case 2://Carpool
                Toast.makeText(getApplicationContext(), "Not Available!", Toast.LENGTH_LONG).show();
                break;
            case 3:Toast.makeText(getApplicationContext(), "Not Available!", Toast.LENGTH_LONG).show();

                //Settings

                break;
            case 4://Help
                Uri heyThereUrl = Uri.parse("http://www.heyteam.me");
                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                browserIntent.setData(heyThereUrl);
                startActivity(browserIntent);
                break;
            case 5: //The About Option!
            startActivity(new Intent(MainScreen.this, about.class));break;
            case 6://Log out
                SharedPreferences pref = getApplicationContext().getSharedPreferences("HeyThere_pref", 0); // 0 - for private mode
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.commit();
                Intent i = new Intent(MainScreen.this, login.class);
                finish();
                startActivity(i);

                break;

            default:break;

        }
        mDrawerList.setItemChecked(0, true);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    public void findNearby() {
        Log.d("Finish Button Clicked", "starting");
        String lat=Double.toString(latitude);
        String lng = Double.toString(longitude);



        new AttemptFind().execute(lat,lng,id,searchRange);
        Log.d("AttemptReg()", "Completed");
    }


    /**************************************************************************************************************/

    class AttemptFind extends AsyncTask<String, String, String>
    {
        @Override protected void onPreExecute() {
            super.onPreExecute();
            Log.d("Inside AttemptReg()", "OnPreExec super");
            pDialog = new ProgressDialog(MainScreen.this);
            Log.d("Inside AttemptReg()", "OnPreExec Dialog Made");
            pDialog.setMessage("Lets see who is around..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

            Log.d("Inside AttemptReg()", "OnPreExec Completed");
        }


        @Override protected String doInBackground(String... args)
        {

        /*  here Check for success tag */
            Log.d("Background AttemptReg()", "Background Starting");
            int success;
            String msg = "0";
            Log.d("Background AttemptReg()", "Creating Input Objects ");


            Log.d("Background AttemptReg()", "String to store obj data");

            String lat_data = args[0];
            String lng_data = args[1];
            String id_data = args[2];
            String range_data=args[3];




            try {
                List<NameValuePair> params = new ArrayList<>();

                Log.d("AttemptReg() Background", "Try Block: Adding Params");

                params.add(new BasicNameValuePair("lat", lat_data));
                params.add(new BasicNameValuePair("lng", lng_data));
                //params.add(new BasicNameValuePair("peoplelimit", new_email_Asyncdata));
                params.add(new BasicNameValuePair("uid", id_data));
                params.add(new BasicNameValuePair("radius", range_data));
                Log.d("JSON request!", "starting");
                JSONObject json = jsonParser.makeHttpRequest(FIND_FRIEND_URL, "POST", params);
                success = json.getInt(TAG_SUCCESS);
                //success=1;
                Log.d("JSON request Over", "Put Scuccess as 1");
                if (success == 1)
                {
                    Log.d("JSON request Overrr", "Inside IF");

                    JSONArray jArray = json.getJSONArray("nearby");

                    System.out.println("*****JARRAY*****" + jArray.length());

                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject json_data = jArray.getJSONObject(i);
                        /*Log.i("nearby JSON", "id" + json_data.getString("id") +
                                        ", fname: " + json_data.getString("fname") +
                                        ", distance: " + json_data.getString("distance")
                        );*/


                        //nearbyNames[i]=json_data.getString("fname");
                        nearbyNames.add(json_data.getString("fname"));
                        nearbyIds.add(json_data.getString("id"));
                        nearbyDist.add(json_data.getString("distance"));
                        lat_p = Double.parseDouble(json_data.getString("lat_p"));
                        lng_p = Double.parseDouble(json_data.getString("lng_p"));
                        nearbyCoordinates.add(new LatLng(lat_p,lng_p));



                        String dp_string_recv= json_data.getString("dp");


                        byte[] dp_decoded_String= Base64.decode(dp_string_recv, Base64.DEFAULT);
                        dpImage_temp = BitmapFactory.decodeByteArray(dp_decoded_String, 0, dp_decoded_String.length);
                        dpImageArry.add(dpImage_temp);







                    }



                }
                else
                {
                    Log.d("JSON request Overrr", "Inside Else");

                    //return json.getString(TAG_MESSAGE);
                    msg = "0";
                    return msg;
                }
                Log.d("JSON request Overrr", "Outside IF ELSE");

            }
            catch (JSONException jexp)
            {
                jexp.printStackTrace();
            }




            return msg;
        }


        @Override
        protected void onPostExecute(String result){
            Bundle b=new Bundle();
            b.putStringArrayList("names",nearbyNames);
            b.putParcelableArrayList("dps", dpImageArry);
            b.putStringArrayList("ids", nearbyIds);
            b.putStringArrayList("dists",nearbyDist);
            Intent i=new Intent(MainScreen.this, nearbyList.class);
            i.putExtras(b);
            putMeOnMap();
        for(int nearbyCount=0;nearbyCount<nearbyCoordinates.size();nearbyCount++) {


            putOnMap(nearbyCoordinates.get(nearbyCount),nearbyNames.get(nearbyCount),nearbyDist.get(nearbyCount));
        }
            pDialog.dismiss();



            //startActivity(i);


        }





    }

    /***************************************************************************************************************/






}
