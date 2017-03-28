package Threads;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import AthalaData.User;
import AthalaPayload.RequeteAndroid;

/**
 * Created by Mathieu on 28-03-17.
 */

public class ThreadClientTimeout extends Thread {
    private String ServerIPAddr;
    private final int port = 4462;
    private boolean finish=true;
    private User currentU;
    public ThreadClientTimeout(String ip, User u)
    {
        ServerIPAddr=ip;
        currentU=u;
    }
    public void run()
    {
        while(finish)
        {
            try {
                Thread.sleep(2000);
                Socket cliSock = new Socket(ServerIPAddr,port);
                ObjectOutputStream oos = new ObjectOutputStream(cliSock.getOutputStream());
                RequeteAndroid req = new RequeteAndroid(RequeteAndroid.REQUEST_BEACON,currentU);
                oos.writeObject(req);
            } catch (IOException e) {
                finish=false;
                System.out.println("An error occurred : I/O Exception ["+e+"]");
            } catch (InterruptedException e) {
                finish=false;
                System.out.println("An error occurred : Interrupted exception ["+e+"]");
            }
        }
    }
}
