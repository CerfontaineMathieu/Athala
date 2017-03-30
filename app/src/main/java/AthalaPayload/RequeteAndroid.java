package AthalaPayload;

import AthalaData.User;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

/**
 * Created by Mathieu on 12-03-17.
 */

public class RequeteAndroid implements Requete,Serializable {

    public static int REQUEST_LOGIN = 1;
    public static int REQUEST_NEW_USER = 2;
    public static int REQUEST_GET_CHARAC= 3;
    public static int REQUEST_NEW_CHARAC= 4;
    public static int REQUEST_BEACON = 5;
    public static int REQUEST_DELETE_CHARAC= 6;
    private int type;
    private User u;
    public RequeteAndroid(int t,String n,String p)
    {
        type=t;
        u = new User(n,p);
    }
    public RequeteAndroid(int t,User ut)
    {
        type = t;
        u=ut;
    }

    public int getType() {
        return type;
    }

    public User getU() {
        return u;
    }


    @Override
    public Runnable createRunnable(final Socket s, final ConsoleServeur cs) {
        if(type==REQUEST_LOGIN)
        {
            return new Runnable()
            {
                public void run()
                {
                    doLogin(s,cs);
                }
            };
        }
        else if(type==REQUEST_NEW_USER){
            return new Runnable()
            {
                public void run()
                {
                    doNewUser(s,cs);
                }
            };
        }
        else if(type==REQUEST_GET_CHARAC){
            return new Runnable()
            {
                public void run()
                {
                    doGetCharac(s,cs);
                }
            };
        }
        else if(type == REQUEST_NEW_CHARAC || type == REQUEST_DELETE_CHARAC){
            return new Runnable() {

                public void run() {
                    doSaveUser(s,cs);
                }
            };
        }
        else{return null;}
    }

    private void doSaveUser(Socket s,ConsoleServeur cs){
        cs.Trace("Sauvegarde des modifications utilisateur : "+ u);
        boolean result = cs.saveUser(u);
        ReponseAndroid rep = null;
        if(result){ rep = new ReponseAndroid(ReponseAndroid.REPONSE_OK);}
        else{rep = new ReponseAndroid(ReponseAndroid.REPONSE_NOK);}
        try
        {
            ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
            oos.writeObject(rep);
            oos.flush();
        } catch (IOException e) {
            cs.Trace("Erreur lors de l'envoi d'une réponse au client.");
        }
    }
    private void doGetCharac(Socket s,ConsoleServeur cs)
    {
        cs.Trace("Récuperation des personnages de l'utilisateur "+u);
        u = cs.getUserInfo(u.getUsername());
        ReponseAndroid rep = new ReponseAndroid(ReponseAndroid.REPONSE_OK,u);
        try
        {
            ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
            oos.writeObject(rep);
            oos.flush();
        } catch (IOException e) {
            cs.Trace("Erreur lors de l'envoi d'une réponse au client.");
        }
    }
    private void doNewUser(Socket s,ConsoleServeur cs)
    {
        cs.Trace("Creation d'un nouvel utilisateur : "+u);
        cs.CreateNewUser(u);
        ReponseAndroid rep = new ReponseAndroid(ReponseAndroid.REPONSE_OK);
        try
        {
            ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
            oos.writeObject(rep);
            oos.flush();
        } catch (IOException e) {
            cs.Trace("Erreur lors de l'envoi d'une réponse au client.");
        }
    }
    private void doLogin(Socket s,ConsoleServeur cs)
    {
        String addr=s.getRemoteSocketAddress().toString();
        // Add user in the connected users list if the user is authenticated successfully.
        ReponseAndroid rep =null;
        if(cs.AuthenticateUser(u))
        {
            cs.AddClient(u);
            rep = new ReponseAndroid(ReponseAndroid.REPONSE_OK);
            cs.Trace("Le client "+u+" s'est correctement connecté au serveur.");
        }
        else{
            rep = new ReponseAndroid(ReponseAndroid.REPONSE_NOK);
            cs.Trace("Echec d'authentification du client "+ addr + " avec comme login "+u);
        }

        try
        {
            ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
            oos.writeObject(rep);
            oos.flush();
        } catch (IOException e) {
            cs.Trace("Erreur lors de l'envoi d'une réponse au client.");
        }
    }
}
