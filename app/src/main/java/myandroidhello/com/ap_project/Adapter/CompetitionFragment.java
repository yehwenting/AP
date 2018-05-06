package myandroidhello.com.ap_project.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import myandroidhello.com.ap_project.Data.MySingleTon;
import myandroidhello.com.ap_project.R;
import myandroidhello.com.ap_project.Util.Values;
import myandroidhello.com.ap_project.model.CompetitionGroup;
import myandroidhello.com.ap_project.model.Competitions;

/**
 * Created by Yehwenting on 2018/4/25.
 */

public class CompetitionFragment extends Fragment{

    Context context;
    @Bind(R.id.addgroup_rv)
    RecyclerView recyclerView;

    List<Competitions> competitionList = new ArrayList<>();



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.adapter_addgroup, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        //抓出成立團
        getCompetition();
    }

    public static Fragment newInstance() {
        return new CompetitionFragment();
    }

    private void getCompetition(){
        StringRequest stringRequest;
        stringRequest = new StringRequest(Request.Method.POST, Values.COMPETITION_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("rrrrr",response);
                        try {
                            //converting the string to json array object
                            JSONObject jsonObject=new JSONObject(response);
                            JSONArray array = jsonObject.getJSONArray("response");

                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {
                                List<CompetitionGroup> competitionGroups=new ArrayList<>();
                                JSONObject competition = array.getJSONObject(i);

                                competitionList.add(new Competitions(
                                        competition.getString("c_name"),
                                        competition.getString("c_content"),
                                        competition.getString("c_group_num"),
                                        competition.getString("c_deadline"),
                                        competition.getString("goal"),
                                        true,
                                        competitionGroups
                                ));

                                JSONArray group = competition.getJSONArray("group");
                                    for(int j=0;j<group.length();j++){
                                        JSONObject competitiong = group.getJSONObject(j);
                                        competitionGroups.add(new CompetitionGroup(
                                                competitiong.getString("name"),
                                                competitiong.getString("pic_url"),
                                                competitiong.getString("cp_name"),
                                                competitiong.getString("cp_time"),
                                                competitiong.getString("cp_place"),
                                                competitiong.getString("cp_num"),
                                                "1",
                                                competitiong.getString("note")
                                        ));
                                    }

                                //getting product object from json array




                            }

                            CompetitionAdapter competitionAdapter = new CompetitionAdapter(competitionList);
                            //要記得有layoutmanager
                            recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
                            recyclerView.setAdapter(competitionAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.d("error","do not get ");

            }


        });
//        {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String,String> params= new HashMap<>();
//                Mysql mysql=new Mysql();
//                String query=mysql.getCompetition();
//                params.put("query",query);
//                Log.d("rrrrr",query);
//                return params;
//            }
//        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleTon.getmInstance(this.getActivity()).addToRequestque(stringRequest);


    }
    }

