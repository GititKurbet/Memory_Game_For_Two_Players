package gamePackage;

import java.io.ObjectOutputStream;
import java.io.Serializable;

public class LogIn implements Serializable, ServerConnections{
    String name;
    char[] password;
    boolean isNew;

    public LogIn(String name, char[] password, boolean isNew){
        this.name = name;
        this.password = password;
        this.isNew = isNew;
    }

    @Override
    public void respond(ObjectOutputStream out, UsersManager manager) throws IllegalArgumentException {

    }
}
