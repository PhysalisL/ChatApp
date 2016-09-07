package model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Yixiu Liu on 8/31/2016.
 */
public class Message implements Serializable{
    private String content;
    private Date date;
    private String senderName;

    public Message(String content){
        this("", content, new Date());
    }

    public Message(String senderName, String content){
        this(senderName, content, new Date());
    }

    public Message(String senderName, String content, Date date){
        this.senderName = senderName;
        this.content = content;
        this.date = date;
    }

    public void setID(String id){
        senderName = id;
    }

    public String getContent(){
        return content;
    }

    public Date getDate() {
        return date;
    }

    public String getSenderName(){
        return senderName;
    }

    public String toString(){
        return String.format("%s: %s", senderName, content);
    }
}