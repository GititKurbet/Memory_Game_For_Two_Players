package gamePackage;

import java.io.Serializable;

public class LogIn implements Serializable {
    String name;
    boolean isNew;

    public LogIn(String name, boolean isNew){
        this.name = name;
        this.isNew = isNew;
    }
}
