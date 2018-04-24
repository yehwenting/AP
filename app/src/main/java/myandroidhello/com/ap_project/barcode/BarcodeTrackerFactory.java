package myandroidhello.com.ap_project.barcode;

import android.content.Context;

import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.barcode.Barcode;

/**
 * Created by jenny on 2017/12/9.
 */

 class BarcodeTrackerFactory implements MultiProcessor.Factory<Barcode>{
     private Context mContext;

     BarcodeTrackerFactory(Context context){
         mContext = context;
     }
    public Tracker<Barcode> create(Barcode barcode) {
        return new BarcodeTracker(mContext);
    }
}
