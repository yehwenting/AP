package myandroidhello.com.ap_project.Model;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stanley on 2017/12/22.
 */

public class Place {
    public static LatLng Swimmingpool = new LatLng(24.98525,121.57768);
    public static LatLng Newgym = new LatLng(24.985,121.5738);
    public static LatLng Field = new LatLng(24.985,121.5748);
    public static LatLng Sixphase = new LatLng(24.9811767,121.578);
    public static LatLng Fivephase = new LatLng(24.9825,121.578);
    public static LatLng River = new LatLng(24.984,121.5728);

    public static Marker mSwimmingpool;
    public static Marker mNewgym;
    public static Marker mField;
    public static Marker mSixphase;
    public static Marker mFivephase;
    public static Marker mRiver;


    List<Marker> markerList = new ArrayList<>();

}
