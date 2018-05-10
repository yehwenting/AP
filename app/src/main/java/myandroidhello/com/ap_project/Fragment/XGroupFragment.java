package myandroidhello.com.ap_project.Fragment;

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

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import myandroidhello.com.ap_project.Adapter.XGroupAdapter;
import myandroidhello.com.ap_project.Model.XGroupitem;
import myandroidhello.com.ap_project.R;

public class XGroupFragment extends Fragment {
    private static final String TAG = "XGroupFragment";

    //vars
    private ArrayList<XGroupitem> xGroupitems;
    private ListView mListView;
    private XGroupAdapter mAdapter;
    private String HTTP_URL = "http://140.119.19.36:80/xgroup.php";
    private String FinalJSonObject;
    private String uid = "1584429851580176";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_xreserve, container, false);
        mListView = (ListView) view.findViewById(R.id.listView);
        xGroupitems = new ArrayList<>();

        getData();

        return view;
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
                        new ParseJSonDataClass(getActivity()).execute();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        // Showing error message if something goes wrong.
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();

                    }
                });

        // Creating String Request Object.
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        // Passing String request into RequestQueue.
        requestQueue.add(stringRequest);

    }

    private void display() {

            mAdapter = new XGroupAdapter(getActivity(), R.layout.layout_xgroupitem, xGroupitems);
            mListView.setAdapter(mAdapter);
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

                            groupitem.setDate(jsonObject.getString("date"));
                            groupitem.setGname(jsonObject.getString("gname"));
                            groupitem.setPlace(jsonObject.getString("place"));
                            groupitem.setType(jsonObject.getString("type"));


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