package vergauwen.simon.moviepop;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.*;
import android.widget.*;
import com.bumptech.glide.Glide;

/**
 * Created by Simon on 19/09/15.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private String[] urlPoster;

    public ImageAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        if(urlPoster != null){
            return urlPoster.length;
        }
        return 0;
    }

    public String[] getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            //.setLayoutParams(new GridView.LayoutParams(350, 500));
            //imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            imageView = (ImageView) convertView;
        }

        //imageView.setImageResource(mThumbIds[position]);

        /*Picasso.with(mContext)
                .load("http://inthecheesefactory.com/uploads/source/glidepicasso/cover.jpg")
                .resize(350, 500)
                .centerCrop()
                .error(R.drawable.sample_2)
                .into(imageView);*/

        Resources r = mContext.getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 110, r.getDisplayMetrics());
        int width = (int) px;
        int height = (int) (px * 1.5);

        Glide.with(mContext)
                .load(urlPoster[position])
                .override(width,height )
                .centerCrop()
                .error(R.drawable.sample_2)
                .into(imageView);
        return imageView;
    }

    public void clear(){
        urlPoster = new String[0];
    }

    public void add(String[][] data){
        urlPoster = new String[data.length];
        for(int i = 0; i < data.length;i++) {
            urlPoster[i] = data[i][3];
        }
    }
}
