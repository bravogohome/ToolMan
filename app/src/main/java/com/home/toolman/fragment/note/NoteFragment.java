package com.home.toolman.fragment.note;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.home.toolman.R;
import com.home.toolman.activity.MainActivity;
import com.home.toolman.activity.TranslateResultActivity;
import com.home.toolman.adapter.NoteAdapter;
import com.home.toolman.vo.WordsBook;

import org.litepal.crud.DataSupport;

import java.util.List;

import static org.litepal.LitePalApplication.getContext;

public class NoteFragment extends Fragment implements RecycleItemTouchHelper.ItemTouchHelperCallback{
    private RecyclerView noteView;
    List<WordsBook> note;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MainActivity.status="NoteFragment";
        View root = inflater.inflate(R.layout.fragment_note, container, false);

        noteView=(RecyclerView)root.findViewById(R.id.note_recycle_view);
        initNote();

        return root;
    }
    private void initNote(){
        note= DataSupport.findAll(WordsBook.class);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        noteView.setLayoutManager(layoutManager);
        NoteAdapter noteAdapter=new NoteAdapter(note);
        ItemTouchHelper.Callback callback=new RecycleItemTouchHelper(this);
        ItemTouchHelper itemTouchHelper=new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(noteView);
        noteView.setAdapter(noteAdapter);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        initNote();
        super.onResume();
    }

    @Override
    public void onItemDelete(final int position) {
        AlertDialog.Builder dialog=new AlertDialog.Builder(getContext());
        dialog.setTitle("!");
        dialog.setMessage("是否删除该单词？");
        dialog.setCancelable(true);
        dialog.setPositiveButton("确定",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog,int which){
                WordsBook word=note.get(position);
                note.remove(position);
                noteView.getAdapter().notifyDataSetChanged();
                DataSupport.deleteAll(WordsBook.class,"origin=? and result=? ",word.getOrigin(),word.getResult());
                //initNote();
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                initNote();
            }
        });
        dialog.show();
    }

    @Override
    public void onItemQuery(int position) {
        WordsBook word=note.get(position);
        Intent intent=new Intent(getContext(), TranslateResultActivity.class);
        intent.putExtra("fromParam",word.getFrom());
        intent.putExtra("toParam",word.getTo());
        intent.putExtra("origin",word.getOrigin());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getContext().startActivity(intent);
    }


}
