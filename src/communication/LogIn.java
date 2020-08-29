package communication;

import java.io.ObjectOutputStream;
import java.io.Serializable;

public class LogIn implements Serializable {
    public String name;
    public char[] password;
    public boolean isNew;

    public LogIn(String name, char[] password, boolean isNew){
        this.name = name;
        this.password = password;
        this.isNew = isNew;
    }

}
