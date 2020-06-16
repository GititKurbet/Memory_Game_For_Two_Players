package gamePackage;

import java.io.ObjectOutputStream;

public interface ServerConnections {
    void respond(ObjectOutputStream out, UsersManager manager) throws IllegalArgumentException;

}
