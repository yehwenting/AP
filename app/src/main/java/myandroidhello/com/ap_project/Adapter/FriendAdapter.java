package myandroidhello.com.ap_project.Adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

import myandroidhello.com.ap_project.R;
import myandroidhello.com.ap_project.font.FontHelper;

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

    public FriendAdapter(List<FriendItem> items) {
        mValues = items;
    }


    @Override
    public FriendAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_friends, parent, false);
        return new FriendAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FriendAdapter.ViewHolder holder, int position) {
        final FriendItem friendItem=mValues.get(position);
        FontHelper.setCustomTypeface(holder.mView);

        // set name and display profile pic
        Log.d("friend",friendItem.name);
        holder.mFriendName.setText(friendItem.name);
        displayProfilePic(holder.mFriendImage, friendItem.image);

        // check if user is following this friend and update follow button appearance



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
            mFriendImage=itemView.findViewById(R.id.friendImage);
            mFollow=itemView.findViewById(R.id.follow);

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
