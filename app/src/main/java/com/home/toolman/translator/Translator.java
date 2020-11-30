package com.home.toolman.translator;

import android.util.Log;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.home.toolman.R;
import com.home.toolman.activity.TranslateResultActivity;
import com.home.toolman.tasks.TranslateTask;
import com.home.toolman.utils.MD5;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Translator {
    private static final String APP_ID="20201120000621579";
    private static final String KEY="0iZjeX8ip2njQQfcN7LQ";
    private static final String HOST="http://api.fanyi.baidu.com/api/trans/vip/translate?";
    private static final String YOUDAO_HOST="https://openapi.youdao.com/api?";
    private static final String YOUDAO_APPID="4aee7a22105809c0";
    private static final String YOUDAO_KEY="M8X76z5w0dubKzQXjDlSyMhKzKhk7YCT";
    public String youdaoUrl;
    public String questUrl;
    public Translator(String from,String to,String origin){
        String randomCode=String.valueOf(System.currentTimeMillis());
        String string1=APP_ID+origin+randomCode+KEY;
        String sign= MD5.md5(string1);
        questUrl=HOST+"q="+origin+"&from="+from+"&to="+to+"&appid="+APP_ID+"&salt="+randomCode+"&sign="+sign;
        String input=truncate(origin);
        String salt= String.valueOf(System.currentTimeMillis());
        String curtime = String.valueOf(System.currentTimeMillis() / 1000);
        String string2=YOUDAO_APPID+input+salt+curtime+YOUDAO_KEY;
        String youdaoSign=getDigest(string2);
        String youdaoFrom=getYoudaoLanguage(from);
        String youdaoTo=getYoudaoLanguage(to);
        youdaoUrl=YOUDAO_HOST+"q="+origin+"&from="+youdaoFrom+"&to="+youdaoTo+"&appKey="+YOUDAO_APPID+"&salt="+salt+"&sign="+youdaoSign+"&signType=v3&curtime="+curtime+"&ext=mp3";
        Log.d("TAG", "Translator: "+youdaoUrl);
    }

    public void showResultOnViewPager(ViewPager viewPager , TextView textResult, TextView textLibrary){
        TranslateResultActivity.textLoading.setText("翻译中Loading.");
        TranslateResultActivity.imageLoading.setImageResource(R.drawable.ic_loading1);
        new TranslateTask(viewPager,textResult,textLibrary).execute(this);
    }

    public static String truncate(String q) {
        if (q == null) {
            return null;
        }
        int len = q.length();
        String result;
        return len <= 20 ? q : (q.substring(0, 10) + len + q.substring(len - 10, len));
    }
    public static String getDigest(String string) {
        if (string == null) {
            return null;
        }
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        byte[] btInput = string.getBytes(StandardCharsets.UTF_8);
        try {
            MessageDigest mdInst = MessageDigest.getInstance("SHA-256");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (byte byte0 : md) {
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
    private String getYoudaoLanguage(String language){
        String youdao="auto";
        switch(language){
            case "zh":
                youdao="zh-CHS";
                break;
            case "jp":
                youdao="ja";
                break;
            case "kor":
                youdao="ko";
                break;
            case "wyw":
                youdao="auto";
                break;
            case "fra":
                youdao="fr";
                break;
            case "spa":
                youdao="es";
                break;
            case "ara":
                youdao="ar";
                break;
            case "dan":
                youdao="da";
                break;
            case "fin":
                youdao="fi";
                break;
            case "rom":
                youdao="ro";
                break;
            case "swe":
                youdao="sv";
                break;
            case "cht":
                youdao="auto";
                break;
            default:
                youdao=language;
                break;
        }
        return youdao;
    }
}
