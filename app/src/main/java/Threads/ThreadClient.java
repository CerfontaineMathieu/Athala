package Threads;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;

import AthalaPayload.ReponseAndroid;
import AthalaPayload.RequeteAndroid;
import cerfontainecorps.athala.LoginActivity;

/**
 * Created by Mathieu on 12-03-17.
 */

public class ThreadClient extends Thread {
    private boolean finish = true;
    private String ip=null;
    private String port = null;
    private RequeteAndroid req=null;

    public void SetRequete(RequeteAndroid r)
    {
        req = r;
    }
    public RequeteAndroid GetRequete()
    {
        return req;
    }
    public ThreadClient()
    {

    }
    public void run()
    {
        int i = 0;
        while(finish)
        {
            //Search beacon server.
            try
            {
                System.out.println("Waiting server beacon.");
                MulticastSocket socket = new MulticastSocket(4446);
                InetAddress group = InetAddress.getByName("225.4.5.6");
                socket.joinGroup(group);
                DatagramPacket packet;
                byte[] buf = new byte[256];
                packet = new DatagramPacket(buf,buf.length);
                System.out.println("Waiting for a packet");
                socket.receive(packet);
                String received = new String(packet.getData());
                System.out.println("Server ip address : "+received);
                String[] ipAndPort = received.split("/");
                ip  = ipAndPort[0];
                port = ipAndPort[1];
                if(ip ==null & port == null){finish=false;}
                socket.leaveGroup(group);
            }catch(Exception e){System.out.println(e);}

            Socket CSocket = null;
            ObjectOutputStream oos = null;
            ObjectInputStream ois = null;

            if(ip!=null && port!=null) {

                try {
                    System.out.println("Création d'une socket : " + ip + "/" + port);
                    CSocket = new Socket(ip, Integer.parseInt(port));
                    oos = new ObjectOutputStream(CSocket.getOutputStream());
                    oos.flush();
                } catch (IOException e) {
                    System.out.println("I/O Exception :" + e);
                    finish = false;
                }
            }else{finish=false;}

            while(finish) {
                if(req!=null)
                {
                    try {
                        LoginActivity.retRep.setCode(ReponseAndroid.REPONSE_NOK);
                        System.out.println("Sending a login request.");
                        oos.writeObject(req);
                        oos.flush();
                        System.out.println("Waiting login response...");
                        ois = new ObjectInputStream(CSocket.getInputStream());
                        ReponseAndroid rep = (ReponseAndroid) ois.readObject();
                        if(rep.getCode()== ReponseAndroid.REPONSE_OK){
                            System.out.println("Request succeed");
                            LoginActivity.retRep=rep;
                        }else{System.out.println("Request failed");}
                    } catch (IOException e) {
                        System.out.println("An error occured during the sending.");
                        finish=false;
                    } catch (ClassNotFoundException e) {

                        System.out.println("Class ReponseAndroid not found.");
                        finish=false;
                    }
                    finally {
                        req = null;
                    }
                }
            }
            try {
                if(CSocket !=null) {
                    oos.close();
                    ois.close();
                    CSocket.close();
                }
            }catch(IOException ex)
            {
                System.out.println("An error occured during the closing of the socket.");
            }
        }
    }
}
