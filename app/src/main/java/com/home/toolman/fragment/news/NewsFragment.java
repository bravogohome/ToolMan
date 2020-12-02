package com.home.toolman.fragment.news;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarLayout;

import com.haibin.calendarview.CalendarView;
import com.home.toolman.R;
import com.home.toolman.activity.MainActivity;
import com.home.toolman.vo.Record;

import org.litepal.crud.DataSupport;

import java.util.List;

public class NewsFragment extends Fragment implements  CalendarView.OnYearChangeListener{
    private CalendarView cv;
    private TextView recordNum,day,curLunar,curMonthDay,curYear;
    private FrameLayout toToday;
    private Calendar mCalendar;

    private int mYear;
    CalendarLayout mCalendarLayout;
    GroupRecyclerView mRecyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MainActivity.status="NewsFragment";
        View root = inflater.inflate(R.layout.fragment_news, container, false);
        cv=(CalendarView)root.findViewById(R.id.calendarView);
        day=(TextView)root.findViewById(R.id.tv_current_day);
        curMonthDay=(TextView)root.findViewById(R.id.tv_month_day);
        curLunar=(TextView)root.findViewById(R.id.tv_lunar);
        curYear=(TextView)root.findViewById(R.id.tv_year);
        day.setText(String.valueOf(cv.getCurDay()));
        recordNum=(TextView)root.findViewById(R.id.record_num);
        mCalendar=cv.getSelectedCalendar();
        initView();
        curMonthDay.setText(cv.getSelectedCalendar().getMonth()+"月"+cv.getSelectedCalendar().getDay()+"日");
        curYear.setText(String.valueOf(cv.getSelectedCalendar().getYear()));
        curLunar.setText(cv.getSelectedCalendar().getLunar());
        cv.setOnYearChangeListener(this);
        cv.setOnCalendarSelectListener(new CalendarView.OnCalendarSelectListener() {
            @Override
            public void onCalendarOutOfRange(Calendar calendar) {

            }

            @Override
            public void onCalendarSelect(Calendar calendar, boolean isClick) {
                mCalendar=calendar;
                initView();
            }

        });
        toToday=(FrameLayout) root.findViewById(R.id.fl_current);
        toToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cv.scrollToCurrent();
            }
        });

        //
        mRecyclerView = root.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new GroupItemDecoration<String, Article>());
        mRecyclerView.setAdapter(new ArticleAdapter(getContext()));
        mRecyclerView.notifyDataSetChanged();

        //
        mCalendarLayout = root.findViewById(R.id.calendarLayout);
        curMonthDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mCalendarLayout.isExpand()) {
                    mCalendarLayout.expand();
                    return;
                }
                cv.showYearSelectLayout(mYear);
                curLunar.setVisibility(View.GONE);
                curYear.setVisibility(View.GONE);
                recordNum.setVisibility(View.GONE);
                curMonthDay.setText(String.valueOf(mYear));
            }
        });

        return root;
    }

    private void initView(){
        curLunar.setVisibility(View.VISIBLE);
        curYear.setVisibility(View.VISIBLE);
        recordNum.setVisibility(View.VISIBLE);
        curMonthDay.setText(mCalendar.getMonth()+"月"+mCalendar.getDay()+"日");
        curYear.setText(String.valueOf(mCalendar.getYear()));
        curLunar.setText(mCalendar.getLunar());
        mCalendar=cv.getSelectedCalendar();
        curMonthDay.setText(mCalendar.getMonth()+"月"+mCalendar.getDay()+"日");
        curYear.setText(String.valueOf(mCalendar.getYear()));
        curLunar.setText(mCalendar.getLunar());
        String timeFormat="%"+mCalendar.getYear()+"年"+mCalendar.getMonth()+"月"+mCalendar.getDay()+"日"+"%";
        String timeFormat1="%"+mCalendar.getYear()+"年"+mCalendar.getMonth()+"月0"+mCalendar.getDay()+"日"+"%";
        String timeFormat2="%"+mCalendar.getYear()+"年0"+mCalendar.getMonth()+"月0"+mCalendar.getDay()+"日"+"%";
        String timeFormat3="%"+mCalendar.getYear()+"年0"+mCalendar.getMonth()+"月"+mCalendar.getDay()+"日"+"%";
        List<Record> recordList= DataSupport.where("addTime like ? or addTime like ? or addTime like ? or addTime like ? ",timeFormat,timeFormat1,timeFormat2,timeFormat3).find(Record.class);
        if (!recordList.isEmpty()){
            recordNum.setText("总搜索数："+recordList.size());
        }else{
            recordNum.setText("无搜索记录");
        }

        mYear = mCalendar.getYear();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    @Override
    public void onYearChange(int year) {
        curMonthDay.setText(String.valueOf(year));
    }

}