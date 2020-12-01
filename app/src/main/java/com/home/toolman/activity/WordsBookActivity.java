package com.home.toolman.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.home.toolman.R;
import com.home.toolman.adapter.WordsAdapter;
import com.home.toolman.vo.WordsBook;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import static org.litepal.LitePalApplication.getContext;

public class WordsBookActivity extends AppCompatActivity implements View.OnClickListener{
    private RecyclerView bookRecyclerView;
    private List<WordsBook> bookList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_words_book);
        bookRecyclerView=findViewById(R.id.words_book);
        initWordsBook();
    }


    @Override
    public void onClick(View v) {

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