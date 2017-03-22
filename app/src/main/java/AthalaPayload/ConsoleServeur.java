/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AthalaPayload;

import AthalaData.User;

/**
 *
 * @author Mathieu
 */
public interface ConsoleServeur {
    public void Trace(String commentaire);
    public void AddClient(User u);
    public boolean AuthenticateUser(User u);
    public void CreateNewUser(User u);
    public User getUserInfo(String n);
    public boolean saveUser(User u);
}
