package myandroidhello.com.ap_project.Adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import myandroidhello.com.ap_project.R;
import myandroidhello.com.ap_project.model.GlobalVariables;
import myandroidhello.com.ap_project.model.Photo;

/**
 * Created by jenny on 2018/3/1.
 */

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";

    //vars
    private ArrayList<Photo> mPhotos;
    private ArrayList<String> mFollowing;
    private ListView mListView;
    private MainfeedListAdapter mAdapter;
    private String HTTP_URL = "http://140.119.19.36:80/home.php";
    private String FinalJSonObject;
    public Context context;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mListView = view.findViewById(R.id.listView);
        mFollowing = new ArrayList<>();
        mPhotos = new ArrayList<>();
        context=container.getContext();

//        getFollowing();
        getPhotos();

        return view;
    }

    //TODO: get the friend list
    private void getFollowing(){
        Log.d(TAG, "getFollowing: searching for following");

    }

    private void getPhotos(){
        Log.d(TAG, "getPhotos: getting photos");
        StringRequest stringRequest = new StringRequest(Request.Method.POST,HTTP_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("eeee",response);
                        // After done Loading store JSON response in FinalJSonObject string variable.
                        FinalJSonObject = response ;

                        // Calling method to parse JSON object.
                        new ParseJSonDataClass(getActivity()).execute();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        // Showing error message if something goes wrong.
                        Toast.makeText(getActivity(),error.getMessage(), Toast.LENGTH_LONG).show();

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                GlobalVariables User=(GlobalVariables)context.getApplicationContext();
                params.put("user",User.getId());
                return params;
            }
        };

        // Creating String Request Object.
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        // Passing String request into RequestQueue.
        requestQueue.add(stringRequest);

    }

    private void displayPhotos(){
        if(mPhotos != null){
            Collections.sort(mPhotos, new Comparator<Photo>() {
                @Override
                public int compare(Photo o1, Photo o2) {
                    return o2.getDate_created().compareTo(o1.getDate_created());
                }
            });

            mAdapter = new MainfeedListAdapter(getActivity(), R.layout.layout_mainfeed_listitem, mPhotos);
            mListView.setAdapter(mAdapter);
        }
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
                        Photo photo;

                        // Defining CustomSubjectNamesList AS Array List.
                        mPhotos = new ArrayList<Photo>();

                        for (int i = 0; i < jsonArray.length(); i++) {

                            photo = new Photo();

                            jsonObject = jsonArray.getJSONObject(i);

                            photo.setCaption(jsonObject.getString("content"));
                            photo.setUser_name(jsonObject.getString("name"));
                            photo.setDate_created(jsonObject.getString("date"));
                            photo.setImage_path(jsonObject.getString("pic_url"));


                            // Adding subject list object into CustomSubjectNamesList.
                            mPhotos.add(photo);
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
            displayPhotos();

        }
    }


    }






