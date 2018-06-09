package myandroidhello.com.ap_project.Adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.json.JSONObject;

import java.util.List;

import myandroidhello.com.ap_project.Model.GlobalVariables;
import myandroidhello.com.ap_project.Model.Home2item;
import myandroidhello.com.ap_project.R;
import myandroidhello.com.ap_project.Util.SqaureImageView;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by jenny on 2018/3/30.
 */

public class Home2ListAdapter extends ArrayAdapter<Home2item> {

    public interface OnLoadMoreItemsListener{
        void onLoadMoreItems();
    }
    OnLoadMoreItemsListener mOnLoadMoreItemsListener;

    private static final String TAG = "Home2ListAdapter";

    private LayoutInflater mInflater;
    private int mLayoutResource;
    private Context mContext;
    private DatabaseReference mDatabase;
    private String uid = "1";
    private String name = "Jenny";
    private String currentId;
    private String Url = "http://140.119.19.36:80/saveXray.php";
    private boolean firstClick = true;
    MediaPlayer xray;
    GlobalVariables User = (GlobalVariables)getApplicationContext();

    com.android.volley.RequestQueue requestQueue;

    public Home2ListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Home2item> objects) {
        super(context, resource, objects);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mLayoutResource = resource;
        this.mContext = context;
    }

    static class ViewHolder{
        SqaureImageView sportIcon;
        TextView content;
        TextView date;
        ImageButton heart;
        LottieAnimationView animationView;
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final Home2ListAdapter.ViewHolder holder;
        mDatabase = FirebaseDatabase.getInstance().getReference();


        if(convertView == null){
            convertView = mInflater.inflate(mLayoutResource, parent, false);
            holder = new Home2ListAdapter.ViewHolder();

            holder.content = (TextView) convertView.findViewById(R.id.contentTv);
            holder.sportIcon = (SqaureImageView) convertView.findViewById(R.id.sportIcon);
            holder.date = (TextView) convertView.findViewById(R.id.dateTv);
            holder.heart = (ImageButton) convertView.findViewById(R.id.send_heart_btn);
            holder.animationView = (LottieAnimationView) convertView.findViewById(R.id.animationView);

            convertView.setTag(holder);

        }else{
            holder = (Home2ListAdapter.ViewHolder) convertView.getTag();
        }

        holder.content.setText(getItem(position).getContent());

        //set the date
        holder.date.setText(String.valueOf(getItem(position).getDate()));

        //set the icon image
        displayProfilePic(holder.sportIcon, getItem(position).getPic_path());
//        final ImageLoader imageLoader = ImageLoader.getInstance();
//        imageLoader.displayImage(getItem(position).getPic_path(), holder.sportIcon);


        holder.heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Drawable d = getResources().getDrawable(R.drawable.ic_fullheart);
                Log.d(TAG, "onClick: the heart button is clicked");
                holder.heart.setImageResource(R.drawable.ic_fullheart);
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                Log.d(TAG, "onClick: enter firebase" + database);
                DatabaseReference myRef = database.getReference("message");
                DatabaseReference sayhi = myRef.child(User.getName());

                sayhi.runTransaction(new Transaction.Handler() {
                    @Override
                    public Transaction.Result doTransaction(MutableData mutableData) {
                        mutableData.setValue(getItem(position).getUid());
                        return Transaction.success(mutableData);
                    }

                    @Override
                    public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                        if (databaseError != null){
                            Log.d(TAG, "onComplete: transaction failed:" + databaseError);
                        }else{
                            Log.d(TAG, "onComplete: transaction success: " + b + dataSnapshot);
                        }
                    }
                });
                //show heart animation
                holder.animationView.setVisibility(View.VISIBLE);
                holder.animationView.playAnimation();
                Toast.makeText(getContext(), "你已向好友發送X光波", Toast.LENGTH_LONG).show();
                xray = MediaPlayer.create(getContext(), R.raw.magic_wand);
                xray.start();
                Log.d(TAG, "media play");
                xray.setLooping(true);
                xray.setVolume(200, 200);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(4000);
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    holder.animationView.setVisibility(View.INVISIBLE);
                                    xray.stop();
                                }
                            });
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    holder.animationView.setVisibility(View.INVISIBLE);
//                                    xray.stop();
//                                }
//                            });
                        }catch (Exception e){

                        }

                    }
                }).start();

                //insert encouragement data to db
                if (firstClick) {
                    firstClick = false;

                    currentId = User.getId();
                    requestQueue = Volley.newRequestQueue(getApplicationContext());
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                            (Request.Method.GET, Url + "?uid=" + currentId + "&sid=" + getItem(position).getSid(), new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.d(TAG, "onResponse: insert into db..uid = " + currentId + "sid = " + getItem(position).getSid());


                                }


                            }, new Response.ErrorListener() {

                                @Override

                                public void onErrorResponse(VolleyError error) {
//                                if (error.getMessage() != null)
//                                    Log.d("11111111",error.getMessage());
                                    VolleyLog.e("error", error.getMessage());
//
                                }

                            });


                    requestQueue.add(jsonObjectRequest);
                }

            }
        });


        if(reachedEndOfList(position)){
            loadMoreData();
        }

        return convertView;
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

    private boolean reachedEndOfList(int position){
        return position == getCount() - 1;
    }

    private void loadMoreData(){

        try{
            mOnLoadMoreItemsListener = (Home2ListAdapter.OnLoadMoreItemsListener) getContext();
        }catch (ClassCastException e){
            Log.e(TAG, "loadMoreData: ClassCastException: " +e.getMessage() );
        }

        try{
            mOnLoadMoreItemsListener.onLoadMoreItems();
        }catch (NullPointerException e){
            Log.e(TAG, "loadMoreData: ClassCastException: " +e.getMessage() );
        }
    }


}
