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

import java.util.Calendar;
import java.util.List;

import myandroidhello.com.ap_project.Model.XGroupitem;
import myandroidhello.com.ap_project.R;

/**
 * Created by jenny on 2018/4/27.
 */

public class HistoryAdapter extends ArrayAdapter<XGroupitem> {
    public interface OnLoadMoreItemsListener{
        void onLoadMoreItems();
    }
    XGroupAdapter.OnLoadMoreItemsListener mOnLoadMoreItemsListener;

    private static final String TAG = "HistoryAdapter";

    private LayoutInflater mInflater;
    private int mLayoutResource;
    private Context mContext;
    private String currentUsername = "";

    public HistoryAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<XGroupitem> objects) {
        super(context, resource, objects);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mLayoutResource = resource;
        this.mContext = context;
    }

    static class ViewHolder{
        TextView type, start_time, end_time;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final HistoryAdapter.ViewHolder holder;

        if(convertView == null){
            convertView = mInflater.inflate(mLayoutResource, parent, false);
            holder = new HistoryAdapter.ViewHolder();

            holder.start_time = (TextView) convertView.findViewById(R.id.start_time);
            holder.end_time = (TextView) convertView.findViewById(R.id.end_time);
            holder.type = (TextView) convertView.findViewById(R.id.type1);


            convertView.setTag(holder);

        }else{
            holder = (ViewHolder) convertView.getTag();
        }



        //set the date, name, place, type,
        Log.d(TAG, "getView: getItem(position): " + getItem(position));

        holder.start_time.setText(convertTime(getItem(position).getDate()));
        holder.end_time.setText(convertTime(getItem(position).getGname()));
        holder.type.setText(getItem(position).getType());

        if(reachedEndOfList(position)){
            loadMoreData();
        }

        return convertView;
    }

    private String convertTime(String TimeMillis){
        Long timeStamp = Long.parseLong(TimeMillis);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp);

        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);

        int seconds = (int) (timeStamp / 1000) % 60 ;
        int minutes = (int) ((timeStamp / (1000*60)) % 60);
        int hours   = (int) ((timeStamp / (1000*60*60)) % 24);

        String date = mYear + "-" + mMonth + "-" + mDay + " ";
        String time = String.valueOf(hours) + ":" + String.valueOf(minutes);
        return date + time;
    }


    private boolean reachedEndOfList(int position){
        return position == getCount() - 1;
    }

    private void loadMoreData(){

        try{
            mOnLoadMoreItemsListener = (XGroupAdapter.OnLoadMoreItemsListener) getContext();
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

