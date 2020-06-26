package gamePackage;

import java.io.ObjectOutputStream;

public interface SentFromUser {
    void passObject(UsersManager manager, ObjectOutputStream[] outputStreams, int playerNum);
    void receive(GameGUI game);
}
