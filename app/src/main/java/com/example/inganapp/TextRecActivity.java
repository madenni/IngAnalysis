package com.example.inganapp;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.MutableLiveData;

import com.example.inganapp.tools.RequestPermissionsTool;
import com.example.inganapp.tools.RequestPermissionsToolImpl;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class TextRecActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback  {

    private static final String TAG = TextRecActivity.class.getSimpleName();
    static final int PHOTO_REQUEST_CODE = 100;
    private TessBaseAPI tess;
    TextView textView;
    ImageView imageView;
    Uri outputFileUri;

    private static final String lang = "rus";
    String result = "empty";
    private int GALLERY = 1, CAMERA = 2;
    private RequestPermissionsTool requestTool; //for API >=23 only
    private boolean tessInit;
    private static final String DATA_PATH = Environment.getExternalStorageDirectory().toString() + "/TesseractSample/";
    private static final String TESSDATA = "tessdata";
    private static final int PERMISSION_STORAGE = 101;
    private final MutableLiveData<String> resulttext = new MutableLiveData<>();

    private AsyncTask<Void, Void, Void> ocr = new ocrTask();
    private ProgressDialog progressOcr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_rec);
        imageView = (ImageView)findViewById(R.id.image);
        Button detectBtn = (Button) findViewById(R.id.detectbtn);
        detectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TextRecActivity.this, ResultActivity.class));
            }
        });
        Button captureImg = (Button) findViewById(R.id.snapbtn);
        if (captureImg != null) {
            captureImg.setOnClickListener(v -> {
                //startCameraActivity();
                //showPictureDialog();
                ImagePicker.with(TextRecActivity.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            });
        }
        textView = (TextView) findViewById(R.id.text);
        if (PermissionUtils.hasPermissions(TextRecActivity.this)) return;
        PermissionUtils.requestPermissions(TextRecActivity.this, PERMISSION_STORAGE);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions();
        }
    }



    /**
     * to get high resolution image from camera
     */




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (/*requestCode == CAMERA &&*/ resultCode == Activity.RESULT_OK) {
            if (data!=null){
                outputFileUri = data.getData();

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), outputFileUri);
                    imageView.setImageBitmap(bitmap);
                    doOCR();


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            //Bitmap photo = (Bitmap) data.getExtras().get("data");
            // Set the image in imageview for display
            //imageView.setImageBitmap(photo);
            //doOCR();
        } else {
            Toast.makeText(this, "ERROR: Image was not obtained.", Toast.LENGTH_SHORT).show();
        }
    }

    private void doOCR() {
        prepareTesseract();

        ocr.execute();
    }

    /**
     * Prepare directory on external storage
     *
     * @param path
     * @throws Exception
     */
   private void prepareDirectory(String path) {

        File dir = new File(path);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                Log.e(TAG, "ERROR: Creation of directory " + path + " failed, check does Android Manifest have permission to write to external storage.");
            }
        } else {
            Log.i(TAG, "Created directory " + path);
        }
    }


    private void prepareTesseract() {
        try {
            prepareDirectory(DATA_PATH + TESSDATA);
        } catch (Exception e) {
            e.printStackTrace();
        }

        copyTessDataFiles(TESSDATA);
    }

    /**
     * Copy tessdata files (located on assets/tessdata) to destination directory
     *
     * @param path - name of directory with .traineddata files
     */
    private void copyTessDataFiles(String path) {
        try {
            String fileList[] = getAssets().list(path);

            for (String fileName : fileList) {

                // open file within the assets folder
                // if it is not already there copy it to the sdcard
                String pathToDataFile = DATA_PATH + path + "/" + fileName;
                if (!(new File(pathToDataFile)).exists()) {

                    InputStream in = getAssets().open(path + "/" + fileName);

                    OutputStream out = new FileOutputStream(pathToDataFile);

                    // Transfer bytes from in to out
                    byte[] buf = new byte[1024];
                    int len;

                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    in.close();
                    out.close();

                    Log.d(TAG, "Copied " + fileName + "to tessdata");
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "Unable to copy files to tessdata " + e.toString());
        }
    }


    /**
     * don't run this code in main thread - it stops UI thread. Create AsyncTask instead.
     * http://developer.android.com/intl/ru/reference/android/os/AsyncTask.html
     *
     * @param imgUri
     */
    private String startOCR(Uri imgUri) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4; // 1 - means max size. 4 - means maxsize/4 size. Don't use value <4, because you need more memory in the heap to store your data.
            Bitmap bitmap = BitmapFactory.decodeFile(imgUri.getPath(), options);
            //result = extractText(bitmap);
            File result1 = new File(imgUri.getPath());
            result = extractText(result1);
                //resulttext.postValue(result);

            //textView.setText(result);

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return result;
    }


    private String extractText(File file) {
        try {
            tess = new TessBaseAPI();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            if (tess == null) {
                Log.e(TAG, "TessBaseAPI is null. TessFactory not returning tess object.");
            }
        }
        tess.init(DATA_PATH, lang, TessBaseAPI.OEM_LSTM_ONLY);


//       //EXTRA SETTINGS
//        //For example if we only want to detect numbers
        //tess.setVariable(TessBaseAPI.VAR_CHAR_WHITELIST, "1234567890АБВГДЕЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдежзийклмнопрстуфхцчшщъыьэюя,.() ");
//
//        //blackList Example
//        tessBaseApi.setVariable(TessBaseAPI.VAR_CHAR_BLACKLIST, "!@#$%^&*()_+=-qwertyuiop[]}{POIU" +
//                "YTRWQasdASDfghFGHjklJKLl;L:'\"\\|~`xcvXCVbnmBNM,./<>?");

        Log.d(TAG, "Training file loaded");
        tess.setImage(file);
        String extractedText = "empty result";
        try {
            tess.getHOCRText(0);
            extractedText = tess.getUTF8Text();
        } catch (Exception e) {
            Log.e(TAG, "Error in recognizing text.");
        }
        tess.recycle();
        return extractedText;
    }


    private void requestPermissions() {
        String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        requestTool = new RequestPermissionsToolImpl();
        requestTool.requestPermissions(this, permissions);
    }
    @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                                     @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "Разрешение предоставлено", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Разрешение не предоставлено", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    private class ocrTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //progressOcr.cancel();
            Intent intent = new Intent(TextRecActivity.this, ResultActivity.class);

            intent.putExtra("result", result);
            intent.putExtra("picture", outputFileUri.toString());

            startActivity(intent);
            finish();

            //textView.setText(result);
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i("OCRTask", "extracting..");
            result = startOCR(outputFileUri);
            return null;
        }
    }

}