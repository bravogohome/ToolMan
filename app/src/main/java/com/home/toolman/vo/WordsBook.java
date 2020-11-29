package com.home.toolman.vo;

import org.litepal.crud.DataSupport;

public class WordsBook extends DataSupport {
    private String from;
    private String to;
    private String origin;
    private String result;
    private String library;
    private int imageID;
    public WordsBook(String from,String to,String origin,String result,String library,int imageID){
        this.from=from;
        this.to=to;
        this.origin=origin;
        this.result=result;
        this.library=library;
        this.imageID=imageID;
    }

    public int getImageID() {
        return imageID;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getLibrary() {
        return library;
    }

    public void setLibrary(String library) {
        this.library = library;
    }
}
