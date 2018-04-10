package myandroidhello.com.ap_project.Adapter;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import myandroidhello.com.ap_project.Activity.JFGroupActivity;
import myandroidhello.com.ap_project.Data.MySingleTon;
import myandroidhello.com.ap_project.Data.Mysql;
import myandroidhello.com.ap_project.R;
import myandroidhello.com.ap_project.Util.Values;
import myandroidhello.com.ap_project.model.GlobalVariables;

//import android.icu.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreateGroupFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.

 * create an instance of this fragment.
 */
public class CreateGroupFragment extends Fragment {
    Button button;
    EditText Name,Num;
    TextView timeText;
    Context context;
    public   String name,type,place,time,uid;
    public int  num,remain;



    public CreateGroupFragment() {

    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.context=container.getContext();
        View creategroup = inflater.inflate(R.layout.fragment_create_group, container, false);
        button = (Button) creategroup.findViewById(R.id.buttonG);
        Name = (EditText) creategroup.findViewById(R.id.sName);
        Num = (EditText) creategroup.findViewById(R.id.sNum);
        timeText = (TextView) creategroup.findViewById(R.id.sTime);
        timeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment datePickerFragment = new DatePickerFragment();
                //调用show方法弹出对话框
                // 第一个参数为FragmentManager对象
                // 第二个为调用该方法的fragment的标签
                android.app.FragmentManager fm = getActivity().getFragmentManager();
                datePickerFragment.show(fm,"date_picker");


            }
        });

        final Spinner spinner = (Spinner)creategroup.findViewById(R.id.spinnerType);
        final String[] typec = {"慢跑", "籃球", "排球", "重訓", "足球"};
        ArrayAdapter typeList = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_dropdown_item,
                typec);
        spinner.setAdapter(typeList);

        final Spinner spinner2 = (Spinner)creategroup.findViewById(R.id.spinnerPlace);
        final String[] placec = {"五期", "六期", "體育館", "游泳館2F", "操場"};
        ArrayAdapter<String> placeList = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_dropdown_item,
                placec);
        spinner2.setAdapter(placeList);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name= Name.getText().toString();
                type =spinner.getSelectedItem().toString();
                place = spinner2.getSelectedItem().toString();
                num = Integer.parseInt(Num.getText().toString());
                remain = num-1;
                time = timeText.getText().toString();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Values.lOGIN_SERVER_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    android.support.v7.app.AlertDialog.Builder alert=new android.support.v7.app.AlertDialog.Builder(getContext());
                                    alert.setMessage("成功新增");
                                    alert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Toast.makeText(getContext(),"來加入別人的團吧！",Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(getActivity(),JFGroupActivity.class);
                                            startActivity(intent);
                                        }
                                    });
                                    alert.show();


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Log.d("error","do not save group to mysql");
                    }
                }){


                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params=new HashMap<>();
                        GlobalVariables user=(GlobalVariables)context.getApplicationContext();
                        uid=user.getId();
                        Mysql mysql=new Mysql();
                        String query = mysql.createNewGroup(name,type,place,uid,time,num,remain);
                        params.put("query", query);
                        return params;
                    }
                };
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                        10000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                MySingleTon.getmInstance(context).addToRequestque(stringRequest);


            }
        });

        return creategroup;

    }

    @NonNull
    public static Fragment newInstance() {
        return new CreateGroupFragment();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    @SuppressLint("ValidFragment")
    class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        private String date;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            //得到Calendar类实例，用于获取当前时间
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            //返回DatePickerDialog对象
            //因为实现了OnDateSetListener接口，所以第二个参数直接传入this

            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        //实现OnDateSetListener接口的onDateSet()方法
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            //这样子写就将选择时间的fragment和选择日期的fragment完全绑定在一起
            //使用的时候只需直接调用DatePickerFragment的show()方法
            //即可选择完日期后选择时间
            TimePickerFragment timePicker = new TimePickerFragment();
            timePicker.show(getFragmentManager(), "time_picker");
            //将用户选择的日期传到TimePickerFragment
            date = year + "年" + (monthOfYear + 1) + "月" + dayOfMonth + "日";
            timePicker.setTime(date);
        }
    }


    @SuppressLint("ValidFragment")
    class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
        private String time = "";

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            //新建日历类用于获取当前时间
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            //返回TimePickerDialog对象
            //因为实现了OnTimeSetListener接口，所以第二个参数直接传入this
            return new TimePickerDialog(getActivity(), this, hour, minute, true);
        }

        //实现OnTimeSetListener的onTimeSet方法
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            time = time + hourOfDay + "點" + minute + "分";
            timeText.setText(time);

        }
        public void setTime(String date){
            time += date;
        }

    }

}
