package vergauwen.simon.moviepop;


import android.content.*;
import android.net.*;
import android.os.*;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.*;
import android.util.*;
import android.view.*;
import android.support.v7.widget.Toolbar;
import android.widget.*;
import org.json.*;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.*;

//Main Activity, Activity that shows top X highest rated movies from IMDB
public class MainActivity extends AppCompatActivity {
    //View injections with Butterknife
    @Bind(R.id.gridview) GridView gridView;
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.drawer_layout) DrawerLayout mDrawerLayout;
    @Bind(R.id.navigation_view) NavigationView navigationView;
    //@Bind(R.id.imageview) ImageView imageView;

    private ImageAdapter imgAdapter;
    private String[][] retrievedData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);

        imgAdapter = new ImageAdapter(this);
        gridView.setAdapter(imgAdapter);

        if (savedInstanceState == null) {
            // TODO instanciate default values

            FetchMovieData fetchMovieData = new FetchMovieData();
            fetchMovieData.execute("Top Movies");
        } else {
            // TODO read instance state from savedInstanceState
            // and set values to views and private fields
            //mSomeUserInput.setText(savedInstanceState.getString("mSomeUserInput"));
            //mSomeExampleField = savedInstanceState.getInt("mSomeExampleField");
        }



        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                //TOAST FOR TESTING
                //Toast.makeText(MainActivity.this, "" + position, Toast.LENGTH_SHORT).show();

                //LAUNCH INTENT WHEN CLICK ON POSTER (item in gridview, onItemClick in Adapter)
                Intent detailIntent = new Intent(MainActivity.this, MovieDetail.class)
                        .putExtra("urlPoster", retrievedData[position][3])
                        .putExtra("title", retrievedData[position][2])
                        .putExtra("rating", retrievedData[position][1])
                        .putExtra("year", retrievedData[position][4])
                        .putExtra("description", retrievedData[position][5]);
                startActivity(detailIntent);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();
                Toast.makeText(MainActivity.this, menuItem.getTitle(), Toast.LENGTH_LONG).show();
                FetchMovieData fetchMovieData = new FetchMovieData();
                fetchMovieData.execute((String) menuItem.getTitle());
                return true;
            }
        });

        //IF GLIDE IS NOT WORKING TEST HERE, REPLACE GRIDVIEW IN LAYOUT TO IMAGEVIEW.
        /*Glide.with(this)
                .load("http://ia.media-imdb.com/images/M/MV5BMTg3OTM2OTc5MV5BMl5BanBnXkFtZTgwMjMxNDM0NTE@._V1_SX214_AL_.jpg")
                .override(200, 300)
                .centerCrop()
                .into(imageView);*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_settings:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    //onResume always gets called, in the case onPause->onResume. onStart doesn't get called.
    @Override
    protected void onStart() {
        super.onStart();
        Log.e("ON START:", "TRUE");
    }

    private class FetchMovieData extends AsyncTask<String, Void, String[][]> {
        private final String LOG_TAG = FetchMovieData.class.getSimpleName();

        @Override
        protected String[][] doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String movieJSONStr = null;
            String FORMAT = "JSON";

            // BUILD URL TO GET JSON FROM IMDB API
            //TOP
            //http://www.myapifilms.com/imdb/top?format=JSON&start=1&end=15&data=S&token=83b73e97-a869-4f3a-8717-38378197ced9
            //TEATHER
            //http://www.myapifilms.com/imdb/inTheaters?format=JSON&lang=en-us&token=83b73e97-a869-4f3a-8717-38378197ced9
            //COMING
            http://www.myapifilms.com/imdb/comingSoon?format=JSON&lang=en-us&date=2015-09&token=83b73e97-a869-4f3a-8717-38378197ced9
            try {

                final String MOVIE_BASE_URL = "www.myapifilms.com";
                final String FORMAT_PARAM = "format";
                final String LANG_PARAM = "lang";
                final String LANG = "en-us";
                final String TOKEN_PARAM = "token";
                final String TOKEN = "83b73e97-a869-4f3a-8717-38378197ced9";

                Uri.Builder builder = new Uri.Builder();

                switch(params[0]){
                    case "Top Movies":
                        final String STOP_PARAM = "end";
                        final String START_PARAM = "start";
                        final String DATA_PARAM = "data";
                        builder.scheme("http")
                                .authority(MOVIE_BASE_URL)
                                .appendPath("imdb")
                                .appendPath("top")
                                .appendQueryParameter(FORMAT_PARAM, FORMAT)
                                .appendQueryParameter(START_PARAM, "1")
                                .appendQueryParameter(STOP_PARAM, "50")
                                .appendQueryParameter(DATA_PARAM, "F")
                                .appendQueryParameter(TOKEN_PARAM, TOKEN);
                        break;
                    case "In Theater":
                        builder.scheme("http")
                                .authority(MOVIE_BASE_URL)
                                .appendPath("imdb")
                                .appendPath("inTheaters")
                                .appendQueryParameter(FORMAT_PARAM, FORMAT)
                                .appendQueryParameter(LANG_PARAM, LANG)
                                .appendQueryParameter(TOKEN_PARAM, TOKEN);
                        break;
                    case "Coming Soon":
                        Date date = new Date();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM", Locale.US);
                        final String DATE = sdf.format(date);
                        final String DATE_PARAM = "date";
                        builder.scheme("http")
                                .authority(MOVIE_BASE_URL)
                                .appendPath("imdb")
                                .appendPath("comingSoon")
                                .appendQueryParameter(FORMAT_PARAM, FORMAT)
                                .appendQueryParameter(LANG_PARAM, LANG)
                                .appendQueryParameter(DATE_PARAM,DATE)
                                .appendQueryParameter(TOKEN_PARAM, TOKEN);
                        break;
                    default:
                        Toast.makeText(MainActivity.this, "ERROR LOADING DATA", Toast.LENGTH_SHORT).show();
                }


                URL url = new URL(builder.build().toString());
                Log.v(LOG_TAG, url.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    //End line to create a formatted JSON LOG for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                movieJSONStr = buffer.toString();

               // Log.v(LOG_TAG, "Movie JSON String" + movieJSONStr);

            } catch (Exception e) {

            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return getDataFromMovies(movieJSONStr,params[0]);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            return null;
        }

        private String[][] getDataFromMovies(String movieJsonStr, String data) throws JSONException {
            // These are the names of the JSON objects that need to be extracted.
            final String[] MOVIE_DETAILS = {"idIMDB", "rating", "title", "urlPoster", "year","simplePlot"};
            JSONArray movieJSONArray;
            JSONObject movieJSONObject;
            String[][] resultStrs;

            switch(data){
                case "Top Movies":
                    movieJSONArray = new JSONArray(movieJsonStr);

                    //JSONArray movieJSONArray = movieJson.getJSONArray("");

                    resultStrs = new String[movieJSONArray.length()][MOVIE_DETAILS.length];
                    for(int i = 0; i < movieJSONArray.length(); i++) {
                        JSONObject movieDetails = movieJSONArray.getJSONObject(i);
                        for(int j = 0; j < MOVIE_DETAILS.length; j++){
                            resultStrs[i][j] = movieDetails.getString(MOVIE_DETAILS[j]);
                        }
                    }

                    //FOR DEBUGGING
                    /*String debugSTR = null;
                    for(int i = 0; i < movieJSONArray.length(); i++) {
                        for(int j = 0; j < MOVIE_DETAILS.length; j++){
                            debugSTR += resultStrs[i][j] + " ";
                        }
                        Log.v(LOG_TAG, "MOVIE DETAIL DEBUG STRING: " + debugSTR);
                        debugSTR = null;
                    }*/
                    break;
                case "In Theater":
                case "Coming Soon":
                    JSONArray movieJSONArrayTOP = new JSONArray(movieJsonStr);
                    int totMovies = 0;
                    for(int i = 0; i < movieJSONArrayTOP.length() ; i++){
                        totMovies += movieJSONArrayTOP.getJSONObject(i).getJSONArray("movies").length();
                    }
                    resultStrs = new String[totMovies][MOVIE_DETAILS.length];

                    int numFilm = 0;
                    for(int k = 0; k < movieJSONArrayTOP.length() ; k++ ) {
                        movieJSONArray = movieJSONArrayTOP.getJSONObject(k).getJSONArray("movies");
                        for (int i = 0; i < movieJSONArray.length(); i++) {
                            JSONObject movieDetails = movieJSONArray.getJSONObject(i);
                            for (int j = 0; j < MOVIE_DETAILS.length; j++) {
                                resultStrs[numFilm][j] = movieDetails.getString(MOVIE_DETAILS[j]);
                            }
                            numFilm++;
                        }
                    }

                    /*//FOR DEBUGGING
                    debugSTR = null;
                    for(int i = 0; i < totMovies; i++) {
                        for(int j = 0; j < MOVIE_DETAILS.length; j++){
                            debugSTR += resultStrs[i][j] + " ";
                        }
                        Log.v(LOG_TAG, "MOVIE DETAIL DEBUG STRING: " + debugSTR);
                        debugSTR = null;
                    }*/
                    break;
                default:
                    resultStrs = null;
            }

            return resultStrs;
        }

        protected void onPostExecute(String[][] result) {
            if(result != null){
                imgAdapter.clear();
                imgAdapter.add(result);
                imgAdapter.notifyDataSetChanged();
                retrievedData = result;
            }
        }
    }
}
