package myandroidhello.com.ap_project.Fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.isapanah.awesomespinner.AwesomeSpinner;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import myandroidhello.com.ap_project.Adapter.RankListAdapter;
import myandroidhello.com.ap_project.R;
import myandroidhello.com.ap_project.Model.Rankitem;

public class RankByExerciseFragment extends Fragment {

    private static final String TAG = "RankByExerciseFragment";

    //vars
    private ListView mListView;
    private RankListAdapter mAdapter;
    private ArrayList<Rankitem> mRankitem;
    private String HTTP_URL = "http://140.119.19.36:80/rankByExercise.php";
    private String FinalJSonObject;
    private List<Rankitem> rankList;
    private TextView rankTv;
    private String sportFilter = null;

    private String uname ="李如紅";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_rank_by_exercise, container, false);
        mListView = (ListView) view.findViewById(R.id.rank_lv);
//        rankTv = (TextView) view.findViewById(R.id.rank_tv);
        AwesomeSpinner spinner = (AwesomeSpinner) view.findViewById(R.id.rank_spinner);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.sports, android.R.layout.simple_spinner_dropdown_item);
//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, sports);
        spinner.setAdapter(arrayAdapter, 0);
        spinner.setOnSpinnerItemClickListener(new AwesomeSpinner.onSpinnerItemClickListener<String>() {
            @Override
            public void onItemSelected(int position, String itemAtPosition) {
                sportFilter = itemAtPosition;
                Log.d(TAG, "onItemSelected: Selected sport: " + sportFilter);
                getRankData(sportFilter);
            }
        });

        return view;
    }

    private void getRankData(String sport){
        Log.d(TAG, "getRankData: getting rank data");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, HTTP_URL + "?getSport=" + sport,
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

    /**
     * Returns a sorted ArrayList of ranking data
     * @param rankitemList
     */
    private List<Rankitem> sortRankData(List<Rankitem> rankitemList){
        Log.d(TAG, "sortRankData: sorting rank map");
        Log.d(TAG, "sortRankData: rankitemList: " + rankitemList);

        List<Rankitem> sortedRankList = new ArrayList<>();
        for (int i = 0; i < rankitemList.size(); i++){
            boolean matchName = false;
            for (int j = 0; j < sortedRankList.size(); j++){
                if (rankitemList.get(i).getUser_name().equals(sortedRankList.get(j).getUser_name())) {
                    sortedRankList.get(j).setStart_time(rankitemList.get(i).getStart_time() + sortedRankList.get(j).getStart_time());
                    sortedRankList.get(j).setEnd_time(rankitemList.get(i).getEnd_time() + sortedRankList.get(j).getEnd_time());
                    matchName = true;
                }
            }
            if (!matchName){
                sortedRankList.add(rankitemList.get(i));
            }
        }
        Log.d(TAG, "sortRankData: sortedRankList: " + sortedRankList);

        Comparator<Rankitem> sortByExerciseTime = (e1, e2) -> {
            return Long.compare(e1.getEnd_time()-e1.getStart_time(), e2.getEnd_time()-e2.getStart_time());
        };
        Collections.sort(sortedRankList, Collections.reverseOrder(sortByExerciseTime));
        Log.d(TAG, "sortRankData: sortedRankList: " + sortedRankList);
//        for (int i = 0; i < sortedRankList.size(); i++){
//            if (uname.equals(sortedRankList.get(i).getUser_name())){
//                rankTv.setText("第 " + (i+1) + " 名");
//            }
//        }
        return sortedRankList;

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
                        Rankitem rankitem;

                        long exerciseTime ;

                        // Defining CustomSubjectNamesList AS Array List.
                        mRankitem = new ArrayList<Rankitem>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            rankitem = new Rankitem();

                            jsonObject = jsonArray.getJSONObject(i);

                            rankitem.setUser_name(jsonObject.getString("name"));
                            rankitem.setStart_time(jsonObject.getLong("start_time"));
                            rankitem.setEnd_time(jsonObject.getLong("end_time"));
                            rankitem.setPic_path(jsonObject.getString("pic_url"));


                            // Adding subject list object into CustomSubjectNamesList.
                            mRankitem.add(rankitem);
                            Log.d(TAG, "doInBackground: mRankitem: " + mRankitem);
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
            rankList = sortRankData(mRankitem);
            mAdapter = new RankListAdapter(getActivity(), R.layout.layout_ranklist_item, rankList);
            mListView.setAdapter(mAdapter);

        }
    }



}
