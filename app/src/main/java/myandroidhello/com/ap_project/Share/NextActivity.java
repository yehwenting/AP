package myandroidhello.com.ap_project.Share;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.util.UUID;

import myandroidhello.com.ap_project.Activity.IndoorStartActivity;
import myandroidhello.com.ap_project.R;
import myandroidhello.com.ap_project.Util.UniversalImageLoader;
import myandroidhello.com.ap_project.model.GlobalVariables;

/**
 * Created by jenny on 2018/2/10.
 */

public class NextActivity extends AppCompatActivity {
    private static final String TAG = "NextActivity";

    //vars
    private String mAppend = "file:/";
    private Button btnShare;
    private String phoneImagePath = null;
    private String uploadURL = "http://140.119.19.36:80/uploadPhoto.php";
    private String uid = "1";
//    private String uname = "winnie";
//    private String pname = "gym";
//    private String ename = null;
    private EditText content;
    private String imgUrl;
    private Bitmap bitmap;
    private Intent intent;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);

        Log.d(TAG, "onCreate: got the chosen image:  " + getIntent().getStringExtra(getString(R.string.selected_image)));
        Log.d(TAG, "onCreate: got the photo:" + getIntent().getStringExtra(getString(R.string.selected_bitmap)));

        ImageView backArrow = (ImageView) findViewById(R.id.ivBackArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: closing the activity");
                finish();
            }
        });

        setImage();

        content = (EditText) findViewById(R.id.description);

        btnShare = (Button) findViewById(R.id.btnShare);
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: uploading photo to the server!");
                uploadMultipart();
            }
        });

    }

    /**
     * gets the image url from the incoming intent and displays the chosen image
     */
    private void setImage(){
        Intent intent = getIntent();
        ImageView image = (ImageView) findViewById(R.id.imageShare);

        if(intent.hasExtra(getString(R.string.selected_image))){
            imgUrl = intent.getStringExtra(getString(R.string.selected_image));
            Log.d(TAG, "setImage: got new image url: " + imgUrl);
            UniversalImageLoader.setImage(imgUrl, image, null, mAppend);
            phoneImagePath = intent.getStringExtra(getString(R.string.selected_image));
        }
        else if(intent.hasExtra(getString(R.string.selected_bitmap))){
            bitmap = (Bitmap) intent.getParcelableExtra(getString(R.string.selected_bitmap));
            Log.d(TAG, "setImage: got new bitmap");
            image.setImageBitmap(bitmap);
            phoneImagePath = intent.getStringExtra("phoneImagePath");
            Log.d(TAG, "setImage: uploading photo from" + phoneImagePath);
//            phoneImagePath = intent.getParcelableExtra(getString(R.string.selected_bitmap));
        }

    }

    public void uploadMultipart() {
        //getting name for the image
        final int index = phoneImagePath.lastIndexOf("/");
        String name = phoneImagePath.substring(index);

        //getting the actual path of the image
        String path = phoneImagePath;

        //Uploading code
        try {
            String uploadId = UUID.randomUUID().toString();
            Log.d(TAG, "uploadMultipart: start uploading photo from " + path);
            GlobalVariables user=(GlobalVariables)getApplicationContext();
            //Creating a multi part request
            new MultipartUploadRequest(this, uploadId, uploadURL)
                    .addFileToUpload( path, "image") //Adding file
                    .addParameter("name", user.getName()) //Adding text parameter to the request
                    .addParameter("uid", user.getId())
                    .addParameter("content", content.getText().toString())
                    .addParameter("date", String.valueOf(System.currentTimeMillis()))
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .startUpload();
            Log.d(TAG, "uploadMultipart: starting the upload...");

            AlertDialog.Builder adb = new AlertDialog.Builder(this)
                    .setTitle("上傳成功")
                    .setMessage("貼文已上傳成功！！")
                    .setPositiveButton("回主畫面", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(NextActivity.this, IndoorStartActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
            AlertDialog alertDialog = adb.create();
            alertDialog.show();

        } catch (Exception exc) {
            Log.d(TAG, "uploadMultipart: " + exc.getMessage());
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


}
