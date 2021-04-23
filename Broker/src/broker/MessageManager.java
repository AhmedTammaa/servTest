/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package broker;

import Messenger.Message;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tammaa
 */
public class MessageManager {
    ObjectInputStream in;
    ObjectOutputStream out;
    final Integer ServerID = 1030;
    Integer clientID;
    boolean keepGoing;
    MessageManager(Socket c){
        initilizeServer(c);
    }
    public Integer readQuery(){
        try {
            return (Integer)in.readObject();
        } catch (IOException |ClassNotFoundException ex) {
            Logger.getLogger(MessageManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }
    private void initilizeServer(Socket c){
        try {
            in = new ObjectInputStream(c.getInputStream());
            out = new ObjectOutputStream(c.getOutputStream());
            clientID = (Integer) in.readObject();
            keepGoing = true;
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(MessageManager.class.getName()).log(Level.SEVERE, null, ex);
            keepGoing = false;
            removeOnlineUser();
        }
    }
    public boolean alive(){
        return keepGoing;
    }
    public void sendPendedMessages() {
        try {
            out.writeInt(Broker.pendedMessages.get(clientID).size());
            for (Message i : Broker.pendedMessages.get(clientID)) {
                out.writeObject(i);
            }
            Broker.pendedMessages.remove(clientID);
        } catch (IOException ex) {
            Logger.getLogger(MultiThreadServer.class.getName()).log(Level.SEVERE, null, ex);
            keepGoing = false;
            removeOnlineUser();
        }

    }

    public void addPendedMessage(Message m) {
        if(m==null)return;
        Vector<Message> msgs = Broker.pendedMessages.get(m.getDest());
        if (msgs != null) {
            msgs.addElement(m);
        } else {
            msgs = new Vector<Message>();
            msgs.add(m);
        }
        Broker.pendedMessages.put(m.getDest(), msgs);
    }
    
    public void sendOnlineMessage(Message m){
        try {
            if(m==null)return;
            Socket socket1 = new Socket("localhost", ServerID + m.getDest());
            ObjectOutputStream cout1 = new ObjectOutputStream(socket1.getOutputStream());
            cout1.writeObject(m);
        } catch (IOException ex) {
            Logger.getLogger(MultiThreadServer.class.getName()).log(Level.SEVERE, null, ex);
            keepGoing = false;
            removeOnlineUser();
        }
    }
    void connectBackToClient(){
        try {
            Socket socket = new Socket("localhost", ServerID + clientID);
        } catch (IOException ex) {
            Logger.getLogger(MultiThreadServer.class.getName()).log(Level.SEVERE, null, ex);
            keepGoing = false;
            removeOnlineUser();
        }
                
    }
    
    public void addOnlineUser(){
        Broker.onlineUsers.add(clientID);
    }
    
    public void removeOnlineUser(){
        Broker.onlineUsers.remove(clientID);
    }
    public boolean isOnline(){
        return Broker.onlineUsers.contains(clientID);
    }
    public boolean hasPendedMessages(){
        try {
            
            boolean hasMessages = Broker.pendedMessages.containsKey(clientID);
            out.writeObject(hasMessages);
            return hasMessages;
        } catch (IOException ex) {
            Logger.getLogger(MessageManager.class.getName()).log(Level.SEVERE, null, ex);
            keepGoing = false;
            removeOnlineUser();
        }
        return false;
    }
    
    public boolean isRecieverOffline(Message m){
        if(m==null)return true;
        return !Broker.onlineUsers.contains(m.getDest());
    }
    
    public void readMessage(){
        try {
            Message m;
            m = (Message) in.readObject();
            if(m==null){
                removeOnlineUser();
                return;
            }
            Broker.usersList.add(m.getSrc());
            Broker.usersList.add(m.getDest());
            addPendedMessage(m);
            
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(MultiThreadServer.class.getName()).log(Level.SEVERE, null, ex);
            keepGoing = false;
        }
       
    }
    
    
}
