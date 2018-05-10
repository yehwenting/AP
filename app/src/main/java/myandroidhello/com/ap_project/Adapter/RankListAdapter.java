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
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import myandroidhello.com.ap_project.R;
import myandroidhello.com.ap_project.Util.SqaureImageView;
import myandroidhello.com.ap_project.Model.Rankitem;
import myandroidhello.com.ap_project.Model.User;
import myandroidhello.com.ap_project.Model.Photo;


/**
 * Created by jenny on 2018/3/23.
 */

public class RankListAdapter extends ArrayAdapter<Rankitem> {
    public interface OnLoadMoreItemsListener{
        void onLoadMoreItems();
    }
    RankListAdapter.OnLoadMoreItemsListener mOnLoadMoreItemsListener;

    private static final String TAG = "RankListAdapter";

    private LayoutInflater mInflater;
    private int mLayoutResource;
    private Context mContext;
    private String currentUsername = "";

    public RankListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Rankitem> objects) {
        super(context, resource, objects);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mLayoutResource = resource;
        this.mContext = context;
    }

    static class ViewHolder{
        SqaureImageView mprofileImage;
        TextView rankNumber, username, exerciseTime;


        //        UserAccountSettings settings = new UserAccountSettings();
        User user  = new User();
        StringBuilder users;
        Photo photo;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final RankListAdapter.ViewHolder holder;

        if(convertView == null){
            convertView = mInflater.inflate(mLayoutResource, parent, false);
            holder = new RankListAdapter.ViewHolder();

            holder.username = (TextView) convertView.findViewById(R.id.userName);
            holder.mprofileImage = (SqaureImageView) convertView.findViewById(R.id.profile_pic);
            holder.rankNumber = (TextView) convertView.findViewById(R.id.rank_number_tv);
            holder.exerciseTime = (TextView) convertView.findViewById(R.id.exercisetime);
            holder.users = new StringBuilder();

            convertView.setTag(holder);

        }else{
            holder = (ViewHolder) convertView.getTag();
        }



        //set the profile image
        final ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(getItem(position).getPic_path(), holder.mprofileImage);

        //set the user name, exercise time,
        Log.d(TAG, "getView: getItem(position): " + getItem(position));
        Long updatedTime = getItem(position).getEnd_time()-getItem(position).getStart_time();
        int secs = (int)(updatedTime/1000);
        int mins = secs / 60;
        int hours = mins / 60;
        mins = mins % 60;
        secs = secs % 60;
        holder.username.setText(getItem(position).getUser_name());
        holder.exerciseTime.setText(hours + "時" + mins + "分" + secs + "秒");
        holder.rankNumber.setText(String.valueOf(position + 1));
//        for (String key : getItem(position).keySet()){
//            holder.username.setText(key);
//            holder.exerciseTime.setText(String.valueOf(getItem(position).get(key)));
//            holder.rankNumber.setText(String.valueOf(position + 1));
//            break;
//        }

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
            mOnLoadMoreItemsListener = (RankListAdapter.OnLoadMoreItemsListener) getContext();
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
