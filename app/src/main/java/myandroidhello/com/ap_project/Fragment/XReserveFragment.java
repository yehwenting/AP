package myandroidhello.com.ap_project.Fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import myandroidhello.com.ap_project.Adapter.XReserveAdapter;
import myandroidhello.com.ap_project.Model.GlobalVariables;
import myandroidhello.com.ap_project.Model.XReserveitem;
import myandroidhello.com.ap_project.R;

public class XReserveFragment extends Fragment {
    private static final String TAG = "XReserveFragment";

    //vars
    private ArrayList<XReserveitem> xReserveitems;
//    private ListView mListView;
    private SwipeMenuListView mListView;
    private XReserveAdapter mAdapter;
    private String HTTP_URL = "http://140.119.19.36:80/xreserve.php";
    private String FinalJSonObject;
    private String uid;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_xreserve, container, false);
//        mListView = (ListView) view.findViewById(R.id.listView);
        mListView = (SwipeMenuListView) view.findViewById(R.id.listView);
        GlobalVariables user=(GlobalVariables)getContext().getApplicationContext();
        uid = user.getId();
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getActivity());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));
                // set item width
                openItem.setWidth(170);
                // set item title
                openItem.setTitle("Open");
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getActivity());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(170);
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

        // set creator
        mListView.setMenuCreator(creator);
        mListView.setCloseInterpolator(new BounceInterpolator());

        xReserveitems = new ArrayList<>();

        getItems();

        return view;
    }


    private void getItems(){
        Log.d(TAG, "getPhotos: getting photos");
        StringRequest stringRequest = new StringRequest(HTTP_URL + "?getUserId=" + uid,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

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
                });

        // Creating String Request Object.
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        // Passing String request into RequestQueue.
        requestQueue.add(stringRequest);

    }

    private void display(){
        if(xReserveitems != null){
            Collections.sort(xReserveitems, new Comparator<XReserveitem>() {
                @Override
                public int compare(XReserveitem o1, XReserveitem o2) {
                    return o2.getDate().compareTo(o1.getDate());
                }
            });

            mAdapter = new XReserveAdapter(getActivity(), R.layout.layout_xreserve_item, xReserveitems);
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
                        XReserveitem reserveitem;

                        // Defining CustomSubjectNamesList AS Array List.
                        xReserveitems = new ArrayList<XReserveitem>();

                        for (int i = 0; i < jsonArray.length(); i++) {

                            reserveitem = new XReserveitem();

                            jsonObject = jsonArray.getJSONObject(i);

                            reserveitem.setDate(jsonObject.getString("date"));
                            reserveitem.setEname(jsonObject.getString("eName"));
                            reserveitem.setStart_time(jsonObject.getLong("start_time"));
                            reserveitem.setEnd_time(jsonObject.getLong("end_time"));
                            reserveitem.setPic_path(jsonObject.getString("url"));


                            // Adding subject list object into CustomSubjectNamesList.
                            xReserveitems.add(reserveitem);
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