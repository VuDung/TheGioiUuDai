package android.tgudapp.ui;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;




import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.Bitmap.CompressFormat;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.tgudapp.listener.CameraFragmentListener;
import android.tgudapp.ui.fragment.CameraFragment;
import android.tgudapp.ui.fragment.PhotoFragment;
import android.tgudapp.utils.Util;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


public class TakeOrderActivity extends Activity implements CameraFragmentListener {
	public static final String TAG = "TGUD/TakeOrderActivity";
	private static final int PICTURE_QUALITY = 90;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_take_order);
		setUpActionBar();
		Typeface mFont = Typeface.createFromAsset(getApplicationContext().getAssets(), "SFUHelveticaLight.ttf"); 
		ViewGroup mContainer = (ViewGroup)findViewById(android.R.id.content).getRootView();
		Util.setAppFont(mContainer, mFont);
	}
	private void setUpActionBar(){
		getActionBar().setTitle(R.string.send_order);
		
	}
	public void backPress(View view){
		onBackPressed();
	}
	public void sendOrder(View view){
		Toast.makeText(this, R.string.please_take_photo_build, Toast.LENGTH_SHORT).show();
	}
	/**
     * The user wants to take a picture.
     *
     * @param view
     */
    public void takePicture(View view) {
        view.setEnabled(false);

        CameraFragment fragment = (CameraFragment) getFragmentManager().findFragmentById(
            R.id.fragmentCamera
        );

        fragment.takePicture();
    }
	@Override
	public void onCameraError() {
		Toast.makeText(
	            this,
	            getString(R.string.toast_error_camera_preview),
	            Toast.LENGTH_SHORT
	        ).show();

	        finish();
	}
	@Override
	public void onPictureTaken(Bitmap bitmap) {
		// TODO Auto-generated method stub
		File mediaStorageDir = new File(
	            Environment.getExternalStoragePublicDirectory(
	                Environment.DIRECTORY_PICTURES
	            ),
	            getString(R.string.app_name)
	    );
		if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                showSavingPictureErrorToast();
                return;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = new File(
            mediaStorageDir.getPath() + File.separator + "TGUD_"+ timeStamp + ".jpg"
        );

        try {
            FileOutputStream stream = new FileOutputStream(mediaFile);
            bitmap.compress(CompressFormat.JPEG, PICTURE_QUALITY, stream);
        } catch (IOException exception) {
            showSavingPictureErrorToast();

            Log.w(TAG, "IOException during saving bitmap", exception);
            return;
        }

        MediaScannerConnection.scanFile(
            this,
            new String[] { mediaFile.toString() },
            new String[] { "image/jpeg" },
            null
        );
        Log.d("File image Path", mediaFile.toString());
//        FragmentManager fragManager = getSupportFragmentManager();
//        FragmentTransaction fragTransaction = fragManager.beginTransaction();
//        PhotoFragment photoFrag = new PhotoFragment();
//        Bundle bundle = new Bundle();
//        bundle.put
//        fragTransaction.replace(R.id.fragmentCamera, photoFrag);
        
        Intent intent = new Intent(this, PhotoActivity.class);
        intent.setData(Uri.fromFile(mediaFile));
        startActivity(intent);

        finish();
	}
	 private void showSavingPictureErrorToast() {
	        Toast.makeText(this, getText(R.string.toast_error_save_picture), Toast.LENGTH_SHORT).show();
	    }
	
}
