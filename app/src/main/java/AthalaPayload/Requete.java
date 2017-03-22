package AthalaPayload;

/**
 * Created by Mathieu on 12-03-17.
 */

import java.net.Socket;

/**
 *
 * @author Mathieu
 */
public interface Requete {
    public Runnable createRunnable(Socket s, ConsoleServeur cs);
}
