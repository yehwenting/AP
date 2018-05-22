package myandroidhello.com.ap_project.Adapter;

import android.animation.ObjectAnimator;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.aakira.expandablelayout.ExpandableLayoutListenerAdapter;
import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import com.github.aakira.expandablelayout.Utils;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import myandroidhello.com.ap_project.Data.MySingleTon;
import myandroidhello.com.ap_project.Data.Mysql;
import myandroidhello.com.ap_project.R;
import myandroidhello.com.ap_project.Util.CustomTimePickerDialog;
import myandroidhello.com.ap_project.Util.Values;
import myandroidhello.com.ap_project.Model.GlobalVariables;
import myandroidhello.com.ap_project.Model.Item;


/**
 * Created by Yehwenting on 2018/2/4.
 */


class MyViewHolderWithChild extends RecyclerView.ViewHolder{
    public TextView textView;
    public ImageView expic;
    public RelativeLayout buttonChild;
    public Button buttonDate,buttonTime,commit;
    public ExpandableLinearLayout expandableLayout;
    public Spinner workout_time;
    public LinearLayout main;

    public MyViewHolderWithChild(View itemView) {
        super(itemView);
        textView=itemView.findViewById(R.id.parentTextView);
        expic=itemView.findViewById(R.id.exerPic);
        buttonChild=itemView.findViewById(R.id.childButton);
        buttonDate=itemView.findViewById(R.id.reserve_date_button);
        buttonTime=itemView.findViewById(R.id.reserve_time_button);
        expandableLayout=itemView.findViewById(R.id.expendableLayout);
        commit=itemView.findViewById(R.id.reserve_button);
        workout_time=itemView.findViewById(R.id.workout_time_spinner);
        main=itemView.findViewById(R.id.main);


    }
}

//TODO　check workout time available or not!
public class ReserveAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    List<Item> items;
    Context context;
    SparseBooleanArray expendState= new SparseBooleanArray();
    private int mYear, mMonth, mDay, mHour, mMinute;
    private String date,eName,selectWorkoutTime,dateNum,id;
    public int start_time_hour,start_time_min,start_year,
            start_month,start_day,end_time,start_time;
    public ArrayAdapter<Integer> adapter;

    public ReserveAdapter(List<Item> items) {
        this.items = items;
        for (int i=0;i<items.size();i++){
            expendState.append(i,false);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(items.get(position).getExpendable()){
            return 1;
        }else {
            return 0;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context=parent.getContext();
            LayoutInflater inflater=LayoutInflater.from(context);
            View view=inflater.inflate(R.layout.layout_reserve_with_child,parent,false);
            return new MyViewHolderWithChild(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

                final MyViewHolderWithChild viewHolderWithChild=(MyViewHolderWithChild)holder;
                final Item item=items.get(position);
//                Log.d("eeeee",item.getText());
//                int test=componentTimeToTimestamp(2018,1,26,20,10);
//                Log.d("ttttt",String.valueOf(test));

                viewHolderWithChild.setIsRecyclable(false);
                //更改每個健身器材名字
                viewHolderWithChild.textView.setText(item.getText());
                displayProfilePic(viewHolderWithChild.expic, item.getPicUrl());

                viewHolderWithChild.expandableLayout.setInRecyclerView(true);
                viewHolderWithChild.expandableLayout.setExpanded(expendState.get(position));
                viewHolderWithChild.expandableLayout.setListener(new ExpandableLayoutListenerAdapter() {

                    @Override
                    public void onPreOpen() {
                        viewHolderWithChild.main.setBackgroundColor(Color.parseColor("#FFFFAA"));

                        changeRotate(viewHolderWithChild.buttonChild,180f,0f).start();
                        expendState.put(position,false);

                    }

                    @Override
                    public void onPreClose() {
                        viewHolderWithChild.main.setBackgroundColor(Color.parseColor("#ffffff"));

                    }

                });

                viewHolderWithChild.buttonChild.setRotation(expendState.get(position)?180f:0f);
                viewHolderWithChild.buttonChild.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        eName=item.getText();
                        Log.d("name",eName);
                        viewHolderWithChild.expandableLayout.toggle();
                    }
                });
                viewHolderWithChild.buttonDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showDatePickerDialog(viewHolderWithChild);
                    }
                });
                viewHolderWithChild.buttonTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showTimePickerDialog(viewHolderWithChild);
                    }
                });
                viewHolderWithChild.commit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        saveReserveDataToDB();
                    }
                });
                //workout time spinner
                addItemsOnSpinner(viewHolderWithChild.workout_time,0,8);
                viewHolderWithChild.workout_time.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
                    public void onItemSelected(AdapterView adapterView, View view, int position, long id){
                        selectWorkoutTime=adapterView.getSelectedItem().toString();
                        Log.d("test",selectWorkoutTime);
                    }
                    public void onNothingSelected(AdapterView arg0) {
                        Toast.makeText(context, "您沒有選擇任何項目", Toast.LENGTH_LONG).show();
                    }
                });

        }

    public void addItemsOnSpinner(Spinner spinner, int start, int end) {

        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i=start; i<=end; i++){
            list.add(i*10);
        }
        adapter=new ArrayAdapter<Integer>(context,android.R.layout.simple_spinner_item,list){
            @Override
            public boolean isEnabled(int position){
                if(position == 1)
                {
                    // Disable the second item from Spinner
                    return false;
                }
                else
                {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position==1) {
                    // Set the disable item text color
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };;
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public void saveReserveDataToDB(){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Values.lOGIN_SERVER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("success",response);
                            JSONObject jsonObject=new JSONObject(response);
//                            Intent intent = new Intent(context,ReserveCheckActivity.class);
//                            Bundle bundle = new Bundle();
//                            bundle.putString("number",String.valueOf(id));
//                            intent.putExtras(bundle);
//                            context.startActivity(intent);
                            Toast.makeText(context, "預約成功!", Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.d("error","do not save reservation to mysql 2");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                GlobalVariables User = (GlobalVariables)context.getApplicationContext();
                Mysql mysql=new Mysql();
                String uid= User.getId();
                id=uid+dateNum;
                Log.d("tttttt",id);
                start_time=componentTimeToTimestamp(start_year,start_month,start_day,start_time_hour,start_time_min);

//                Log.d("time",String.valueOf(time));
                if(start_time_min+Integer.parseInt(selectWorkoutTime)>=60){
                    start_time_hour+=1;
                    start_time_min=(start_time_min+Integer.parseInt(selectWorkoutTime))-60;
                    end_time=componentTimeToTimestamp(start_year,start_month,start_day,start_time_hour,start_time_min);
                    Log.d("test",String.valueOf(end_time));

                }else{
                    start_time_min=start_time_min+Integer.parseInt(selectWorkoutTime);
                    end_time=componentTimeToTimestamp(start_year,start_month,start_day,start_time_hour,start_time_min);
                    Log.d("test",String.valueOf(end_time));
                }


                String query=mysql.saveReservation(id,start_time,end_time,eName,uid,date);
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

    public void checkReserveTimeAvailable(final int start_time){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Values.lOGIN_SERVER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("success",response);
                            JSONObject jsonObject=new JSONObject(response);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.d("error","do not save reservation to mysql 2");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                Mysql mysql=new Mysql();
                String query=mysql.checkReserveTimeAvailable(eName);
                int time=start_time;
                params.put("query",query);
                params.put("time",String.valueOf(time));
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleTon.getmInstance(context).addToRequestque(stringRequest);


    }

    public void showDatePickerDialog(final MyViewHolderWithChild viewHolderWithChild) {
        // 設定初始日期
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        Log.d("tttttttt",String.valueOf(mYear)+String.valueOf(mMonth+1)+String.valueOf(mDay));
        dateNum=String.valueOf(mYear)+String.valueOf(mMonth+1)+String.valueOf(mDay);
        // 跳出日期選擇器
        DatePickerDialog dpd = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        viewHolderWithChild.buttonDate.setText(year + "-" + (monthOfYear + 1) + "-"
                                + dayOfMonth);
                        date= year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        start_year=year;
                        start_month=monthOfYear;
                        start_day=dayOfMonth;

                    }
                }, mYear, mMonth, mDay);
        dpd.show();
    }

    public void showTimePickerDialog(final MyViewHolderWithChild viewHolderWithChild) {
        // 設定初始時間
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        boolean is24Hour = true;

        CustomTimePickerDialog customTimePickerDialog=new CustomTimePickerDialog(context,
                new TimePickerDialog.OnTimeSetListener() {
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        // 完成選擇，顯示時間
                        viewHolderWithChild.buttonTime.setText(hourOfDay + ":" + minute);
                        start_time_hour=hourOfDay;
                        start_time_min=minute;
                        start_time=componentTimeToTimestamp(start_year,start_month,start_day,start_time_hour,start_time_min);
                        checkReserveTimeAvailable(start_time);
                    }
                },mHour,mMinute,is24Hour);
        customTimePickerDialog.show();
    }

    public int componentTimeToTimestamp(int year, int month, int day, int hour, int minute) {

        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        c.set(Calendar.HOUR_OF_DAY , hour);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        return (int) (c.getTimeInMillis() / 1000L);
    }


    private ObjectAnimator changeRotate(RelativeLayout button, float from, float to) {
        ObjectAnimator animator=ObjectAnimator.ofFloat(button,"rotation",from,to);
        animator.setDuration(300);
        animator.setInterpolator(Utils.createInterpolator(Utils.LINEAR_INTERPOLATOR));
        return animator;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private void displayProfilePic(ImageView imageView, String url) {
        // helper method to load the profile pic in a circular imageview
        Transformation transformation = new RoundedTransformationBuilder()
                .cornerRadiusDp(30)
                .oval(false)
                .build();
        Picasso.with(imageView.getContext())
                .load(url)
                .placeholder(R.drawable.logo)
                .transform(transformation)
                .into(imageView);
    }
}
