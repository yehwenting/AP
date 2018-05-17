package myandroidhello.com.ap_project.Adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import myandroidhello.com.ap_project.Model.Home2item;
import myandroidhello.com.ap_project.R;
import myandroidhello.com.ap_project.Util.SqaureImageView;

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

            convertView.setTag(holder);

        }else{
            holder = (Home2ListAdapter.ViewHolder) convertView.getTag();
        }

        holder.content.setText(getItem(position).getContent());

        //set the date
        holder.date.setText(String.valueOf(getItem(position).getDate()));

        //set the icon image
        final ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(getItem(position).getPic_path(), holder.sportIcon);

        holder.heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Drawable d = getResources().getDrawable(R.drawable.ic_fullheart);
                Log.d(TAG, "onClick: the heart button is clicked");
                holder.heart.setImageResource(R.drawable.ic_fullheart);
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                Log.d(TAG, "onClick: database" + database);
                DatabaseReference myRef = database.getReference("message");
                DatabaseReference sayhi = myRef.child(name);

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
            }
        });


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
