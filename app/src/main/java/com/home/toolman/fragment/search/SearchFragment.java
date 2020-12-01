package com.home.toolman.fragment.search;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.home.toolman.R;
import com.home.toolman.activity.MainActivity;
import com.home.toolman.activity.TranslateResultActivity;
import com.home.toolman.adapter.RecordAdapter;
import com.home.toolman.utils.JsonParser;
import com.home.toolman.vo.Record;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class SearchFragment extends Fragment implements View.OnClickListener{
    private Spinner languagesFrom,languagesTo;
    public String fromParam="auto";
    public String toParam="zh";
    private String origin;
    private EditText editText;
    // 语音听写UI
    private RecognizerDialog mIatDialog;
    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
    private StringBuffer buffer = new StringBuffer();
    private RecyclerView recordRecyclerView;
    private Button buttonTrans,deleteOrigin;
    private LinearLayout recordLayout;
    private TextView deleteAllRecord;
    List<Record> recordList=new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Log.d("SearchFragment", "onCreateView: ");
        MainActivity.status="SearchFragment";
        View root = inflater.inflate(R.layout.fragment_search, container, false);
        languagesFrom=(Spinner)root.findViewById(R.id.choose_language_from);
        languagesTo=(Spinner)root.findViewById(R.id.choose_language_to);
        languagesFrom.setOnItemSelectedListener(new SelectedListener());
        languagesTo.setOnItemSelectedListener(new SelectedListener());
        editText=(EditText)root.findViewById(R.id.edit_text_fragment_search);
        editText.setOnClickListener(this);
        deleteOrigin=(Button)root.findViewById(R.id.button_delete_origin_text);
        deleteOrigin.setOnClickListener(this);
        root.findViewById(R.id.button_voice_to_origin).setOnClickListener(this);
        // 初始化听写Dialog，如果只使用有UI听写功能，无需创建SpeechRecognizer
        // 使用UI听写功能，请根据sdk文件目录下的notice.txt,放置布局文件和图片资源
        mIatDialog = new RecognizerDialog(getContext(), mInitListener);
        recordRecyclerView=root.findViewById(R.id.recycler_view_search_record);
        recordLayout=(LinearLayout)root.findViewById(R.id.record_layout);
        buttonTrans=root.findViewById(R.id.button_translate);
        buttonTrans.setOnClickListener(this);
        deleteAllRecord=(TextView)root.findViewById(R.id.delete_all_record);
        deleteAllRecord.setOnClickListener(this);
        initRecords();
        return root;
    }
    private void initRecords(){
        List<Record> rList=new ArrayList<>();
        List<Record> recordListESC= DataSupport.findAll(Record.class);
        for(int i=recordListESC.size()-1;i>=0;i--){
            rList.add(recordListESC.get(i));
        }
        recordList=rList;
        if(!recordList.isEmpty()) {
            LinearLayoutManager recordItemLayoutManager = new LinearLayoutManager(getContext());
            recordRecyclerView.setLayoutManager(recordItemLayoutManager);
            RecordAdapter adapter = new RecordAdapter(recordList);
            recordRecyclerView.setAdapter(adapter);
        }
    }
    class SelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            Toast.makeText(getContext(), "NoSelect", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String content=parent.getItemAtPosition(position).toString();
            switch(parent.getId()){
                case R.id.choose_language_from:
                    switch(content){
                        case "自动检测":
                            fromParam="auto";
                            break;
                        case "英语":
                            fromParam="en";
                            break;
                        case "中文":
                            fromParam="zh";
                            break;
                        case "日语":
                            fromParam="jp";
                            break;
                        case "文言文":
                            fromParam="wyw";
                            break;
                        case "韩语":
                            fromParam="kor";
                            break;
                        case "法语":
                            fromParam="fra";
                            break;
                        case "西班牙语":
                            fromParam="spa";
                            break;
                        case "泰语":
                            fromParam="th";
                            break;
                        case "阿拉伯语":
                            fromParam="ara";
                            break;
                        case "俄语":
                            fromParam="ru";
                            break;
                        case "葡萄牙语":
                            fromParam="pt";
                            break;
                        case "德语":
                            fromParam="de";
                            break;
                        case "意大利语":
                            fromParam="it";
                            break;
                        case "希腊语":
                            fromParam="el";
                            break;
                        case "荷兰语":
                            fromParam="nl";
                            break;
                        case "波兰语":
                            fromParam="pl";
                            break;
                        case "丹麦语":
                            fromParam="dan";
                            break;
                        case "芬兰语":
                            fromParam="fin";
                            break;
                        case "捷克语":
                            fromParam="cs";
                            break;
                        case "罗马尼亚语":
                            fromParam="rom";
                            break;
                        case "瑞典语":
                            fromParam="swe";
                            break;
                        case "繁体中文":
                            fromParam="cht";
                            break;
                        default:
                            fromParam="auto";
                    }
                    break;
                case R.id.choose_language_to:
                    switch(content){
                        case "英语":
                            toParam="en";
                            break;
                        case "中文":
                            toParam="zh";
                            break;
                        case "日语":
                            toParam="jp";
                            break;
                        case "文言文":
                            toParam="wyw";
                            break;
                        case "韩语":
                            toParam="kor";
                            break;
                        case "法语":
                            toParam="fra";
                            break;
                        case "西班牙语":
                            toParam="spa";
                            break;
                        case "泰语":
                            toParam="th";
                            break;
                        case "阿拉伯语":
                            toParam="ara";
                            break;
                        case "俄语":
                            toParam="ru";
                            break;
                        case "葡萄牙语":
                            toParam="pt";
                            break;
                        case "德语":
                            toParam="de";
                            break;
                        case "意大利语":
                            toParam="it";
                            break;
                        case "希腊语":
                            toParam="el";
                            break;
                        case "荷兰语":
                            toParam="nl";
                            break;
                        case "波兰语":
                            toParam="pl";
                            break;
                        case "丹麦语":
                            toParam="dan";
                            break;
                        case "芬兰语":
                            toParam="fin";
                            break;
                        case "捷克语":
                            toParam="cs";
                            break;
                        case "罗马尼亚语":
                            toParam="rom";
                            break;
                        case "瑞典语":
                            toParam="swe";
                            break;
                        case "繁体中文":
                            toParam="cht";
                            break;
                        default:
                            toParam="zh";
                    }
                    break;
                default:
            }
        }
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.button_voice_to_origin:
                buffer.setLength(0);
                editText.setText(null);// 清空显示内容
                mIatResults.clear();
                // 显示听写对话框
                mIatDialog.setListener(mRecognizerDialogListener);
                mIatDialog.show();
                Toast.makeText(getContext(), getString(R.string.voice_begin), Toast.LENGTH_SHORT).show();
                break;
            case R.id.edit_text_fragment_search:
                //editText.setText("....");
                //editText.setSelection(editText.getText().length());
                recordLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.button_translate:
                origin=editText.getText().toString();
                if (TextUtils.isEmpty(origin)){
                    Toast.makeText(getContext(), "请输入翻译原文", Toast.LENGTH_SHORT).show();
                    break;
                }
                Intent intent=new Intent(getContext(), TranslateResultActivity.class);
                intent.putExtra("fromParam",fromParam);
                intent.putExtra("toParam",toParam);
                intent.putExtra("origin",origin);
                startActivity(intent);
                break;
            case R.id.delete_all_record:
                AlertDialog.Builder dialog=new AlertDialog.Builder(getContext());
                dialog.setTitle("注意");
                dialog.setMessage("确定清空历史记录？");
                dialog.setCancelable(true);
                dialog.setPositiveButton("确定",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog,int which){
                        clearRecord();
                        initRecords();
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                dialog.show();
                break;
            case R.id.button_delete_origin_text:
                editText.setText(null);
                recordLayout.setVisibility(View.GONE);
            default:
        }
    }
    private void clearRecord(){
        recordList.clear();
        recordRecyclerView.getAdapter().notifyDataSetChanged();
        DataSupport.deleteAll(Record.class);
    }
    public static void addRecord(String fromParam,String toParam,String word){
        if (TextUtils.isEmpty(word)){
            return ;
        }
        List<Record> recordList=DataSupport.findAll(Record.class);
        for (int i=0;i<recordList.size();i++){
            if(TextUtils.equals(recordList.get(i).getWord(),word)&&TextUtils.equals(recordList.get(i).getResult(),MainActivity.result)){
                DataSupport.deleteAll(Record.class,"word = ? and result = ?",word,MainActivity.result);
            }
        }
        Record record=new Record(word,R.drawable.ic_delete_record,fromParam,toParam,MainActivity.result);
        record.save();
    }

    /**
     * 初始化监听器。
     */
    private InitListener mInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            Log.d("MainActivity", "SpeechRecognizer init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                Toast.makeText(getContext(), "初始化失败，错误码：" + code+",请点击网址https://www.xfyun.cn/document/error-code查询解决方案", Toast.LENGTH_SHORT).show();
            }
        }
    };

    /**
     * 听写UI监听器
     */
    private RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener() {
        public void onResult(RecognizerResult results, boolean isLast) {
            printResult(results);
        }
        /**
         * 识别回调错误.
         */
        public void onError(SpeechError error) {
            Toast.makeText(getContext(), error.getPlainDescription(true), Toast.LENGTH_SHORT).show();
        }
    };
    private void printResult(RecognizerResult results) {
        String text = JsonParser.parseIatResult(results.getResultString());

        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mIatResults.put(sn, text);

        StringBuffer resultBuffer = new StringBuffer();
        for (String key : mIatResults.keySet()) {
            resultBuffer.append(mIatResults.get(key));
        }

        editText.setText(resultBuffer.toString());
        editText.setSelection(editText.length());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MainActivity.result=null;
    }

    @Override
    public void onPause() {
        super.onPause();
        MainActivity.result=null;
        recordLayout.setVisibility(View.GONE);
        editText.setText(null);
        origin=null;
    }

    @Override
    public void onResume() {
        super.onResume();
        initRecords();
    }

}


