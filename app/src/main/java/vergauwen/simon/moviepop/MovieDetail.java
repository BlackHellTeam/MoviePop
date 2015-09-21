package vergauwen.simon.moviepop;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.*;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.bumptech.glide.*;
import butterknife.*;

public class MovieDetail extends AppCompatActivity {
    private String urlPoster;
    @Bind(R.id.imageView) ImageView imageView;
    @Bind(R.id.txtTitle) TextView vwTitle;
    @Bind(R.id.txtRating) TextView vwRating;
    @Bind(R.id.txtYear) TextView vwYear;
    @Bind(R.id.txtDescription) TextView vwDescription;
    @Bind(R.id.toolbar) android.support.v7.widget.Toolbar toolbar;
    @Bind(R.id.drawer_layout) DrawerLayout mDrawerLayout;
    @Bind(R.id.navigation_view) NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        //actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        //If intent exist, and has the extraData
        if (intent != null && intent.hasExtra("urlPoster")) {
            vwTitle.setText("Title: " + intent.getStringExtra("title"));
            vwRating.setText("Rating: " + intent.getStringExtra("rating"));
            vwYear.setText("Year: " + intent.getStringExtra("year"));
            vwDescription.setText("Plot: " + intent.getStringExtra("description"));
            urlPoster = intent.getStringExtra("urlPoster");
        }
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus){
        super.onWindowFocusChanged(hasFocus);
        int height = imageView.getHeight();
        int width = (int) (height / 1.5);

        Glide.with(this)
                .load(urlPoster)
                .override(width, height)
                .centerCrop()
                .error(R.drawable.sample_2)
                .into(imageView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_movie_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // Respond to the action bar's Up/Home button
        if( id == R.id.home){
            NavUtils.navigateUpFromSameTask(this);
            Log.e("NAVIGATE FROM ACTION BAR UP BUTTON: ", "TRUE");
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
