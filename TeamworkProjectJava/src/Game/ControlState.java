package Game;

import javafx.scene.input.KeyCode;
import java.util.HashSet;
import java.util.Set;

public class ControlState {
    double mouseX;
    double mouseY;
    boolean mouseLeft;
    boolean mouseRight;

    Set<KeyCode> keyState;

    public ControlState() {
        mouseX = 0.0;
        mouseY = 0.0;
        mouseLeft = false;
        mouseRight = false;

        keyState = new HashSet<>();
    }

    public void addKey(KeyCode kc) {
        keyState.add(kc);
    }
    public void removeKey(KeyCode kc) {
        keyState.remove(kc);
    }

    public Set<KeyCode> getCombo() {
        return keyState;
    }

    //Shorthand for getting just pressed keys
    public boolean pressed(KeyCode kc) {
        if (keyState.contains(kc)) return true;
        return false;
    }

    public double getMouseX() {
        return mouseX;
    }
    public double getMouseY() {
        return mouseY;
    }
    public double[] getMouse() {
        double[] result = { mouseX, mouseY };
        return result;
    }

    public void update(double x, double y) {
        mouseX = x;
        mouseY = y;
    }

}
