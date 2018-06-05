package myandroidhello.com.ap_project.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
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

import myandroidhello.com.ap_project.Activity.CompeteGroupMemberActivity;
import myandroidhello.com.ap_project.Activity.JFGroupActivity;
import myandroidhello.com.ap_project.Data.MySingleTon;
import myandroidhello.com.ap_project.Data.Mysql;
import myandroidhello.com.ap_project.Model.CompetitionGroup;
import myandroidhello.com.ap_project.Model.GlobalVariables;
import myandroidhello.com.ap_project.R;
import myandroidhello.com.ap_project.Util.Values;

public class CompetitionGroupAdapter extends  RecyclerView.Adapter<CompetitionGroupAdapter.CompetitionViewHolder> {
    public class CompetitionViewHolder extends RecyclerView.ViewHolder {

        TextView gname,uname,num,time,place,remain,note;
        Button join;
        ImageView pic,more;

        public CompetitionViewHolder(View itemView) {
            super(itemView);

            gname=itemView.findViewById(R.id.cga_name);
            uname=itemView.findViewById(R.id.uName);
            num=itemView.findViewById(R.id.cga_num);
            time=itemView.findViewById(R.id.cga_time);
            place=itemView.findViewById(R.id.cga_place);
            remain=itemView.findViewById(R.id.cga_remain);
            note=itemView.findViewById(R.id.cga_note);
            join=itemView.findViewById(R.id.cga_create_button);
            pic=itemView.findViewById(R.id.groupImg);
            more=itemView.findViewById(R.id.more);

        }
    }

    private List<CompetitionGroup> competitionGroups;
    Context context;

    public CompetitionGroupAdapter(List<CompetitionGroup> competitions) {
        this.competitionGroups = competitions;
        Log.d("hhhhh","hello");

    }

    @NonNull
    @Override
    public CompetitionGroupAdapter.CompetitionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.adapter_competition_group,null);

        return new CompetitionGroupAdapter.CompetitionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CompetitionGroupAdapter.CompetitionViewHolder holder, int position) {
            final CompetitionGroup competitionGroup=competitionGroups.get(position);
            Log.d("hhhhh","hello");
//            if(competitionGroup.getStatus().equals("true")){

            holder.gname.setText(competitionGroup.getCpName());
            holder.uname.setText(competitionGroup.getuName());
            holder.time.setText(competitionGroup.getTime());
            holder.place.setText(competitionGroup.getPlace());
            holder.note.setText(competitionGroup.getNote());
            holder.remain.setText(competitionGroup.getRemain());
            holder.num.setText(competitionGroup.getNum());
            holder.more.setOnClickListener(view -> {
                Intent intent=new Intent(context, CompeteGroupMemberActivity.class);
                intent.putExtra("group_number",competitionGroup.getCid());
                context.startActivity(intent);
            });
            displayProfilePic(holder.pic,competitionGroup.getuPic());
            Log.d("ttttt",competitionGroup.getCid());
        //check if the user has joined the group
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Values.READ_DATA_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("ttttt",response);
                            JSONObject jsonObject=new JSONObject(response);
                            Log.d("success",jsonObject.getString("response"));
                            if(jsonObject.getString("response").equals("null")){
                                Log.d("ttttt","null"+jsonObject.getString("response"));
                            }else{
                                holder.join.setText("退出X戰隊");
                                holder.join.setClickable(true);
                                Resources resource = context.getResources();
                                ColorStateList csl = (ColorStateList) resource.getColorStateList(R.color.accountLabel);
                                holder.join.setTextColor(csl);

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
                String query=mysql.checkIfJoinedCompetition(User.getId(),competitionGroup.getCid());
                params.put("query",query);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleTon.getmInstance(context).addToRequestque(stringRequest);

        holder.join.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    if(holder.join.getText().equals("加入X戰隊")){
                        String type="add";
                        joinGroup(competitionGroup.getCid(),type);
                    }else {
                        AlertDialog.Builder alert = new AlertDialog.Builder(context);
                        alert.setMessage("不想參加了");
                        alert.setPositiveButton("退意已決", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String type = "delete";
                                joinGroup(competitionGroup.getCid(), type);
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
        if(competitionGroup.getRemain().equals("0")){
            holder.join.setTextColor(Color.RED);
            holder.join.setText("本團已滿");
            holder.join.setClickable(false);
        }
    }

    @Override
    public int getItemCount() {
        Log.d("hhhhh",String.valueOf(competitionGroups.size()));
        return competitionGroups.size();
    }

    private void joinGroup(final String cid, final String type){
        StringRequest stringRequest= new StringRequest(Request.Method.POST, Values.ADD_COMPETITION_URL,
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
                params.put("cid",cid);
                params.put("type",type);
//                Log.d("qqqq",type+String.valueOf(gId));
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
