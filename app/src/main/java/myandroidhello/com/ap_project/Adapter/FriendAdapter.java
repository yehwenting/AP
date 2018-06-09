package myandroidhello.com.ap_project.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import myandroidhello.com.ap_project.Activity.FriendInfoActivity;
import myandroidhello.com.ap_project.Activity.FriendsActivity;
import myandroidhello.com.ap_project.Data.MySingleTon;
import myandroidhello.com.ap_project.Data.Mysql;
import myandroidhello.com.ap_project.Font.FontHelper;
import myandroidhello.com.ap_project.Model.GlobalVariables;
import myandroidhello.com.ap_project.R;
import myandroidhello.com.ap_project.Util.Values;

/**
 * Created by Yehwenting on 2017/11/29.
 */

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder>{

    public static class FriendItem {
        final String id;
        final String name;
        final String image;

        public FriendItem(String id, String name, String image) {
            this.id = id;
            this.name = name;
            this.image = image;
        }
    }

    private final List<FriendItem> mValues;
    private Context context;

    public FriendAdapter(List<FriendItem> items) {
        mValues = items;
    }


    @Override
    public FriendAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context=parent.getContext();
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_friends, parent, false);
        return new FriendAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final FriendAdapter.ViewHolder holder, int position) {
        final FriendItem friendItem=mValues.get(position);
        FontHelper.setCustomTypeface(holder.mView);

        // set name and display profile pic
        holder.mFriendName.setText(friendItem.name);
        displayProfilePic(holder.mFriendImage, friendItem.image);
        // check friend profile
        holder.mFriendImage.setOnClickListener(view -> {
            Intent intent=new Intent(context, FriendInfoActivity.class);
            intent.putExtra("id",friendItem.id);
            context.startActivity(intent);
        });

        //display
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
//                                holder.mFollow.setVisibility(View.GONE);
                                holder.mFollow.setText("好 友");
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
                String query=mysql.checkIsFriend(User.getId(),friendItem.id);
                params.put("query",query);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleTon.getmInstance(context).addToRequestque(stringRequest);





        // check if user is following this friend and update follow button appearance
        holder.mFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.mFollow.getText().equals("追 蹤")){
                    StringRequest stringRequest=new StringRequest(Request.Method.POST, Values.lOGIN_SERVER_URL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        Log.d("success",response);
                                        JSONObject jsonObject=new JSONObject(response);
                                        holder.mFollow.setText("好 友");
//                                    holder.mFollow.setVisibility(View.GONE);
                                        Toast.makeText(context, "已成為好友", Toast.LENGTH_LONG).show();
                                        Intent intent1 = new Intent(context,FriendsActivity.class);
                                        context.startActivity(intent1);
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
                            String query=mysql.addFriend(User.getId(),friendItem.id);
                            params.put("query",query);
                            return params;
                        }
                    };
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                            10000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    MySingleTon.getmInstance(context).addToRequestque(stringRequest);
                }else{
                    AlertDialog.Builder alert = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.AlertDialogCustom));
                    alert.setMessage("不想當戰友?");
                    alert.setPositiveButton("退意已決", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            StringRequest stringRequest=new StringRequest(Request.Method.POST, Values.lOGIN_SERVER_URL,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            try {
                                                Log.d("success",response);
                                                JSONObject jsonObject=new JSONObject(response);
//                                        holder.mFollow.setText("追 蹤");
//                                    holder.mFollow.setVisibility(View.GONE);
                                                Toast.makeText(context, "已取消追蹤好友", Toast.LENGTH_LONG).show();
                                                Intent intent=new Intent(context, FriendsActivity.class);
                                                context.startActivity(intent);
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
                                    String query=mysql.deleteFriend(User.getId(),friendItem.id);
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
                    });
                    alert.setNegativeButton("按錯了!我們是戰友啦", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    AlertDialog dialog = alert.create();
                    dialog.show();


                }

            }
        });



    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView mFriendName;
        ImageView mFriendImage;
        Button mFollow;
        public ViewHolder(View itemView) {
            super(itemView);

            mView=itemView;
            mFriendName=itemView.findViewById(R.id.friendName);
            mFriendImage=itemView.findViewById(R.id.eImage);
            mFollow=itemView.findViewById(R.id.follow);

        }
    }

    private void displayProfilePic(ImageView imageView, String url) {
        // helper method to load the profile pic in a circular imageview
        Transformation transformation = new RoundedTransformationBuilder()
                .cornerRadiusDp(30)
                .oval(true)
                .build();
        Picasso.with(imageView.getContext())
                .load(url)
                .placeholder(R.drawable.user)
                .transform(transformation)
                .into(imageView);
    }
}
