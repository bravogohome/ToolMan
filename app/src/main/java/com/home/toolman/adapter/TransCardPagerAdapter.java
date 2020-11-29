package com.home.toolman.adapter;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import com.home.toolman.R;
import com.home.toolman.translator.Translator;
import com.home.toolman.vo.Card;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.http2.Header;

import static org.litepal.LitePalApplication.getContext;

public class TransCardPagerAdapter extends PagerAdapter {
    private MediaPlayer mediaPlayer=new MediaPlayer();
    private Context mContext;
    private List<Card> mCard;
    private Map<String,String> mMap;
    private String transUrl,speakUrl;
    private String[] mTitles={"常规","进阶"};
    public TransCardPagerAdapter(Context context ,List<Card> list) {
        mContext = context;
        mCard = list;
    }

    @Override
    public int getCount() {
        return mCard.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view=null;
        switch (position){
            case 0:
                view = View.inflate(mContext, R.layout.trans_page_layout_first,null);
                TextView result = (TextView) view.findViewById(R.id.page_one_text_result);
                TextView library=(TextView)view.findViewById(R.id.page_one_text_library);
                Card card=mCard.get(position);
                result.setText(card.getResult());
                library.setText(card.getLibrary());
                container.addView(view);
                return view;
            case 1:
                view=View.inflate(mContext,R.layout.trans_page_layout_second,null);
                TextView card1_result=(TextView)view.findViewById(R.id.card1_text_result);
                TextView card1_library=(TextView)view.findViewById(R.id.card1_text_library);
                TextView us_phonetic=(TextView)view.findViewById(R.id.us_phonetic);
                TextView uk_phonetic=(TextView)view.findViewById(R.id.uk_phonetic);
                TextView phonetic=(TextView)view.findViewById(R.id.phonetic);
                Button voice=(Button)view.findViewById(R.id.button_result_voice);
                Card card1=mCard.get(position);
                card1_result.setText(card1.getResult());
                card1_library.setText(card1.getLibrary());
                mMap=card1.getMap();
                String us=mMap.get("us_phonetic");
                String uk=mMap.get("uk_phonetic");
                String all=mMap.get("phonetic");
                if (!TextUtils.isEmpty(us)&&!TextUtils.isEmpty(uk)){
                    us_phonetic.setText("/"+us+"/");
                    uk_phonetic.setText("/"+uk+"/");
                    all=null;
                }else if(!TextUtils.isEmpty(us)&&TextUtils.isEmpty(uk)){
                    us_phonetic.setText("/"+us+"/");
                    uk_phonetic.setText("暂无音标");
                    all=null;
                }else if(TextUtils.isEmpty(us)&&!TextUtils.isEmpty(uk)){
                    us_phonetic.setText("暂无音标");
                    uk_phonetic.setText("/"+uk+"/");
                    all=null;
                } else{
                    us_phonetic.setText("暂无音标");
                    uk_phonetic.setText("暂无音标");
                }
                if(!TextUtils.isEmpty(all)){
                    phonetic.setText(all);
                }else{
                    phonetic.setText("暂无注音");
                }
                voice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                            final String speakUrl=mMap.get("tSpeakUrl");
                            try {
                                mediaPlayer.reset();
                                mediaPlayer.setDataSource(speakUrl);
                                mediaPlayer.prepare();
                                mediaPlayer.start();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Translator translator=new Translator(mMap.get("from"),mMap.get("to"),mMap.get("origin"));
                            initSpeakUrl(translator);
                        }
                });
                container.addView(view);
                return view;

        }
        return view;
    }

    private void initSpeakUrl(Translator translator){

        transUrl=translator.youdaoUrl;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client=new OkHttpClient();//创建OKhttpClient实例
                    Request request=new Request.Builder()
                            .url(transUrl)
                            .build();
                    Response response=client.newCall(request).execute();
                    String responseData=response.body().string();
                    Log.d("TAG", "run: "+responseData);
                    JSONObject jsonObject=new JSONObject(responseData);
                    mMap.put("tSpeakUrl",jsonObject.getString("tSpeakUrl"));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // super.destroyItem(container,position,object); 这一句要删除，否则报错
        container.removeView((View)object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }

}
