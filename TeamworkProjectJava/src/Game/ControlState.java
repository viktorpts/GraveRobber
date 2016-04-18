package Game;

import javafx.scene.input.KeyCode;
import java.util.HashSet;
import java.util.Set;

/**
 * This object keeps track of pressed keys and mouse buttons, as well as the position of the cursor, in screen
 * coordinates.
 */
public class ControlState {
    private double mouseX;
    private double mouseY;
    private boolean mouseLeft;
    private boolean mouseRight;

    private Set<KeyCode> keyState;

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

    public void setMouseLeft(boolean mouseLeft) {
        this.mouseLeft = mouseLeft;
    }

    public void setMouseRight(boolean mouseRight) {
        this.mouseRight = mouseRight;
    }

    public boolean isMouseLeft() {
        return mouseLeft;
    }

    public boolean isMouseRight() {
        return mouseRight;
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
