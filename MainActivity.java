package edu.illinois.cs.cs125.mp7;

//import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
//import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    /** Default logging tag for messages from the main activity. */
    private static final String TAG = "MP7:Main";

    private String copy = "";

    /** Request queue for our API requests. */
    protected static RequestQueue requestQueue;

    private ArrayList<String> savedQuotes = new ArrayList<String>();
    public void save(final String quote) {
        savedQuotes.add(quote);
    }
    public ArrayList<String> getSavedQuotes() {
        return savedQuotes;
    }

    /**
     * Run when this activity comes to the foreground.
     *
     * @param savedInstanceState unused
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up the queue for our API requests
        requestQueue = Volley.newRequestQueue(this);

        setContentView(R.layout.activity_main);
        final Button quote = findViewById(R.id.getQuote);
        quote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Log.d(TAG, "quote button clicked");
                startAPICall();
            }
        });
        final Button saved = findViewById(R.id.saveQuote);
        saved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Log.d(TAG, "save button clicked");
                save(copy);
            }
        });
        final Button load = findViewById(R.id.accessQuotes);
        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Log.d(TAG, "load button clicked");
                TextView text = findViewById(R.id.dailyQuote);
                text.setText(getSavedQuotes().toString());
            }
        });

    }

    /**
     * Run when this activity is no longer visible.
     */
    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * Make a call to the daily quote api.
     */
    void startAPICall() {

        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    "https://favqs.com/api/qotd",
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            try {
                                if (response != null) {
                                    final TextView text = findViewById(R.id.dailyQuote);
                                    if (text == null) {
                                        Log.d(TAG, "the text is null");
                                    } else {
                                        copy = response.toString();
                                        text.setText(response.toString());
                                    }
                                }

                                Log.d(TAG, response.toString(2));
                            } catch (JSONException ignored) {
                                Log.e(TAG, ignored.getStackTrace().toString());
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(final VolleyError error) {
                    Log.e(TAG, error.toString());
                }
            });
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            Log.d(TAG, "exception 127");
            e.printStackTrace();
        }
    }
}
