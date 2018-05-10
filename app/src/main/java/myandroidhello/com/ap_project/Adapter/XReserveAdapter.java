package myandroidhello.com.ap_project.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.Calendar;
import java.util.List;

import myandroidhello.com.ap_project.Model.XReserveitem;
import myandroidhello.com.ap_project.R;
import myandroidhello.com.ap_project.Util.SqaureImageView;

/**
 * Created by jenny on 2018/4/27.
 */

public class XReserveAdapter extends ArrayAdapter<XReserveitem> {
    public interface OnLoadMoreItemsListener{
        void onLoadMoreItems();
    }
    XReserveAdapter.OnLoadMoreItemsListener mOnLoadMoreItemsListener;

    private static final String TAG = "XReserveAdapter";

    private LayoutInflater mInflater;
    private int mLayoutResource;
    private Context mContext;
    private String currentUsername = "";
    private Drawable d;

    public XReserveAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<XReserveitem> objects) {
        super(context, resource, objects);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mLayoutResource = resource;
        this.mContext = context;
        d = mContext.getResources().getDrawable(R.drawable.radius10_fffacd);
    }

    static class ViewHolder{
        SqaureImageView mEquipImage;
        TextView date, duration, eName;
        RelativeLayout relativeLayout;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final XReserveAdapter.ViewHolder holder;

        if(convertView == null){
            convertView = mInflater.inflate(mLayoutResource, parent, false);
            holder = new XReserveAdapter.ViewHolder();

            holder.date = (TextView) convertView.findViewById(R.id.reserve_date);
            holder.mEquipImage = (SqaureImageView) convertView.findViewById(R.id.equip_img);
            holder.duration = (TextView) convertView.findViewById(R.id.duration);
            holder.eName = (TextView) convertView.findViewById(R.id.equip);
            holder.relativeLayout = (RelativeLayout) convertView.findViewById(R.id.relLayout2x);

            convertView.setTag(holder);

        }else{
            holder = (ViewHolder) convertView.getTag();
        }



        //set the equipment image
        final ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(getItem(position).getPic_path(), holder.mEquipImage);

        //set the date, equip name, exercise time,
        Log.d(TAG, "getView: getItem(position): " + getItem(position));
        Long updatedTime = getItem(position).getEnd_time()-getItem(position).getStart_time();
        int secs = (int)(updatedTime/1000);
        int mins = secs / 60;
        int hours = mins / 60;
        mins = mins % 60;
        secs = secs % 60;
        holder.date.setText(getItem(position).getDate());
        holder.duration.setText(hours + "時" + mins + "分" + secs + "秒");
        holder.eName.setText(getItem(position).getEname());
        if(changeBoxColor(getItem(position).getDate())){
            holder.relativeLayout.setBackground(d);
        }

//        if (changeBoxColor(getItem(position).getDate())){
//            holder.relativeLayout.setBackgroundColor(16758784);
//        }

        if(reachedEndOfList(position)){
            loadMoreData();
        }

        return convertView;
    }

    private boolean changeBoxColor(String date){
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH)+1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        Boolean targetTimeIsPast = Boolean.FALSE;

        if (Integer.parseInt(date.split("-")[0]) - year < 0){
            targetTimeIsPast = Boolean.TRUE;
        }else if (Integer.parseInt(date.split("-")[1]) - month < 0){
            targetTimeIsPast = Boolean.TRUE;
        }else if (Integer.parseInt(date.split("-")[2]) - day < 0){
            targetTimeIsPast = Boolean.TRUE;
        }
        return targetTimeIsPast;
    }

    private boolean reachedEndOfList(int position){
        return position == getCount() - 1;
    }

    private void loadMoreData(){

        try{
            mOnLoadMoreItemsListener = (XReserveAdapter.OnLoadMoreItemsListener) getContext();
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

