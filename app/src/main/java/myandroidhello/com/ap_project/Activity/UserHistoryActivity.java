package myandroidhello.com.ap_project.Activity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import myandroidhello.com.ap_project.Adapter.HistoryAdapter;
import myandroidhello.com.ap_project.Model.GlobalVariables;
import myandroidhello.com.ap_project.Model.XGroupitem;
import myandroidhello.com.ap_project.R;

public class UserHistoryActivity extends AppCompatActivity {
    private static final String TAG = "UserHistoryActivity";

    //vars
    private ArrayList<XGroupitem> xGroupitems;
    private ListView mListView;
    private HistoryAdapter mAdapter;
    private String HTTP_URL = "http://140.119.19.36:80/history.php";
    private String FinalJSonObject;
    private String uid ;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_history);

        mListView = (ListView) findViewById(R.id.listView);
        xGroupitems = new ArrayList<>();
        GlobalVariables user=(GlobalVariables)this.getApplicationContext();
        uid = user.getId();
        back=findViewById(R.id.back);
        back.setOnClickListener(view -> {
            finish();
        });

        getData();

    }
    private void display() {

        mAdapter = new HistoryAdapter(UserHistoryActivity.this, R.layout.layout_history_item, xGroupitems);
        mListView.setAdapter(mAdapter);
    }


    private void getData() {
        Log.d(TAG, "getData: getting data");
        StringRequest stringRequest = new StringRequest(HTTP_URL + "?getUserId=" + uid,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        // After done Loading store JSON response in FinalJSonObject string variable.
                        FinalJSonObject = response;

                        // Calling method to parse JSON object.
                        new ParseJSonDataClass(UserHistoryActivity.this).execute();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        // Showing error message if something goes wrong.
                        Toast.makeText(UserHistoryActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();

                    }
                });

        // Creating String Request Object.
        RequestQueue requestQueue = Volley.newRequestQueue(UserHistoryActivity.this);

        // Passing String request into RequestQueue.
        requestQueue.add(stringRequest);

    }

    private class ParseJSonDataClass extends AsyncTask<Void, Void, Void> {

        public Context context;


        public ParseJSonDataClass(Context context) {

            this.context = context;
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            try {

                // Checking whether FinalJSonObject is not equals to null.
                if (FinalJSonObject != null) {

                    // Creating and setting up JSON array as null.
                    JSONArray jsonArray = null;
                    try {

                        // Adding JSON response object into JSON array.
                        jsonArray = new JSONObject(FinalJSonObject).getJSONArray("data");

                        // Creating JSON Object.
                        JSONObject jsonObject;

                        // Creating Subject class object.
                        XGroupitem groupitem;

                        // Defining CustomSubjectNamesList AS Array List.
                        xGroupitems = new ArrayList<XGroupitem>();

                        for (int i = 0; i < jsonArray.length(); i++) {

                            groupitem = new XGroupitem();

                            jsonObject = jsonArray.getJSONObject(i);

                            groupitem.setDate(jsonObject.getString("start_time"));
                            groupitem.setGname(jsonObject.getString("end_time"));
                            groupitem.setType(jsonObject.getString("ename"));


                            // Adding subject list object into CustomSubjectNamesList.
                            xGroupitems.add(groupitem);
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result)

        {
            display();

        }
    }


}
