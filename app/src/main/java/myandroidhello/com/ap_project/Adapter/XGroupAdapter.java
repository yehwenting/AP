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

import java.util.List;

import myandroidhello.com.ap_project.Model.XGroupitem;
import myandroidhello.com.ap_project.R;

/**
 * Created by jenny on 2018/4/27.
 */

public class XGroupAdapter extends ArrayAdapter<XGroupitem> {
    public interface OnLoadMoreItemsListener{
        void onLoadMoreItems();
    }
    XGroupAdapter.OnLoadMoreItemsListener mOnLoadMoreItemsListener;

    private static final String TAG = "XGroupAdapter";

    private LayoutInflater mInflater;
    private int mLayoutResource;
    private Context mContext;
    private String currentUsername = "";

    public XGroupAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<XGroupitem> objects) {
        super(context, resource, objects);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mLayoutResource = resource;
        this.mContext = context;
    }

    static class ViewHolder{
        TextView name, date, place, type;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final XGroupAdapter.ViewHolder holder;

        if(convertView == null){
            convertView = mInflater.inflate(mLayoutResource, parent, false);
            holder = new XGroupAdapter.ViewHolder();

            holder.date = (TextView) convertView.findViewById(R.id.date);
            holder.name = (TextView) convertView.findViewById(R.id.group_name);
            holder.place = (TextView) convertView.findViewById(R.id.place);
            holder.type = (TextView) convertView.findViewById(R.id.type);

            convertView.setTag(holder);

        }else{
            holder = (ViewHolder) convertView.getTag();
        }



        //set the date, name, place, type,
        Log.d(TAG, "getView: getItem(position): " + getItem(position));

        holder.date.setText(getItem(position).getDate());
        holder.name.setText(getItem(position).getGname());
        holder.place.setText(getItem(position).getPlace());
        holder.type.setText(getItem(position).getType());

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

