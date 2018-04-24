package myandroidhello.com.ap_project.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

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
        TextView mGName,mGType,mGDate,mGPlace,mGRemain,mGNumber,textUname;
        Button mAdd;
        ImageView groupImg;
        public GroupViewHolder(View itemView){
            super(itemView);

            textUname=itemView.findViewById(R.id.textUname);
            mGName = itemView.findViewById(R.id.mGName);
            mGType = itemView.findViewById(R.id.mGType);
            mGDate = itemView.findViewById(R.id.mGDate);
            mGPlace = itemView.findViewById(R.id.mGPlace);
            mGRemain = itemView.findViewById(R.id.mGRemain);
            mGNumber = itemView.findViewById(R.id.mGNumber);
            groupImg=itemView.findViewById(R.id.groupImg);
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
    public void onBindViewHolder(@NonNull final GroupViewHolder holder, int position) {
        final AddGroup addGroup =addGroupList.get(position);
        gid = addGroup.getGid();
        //Log.d("gid:",String.valueOf(gid));

        holder.textUname.setText(addGroup.getGname());
        holder.mGName.setText(addGroup.getName());
        holder.mGType.setText(addGroup.getType());
        holder.mGPlace.setText(addGroup.getPlace());
        holder.mGDate.setText(addGroup.getDate());
        holder.mGNumber.setText(String.valueOf(addGroup.getNumber()));
        holder.mGRemain.setText(String.valueOf(addGroup.getRemain()));
        //check if the user has joined the group
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Values.READ_DATA_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("success",response);
                            JSONObject jsonObject=new JSONObject(response);
                            Log.d("success",jsonObject.getString("response"));
                            if(jsonObject.getString("response").equals("null")){

                            }else{
                                holder.mAdd.setText("-1");
                                Resources resource = context.getResources();
                                ColorStateList csl = (ColorStateList) resource.getColorStateList(R.color.accountLabel);
                                holder.mAdd.setTextColor(csl);

                            }
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
                GlobalVariables User=(GlobalVariables)context.getApplicationContext();
                String query=mysql.checkIfJoined(User.getId(),String.valueOf(addGroup.getGid()));
                params.put("query",query);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleTon.getmInstance(context).addToRequestque(stringRequest);
        holder.mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.mAdd.getText().equals("+1")){
                    String type="add";
                    saveJoinToDB(addGroup.getGid(),type);
                }else{
                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setMessage("補想參加了");
                    alert.setPositiveButton("退意已決", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String type="delete";
                            saveJoinToDB(addGroup.getGid(),type);
                        }
                    });
                    alert.setNegativeButton("按錯了!我要參加啦", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    AlertDialog dialog = alert.create();
                    dialog.show();


                }
            }
        });
        displayProfilePic(holder.groupImg,addGroup.getUrl());


    }

    @Override
    public int getItemCount() {

        return addGroupList.size();
    }


    public void saveJoinToDB(final int gId, final String type){
        StringRequest stringRequest= new StringRequest(Request.Method.POST, Values.ADD_GROUP_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("qqqq",response);
                            String title;
                            if(type.equals("add")){
                                title="你已是X特攻隊一員哩!!";
                            }else{
                                title="殘念!你就這樣脫離X特攻隊";
                            }
                            JSONObject jsonObject = new JSONObject(response);
                            AlertDialog.Builder alert = new AlertDialog.Builder(context);
                            alert.setTitle(title);
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
                params.put("uid",uid);
                params.put("gid",String.valueOf(gId));
                params.put("type",type);
                Log.d("qqqq",type+String.valueOf(gId));
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleTon.getmInstance(context).addToRequestque(stringRequest);

    }

    private void displayProfilePic(ImageView imageView, String url) {

        // helper method to load the profile pic in a circular imageview
        Transformation transformation = new RoundedTransformationBuilder()
                .cornerRadiusDp(30)
                .oval(false)
                .build();
        Picasso.with(context)
                .load(url)
                .placeholder(R.drawable.user)
                .transform(transformation)
                .into(imageView);
    }



}
