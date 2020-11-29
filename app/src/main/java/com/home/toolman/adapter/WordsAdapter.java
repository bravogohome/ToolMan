package com.home.toolman.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.home.toolman.R;
import com.home.toolman.activity.TranslateResultActivity;
import com.home.toolman.vo.WordsBook;


import org.litepal.crud.DataSupport;

import java.util.List;

import static org.litepal.LitePalApplication.getContext;

public class WordsAdapter extends RecyclerView.Adapter<WordsAdapter.ViewHolder> {
    private List<WordsBook> wordsBooks;
    private WordsBook tempWord;
    static class ViewHolder extends RecyclerView.ViewHolder{
        View bookView;
        ImageView mImage;
        TextView textOrigin,textResult,textLibrary;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bookView=itemView;
            mImage=(ImageView)itemView.findViewById(R.id.favorite_button);
            textOrigin=(TextView)itemView.findViewById(R.id.text_item_origin);
            textResult=(TextView)itemView.findViewById(R.id.text_item_result);
            textLibrary=(TextView)itemView.findViewById(R.id.text_item_library);
        }
    }

    public WordsAdapter(List<WordsBook> books){
        wordsBooks=books;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_words_book,parent,false);
        final ViewHolder holder=new ViewHolder(view);
        holder.bookView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                WordsBook wordsBook=wordsBooks.get(position);
                Intent intent=new Intent(getContext(), TranslateResultActivity.class);
                intent.putExtra("fromParam",wordsBook.getFrom());
                intent.putExtra("toParam",wordsBook.getTo());
                intent.putExtra("origin",wordsBook.getOrigin());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
            }
        });
        holder.mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                tempWord=wordsBooks.get(position);
                if(holder.mImage.getKeepScreenOn()){
                    DataSupport.deleteAll(WordsBook.class,"from=? and to=? and origin=?",tempWord.getFrom(),tempWord.getTo(),tempWord.getOrigin());
                    holder.mImage.setImageResource(R.drawable.ic_favorite_word_no);
                    holder.mImage.setKeepScreenOn(false);
                    Toast.makeText(getContext(), "取消收藏:"+tempWord.getOrigin(), Toast.LENGTH_SHORT).show();
                }else{
                    WordsBook wordsBookRe=new WordsBook(tempWord.getFrom(),tempWord.getTo(),tempWord.getOrigin(),tempWord.getResult(),tempWord.getLibrary(),R.drawable.ic_favorite_word_yes);
                    wordsBookRe.save();
                    holder.mImage.setImageResource(R.drawable.ic_favorite_word_yes);
                    holder.mImage.setKeepScreenOn(true);
                    Toast.makeText(getContext(), "收藏:"+wordsBookRe.getOrigin(), Toast.LENGTH_SHORT).show();
                }

            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WordsBook wordsBook=wordsBooks.get(position);
        holder.mImage.setImageResource(wordsBook.getImageID());
        holder.textOrigin.setText(wordsBook.getOrigin());
        holder.textResult.setText(wordsBook.getResult());
        holder.textLibrary.setText(wordsBook.getLibrary());
    }

    @Override
    public int getItemCount() {
        return wordsBooks.size();
    }
}
