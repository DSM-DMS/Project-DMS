package com.dms.beinone.application.mypage;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dms.beinone.application.DMSService;
import com.dms.beinone.application.R;
import com.dms.beinone.application.utils.JSONParser;
import com.dms.beinone.application.utils.MultipartUtility;
import com.dms.boxfox.networking.HttpBox;
import com.dms.boxfox.networking.HttpBoxCallback;
import com.dms.boxfox.networking.datamodel.Request;
import com.dms.boxfox.networking.datamodel.Response;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by BeINone on 2017-02-20.
 */

public class MypageActivity extends AppCompatActivity {

    private static final int ACTIVITY_REQUEST_PICK_IMAGE = 11;
    private static final int PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 21;

    private Toolbar mToolbar;
    private NavigationView mNavigationView;
    private ImageView mProfileIV;
    private TextView mNameTV;
    private TextView mMeritTV;
    private TextView mDemeritTV;
    private TextView mTotalTV;
    private TextView mExtensionApplyStatusTV;

    private String mProfileImageString;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_mypage);

        init();
    }

    /**
     * 초기화
     */
    private void init() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar_mypage);
        mProfileIV = (ImageView) findViewById(R.id.iv_mypage_profile);
        mNameTV = (TextView) findViewById(R.id.tv_mypage_name);
        mMeritTV = (TextView) findViewById(R.id.tv_mypage_merit);
        mDemeritTV = (TextView) findViewById(R.id.tv_mypage_demerit);
        mTotalTV = (TextView) findViewById(R.id.tv_mypage_total);
        mExtensionApplyStatusTV = (TextView) findViewById(R.id.tv_mypage_extensionapply_status);

        setSupportActionBar(mToolbar);
        // display back button on action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final CollapsingToolbarLayout collapsingToolbarLayout =
                (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout_mypage);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout_mypage);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle("조성빈");
                } else {
                    collapsingToolbarLayout.setTitle(" ");
                }
            }
        });

        mProfileIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MypageActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.shouldShowRequestPermissionRationale(MypageActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        Toast.makeText(MypageActivity.this, "프로필 사진을 변경하기 위해서는 권한이 필요합니다.", Toast.LENGTH_SHORT).show();
                        ActivityCompat.requestPermissions(MypageActivity.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    } else {
                        ActivityCompat.requestPermissions(MypageActivity.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                } else {
                    selectProfileImage();
                }
            }
        });

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://dsm2015.cafe24.com:80")
                .build();

        DMSService dmsService = retrofit.create(DMSService.class);
        Call<Account> call = dmsService.loadMyPage();

        call.enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, retrofit2.Response<Account> response) {
                if (response.code() == 200) {
                    Account account = response.body();
                    bind(account);
                } else {
                    Toast.makeText(MypageActivity.this, R.string.mypage_load_internal_server_error, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {
                Toast.makeText(MypageActivity.this, R.string.mypage_load_internal_server_error, Toast.LENGTH_SHORT).show();
            }
        });

        try {
            loadMypage();
        } catch (IOException e) {
            System.out.println("IOException in MypageActivity: loadMyPage()");
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mProfileImageString != null) {
            Glide.with(this).load(mProfileImageString).centerCrop().into(mProfileIV);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return true;
    }

    @Override
    public void onStop() {
        super.onStop();
        Glide.clear(mProfileIV);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case ACTIVITY_REQUEST_PICK_IMAGE:
                if (resultCode == RESULT_OK && data != null && data.getData() != null) {
                    try {
                        Uri uri = data.getData();
                        InputStream iStream = getContentResolver().openInputStream(uri);
                        byte[] profileImageData = getBytes(iStream);
//                        new UploadProfileImageTask().execute(profileImageData);
//                        Glide.with(this).load(urSi).centerCrop().into(mProfileIV);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    selectProfileImage();
                } else {
                    Toast.makeText(this, "프로필 사진을 변경하기 위해서는 권한이 필요합니다.", Toast.LENGTH_SHORT).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
        }
    }

    private void selectProfileImage() {
        Intent intent = new Intent();
        // Show only images, no videos or anything else
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        // Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "Select Image"), ACTIVITY_REQUEST_PICK_IMAGE);
    }

    private void bind(Account account) {
        mNameTV.setText(account.getName());
        mMeritTV.setText(String.valueOf(account.getMerit()));
        mDemeritTV.setText(String.valueOf(account.getDemerit()));
        mTotalTV.setText(String.valueOf(account.getMerit() - account.getDemerit()));
        if (account.getRoom() == -1) {
            // not applied
            mExtensionApplyStatusTV.setText(R.string.mypage_notapplied);
            mExtensionApplyStatusTV.setTextColor(ContextCompat.getColor(this, R.color.negative));
        } else {
            // applied
            mExtensionApplyStatusTV.setText(R.string.mypage_applied);
            mExtensionApplyStatusTV.setTextColor(ContextCompat.getColor(this, R.color.positive));
        }
//        if (account.getProfileImage() != null) {
//            Glide.with(this).load(account.getProfileImage()).centerCrop().into(mProfileIV);
//        }
    }

    private byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

//        public Bitmap stringToBitmap(String encodedString){
//            try{
//                byte [] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
//                Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
//                return bitmap;
//            }catch(Exception e){
//                e.getMessage();
//                return null;
//            }
//        }

    private void loadMypage() throws IOException {
        HttpBox.get(MypageActivity.this, "/account/student")
                .push(new HttpBoxCallback() {
                    @Override
                    public void done(Response response) {
                        int code = response.getCode();
                        switch (code) {
                            case HttpBox.HTTP_OK:
                                try {
                                    Account account = JSONParser.parseStudentJSON(response.getJsonObject());
                                    bind(account);
                                } catch (JSONException e) {
                                    System.out.println("JSONException in MypageActivity: GET /account/student");
                                    e.printStackTrace();
                                }
                                break;
                            case HttpBox.HTTP_NO_CONTENT:
                                Toast.makeText(MypageActivity.this, R.string.mypage_load_no_content, Toast.LENGTH_SHORT).show();
                                break;
                            case HttpBox.HTTP_BAD_REQUEST:
                                Toast.makeText(MypageActivity.this, R.string.http_bad_request, Toast.LENGTH_SHORT).show();
                                break;
                            case HttpBox.HTTP_INTERNAL_SERVER_ERROR:
                                Toast.makeText(MypageActivity.this, R.string.mypage_load_internal_server_error, Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                break;
                        }
                    }

                    @Override
                    public void err(Exception e) {
                        System.out.println("Error in LoginActivity: GET /account/student");
                        e.printStackTrace();
                    }
                });
    }

//    private class UploadProfileImageTask extends AsyncTask<byte[], Void, Response> {
//
//        private static final String MSG_NOT_IMAGE_FILE = "It's Not Image File";
//        private static final String MSG_NEED_LOGIN = "Need Login";
//
//        @Override
//        protected Response doInBackground(byte[]... params) {
//            Response response = null;
//
//            byte[] profileImageData = params[0];
//            try {
//                response = uploadProfileImage(profileImageData);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            return response;
//        }
//
//        @Override
//        protected void onPostExecute(Response response) {
//            super.onPostExecute(response);
//
//            int code = response.getCode();
//            String message = response.getMessage();
//            if (code == 200) {
//                // success
//                Toast.makeText(MypageActivity.this, R.string.mypage_upload_profile_success,
//                        Toast.LENGTH_SHORT).show();
//            } else if (code == 400) {
//                // failure
//                if (message.equals(MSG_NOT_IMAGE_FILE)) {
//                    // not image file
//                    Toast.makeText(MypageActivity.this, R.string.mypage_upload_profile_message_notimagefile,
//                            Toast.LENGTH_SHORT).show();
//                } else if (message.equals(MSG_NEED_LOGIN)) {
//                    // need login
//                    Toast.makeText(MypageActivity.this, R.string.mypage_upload_profile_message_needlogin,
//                            Toast.LENGTH_SHORT).show();
//                }
//            }
//        }
//
//        private void uploadProfileImage(byte[] profileImageData) throws IOException {
//            MultipartUtility multipart = new MultipartUtility("UTF-8");
//            multipart.addFilePart("profile_image", profileImageData, "test.jpg");
//            String imageString = multipart.finish();
//
////            HttpBox.post(MypageActivity.this, "/upload/image")
////                    .putHeaderProperty(Request.CONTENT_TYPE, "multipart/form-data; boundary=" + multipart.getBoundary())
////                    .putBodyData(imageString)
////                    .push();
////            HttpBox.post(MypageActivity.this, "/upload/image")
//        }
//    }

}
