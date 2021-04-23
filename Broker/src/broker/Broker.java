/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package broker;

import Messenger.Message;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

/**
 *
 * @author Tammaa
 */
public class Broker {

    /**
     * @param args the command line arguments
     */
    static HashSet<Integer> usersList;
            static HashSet<Integer> onlineUsers;
            static HashMap<Integer, Vector<Message>> pendedMessages;
            
    public static void init(){
       usersList = new HashSet<>();
       onlineUsers = new HashSet<>();
       pendedMessages = new HashMap<>();
    }
    public static void main(String[] args) {
    try {
            BrokerGUI bg = new BrokerGUI();
            bg.show();
           ServerSocket ss = new ServerSocket(1030);
            init();//initialize the values
            Thread statues = new Thread(new Statues());
            statues.start();
            while(true){
                Socket s = ss.accept();
                Thread client = new Thread(new MultiThreadServer(s));
                client.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    
}
