package AthalaPayload;

import java.io.Serializable;

import AthalaData.User;

/**
 * Created by Mathieu on 12-03-17.
 */

public class ReponseAndroid implements Reponse,Serializable {
    public static int REPONSE_OK = 1;
    public static int REPONSE_NOK = 2;
    private int code;
    private User MainUser;
    public ReponseAndroid(int i)
    {
        setCode(i);
    }
    public ReponseAndroid(int i, User u)
    {
        setCode(i);
        setMainUser(u);
    }
    public void setCode(int i)
    {
        code = i;
    }
    @Override
    public int getCode() {
        return code;
    }

    public User getMainUser() {
        return MainUser;
    }

    public void setMainUser(User mainUser) {
        MainUser = mainUser;
    }
}
