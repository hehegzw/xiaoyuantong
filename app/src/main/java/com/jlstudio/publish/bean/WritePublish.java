package com.jlstudio.publish.bean;

/**保存用户写的通知，发送时用
 * Created by gzw on 2015/11/27.
 */
public class WritePublish {
    private String title;
    private String content;
    private String filePath;
    private String type;

    public WritePublish() {
    }

    public WritePublish(String title, String content, String filePath) {
        this.title = title;
        this.content = content;
        this.filePath = filePath;
    }

    public WritePublish(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
