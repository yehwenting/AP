package myandroidhello.com.ap_project.Share;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import myandroidhello.com.ap_project.R;
import myandroidhello.com.ap_project.Util.Permissions;

import static android.app.Activity.RESULT_OK;

/**
 * Created by jenny on 2018/2/3.
 */

public class PhotoFragment extends Fragment {
    private static final String TAG = "PhotoFragment";
    private static final int PHOTO_FRAGMENT_NUM = 1;
    private static final int GALLERY_FRAGMENT_NUM = 2;
    private static final int CAMERA_REQUEST_CODE = 5;
    private String mCurrentPhotoPath;
    private String path = "";
    private File photoFile = null;


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_photo, container, false);
        Log.d(TAG, "onCreateView: started");

        Button btnLaunchCamera = (Button) view.findViewById(R.id.btnLaunchCamera);
        btnLaunchCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: launching camera");

                if (((ShareActivity)getActivity()).getCurrentTabNumber() == PHOTO_FRAGMENT_NUM){
                    if (((ShareActivity)getActivity()).checkPermissions(Permissions.CAMERA_PERMISSION[0])){
                        Log.d(TAG, "onClick: starting camera.");
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                        File photoFile = null;
                        try {
                            photoFile = createImageFile();
                        }catch (IOException ex){
                            Log.d(TAG, "onClick: failed to create photoFile:" + ex);
                        }
                        //continue only if the file was successfully created
                        if (photoFile != null){
                            Uri photoUri = FileProvider.getUriForFile(getActivity(),
                                    "myandroidhello.com.ap_project.fileprovider",
                                    photoFile);
                            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                            startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
                        }


                    }else {
                        Intent intent = new Intent(getActivity(), ShareActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
                        startActivity(intent);
                    }

                }
            }
        });

        return view;
    }

//    private File createImageFile() throws IOException {
//        // Create an image file name
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        File image = File.createTempFile(
//                imageFileName,  /* prefix */
//                ".jpg",         /* suffix */
//                storageDir      /* directory */
//        );
//
//        // Save a file: path for use with ACTION_VIEW intents
//        mCurrentPhotoPath = image.getAbsolutePath();
//        return image;
//    }


    private boolean isRootTask(){
        if(((ShareActivity)getActivity()).getTask() == 0){
            return true;
        }
        else{
            return false;
        }
    }

    private File createImageFile() throws IOException{
        //create File name
        String root = Environment.getExternalStorageDirectory().toString();
        mCurrentPhotoPath = root + "/Pictures/LINE";
        File myDir = new File(mCurrentPhotoPath);
        myDir.mkdirs();
        Calendar c = Calendar.getInstance();
        String month, day, year, hour, minute, second;
        month = ""+ (c.get(Calendar.MONTH)+1);
        day = "" + c.get(Calendar.DAY_OF_MONTH);
        year = "" + c.get(Calendar.YEAR);
        hour = ""+c.get(Calendar.HOUR_OF_DAY);
        minute = "" + c.get(Calendar.MINUTE);
        int seconds = c.get(Calendar.SECOND);
        if (seconds<10) second = "0"+ seconds;
        else second = ""+seconds;
        String fname = hour + "L" + minute + "L" + second + "L"  + month + "L" + day + "L" + year +".jpg";
        File file = new File(myDir, fname);
        if (file.exists()){
            file.delete();
        }
        mCurrentPhotoPath = file.getAbsolutePath();

        return file;
    }

    private void saveImage(Bitmap finalBitmap) {

        //creating directory to save photo
//        String root = Environment.getExternalStorageDirectory().toString();
//        File myDir = new File(root + "/Pictures/LINE");
//        myDir.mkdirs();
//        Calendar c = Calendar.getInstance();
//        String month, day, year, hour, minute, second;
//        month = ""+ (c.get(Calendar.MONTH)+1);
//        day = "" + c.get(Calendar.DAY_OF_MONTH);
//        year = "" + c.get(Calendar.YEAR);
//        hour = ""+c.get(Calendar.HOUR_OF_DAY);
//        minute = "" + c.get(Calendar.MINUTE);
//        int seconds = c.get(Calendar.SECOND);
//        if (seconds<10) second = "0"+ seconds;
//        else second = ""+seconds;
//        String fname = hour + "L" + minute + "L" + second + "L"  + month + "L" + day + "L" + year +".jpg";
//        File file = new File(myDir, fname);
//        if (file.exists()) file.delete();
//        Log.i("LOAD", root + fname);

        try {
            FileOutputStream out = new FileOutputStream(photoFile);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            String[] paths = {photoFile.toString()};
            String[] mimeTypes = {"/image/jpeg"};
            path = photoFile.toString();
            MediaScannerConnection.scanFile(getActivity(), paths, mimeTypes, null);
            Log.d(TAG, "saveImage: path :" + photoFile.toString());
//            Log.d(TAG, "saveImage: dir: " + root + "/Pictures/LINE");

        } catch (Exception e) {
            e.printStackTrace();
        }

//        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
////        File f = new File(mCurrentPhotoPath);
//        Uri contentUri = Uri.fromFile(photoFile);
//        mediaScanIntent.setData(contentUri);
//        getActivity().sendBroadcast(mediaScanIntent);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK){
            Log.d(TAG, "onActivityResult: done taking a photo.");
            Log.d(TAG, "onActivityResult: attempting to navigate to final share screen.");
            //navigate to the final share screen to share photo

//            Bitmap bitmap;
//            bitmap = (Bitmap) data.getExtras().get("data");
            int orientation;
            try{
                int targetW = 1150;
                int targetH = 1150;

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(mCurrentPhotoPath, options);
                int photoW = options.outWidth;
                int photoH = options.outHeight;

                // Determine how much to scale down the image
                int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

                // Decode the image file into a Bitmap sized to fill the View
                options.inJustDecodeBounds = false;
                options.inSampleSize = scaleFactor;
                options.inPurgeable = true;

                Bitmap bm = BitmapFactory.decodeFile(mCurrentPhotoPath, options);
                Bitmap bitmap = bm;

                ExifInterface exif = new ExifInterface(mCurrentPhotoPath);
                orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                Log.d(TAG, "onActivityResult: ExifInterface... rotation = " + orientation);

                Matrix m = new Matrix();

                if ((orientation == ExifInterface.ORIENTATION_ROTATE_180)) {
                    m.postRotate(180);
                    //m.postScale((float) bm.getWidth(), (float) bm.getHeight());
                    // if(m.preRotate(90)){
                    Log.e("in orientation", "" + orientation);
                    bitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),bm.getHeight(), m, true);

                } else if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                    m.postRotate(90);
                    Log.e("in orientation", "" + orientation);
                    bitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),bm.getHeight(), m, true);

                }
                else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                    m.postRotate(270);
                    Log.e("in orientation", "" + orientation);
                    bitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),bm.getHeight(), m, true);

                }

                if(isRootTask()){
                    try{
                        Log.d(TAG, "onActivityResult: received new bitmap from camera: " + bitmap);
                        saveImage(bitmap);
                        Intent intent = new Intent(getActivity(), NextActivity.class);
                        intent.putExtra(getString(R.string.selected_bitmap), "bitmap");
                        intent.putExtra("phoneImagePath", mCurrentPhotoPath);
                        startActivity(intent);
                    }catch (NullPointerException e){
                        Log.d(TAG, "onActivityResult: NullPointerException: " + e.getMessage());
                    }
                }
            }catch (Exception e){
                Log.d(TAG, "onActivityResult: exception: " + e.getMessage());
            }



//            else{
//                try{
//                    Log.d(TAG, "onActivityResult: received new bitmap from camera: " + bitmap);
//                    Intent intent = new Intent(getActivity(), AccountSettingsActivity.class);
//                    intent.putExtra(getString(R.string.selected_bitmap), bitmap);
//                    intent.putExtra(getString(R.string.return_to_fragment), getString(R.string.edit_profile_fragment));
//                    startActivity(intent);
//                    getActivity().finish();
//                }catch (NullPointerException e){
//                    Log.d(TAG, "onActivityResult: NullPointerException: " + e.getMessage());
//                }
//            }

        }
    }
}
