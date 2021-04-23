/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package broker;

import Messenger.Message;
import java.net.Socket;

/**
 *
 * @author Tammaa
 */
public class MultiThreadServer implements Runnable {

    Socket c;

    MultiThreadServer(Socket s) {
        c = s;
    }

    @Override
    public void run() {
        MessageManager manager = new MessageManager(c);
        System.out.println("Client Connected");
        manager.connectBackToClient();
        manager.addOnlineUser();
        boolean online = true;
        while(online){
            //querey reader
            
            int query = manager.readQuery();
            switch(query){
                case 1:
                    manager.readMessage();
                    break;
                case 2:
                    manager.hasPendedMessages();
                    break;
                case 3:
                    manager.sendPendedMessages();
                    break;
                case -1:
                case 4:
                    online = false;
                    break;
            }
            
        }
        manager.removeOnlineUser();
        //console version
       /* while (true) {

            if (!manager.isOnline() && !manager.alive()) {
                manager.removeOnlineUser(); break;
            }
            if (manager.hasPendedMessages()) {
                manager.sendPendedMessages();
            }
            if (!manager.isOnline() && !manager.alive()) {
                break;
            }
            Message m = manager.readMessage();
            if (!manager.isOnline() && !manager.alive()) {
                break;
            }

            manager.addPendedMessage(m);

        }*/
       

    }
}
