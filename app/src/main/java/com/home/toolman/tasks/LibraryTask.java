package com.home.toolman.tasks;


import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.home.toolman.R;
import com.home.toolman.activity.TranslateResultActivity;
import com.home.toolman.adapter.TransCardPagerAdapter;
import com.home.toolman.vo.Card;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static org.litepal.LitePalApplication.getContext;

public class LibraryTask extends AsyncTask<String,Integer,String> {
    private TextView textLibrary;
    private ViewPager viewPager;
    private Card card,card1;
    public LibraryTask(ViewPager viewPager,Card card,Card card1,TextView textLibrary){
        this.viewPager=viewPager;
        this.card=card;
        this.card1=card1;
        this.textLibrary=textLibrary;
    }

    @Override
    protected String doInBackground(String... strings) {
        String responseData=null;
        try{
            OkHttpClient client=new OkHttpClient();//创建OKhttpClient实例
            Request request=new Request.Builder()
                    .url(strings[0])
                    .build();
            Response response=client.newCall(request).execute();
            responseData=response.body().string();
            TranslateResultActivity.textLoading.setText("翻译中Loading......");
            TranslateResultActivity.imageLoading.setImageResource(R.drawable.ic_loading6);
            return responseData;
        }catch (Exception e){
            e.printStackTrace();
        }
        return responseData;
    }

    @Override
    protected void onPostExecute(String responseData) {
        super.onPostExecute(responseData);
        Map<String,String> map=new HashMap<>();
        map=parseToResult(responseData);
        String library=null;
        TranslateResultActivity.textLoading.setText("翻译中Loading");
        TranslateResultActivity.imageLoading.setImageResource(R.drawable.ic_loading);
        if(!TextUtils.equals(map.get("msg"),"success")){
            library=map.get("fail");
        }else{
            library=map.get("content");
        }
        textLibrary.setText(library);
        card.setLibrary(library);
        List<Card> cards=new ArrayList<>();
        cards.add(card);
        cards.add(card1);
        TransCardPagerAdapter adapter=new TransCardPagerAdapter(getContext(),cards);
        viewPager.setAdapter(adapter);
        TranslateResultActivity.textLoading.setText("翻译中Loading.");
        TranslateResultActivity.imageLoading.setImageResource(R.drawable.ic_loading1);
        TranslateResultActivity.linearLayout.setVisibility(View.VISIBLE);
    }

    private Map<String,String> parseToResult(String jsonData){
        Map<String,String> map=new HashMap<>();
        try{
            JSONObject jsonObject = new JSONObject(jsonData);
            String code=jsonObject.getString("code");
            String msg=jsonObject.getString("msg");
            map.put("code",code);
            map.put("msg",msg);
            JSONArray newslist=jsonObject.getJSONArray("newslist");
            JSONObject jsonObject1=newslist.getJSONObject(0);
            String word=jsonObject1.getString("word");
            String content=jsonObject1.getString("content");
            map.put("word",word);
            map.put("content",content);
            Log.d("MainActivity", "parseJSONWithJSONObject: "+code+" "+msg+" "+word+" "+content);
            return map;
        }catch (Exception e){
            e.printStackTrace();
        }
        map.put("fail","暂无释义");
        return map;
    }
}
