package myandroidhello.com.ap_project.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import myandroidhello.com.ap_project.Data.MySingleTon;
import myandroidhello.com.ap_project.Data.Mysql;
import myandroidhello.com.ap_project.Model.GlobalVariables;
import myandroidhello.com.ap_project.R;
import myandroidhello.com.ap_project.Util.Values;
import myandroidhello.com.ap_project.Model.FindFdPlace;
import myandroidhello.com.ap_project.Model.Place;

import static myandroidhello.com.ap_project.Model.Place.mSwimmingpool;



public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private LocationListener locationListener;
    Button buttonStart;
    TextView textView;
    TextView showTextsport;
    String mplace = null;
    String sport =null;
    TextView showfd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Bundle bundle = this.getIntent().getExtras();
        sport = bundle.getString("type");

        textView = (TextView) findViewById(R.id.showplace);
        showTextsport =(TextView)findViewById(R.id.showsport);
        showTextsport.setText(sport);

        buttonStart = (Button) findViewById(R.id.buttonStart);
        showfd=findViewById(R.id.showfd);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;

        Place.mSwimmingpool = mMap.addMarker(new MarkerOptions().position(Place.Swimmingpool)
                .title("游泳池"));
        Place.mNewgym = mMap.addMarker(new MarkerOptions().position(Place.Newgym)
                .title("體育館")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        Place.mSixphase = mMap.addMarker(new MarkerOptions().position(Place.Sixphase)
                .title("六期")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        Place.mField = mMap.addMarker(new MarkerOptions().position(Place.Field)
                .title("操場"));

        Place.mFivephase = mMap.addMarker(new MarkerOptions().position(Place.Fivephase)
                .title("五期"));

        Place.mRiver = mMap.addMarker(new MarkerOptions().position(Place.River)
                .title("河堤"));
        List<Marker> markerList = new ArrayList<>();
        markerList.add(mSwimmingpool);
        markerList.add(Place.mNewgym);
        markerList.add(Place.mSixphase);
        for (Marker m : markerList) {
            LatLng latlng = new LatLng(m.getPosition().latitude, m.getPosition().longitude);
            mMap.addMarker(new MarkerOptions().position(latlng));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 2));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15));
        }

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                mplace = marker.getTitle();
                getFriends(mplace);

                textView.setText(mplace);

                return false;
            }
        }); //register our click listener

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MapsActivity.this, StartExercise1Activity.class);
                GlobalVariables user=(GlobalVariables)getApplicationContext();
                Bundle bundle = new Bundle();
                intent.putExtra("uid",user.getId() );
                intent.putExtra("uname", user.getName());
                bundle.putString("ename", sport);
                bundle.putString("pname",mplace);

                intent.putExtras(bundle);
                startActivity(intent);

                Log.d("Type:",sport);
                Log.d("place",mplace);

            }
        });



        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Marker marker = null;
                LatLng newLocation = new LatLng(location.getLatitude(), location.getLongitude());


                if (marker != null) {
                    marker.remove();
                    marker.setPosition(newLocation);

                } else {

                    marker = mMap.addMarker(new MarkerOptions().position(newLocation).title("您在此位置"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(newLocation));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(newLocation, 15));


                }


            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (Build.VERSION.SDK_INT < 23) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }else{
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                //Ask for permission
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION},1);
            }else {
                // we have permission
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
            }


        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length > 0 && grantResults[0] ==
                PackageManager.PERMISSION_GRANTED) {

            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED){

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }

        }

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }
    public void getFriends(final String newplace) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Values.READ_DATA_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("rrrrr",response);
                        String name ="";
                        try {
                            //converting the string to json array object
                            JSONObject jsonObject=new JSONObject(response);

                            if(jsonObject.getString("response").equals("null")){
                                showfd.setText("沒有戰友在附近運動");
                                Log.d("rrrrr","null");
                            }else{
                                JSONArray array = jsonObject.getJSONArray("response");
                                //traversing through all the object
                                for (int i = 0; i < array.length(); i++) {

                                    //getting product object from json array
                                    List<FindFdPlace> findFdPlace = new ArrayList<>();
                                    JSONObject friends = array.getJSONObject(i);

                                    findFdPlace.add(new FindFdPlace(
                                            friends.getString("name"),
                                            friends.getString("ex_place")


                                    ));
                                    name += friends.getString("name")+ " , ";
                                    showfd.setText(name);
                                    Log.d("nnnn", name);
                            }


                            }

                        }

                        catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.d("error","do not get ");

            }


        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params= new HashMap<>();
                Mysql mysql=new Mysql();
                String query=mysql.getFriendsPlace(newplace);
                params.put("query",query);
                Log.d("final",query);
                return params;
            }
        };

        MySingleTon.getmInstance(MapsActivity.this).addToRequestque(stringRequest);

    }
}
