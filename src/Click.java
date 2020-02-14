import javax.swing.*;
import java.io.Serializable;

public class Click implements Serializable {
    int imageIndex;
    int buttonIndex;

    public Click(int imageIndex, int buttonIndex) {
        this.imageIndex = imageIndex;
        this.buttonIndex = buttonIndex;
    }
}
