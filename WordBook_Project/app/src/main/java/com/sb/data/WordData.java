package com.sb.data;

import com.sb.wordbook.Config;

public class WordData {

    private int seq;
    private String text;
    private String date;

    public WordData(int seq, String text, String date) {

        this.seq = seq;
        this.text = text;
        this.date = date;
    }

    public WordData() {

    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getSeq() {
        return seq;
    }

    public String getText() {
        return text;
    }

    public String getDate() {
        return date;
    }

    public String getImageFile() {
        return date + Config.IMAGE_FILE_EXT;
    }

    public String getAudioFile() {
        return date + Config.AUDIO_FILE_EXT;
    }
}
