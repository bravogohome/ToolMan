package com.home.toolman.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.home.toolman.BaseActivity;
import com.home.toolman.R;
import com.home.toolman.adapter.WordsAdapter;
import com.home.toolman.vo.WordsBook;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import static org.litepal.LitePalApplication.getContext;

public class WordsBookActivity extends BaseActivity implements View.OnClickListener{
    private RecyclerView bookRecyclerView;
    private List<WordsBook> bookList=new ArrayList<>();
    private EditText editTextSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_words_book);
        bookRecyclerView=findViewById(R.id.words_book);
        initWordsBook();
        ImageView imageViewSearch=(ImageView)findViewById(R.id.button_search_selector_words);
        ImageView deleteEditText=(ImageView)findViewById(R.id.button_delete_edit_text);
        editTextSearch=(EditText)findViewById(R.id.edit_selector_words);
        deleteEditText.setOnClickListener(this);
        imageViewSearch.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.button_search_selector_words:
                String searchWord=editTextSearch.getText().toString();
                if (TextUtils.isEmpty(searchWord)){
                    Toast.makeText(this, "请输入要查询的单词", Toast.LENGTH_SHORT).show();
                    break;
                }
                bookList=DataSupport.where("origin like ?","%"+searchWord+"%").find(WordsBook.class);
                WordsAdapter adapter=new WordsAdapter(bookList);
                bookRecyclerView.setAdapter(adapter);
                break;
            case R.id.button_delete_edit_text:
                editTextSearch.setText(null);
                initWordsBook();
                break;
            default:
        }
    }

    private void initWordsBook(){
        bookList= DataSupport.findAll(WordsBook.class);
        if(!bookList.isEmpty()){
            LinearLayoutManager recordItemLayoutManager = new LinearLayoutManager(getContext());
            bookRecyclerView.setLayoutManager(recordItemLayoutManager);
            WordsAdapter adapter = new WordsAdapter(bookList);
            bookRecyclerView.setAdapter(adapter);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initWordsBook();
    }
}