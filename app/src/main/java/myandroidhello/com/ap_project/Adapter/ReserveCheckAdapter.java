package myandroidhello.com.ap_project.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import myandroidhello.com.ap_project.Data.MySingleTon;
import myandroidhello.com.ap_project.Data.Mysql;
import myandroidhello.com.ap_project.R;
import myandroidhello.com.ap_project.Util.Values;
import myandroidhello.com.ap_project.font.FontHelper;

/**
 * Created by Yehwenting on 2018/3/10.
 */

public class ReserveCheckAdapter extends RecyclerView.Adapter<ReserveCheckAdapter.ViewHolder> {
    public static class Data {
        final String id;
        final String time;
        final String name;
        final String image;

        public Data(String id,String time, String name, String image) {
            this.id=id;
            this.time = time;
            this.name = name;
            this.image = image;
        }
    }

    private final List<Data> mValues;
    private Context context;

    public ReserveCheckAdapter(List<Data> items) {
        mValues = items;
    }

    @NonNull
    @Override
    public ReserveCheckAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context=parent.getContext();
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_reserve_check, parent, false);
        return new ReserveCheckAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReserveCheckAdapter.ViewHolder holder, final int position) {
        final Data data=mValues.get(position);
        FontHelper.setCustomTypeface(holder.mView);

        // set name and display profile pic
        holder.time.setText(data.time);
        holder.eName.setText(data.name);
        displayProfilePic(holder.ePic, data.image);
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                // 2. Chain together various setter methods to set the dialog characteristics
                builder.setMessage("刪除此預約?");
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deleteReserve(data.id,position);
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
                // 3. Get the AlertDialog from create()
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public void deleteReserve(final String id, final int position){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Values.lOGIN_SERVER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("success",response);
                            JSONObject jsonObject=new JSONObject(response);
                            mValues.remove(position);
                            notifyDataSetChanged();
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
                String query=mysql.deleteReserveData(id);
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


    public class ViewHolder extends RecyclerView.ViewHolder{
        View mView;
        TextView time;
        TextView eName;
        ImageView ePic;
        ImageView delete;

        public ViewHolder(View itemView) {
            super(itemView);
            mView=itemView;
            time=itemView.findViewById(R.id.reserve_check_date);
            eName=itemView.findViewById(R.id.reserve_check_ename);
            ePic=itemView.findViewById(R.id.eImage);
            delete=itemView.findViewById(R.id.reserve_delete);
        }
    }

    private void displayProfilePic(ImageView imageView, String url) {
        // helper method to load the profile pic in a circular imageview
        Transformation transformation = new RoundedTransformationBuilder()
                .cornerRadiusDp(30)
                .oval(false)
                .build();
        Picasso.with(imageView.getContext())
                .load(url)
                .placeholder(R.drawable.user)
                .transform(transformation)
                .into(imageView);
    }
}
