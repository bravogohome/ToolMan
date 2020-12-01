package com.home.toolman.tasks;


import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.home.toolman.R;
import com.home.toolman.activity.MainActivity;
import com.home.toolman.activity.TranslateResultActivity;
import com.home.toolman.adapter.TransCardPagerAdapter;
import com.home.toolman.fragment.search.SearchFragment;
import com.home.toolman.translator.Translator;
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

public class TranslateTask extends AsyncTask<Translator,Integer,List<String>>{
    private TextView textResult,textLibrary;
    private ViewPager viewPager;
    private String from,to;
    private Card card1=new Card();
    public TranslateTask(ViewPager viewPager,TextView textResult,TextView textLibrary){
        this.textResult=textResult;
        this.textLibrary=textLibrary;
        this.viewPager=viewPager;
    }


    @Override
    protected List<String> doInBackground(Translator... translators) {
        List<String> responseDataList=new ArrayList<>();
        TranslateResultActivity.textLoading.setText("翻译中Loading..");
        TranslateResultActivity.imageLoading.setImageResource(R.drawable.ic_loading2);
        try{
            OkHttpClient client=new OkHttpClient();//创建OKhttpClient实例
            Request request=new Request.Builder()
                    .url(translators[0].questUrl)
                    .build();
            Response response=client.newCall(request).execute();
            String responseData=response.body().string();
            Log.d("TAG", "doInBackground: "+responseData);
            responseDataList.add(responseData);
            TranslateResultActivity.textLoading.setText("翻译中Loading...");
            TranslateResultActivity.imageLoading.setImageResource(R.drawable.ic_loading3);
            OkHttpClient client_youdao=new OkHttpClient();//创建OKhttpClient实例
            Request request_youdao=new Request.Builder()
                    .url(translators[0].youdaoUrl)
                    .build();
            Response response_youdao=client_youdao.newCall(request_youdao).execute();
            String responseData_youdao=response_youdao.body().string();
            Log.d("TAG", "doInBackground: "+responseData_youdao);
            responseDataList.add(responseData_youdao);
            return responseDataList;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    @Override
    protected void onPostExecute(List<String> responseDataList) {
        super.onPostExecute(responseDataList);
        transYoudao(responseDataList);
        transBaidu(responseDataList);

    }
    private void transYoudao(List<String> responseDataList){
        Map<String,String> map=new HashMap<>();
        map=parseYoudaoResult(responseDataList.get(1));
        card1.setMap(map);
        card1.setResult(map.get("resultWord"));
        card1.setLibrary(map.get("explain"));
        TranslateResultActivity.textLoading.setText("翻译中Loading....");
        TranslateResultActivity.imageLoading.setImageResource(R.drawable.ic_loading4);
    }
    private void transBaidu(List<String> responseDataList){
        List<String> rs=new ArrayList<>();
        rs=parseToResult(responseDataList.get(0));
        String word=rs.get(3);
        String src=rs.get(2);
        MainActivity.result=word;
        String from=rs.get(0);
        String to=rs.get(1);
        SearchFragment.addRecord(from,to,src);
        Map<String,String> map1=new HashMap<>();
        map1=card1.getMap();
        map1.put("from",from);
        map1.put("to",to);
        map1.put("origin",src);
        card1.setMap(map1);
        String key="fb1f818cbd0e7cabde1082177b248c82";
        String url=null;
        textResult.setText(word);
        Card card=new Card();
        card.setResult(word);
        TranslateResultActivity.textLoading.setText("翻译中Loading.....");
        TranslateResultActivity.imageLoading.setImageResource(R.drawable.ic_loading5);
        switch(to){
            case "cht":
            case "wyw":
            case "zh":
                url="http://api.tianapi.com/txapi/lexicon/index?key="+key+"&word="+word;
                Log.d("TAG", "onPostExecute: "+url);
                new LibraryTask(viewPager,card,card1,textLibrary).execute(url);
                break;
            case "en":
                url="http://api.tianapi.com/txapi/enwords/index?key="+key+"&word="+word;
                Log.d("TAG", "onPostExecute: "+url);
                new LibraryTask(viewPager,card,card1,textLibrary).execute(url);
                break;
            default:
                textLibrary.setText("暂无释义");
                card.setLibrary("暂无释义");
                List<Card> cards=new ArrayList<>();
                cards.add(card);
                cards.add(card1);
                TransCardPagerAdapter adapter=new TransCardPagerAdapter(getContext(),cards);
                viewPager.setAdapter(adapter);
                TranslateResultActivity.textLoading.setText("翻译中Loading......");
                TranslateResultActivity.imageLoading.setImageResource(R.drawable.ic_loading6);
                TranslateResultActivity.linearLayout.setVisibility(View.VISIBLE);
                break;
        }
    }
    private Map<String,String> parseYoudaoResult(String jsonData){
        Map<String,String> map=new HashMap<>();
        try{
            JSONObject jsonObject=new JSONObject(jsonData);
            String l=jsonObject.getString("l");
            map.put("l",l);
            try{
                map.put("tSpeakUrl",jsonObject.getString("tSpeakUrl"));
            }catch (Exception e){
                e.printStackTrace();
            }
            JSONArray translation=jsonObject.getJSONArray("translation");
            String resultWord=translation.getString(0);
            map.put("resultWord",resultWord);
            JSONObject basic=jsonObject.getJSONObject("basic");
            JSONArray explains=basic.getJSONArray("explains");
            String explain=" ";
            for (int i=0;i<explains.length();i++){
                explain+="\n";
                explain+=explains.getString(i);
            }
            map.put("explain",explain);
            try{
                map.put("uk_phonetic",basic.getString("uk-phonetic"));
            }catch (Exception e){
                e.printStackTrace();
            }
            try{
                map.put("us_phonetic",basic.getString("us-phonetic"));
            }catch (Exception e){
                e.printStackTrace();
            }
            try{
                map.put("phonetic",basic.getString("phonetic")) ;
            }catch (Exception e){
                e.printStackTrace();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return map;
    }
    private List<String> parseToResult(String jsonData){
        List<String> rs=new ArrayList<>();
        try{
            JSONObject jsonObject = new JSONObject(jsonData);
            from=jsonObject.getString("from");
            to=jsonObject.getString("to");
            JSONArray results=jsonObject.getJSONArray("trans_result");
            JSONObject jsonObject1=results.getJSONObject(0);
            String src=jsonObject1.getString("src");
            String dst=jsonObject1.getString("dst");
            Log.d("MainActivity", "parseJSONWithJSONObject: "+from+" "+to+" "+src+" "+dst);
            rs.add(from);
            rs.add(to);
            rs.add(src);
            rs.add(dst);
            return rs;
        }catch (Exception e){
            e.printStackTrace();
        }
        rs.add("error:解析失败");
        return rs;
    }
}
