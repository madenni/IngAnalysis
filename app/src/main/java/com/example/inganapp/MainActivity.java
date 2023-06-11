package com.example.inganapp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.inganapp.tools.RequestPermissionsTool;
import com.example.inganapp.tools.RequestPermissionsToolImpl;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.navigation.NavigationView;
import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Button view, rec, button2;
    //DBHelper DB;
    private static final String lang = "rus";
    String result = "empty";
    //private int GALLERY = 1, CAMERA = 2;
    private RequestPermissionsTool requestTool; //for API >=23 only
    //private boolean tessInit;
    private static final String DATA_PATH = Environment.getExternalStorageDirectory().toString() + "/TesseractSample/";
    private static final String TESSDATA = "tessdata";
    private static final int PERMISSION_STORAGE = 101;
    private static final String TAG = MainActivity.class.getSimpleName();
    //static final int PHOTO_REQUEST_CODE = 100;
    private TessBaseAPI tess;
    Uri outputFileUri;

    private AsyncTask<Void, Void, Void> ocr = new ocrTask();
    private ProgressBar progressOcr;
    DBHelper DB;
    Cursor cursor;
    SQLiteDatabase sql;
    String nameS, idS;
    ArrayList<String> name = new ArrayList<>();
    ArrayList<String> id = new ArrayList<>();
    NavigationView navigationView;
    TextView toolbarText;


    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
                PackageManager.PERMISSION_GRANTED);
        */
        Toolbar toolbar = findViewById(R.id.toolbar); //Ignore red line errors
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        System.out.println("Menu menu = navigationView.getMenu(); ");
        // find MenuItem you want to change
        MenuItem nav_1 = menu.findItem(R.id.nav_1);
        MenuItem nav_2 = menu.findItem(R.id.nav_2);
        MenuItem nav_3 = menu.findItem(R.id.nav_3);
        MenuItem nav_4 = menu.findItem(R.id.nav_4);
        MenuItem nav_5 = menu.findItem(R.id.nav_5);
        System.out.println("MenuItem nav_5 = menu.findItem(R.id.nav_5); ");
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav,
                R.string.close_nav);
        System.out.println("ActionBarDrawerToggle toggle ");
        drawerLayout.addDrawerListener(toggle);
        System.out.println("drawerLayout.addDrawerListener(toggle); ");
        toggle.syncState();
        System.out.println("toggle.syncState(); ");
        view = findViewById(R.id.btnView);
        rec = findViewById(R.id.btnRec);
        TextView text = findViewById(R.id.text1);

        progressOcr = findViewById(R.id.progressBar);

        if (!PermissionUtils.hasPermissions(MainActivity.this)) {
        PermissionUtils.requestPermissions(MainActivity.this, PERMISSION_STORAGE);}
        System.out.println("PermissionUtils.requestPermissions ");
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions();
        }
        System.out.println("requestPermissions(); ");

        //DB = new DBHelper(this);
        DB = new DBHelper(this);
        DB.create_db();
        sql = DB.open();
        System.out.println("sql = DB.open(); ");
        cursor = sql.rawQuery("SELECT _id, Name FROM Users", null);
        System.out.println("SELECT _id, Name FROM Users ");
        if (cursor.getCount() == 0){
            Intent intent = new Intent(MainActivity.this, AddUserActivity.class);
            startActivity(intent);

        }else {
            while (cursor.moveToNext()){
                id.add(cursor.getString(0));
                name.add(cursor.getString(1));}
            System.out.println("name " + name.size());
        }
        if (savedInstanceState == null) {
            navigationView.setCheckedItem(R.id.nav_1);
            toolbarText = (TextView)toolbar.findViewById(R.id.toolbarTextView);
            nameS = name.get(0);
            idS = id.get(0);
            toolbarText.setText(nameS);
            System.out.println("savedInstanceState == null ");
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }


        switch (id.size()) {
            case 0:
                Intent intent = new Intent(MainActivity.this, AddUserActivity.class);
                startActivity(intent);
                break;
            case 1:
                nav_1.setVisible(true);
                nav_1.setTitle(name.get(0));
                nav_2.setVisible(false);
                nav_3.setVisible(false);
                nav_4.setVisible(false);
                nav_5.setVisible(false);
                break;
            case 2:
                nav_1.setVisible(true);
                nav_1.setTitle(name.get(0));
                nav_2.setVisible(true);
                nav_2.setTitle(name.get(1));
                nav_3.setVisible(false);
                nav_4.setVisible(false);
                nav_5.setVisible(false);
                break;
            case 3:
                nav_1.setVisible(true);
                nav_1.setTitle(name.get(0));
                nav_2.setVisible(true);
                nav_2.setTitle(name.get(1));
                nav_3.setVisible(true);
                nav_3.setTitle(name.get(2));
                nav_4.setVisible(false);
                nav_5.setVisible(false);
                break;
            case 4:
                nav_1.setVisible(true);
                nav_1.setTitle(name.get(0));
                nav_2.setVisible(true);
                nav_2.setTitle(name.get(1));
                nav_3.setVisible(true);
                nav_3.setTitle(name.get(2));
                nav_4.setVisible(true);
                nav_4.setTitle(name.get(3));
                nav_5.setVisible(false);
                break;
            case 5:
                nav_1.setVisible(true);
                nav_1.setTitle(name.get(0));
                nav_2.setVisible(true);
                nav_2.setTitle(name.get(1));
                nav_3.setVisible(true);
                nav_3.setTitle(name.get(2));
                nav_4.setVisible(true);
                nav_4.setTitle(name.get(3));
                nav_5.setVisible(true);
                nav_5.setTitle(name.get(4));
                break;
        }






        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, IngredlistActivity.class);
                intent.putExtra("user", Integer.parseInt(idS));

                startActivity(intent);

            }
        });

        rec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(MainActivity.this, TextRecActivity.class));
                ImagePicker.with(MainActivity.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });






    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        try {
            switch (item.getItemId()) {
                case R.id.nav_1:
                    nameS = name.get(0);
                    idS = id.get(0);
                    navigationView.setCheckedItem(R.id.nav_1);
                    break;
                case R.id.nav_2:
                    nameS = name.get(1);
                    idS = id.get(1);
                    navigationView.setCheckedItem(R.id.nav_2);
                    break;
                case R.id.nav_3:
                    nameS = name.get(2);
                    idS = id.get(2);
                    navigationView.setCheckedItem(R.id.nav_3);
                    break;
                case R.id.nav_4:
                    nameS = name.get(3);
                    idS = id.get(3);
                    navigationView.setCheckedItem(R.id.nav_4);
                    break;
                case R.id.nav_5:
                    nameS = name.get(4);
                    idS = id.get(4);
                    navigationView.setCheckedItem(R.id.nav_5);
                    break;
                case R.id.nav_user:
                    Intent intent = new Intent(this, UserActivity.class);
                    intent.putExtra("user", Integer.parseInt(idS));

                    startActivity(intent);
                    break;
                case R.id.nav_new:
                    if (id.size() < 5) {
                        Intent intent2 = new Intent(this, AddUserActivity.class);
                        startActivity(intent2);
                        finish();
                    } else
                        Toast.makeText(this, "Добавлено слишком много пользователей", Toast.LENGTH_SHORT).show();
                    break;


            }
        } catch(Exception e){
            Log.i(TAG,"didnt work at all");
        }
        toolbarText.setText(nameS);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        // Start digging into the view hierarchy until the correct view is found
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        ViewGroup navigationMenuView = (ViewGroup)navigationView.getChildAt(0);
        ViewGroup navigationMenuItemView = (ViewGroup)navigationMenuView.getChildAt(1);
        View appCompatCheckedTextView = navigationMenuItemView.getChildAt(0);

        // Attach click listener


        return super.onPrepareOptionsMenu(menu);
    }
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (/*requestCode == CAMERA &&*/ resultCode == Activity.RESULT_OK) {
            if (data!=null){
                outputFileUri = data.getData();

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), outputFileUri);
                    //imageView.setImageBitmap(bitmap);
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
            progressOcr.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //progressOcr.cancel();
            progressOcr.setVisibility(View.INVISIBLE);
            String r = result.replaceAll("[\\n]+"," ");
            Matcher matcher = Pattern.compile("(Состав:)(.*?)(\\.)").matcher(r);
            matcher.find();

            Intent intent = new Intent(MainActivity.this, ResultActivity.class);

            intent.putExtra("result", matcher.group());
            intent.putExtra("picture", outputFileUri.toString());
            intent.putExtra("user", Integer.parseInt(idS));

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