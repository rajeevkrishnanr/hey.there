package in.heythere.heythere;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;




public class RegActivity extends FragmentActivity {

    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private Uri imageUri;

    private static Bitmap dpImage = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        // Initilization
        mPager = (ViewPager) findViewById(R.id.pager);

        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());


        mPager.setAdapter(mPagerAdapter);

    }

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
            //TODO App Crash fix
            ImageView dpPreview = (ImageView)findViewById(R.id.dp_preview);

            //APP Crash on dpPreview Bitmap set
            Log.d("Req Code 2", "Going to set null");

            // dpPreview.setImageBitmap(null);

            if (dpImage != null)
                dpImage.recycle();


            Log.d("Login attempt", "");
            //Create an instance of bundle and get the returned data
            Bundle extras = data.getExtras();
            //get the cropped bitmap from extras
            Bitmap dpPic = extras.getParcelable("data");
            //set image bitmap to image view


            dpPreview.setImageBitmap(dpPic);
            Log.d("Req Code 2", "Going set null Completed");

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










    public void finishReg(String displayToastText)
    {
        Toast.makeText(this, displayToastText, Toast.LENGTH_LONG).show();
    }

    public void nextPage(View view) {
        mPager.setCurrentItem(mPager.getCurrentItem() + 1);
    }


    public void finishPage(View view) {
        finishReg("OK We are Done! :D :D ");

    }

    /**
     * A simple pager adapter that represents 5 objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {

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



}
