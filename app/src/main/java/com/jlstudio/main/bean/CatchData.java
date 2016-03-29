package com.jlstudio.main.bean;

/**
 * Created by gzw on 2015/10/25.
 */
public class CatchData {
    private String url;
    private String content;
    private String time;
    private boolean isExist;

    public CatchData(String url, String content, String time, boolean isExist) {
        this.url = url;
        this.content = content;
        this.time = time;
        this.isExist = isExist;
    }

    public CatchData(boolean isExist) {
        this.isExist = isExist;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isExist() {
        return isExist;
    }

    public void setIsExist(boolean isExist) {
        this.isExist = isExist;
    }
}
