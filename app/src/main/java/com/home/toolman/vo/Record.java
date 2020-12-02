package com.home.toolman.vo;

import org.litepal.crud.DataSupport;

public class Record extends DataSupport {
    private String word;
    private int imageID;
    private String fromParam;
    private String toParam;
    private String result;
    private String addTime;
    private int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Record(String word, int imageID, String fromParam, String toParam, String result, String addTime){
        this.word=word;
        this.imageID=imageID;
        this.fromParam=fromParam;
        this.toParam=toParam;
        this.result=result;
        this.addTime=addTime;
        count=1;
    }
    public String getFromParam() {
        return fromParam;
    }

    public void setFromParam(String fromParam) {
        this.fromParam = fromParam;
    }

    public String getToParam() {
        return toParam;
    }

    public void setToParam(String toParam) {
        this.toParam = toParam;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }


    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getWord() {
        return word;
    }

    public int getImageID() {
        return imageID;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }
}
