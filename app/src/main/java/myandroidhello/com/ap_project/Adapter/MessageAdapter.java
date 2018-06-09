package myandroidhello.com.ap_project.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

import myandroidhello.com.ap_project.Activity.FriendInfoActivity;
import myandroidhello.com.ap_project.Font.FontHelper;
import myandroidhello.com.ap_project.R;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    public MessageAdapter(List<MessageItem> mValues) {
        this.mValues = mValues;
    }

    public static class MessageItem {
        final String id;
        final String name;
        final String image;
        final String content;

        public MessageItem(String id, String name, String image,String content) {
            this.id = id;
            this.name = name;
            this.image = image;
            this.content = content;
        }
    }

    private final List<MessageItem> mValues;
    private Context context;

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context=parent.getContext();
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_message, parent, false);
        return new MessageAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
        final MessageItem messageItem=mValues.get(position);
        FontHelper.setCustomTypeface(holder.mView);

        // set name and display profile pic
        holder.mName.setText(messageItem.name);
        displayProfilePic(holder.mimg, messageItem.image);
        holder.mContent.setText(messageItem.content);
        holder.mimg.setOnClickListener(view -> {
            Intent intent = new Intent(context, FriendInfoActivity.class);
            intent.putExtra("id",messageItem.id);
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView mName,mContent;
        ImageView mimg;
//        Button mFollow;
        public ViewHolder(View itemView) {
            super(itemView);

            mView=itemView;
            mName=itemView.findViewById(R.id.mName);
            mContent=itemView.findViewById(R.id.mContent);
            mimg=itemView.findViewById(R.id.mimg);

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
