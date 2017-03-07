package main.com.dvb.homePageFragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import main.com.dvb.Constants;
import main.com.dvb.Dashboard_main;
import main.com.dvb.R;
import main.com.dvb.adapters.FacebookAdapter;
import main.com.dvb.adapters.NewsAdapter;
import main.com.dvb.fragments.NoNetworkFragment;
import main.com.dvb.pojos.NewsBean;
import main.com.dvb.pojos.User;
import main.com.dvb.services.WebServices;

/**
 * Created by AIA on 12/8/16.
 */

public class NewsFragment extends Fragment{
    public static NewsFragment newsFragment;
    WebServices webServices;
    ProgressBar progressBar;
    RecyclerView newsRecycle;
    private FragmentManager manager;

//    ArrayList<NewsBean> newList = new ArrayList<>();
    NewsAdapter newsAdapter;

    User user;
    public NewsFragment(){
        newsFragment = NewsFragment.this;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_fragment, container, false);

        newsRecycle = (RecyclerView) view.findViewById(R.id.newsList);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBarCenter);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(((Dashboard_main)Dashboard_main.context).getApplicationContext());
        newsRecycle.setLayoutManager(mLayoutManager);
        newsRecycle.setItemAnimator(new DefaultItemAnimator());

        webServices = new WebServices();
//        new FetchNews().execute(Constants.NEWS_FEEDS);
//        progressBar.setVisibility(View.GONE);
//        newsAdapter = new NewsAdapter(Constants.newsBeanArrayList);
//        newsRecycle.setAdapter(newsAdapter);


        if(Constants.newsBeanArrayList == null || Constants.newsBeanArrayList.size() ==0){
//            NewsBean newsBean = new NewsBean();
//            Constants.newsBeanArrayList.add(newsBean);
            /*if (checkIsNetworkAvailable()){
                new FetchNews().execute(Constants.NEWS_FEEDS);
            }else {
                errorMessage();
            }*/
            new FetchNews().execute(Constants.NEWS_FEEDS);
        }else{
            progressBar.setVisibility(View.GONE);
            newsAdapter = new NewsAdapter(Constants.newsBeanArrayList);
            newsRecycle.setAdapter(newsAdapter);
        }



        return view;
    }


    private class FetchNews extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... urls) {

            try {

                return webServices.getRequest(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }
        @Override
        protected void onPostExecute(String result) {
            progressBar.setVisibility(View.GONE);

            try {

                formatData(result);

                if(newsAdapter == null) {
                   newsAdapter = new NewsAdapter(Constants.newsBeanArrayList);
                    newsRecycle.setAdapter(newsAdapter);
                }else{
                    newsAdapter.resetData(Constants.newsBeanArrayList);
                    newsAdapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        public void formatData(String result) throws JSONException {
            JSONArray jsonArray = new JSONArray(result);

            for (int i =0;i<jsonArray.length();i++){
                JSONObject object = jsonArray.getJSONObject(i);
                NewsBean newsBean = new NewsBean();
                newsBean.id = object.getString("id");
                newsBean.content = object.getString("message");
                newsBean.date = object.getString("inserted");
                newsBean.subject = object.getString("title");
                //newsBean.imageURL = object.getString("fileToUpload");
                String url = object.getString("fileToUpload");
                newsBean.setImageURL(url);

                Constants.newsBeanArrayList.add(newsBean);
                Log.e("mahesh",""+newsBean.getImageURL());
            }
        }
    }
    //Diolog for showing network information
    public void errorMessage(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Alert.!");
        builder.setMessage("Unable to connect server. Please check your internet connection.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
               // manager = getSupportFragmentManager();
                manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.main_fragment, new NoNetworkFragment());
                transaction.commit();

            }
        });
        builder.setNegativeButton("Refresh", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (checkIsNetworkAvailable()){
                    new FetchNews().execute(Constants.NEWS_FEEDS);
                }else {errorMessage();}

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

        return;
    }
    public boolean checkIsNetworkAvailable(){
        ConnectivityManager connMgr = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

}
