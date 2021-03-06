//package com.dms.beinone.application.mypage;
//
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.support.v7.app.AppCompatActivity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.bumptech.glide.Glide;
//import com.dms.beinone.application.R;
//import com.dms.beinone.application.utils.JSONParser;
//import com.dms.boxfox.networking.HttpBox;
//import com.dms.boxfox.networking.datamodel.Request;
//import com.dms.boxfox.networking.datamodel.Response;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * Created by BeINone on 2017-02-20.
// */
//
//public class MypageFragment extends Fragment {
//
//    private ImageView mProfileIV;
//    private TextView mNameTV;
//    private TextView mMeritTV;
//    private TextView mDemeritTV;
//    private TextView mTotalTV;
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_mypage, container, false);
//
//        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
//
//        init(view);
//
//        return view;
//    }
//
//    /**
//     * 초기화
//     * @param rootView 필요한 뷰를 찾을 최상위 뷰
//     */
//    private void init(View rootView) {
//        getActivity().setTitle(R.string.nav_mypage);
//
//        mProfileIV = (ImageView) rootView.findViewById(R.id.iv_mypage_profile);
//        mNameTV = (TextView) rootView.findViewById(R.id.tv_mypage_name);
//        mMeritTV = (TextView) rootView.findViewById(R.id.tv_mypage_merit);
//        mDemeritTV = (TextView) rootView.findViewById(R.id.tv_mypage_demerit);
//        mTotalTV = (TextView) rootView.findViewById(R.id.tv_mypage_total);
//
//        new LoadMypageTask().execute();
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//
//        Glide.with(getContext()).load(R.drawable.test).centerCrop().into(mProfileIV);
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//
//        Glide.clear(mProfileIV);
//    }
//
//    private void bind(Account account) {
//        mNameTV.setText(account.getName());
//        mMeritTV.setText(String.valueOf(account.getMerit()));
//        mDemeritTV.setText(String.valueOf(account.getDemerit()));
//        mTotalTV.setText(String.valueOf(account.getMerit() - account.getDemerit()));
//    }
//
//    private class LoadMypageTask extends AsyncTask<Void, Void, Account> {
//
//        @Override
//        protected Account doInBackground(Void... params) {
//            Account account = null;
//
//            try {
//                account = loadMypage();
//            } catch (IOException e) {
//                e.printStackTrace();
//                return null;
//            } catch (JSONException e) {
//                e.printStackTrace();
//                return null;
//            }
//
//            return account;
//        }
//
//        @Override
//        protected void onPostExecute(Account account) {
//            super.onPostExecute(account);
//
//            if (account == null) {
//                Toast.makeText(getContext(), R.string.mypage_load_internal_server_error, Toast.LENGTH_SHORT).show();
//            } else {
//                bind(account);
//            }
//        }
//
//        private Account loadMypage() throws IOException, JSONException {
//            Map<String, String> requestParams = new HashMap<>();
//
//            Response response = HttpBox.post(getContext(), "/account/student", Request.TYPE_GET)
//                    .putBodyData(requestParams)
//                    .push();
//            JSONObject responseJSONObject = response.getJsonObject();
//
//            return JSONParser.parseStudentJSON(responseJSONObject);
//        }
//    }
//
//}
