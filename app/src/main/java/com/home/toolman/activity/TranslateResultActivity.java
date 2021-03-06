package com.home.toolman.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.viewpager.widget.ViewPager;


import com.home.toolman.BaseActivity;
import com.home.toolman.R;

import com.home.toolman.translator.Translator;

import com.home.toolman.vo.WordsBook;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;


public class TranslateResultActivity extends BaseActivity implements View.OnClickListener{
    private String fromParam,toParam,origin;
    private TextView textOrigin,textResult,textLibrary;
    private ImageView favoriteButton;
    private List<WordsBook> wordsBook=new ArrayList<>();
    private ViewPager viewPager;
    public static LinearLayout linearLayout;
    public static TextView textLoading;
    public static ImageView imageLoading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_translate_result);
        linearLayout=(LinearLayout)findViewById(R.id.layout_translate_result);
        linearLayout.setVisibility(View.GONE);
        textLoading=(TextView)findViewById(R.id.text_loading);
        imageLoading=(ImageView)findViewById(R.id.image_loading);
        textOrigin=(TextView)findViewById(R.id.text_trans_origin);
        textResult=(TextView)findViewById(R.id.text_trans_result);
        textLibrary=(TextView)findViewById(R.id.text_trans_library);
        favoriteButton=(ImageView) findViewById(R.id.button_add_to_words_book);
        Intent intent=getIntent();
        fromParam=intent.getStringExtra("fromParam");
        toParam=intent.getStringExtra("toParam");
        origin=intent.getStringExtra("origin");
        textOrigin.setText(origin);
        viewPager= (ViewPager) findViewById(R.id.viewPager);
        viewPager.setPageTransformer(false,new ZoomOutPageTransformer());//viewPager???????????????
        Translator translator=new Translator(fromParam,toParam,origin);
        translator.showResultOnViewPager(viewPager,textResult,textLibrary);

        //????????????
        favoriteButton.setOnClickListener(this);
        initFavoriteButton();

        Button textButton=(Button)findViewById(R.id.to_book);
        textButton.setOnClickListener(this);
        Button toBaiduTrans=(Button)findViewById(R.id.button_to_baidu_trans);
        toBaiduTrans.setOnClickListener(this);
        Button toWangyiTrans=(Button)findViewById(R.id.button_to_wangyi_trans);
        toWangyiTrans.setOnClickListener(this);
    }
    private void initFavoriteButton(){
        wordsBook= DataSupport.findAll(WordsBook.class);
        if(!wordsBook.isEmpty()){
            for(int i=0;i<wordsBook.size();i++){
                if(TextUtils.equals(textOrigin.getText().toString(),wordsBook.get(i).getOrigin())&&TextUtils.equals(fromParam,wordsBook.get(i).getFrom())&&TextUtils.equals(toParam,wordsBook.get(i).getTo())){
                    favoriteButton.setImageResource(R.drawable.ic_favorite_word_yes);
                    favoriteButton.setKeepScreenOn(true);
                    break;
                }else{
                    favoriteButton.setKeepScreenOn(false);
                    favoriteButton.setImageResource(R.drawable.ic_favorite_word_no);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.button_add_to_words_book:
                if(favoriteButton.getKeepScreenOn()){
                    wordsBook= DataSupport.findAll(WordsBook.class);
                    DataSupport.deleteAll(WordsBook.class,"from=? and to=? and origin=?",fromParam,toParam,origin);
                   favoriteButton.setImageResource(R.drawable.ic_favorite_word_no);
                   favoriteButton.setKeepScreenOn(false);
                   Toast.makeText(this, "????????????", Toast.LENGTH_SHORT).show();
                }else{
                    wordsBook= DataSupport.findAll(WordsBook.class);
                    WordsBook wordsBook=new WordsBook(fromParam,toParam,textOrigin.getText().toString(),textResult.getText().toString(),textLibrary.getText().toString(),R.drawable.ic_favorite_word_yes);
                    wordsBook.save();
                    favoriteButton.setImageResource(R.drawable.ic_favorite_word_yes);
                    favoriteButton.setKeepScreenOn(true);
                    Toast.makeText(this, "????????????", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.to_book:
                Intent intent=new Intent(this,WordsBookActivity.class);
                startActivity(intent);
                break;
            case R.id.button_to_baidu_trans:
                Intent intentToBaidu = new Intent();
                PackageManager packageManager = getPackageManager();
                intentToBaidu = packageManager.getLaunchIntentForPackage("com.baidu.baidutranslate");
                startActivity(intentToBaidu);
                break;
            case R.id.button_to_wangyi_trans:
                Intent intentToWangyi = new Intent();
                PackageManager packageManager1 = getPackageManager();
                intentToWangyi = packageManager1.getLaunchIntentForPackage("com.youdao.dict");
                startActivity(intentToWangyi);
                break;
            default:

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initFavoriteButton();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    };

    //viewPager???????????????
    public class ZoomOutPageTransformer implements ViewPager.PageTransformer
    {
        private static final float MIN_SCALE = 0.85f;
        private static final float MIN_ALPHA = 0.5f;
        public void transformPage(View view, float position)
        {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();
            if (position < -1)
            { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);
            } else if (position <= 1) //a????????????b??? ??? a?????? 0.0 -1 ???b??????1 ~ 0.0
            { // [-1,1]
                // Modify the default slide transition to shrink the page as well
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0)
                {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else
                {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }
                // Scale the page down (between MIN_SCALE and 1)
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);
                // Fade the page relative to its size.
                view.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE)
                        / (1 - MIN_SCALE) * (1 - MIN_ALPHA));
            } else
            { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }
}