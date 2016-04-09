package Renderer;

import Game.Main;
import Models.Enemy;
import javafx.geometry.VPos;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

public class DebugView {
    public static void showPlayerInfo() {
        String playerData = String.format("Position %.2f, %.2f%n", Main.game.getLevel().getPlayer().getX(), Main.game.getLevel().getPlayer().getY());
        playerData += String.format("Velocity %.2f%n", Main.game.getLevel().getPlayer().getVelocity().getMagnitude());
        playerData += String.format("%.2f %.2f", Main.game.getLevel().getPlayer().getVelocity().getX(), Main.game.getLevel().getPlayer().getVelocity().getY());
        // Control state

        Main.debugc.fillText(playerData, 5, 15);
    }

    public static void showControlInfo() {
        String controlData = String.format("Mouse [%s|%s]%n", Main.game.getControlState().isMouseLeft() ? "#" : "_", Main.game.getControlState().isMouseRight() ? "#" : "_");
        controlData += String.format("Keyboard: ");
        for (KeyCode key : Main.game.getControlState().getCombo()) {
            controlData += String.format("%s ", key.toString());
        }
        Main.debugc.fillText(controlData, 120, 15);
    }

    public static void showEntityData() {
        final String[] entityData = { String.format("Entity Data:%n") };
        Main.game.getLevel().getEntities().stream().forEach(entity -> {
            entityData[0] += String.format("%3s: %s - %s %s%n",
                    entity.getID(),
                    entity.getClass().toString().replaceFirst("class Models.", ""),
                    entity.getState().toString(),
                    entity instanceof Enemy ? ((Enemy)entity).getThought(0).toString() : entity.getAnimationState().toString());
                });
        Main.debugc.fillText(entityData[0], 5, 70);
    }

    public static void showInfo() {
        Main.debugc.setTextBaseline(VPos.BOTTOM);
        Main.debugc.fillText(Main.debugInfo, 5, Main.verticalRes - 5);
        Main.debugInfo= "";
        Main.debugc.setTextBaseline(VPos.BASELINE);
    }

    public static void clear() {
        Main.debugc.setFill(Color.BLACK);
        Main.debugc.fillRect(0, 0, 300, Main.verticalRes);
        Main.debugc.strokeRect(1, 1, 298, Main.verticalRes - 2);
        Main.debugc.setFill(Color.GREENYELLOW);
    }

    public static void init() {
        Main.debugc.setStroke(Color.GREENYELLOW);
        clear();
    }
}
