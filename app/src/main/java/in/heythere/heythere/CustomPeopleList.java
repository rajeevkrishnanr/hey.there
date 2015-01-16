package in.heythere.heythere;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by rajeev on 17-Jan-15.
 */
public class CustomPeopleList extends ArrayAdapter {

    private final Activity context;
    private final ArrayList<String> details;
    private final ArrayList<Bitmap> dpimage;

    public CustomPeopleList(Activity context, ArrayList<String> details, ArrayList<Bitmap> dpimage) {
        super(context, R.layout.list_people, details);
        this.context = context;
        this.details = details;
        this.dpimage = dpimage ;
    }
    @Override

    public View getView(int position, View view, ViewGroup parent)
    {
        LayoutInflater inflater =context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.list_people,null,true);
        TextView Name = (TextView)rowView.findViewById(R.id.people_name);
        ImageView dpImageView = (ImageView)rowView.findViewById(R.id.people_dp_img);
        Name.setText(details.get(position));
        dpImageView.setImageBitmap(dpimage.get(position));
        return rowView;

    }


}
