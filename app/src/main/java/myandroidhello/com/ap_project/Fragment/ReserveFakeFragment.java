package myandroidhello.com.ap_project.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import myandroidhello.com.ap_project.Adapter.ReserveAdapter;
import myandroidhello.com.ap_project.Data.MySingleTon;
import myandroidhello.com.ap_project.Data.Mysql;
import myandroidhello.com.ap_project.R;
import myandroidhello.com.ap_project.Util.Values;
import myandroidhello.com.ap_project.Model.Item;

/**
 * Created by Yehwenting on 2018/2/4.
 */



public class ReserveFakeFragment extends Fragment {

    List<Item> items=new ArrayList<>();
    @Bind(R.id.reserve_rv) RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.adapter_reserve, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        //顯示健身器材，連接資料庫
        getEquipment();
    }

    public static Fragment newInstance() {
        return new ReserveFakeFragment();
    }

    public void getEquipment(){
            StringRequest stringRequest=new StringRequest(Request.Method.POST, Values.READ_DATA_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                Log.d("success",response);
                                JSONObject jsonObject=new JSONObject(response);
                                JSONArray subArray = jsonObject.getJSONArray("response");
                                    Log.d("response111",String.valueOf(subArray.length()));
                                    Log.d("success","responseOK");
                                for(int i=0;i<subArray.length();i++){
                                    String text=subArray.getJSONObject(i).getString("eName");
                                    String url=subArray.getJSONObject(i).getString("url");
                                    Log.d("response111",text);
                                    Item item = new Item(text, "this is child item" + (i + 1), true,url);
                                    items.add(item);
                                    Log.d("response111",item.getText());
                                }
                                ReserveAdapter myAdapter=new ReserveAdapter(items);
                                recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
                                recyclerView.setAdapter(myAdapter);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    Log.d("error","do not get equipment from mysql 2");
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params=new HashMap<>();
                    Mysql mysql=new Mysql();
                    String query=mysql.getEquipmentName();
                    params.put("query",query);
                    return params;
                }
            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            MySingleTon.getmInstance(this.getActivity()).addToRequestque(stringRequest);

    }

}
