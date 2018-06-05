package myandroidhello.com.ap_project.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

import myandroidhello.com.ap_project.Activity.FriendInfoActivity;
import myandroidhello.com.ap_project.Model.GlobalVariables;
import myandroidhello.com.ap_project.Model.User;
import myandroidhello.com.ap_project.R;


/**
 * Created by jenny on 2018/3/23.
 */

public class GmemberAdapter extends ArrayAdapter<User> {
    public interface OnLoadMoreItemsListener{
        void onLoadMoreItems();
    }
    GmemberAdapter.OnLoadMoreItemsListener mOnLoadMoreItemsListener;

    private static final String TAG = "GmemberAdapter";

    private LayoutInflater mInflater;
    private int mLayoutResource;
    private Context mContext;
    private String currentUsername = "";

    public GmemberAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<User> objects) {
        super(context, resource, objects);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mLayoutResource = resource;
        this.mContext = context;
    }

    static class ViewHolder{
        ImageView mprofileImage;
        TextView  username;
        Button find;



    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final GmemberAdapter.ViewHolder holder;

        if(convertView == null){
            convertView = mInflater.inflate(mLayoutResource, parent, false);
            holder = new GmemberAdapter.ViewHolder();

            holder.username = (TextView) convertView.findViewById(R.id.mName);
            holder.mprofileImage = (ImageView) convertView.findViewById(R.id.mImage);
            holder.find=convertView.findViewById(R.id.find);
            GlobalVariables user=(GlobalVariables)mContext.getApplicationContext();
            if(user.getId().equals(getItem(position).getUser_id())){
                holder.find.setVisibility(View.GONE);
            }
            holder.find.setOnClickListener(view -> {
                Intent intent=new Intent(mContext, FriendInfoActivity.class);
                intent.putExtra("id",getItem(position).getUser_id());
                mContext.startActivity(intent);
            });

            convertView.setTag(holder);

        }else{
            holder = (ViewHolder) convertView.getTag();
        }



        //set the profile image
        displayProfilePic(holder.mprofileImage, getItem(position).getPic_url());

        //set the user name, exercise time,
        Log.d(TAG, "getView: getItem(position): " + getItem(position));

        holder.username.setText(getItem(position).getUsername());

        if(reachedEndOfList(position)){
            loadMoreData();
        }

        return convertView;
    }

    private boolean reachedEndOfList(int position){
        return position == getCount() - 1;
    }

    private void loadMoreData(){

        try{
            mOnLoadMoreItemsListener = (GmemberAdapter.OnLoadMoreItemsListener) getContext();
        }catch (ClassCastException e){
            Log.e(TAG, "loadMoreData: ClassCastException: " +e.getMessage() );
        }

        try{
            mOnLoadMoreItemsListener.onLoadMoreItems();
        }catch (NullPointerException e){
            Log.e(TAG, "loadMoreData: ClassCastException: " +e.getMessage() );
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
