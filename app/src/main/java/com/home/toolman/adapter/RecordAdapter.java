package com.home.toolman.adapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.home.toolman.R;
import com.home.toolman.activity.TranslateResultActivity;
import com.home.toolman.fragment.search.SearchFragment;
import com.home.toolman.vo.Record;

import org.litepal.crud.DataSupport;

import java.util.List;

import static org.litepal.LitePalApplication.getContext;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder> {
    private List<Record> recordList;
    static class ViewHolder extends RecyclerView.ViewHolder{
        View recordView;
        ImageView mImage;
        TextView recordText,recordResult;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            recordView=itemView;
            mImage=(ImageView)itemView.findViewById(R.id.item_record_image);
            recordText=(TextView)itemView.findViewById(R.id.item_record_text);
            recordResult=(TextView)itemView.findViewById(R.id.item_record_result_text);
        }
    }

    public RecordAdapter(List<Record> records){
        recordList=records;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.record_item_layout,parent,false);
        final ViewHolder holder=new ViewHolder(view);
        holder.mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                Record record=recordList.get(position);
                recordList.remove(position);
                notifyItemRemoved(position);
                notifyItemChanged(0,recordList.size());
                DataSupport.deleteAll(Record.class,"word = ? and result = ?",record.getWord(),record.getResult());
            }
        });
        holder.recordView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                Record record=recordList.get(position);
                Record newRecord=new Record(record.getWord(),R.drawable.ic_delete_record,record.getFromParam(),record.getToParam(),record.getResult(),SearchFragment.getRecordTime()+";"+ record.getAddTime());
                Intent intent=new Intent(getContext(), TranslateResultActivity.class);
                intent.putExtra("fromParam",record.getFromParam());
                intent.putExtra("toParam",record.getToParam());
                intent.putExtra("origin",record.getWord());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
                recordList.remove(position);
                DataSupport.deleteAll(Record.class,"word = ? and result=?",record.getWord(),record.getResult());
                newRecord.setCount(record.getCount()+1);
                recordList.add(newRecord);
                newRecord.save();
                Log.d("TAG", "addRecord: "+newRecord.getAddTime());
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Record record=recordList.get(position);
        holder.mImage.setImageResource(record.getImageID());
        holder.recordText.setText(record.getWord());
        holder.recordResult.setText(record.getResult());
    }

    @Override
    public int getItemCount() {
        return recordList.size();
    }
}
