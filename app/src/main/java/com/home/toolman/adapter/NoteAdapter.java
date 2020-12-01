package com.home.toolman.adapter;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.home.toolman.R;
import com.home.toolman.activity.TranslateResultActivity;
import com.home.toolman.fragment.note.RecycleItemTouchHelper;
import com.home.toolman.vo.WordsBook;

import org.litepal.crud.DataSupport;

import java.util.List;

import static org.litepal.LitePalApplication.getContext;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {
    private List<WordsBook> note;
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView originText,resultText;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            originText=(TextView)itemView.findViewById(R.id.note_text_origin);
            resultText=(TextView)itemView.findViewById(R.id.note_text_result);
        }
    }
    public NoteAdapter(List<WordsBook> words){
        this.note=words;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note_layout,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WordsBook word=note.get(position);
        holder.originText.setText(word.getOrigin());
        holder.resultText.setText(word.getResult());
    }

    @Override
    public int getItemCount() {
        return note.size();
    }

}
