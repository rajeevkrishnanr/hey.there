package in.heythere.heythere;



import android.app.Fragment;
import android.app.FragmentManager;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentActivity;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Demonstrates a "screen-slide" animation using a
 * automatically plays such an animation when
 *
 * <p>This sample shows a "next" button that advances the user to the next step in a wizard,
 * animating the current screen out (to the left) and the next screen in (from the right). The
 * reverse animation is played when the user presses the "previous" button.</p>
 *
 *
 */
public class RegActivity extends FragmentActivity {
    /**
     * The Variables
     *
     */

    private String new_user, new_pass,new_name,new_emailid;
    private ProgressDialog pDialog;
    private static final String REG_URL = "http://192.168.0.13/heyserver/server/reg_exec.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private Uri imageUri;
    private Button finishbut;
    private static Bitmap dpImage = null;
    JSONParser jsonParser = new JSONParser();



    //Debug String
    String newdbgtext;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);



        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        //Init The UI Fields
        mPager.setOnTouchListener(new ViewPager.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                return false;
            }


        });


        mPager.setOnFocusChangeListener(new ViewPager.OnFocusChangeListener() {
            @Override

            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus == false){
                    //get text from edittext field...
                   /* new_user = (EditText) mPager.findViewById(R.id.new_username);
                    new_pass = (EditText) mPager.findViewById(R.id.new_password);
                    new_name = (EditText) mPager.findViewById(R.id.new_fullname);
                    new_emailid = (EditText) mPager.findViewById(R.id.new_email);
*/
                    Log.d("In Onfocus Change", "Possible Fail Point!");
//                        newdbgtext = new_user.getText().toString();
                    Log.d("In Onfocus Change", "Fail Point Over.");



                }

            }
        });




    }









    public void toastfinishReg(String displayToastText)
    {
        Toast.makeText(this, displayToastText, Toast.LENGTH_LONG).show();
    }


    public void nextPage(View view) {
        mPager.setCurrentItem(mPager.getCurrentItem() + 1);
    }


    public void finishPage(View view) {
        Log.d("Finish Button Clicked", "starting");
        //String new_username_data = new_user.getText().toString();
        //Log.d("*** DEBUG ***The Usernames : ", new_username_data);
        //Log.d("*** DEBUG ***The Usernames : ", newdbgtext);
        //startAsyncInfo();

        toastfinishReg(newdbgtext);
        new AttemptReg().execute(new_user,new_pass,new_name,new_emailid);
        Log.d("AttemptReg()", "Completed");
    }

    //TODO
    public void getDatafromFragment1(String username_frag,String pass_frag,String pass_frag_verify)
        {
        Log.d("Datafrom Fragment in activity()", "Working");
        new_user= username_frag;

            new_pass = pass_frag;


            //mPager.setCurrentItem(2);



        Log.d("Data from frgament received", "data is" + newdbgtext);

    }



    public void getDatafromFragment2(String fullname_frag, String email_frag) {

        new_name=fullname_frag;
        new_emailid=email_frag;

    }

    public void actionNextPage() {
        mPager.setCurrentItem(mPager.getCurrentItem() + 1);




    }

    public void actionBackPage() {mPager.setCurrentItem(mPager.getCurrentItem() - 1);
    }

    /**
     * A simple pager adapter that represents 3 objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentPagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    // Top Rated fragment activity
                    return new regscreen1();
                case 1:
                    // Games fragment activity
                    return new regscreen2();
                case 2:
                    // Movies fragment activity
                    return new regscreen3();
            }

            return null;
        }

        @Override
        public int getCount() {
            // get item count - equal to number of tabs
            return 3;
        }


    }





    /**************************************************************************************************************/

    class AttemptReg extends AsyncTask<String, String, String>
    {
        @Override protected void onPreExecute() {
            Log.d("Inside AttemptReg()", "OnPreExec start");
            super.onPreExecute();
            Log.d("Inside AttemptReg()", "OnPreExec super");
            pDialog = new ProgressDialog(RegActivity.this);
            Log.d("Inside AttemptReg()", "OnPreExec Dialog Made");
            pDialog.setMessage("Registration: Getting you Abroad...");
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
            //TODO Debug

            /*Debug Code   Log.d("Async Background ", "Taking String Params");
            */String new_username_Asyncdata = args[0];
            String new_password_Asyncdata = args[1];
            String new_fullname_Asyncdata = args[2];
            String new_email_Asyncdata = args[3];


            Log.d("AttemptReg() Params are",
                    new_username_Asyncdata + "..." + new_password_Asyncdata + "..." +
                            new_fullname_Asyncdata + "..." + new_email_Asyncdata

            );

            Log.d("Background AttemptReg()", "Going to Try Block");

            //todo
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            dpImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            Log.d("AttemptReg() Background", "DP Encoding...");
            String dpImage_encoded = Base64.encodeToString(imageBytes, Base64.DEFAULT);

            try {
                Log.d("AttemptReg() Background", "DP encoded!");


                List<NameValuePair> params = new ArrayList<>();

                Log.d("AttemptReg() Background", "Try Block: Adding Params");

                params.add(new BasicNameValuePair("username", new_username_Asyncdata));
                params.add(new BasicNameValuePair("password", new_password_Asyncdata));
                params.add(new BasicNameValuePair("fullname", new_fullname_Asyncdata));
                params.add(new BasicNameValuePair("email", new_email_Asyncdata));

                // TODO
                params.add(new BasicNameValuePair("dpimage", dpImage_encoded));

                Log.d("JSON request!", "starting");
                JSONObject json = jsonParser.makeHttpRequest( REG_URL, "POST", params);
                success = json.getInt(TAG_SUCCESS);
                //Log.d("Value of Success is", success);
                //success=1;


                if (success == 1) {
                    //Log.d("Successfully Login!", json.toString());
                    msg = "1";
                    Intent ii = new Intent(RegActivity.this, login.class);
                                        finish();

                    // this finish() method is used to tell android os that we are done with current
                    // activity now! Moving to other activity
                    startActivity(ii);
                    //return json.getString(TAG_MESSAGE);
                    return msg;
                } else {
                    //return json.getString(TAG_MESSAGE);
                    msg = "0";
                    return msg;
                }
            }
            catch (JSONException jexp)
            {
                jexp.printStackTrace();
            }




            return msg;
        }


        @Override protected void onPostExecute(String result){

            int status = Integer.parseInt(result) ;
            String disp_text="Try Again";
            switch (status){
                case 0: disp_text =  "Registration Failed";break;
                case 1:disp_text= "Registration Successful! \n Kindly Login ";break;
                case 2: disp_text= "Username already taken!"; mPager.setCurrentItem(0);break;
                default:break;
            }
            pDialog.dismiss();

            Toast.makeText(getApplicationContext(),disp_text , Toast.LENGTH_LONG).show();

        }





    }

    /***************************************************************************************************************/


//The below part Refer http://shaikhhamadali.blogspot.in/2013/09/capture-images-and-crop-images-using.html
    public void uploadDP(View view)
    {
    /* create an instance of intent
     * pass action android.media.action.IMAGE_CAPTURE
     * as argument to launch camera
     */
        //     Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
    /*create instance of File with name img.jpg*/
        //   File file = new File(Environment.getExternalStorageDirectory()+File.separator + "img.jpg");
    /*put uri as extra in intent object*/
        // intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        //My code:


        Intent intent = new Intent();

        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
    /*start activity for result pass intent as argument and request code */
        startActivityForResult(intent, 1);



    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //if request code is same we pass as argument in startActivityForResult
        if(requestCode==1){
            //Crop the captured image using an other intent
            try {
        /*the user's device may not support cropping*/
                imageUri = data.getData();
                cropCapturedImage(imageUri);
            }
            catch(ActivityNotFoundException aNFE){
                //display an error message if user device doesn't support
                String errorMessage = "Sorry - your device doesn't support the crop action!";
                Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        if(requestCode==2){

            ImageView dpPreview = (ImageView)findViewById(R.id.dp_preview);

            //APP Crash on dpPreview Bitmap set
            Log.d("Req Code 2", "Going to set null");



            if (dpImage != null)
                dpImage.recycle();



            //Create an instance of bundle and get the returned data
            Bundle extras = data.getExtras();
            //get the cropped bitmap from extras
            Bitmap dpPic = extras.getParcelable("data");
            //set image bitmap to image view


            dpPreview.setImageBitmap(dpPic);
            dpImage=dpPic;
            Log.d("Req Code 2", "Image set-up  Completed");

        }
    }




    //create helping method cropCapturedImage(Uri picUri)
    public void cropCapturedImage(Uri picUri){
        //call the standard crop action intent
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        //indicate image type and Uri of image
        //cropIntent.setDataAndType(picUri, "image/*");
        cropIntent.setType("image/*");
        Log.d("Crop Fn", "Set pic URI");

        cropIntent.setData(picUri);
        Log.d("Crop Fn", "PicUri is: " + picUri);

        //set crop properties
        cropIntent.putExtra("crop", "true");
        //indicate aspect of desired crop
        cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1);
        //indicate output X and Y
        cropIntent.putExtra("outputX", 200);
        cropIntent.putExtra("outputY", 200);
        //retrieve data on return
        Log.d("Crop Fn:", "Extra title:" + cropIntent.EXTRA_TITLE);
        cropIntent.putExtra("return-data", true);
        Log.d("Crop Fn", "After Put Extra");

        //start the activity - we handle returning in onActivityResult
        startActivityForResult(cropIntent, 2);
    }
/**********************************************************************************************************/

}
