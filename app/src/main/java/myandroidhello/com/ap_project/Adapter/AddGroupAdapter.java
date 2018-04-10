package myandroidhello.com.ap_project.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import myandroidhello.com.ap_project.Activity.JFGroupActivity;
import myandroidhello.com.ap_project.Data.MySingleTon;
import myandroidhello.com.ap_project.Data.Mysql;
import myandroidhello.com.ap_project.R;
import myandroidhello.com.ap_project.Util.Values;
import myandroidhello.com.ap_project.model.AddGroup;
import myandroidhello.com.ap_project.model.GlobalVariables;

/**
 * Created by Stanley on 2018/3/7.
 */

public class AddGroupAdapter extends RecyclerView.Adapter<AddGroupAdapter.GroupViewHolder>{
     private int gid;

    public class GroupViewHolder extends RecyclerView.ViewHolder{
        TextView mGName,mGType,mGDate,mGPlace,mGRemain,mGNumber;
        Button mAdd;
        public GroupViewHolder(View itemView){
            super(itemView);

            mGName = itemView.findViewById(R.id.mGName);
            mGType = itemView.findViewById(R.id.mGType);
            mGDate = itemView.findViewById(R.id.mGDate);
            mGPlace = itemView.findViewById(R.id.mGPlace);
            mGRemain = itemView.findViewById(R.id.mGRemain);
            mGNumber = itemView.findViewById(R.id.mGNumber);

            mAdd = itemView.findViewById(R.id.join_button);


        }


    }


    private List<AddGroup> addGroupList;
    Context context;

    public AddGroupAdapter(List<AddGroup> addGroupList){

        this.addGroupList =addGroupList;
    }



    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.try_card_glist,null);
        return new GroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        AddGroup addGroup =addGroupList.get(position);
        gid = addGroup.getGid();
        //Log.d("gid:",String.valueOf(gid));

        holder.mGName.setText(addGroup.getGName());
        holder.mGType.setText(addGroup.getType());
        holder.mGPlace.setText(addGroup.getPlace());
        holder.mGDate.setText(addGroup.getDate());
        holder.mGNumber.setText(String.valueOf(addGroup.getNumber()));
        holder.mGRemain.setText(String.valueOf(addGroup.getRemain()));
        holder.mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveJoinToDB();
            }
        });


    }

    @Override
    public int getItemCount() {

        return addGroupList.size();
    }


    public void saveJoinToDB(){
        StringRequest stringRequest= new StringRequest(Request.Method.POST, Values.lOGIN_SERVER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("success",response);
                            JSONObject jsonObject = new JSONObject(response);
                            AlertDialog.Builder alert = new AlertDialog.Builder(context);
                            alert.setTitle("快樂運動！");
                            alert.setMessage("加入成功囉");
                            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(context, JFGroupActivity.class);
                                    context.startActivity(intent);
                                }
                            });
                            AlertDialog dialog = alert.create();
                            dialog.show();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.d("error","do not join group");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                GlobalVariables user=(GlobalVariables)context.getApplicationContext();
                String uid = user.getId();
                Map<String,String> params=new HashMap<>();
                Mysql mysql=new Mysql();
                String query = mysql.addUserToGroupSql(uid,gid);
                params.put("query",query);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleTon.getmInstance(context).addToRequestque(stringRequest);

    }





}
