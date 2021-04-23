/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package broker;

import Messenger.Message;
import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;

/**
 *
 * @author Tammaa
 */
public class Statues implements Runnable {

    @Override
    public void run() {
        DefaultListModel<String> onlineUsers = new DefaultListModel<>();
        DefaultListModel<String> users = new DefaultListModel<>();
        DefaultListModel<String> pendedMsgs = new DefaultListModel<>();
        BrokerGUI.onlineList.setModel(onlineUsers);
        BrokerGUI.userList.setModel(users);
        BrokerGUI.listUndelievered.setModel(pendedMsgs);
        while (true) {
            try {
                sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(Statues.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (onlineUsers.size() != Broker.onlineUsers.size()) {
                Object[] x = Broker.onlineUsers.toArray();
                onlineUsers.clear();
                for (int i = 0; i < Broker.onlineUsers.size(); i++) {
                    onlineUsers.addElement(x[i].toString());
                }
            }
            if (users.size() != Broker.usersList.size()) {
                users.clear();
                Object[] x = Broker.usersList.toArray();
                for (int i = 0; i < Broker.usersList.size(); i++) {
                    users.addElement(x[i].toString());
                }
            }

            pendedMsgs.clear();
            Broker.pendedMessages.entrySet().forEach((entry) -> {
                //  pendedMsgs.addElement("Client with ID " + entry.getKey() + "has the following messages");
                pendedMsgs.addElement("Client with ID " + entry.getKey() +" has the follwing messages" );
                for (Message i : entry.getValue()) {
                    
                    pendedMsgs.addElement(i.toString());
                }
            });

        }

    }

}
