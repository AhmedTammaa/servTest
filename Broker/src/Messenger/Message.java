package Messenger;

import java.io.Serializable;

public class Message implements Serializable {
    Integer src,dest;
    String message;
    public Message(Integer src, Integer dest, String message){
        this.src = src;
        this.dest = dest;
        this.message = message;
    }
    public Message(Integer dest, String message){
        this.dest = dest;
        this.message = message;
    }
    public Integer getSrc(){
        return src;
    }
    public Integer getDest(){
        return dest;
    }
    public String getMessage(){
        return message;
    }
    public void setSrc(Integer src){
        this.src = src;
    }
    public String toString(){
        return "From: "+ src +" "+ message;
    }
}
