/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AthalaData;

import java.io.Serializable;
import java.util.LinkedList;

/**
 *
 * @author Mathieu
 */
public class User implements Serializable{
    private String username;
    private String password;
    private LinkedList listOfCharac;

    public User(String u,String p)
    {
        username=u;
        password=p;
        listOfCharac = new LinkedList();
    }

    public void addCharacter(Character c)
    {
        listOfCharac.add(c);
    }
    public Character getCharacter(String name)
    {
        for(int i = 0; i<listOfCharac.size();i++)
        {
            Character c = (Character)listOfCharac.get(i);
            if(c.getName().equals(name)){return c;}
        }
        return null;
    }

    public LinkedList getListOfCharac() {
        return listOfCharac;
    }

    public void setListOfCharac(LinkedList listOfCharac) {
        this.listOfCharac = listOfCharac;
    }

    @Override
    public String toString() {
        return username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
