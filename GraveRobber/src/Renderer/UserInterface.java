package Renderer;

import Abilities.*;
import Enumerations.AbilityTypes;
import Enumerations.GameState;
import Game.Main;
import Models.Consumable;
import Models.Creature;
import Models.Inventory;
import Models.Player;
import javafx.application.Platform;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class UserInterface {

    static public Image menuCursor;
    final static GraphicsContext gc = Main.game.getGc();

    //========================================
    //region Menu Elements

    public static void loadMouseImage(){
        InputStream is = null;
        try {
            is = Files.newInputStream(Paths.get("resources/cursor.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Image img = new Image(is);
        menuCursor = img;
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static public void renderMenuBackground() {
        GraphicsContext gc = Main.game.getGc();
        gc.setFill(Color.BLACK);
        gc.fillRect(0,0,1100,600);
    }

    static public void renderTitle(double x, double y) {
        GraphicsContext gc = Main.game.getGc();
        gc.setFill(Color.GRAY);
        String t = "GRAVE ROBBER";
        gc.setFont(Font.font(68));
        gc.fillText("GRAVE ROBBER",x,y);
    }

    static public void renderStartButton(double x, double y) {
        GraphicsContext gc = Main.game.getGc();
        double mouseX = Main.game.getPlayer().getMouseX();
        double mouseY = Main.game.getPlayer().getMouseY();
        if (mouseX >= x && mouseX <= x + 200 && mouseY >= y && mouseY <= y + 30){
            gc.setFill(Color.RED);
            gc.fillRoundRect(x,y,200,30,30,30);
            gc.setFill(Color.WHITE);
            gc.setFont(Font.font(30));
            if (Main.game.getControlState().isMouseLeft()) {
                Main.game.setGameState(GameState.LIVE);
            }
        }
        else{
            gc.setFill(Color.WHITE);
            gc.fillRoundRect(x,y,200,30,30,30);
            gc.setFill(Color.RED);
            gc.setFont(Font.font(30));
        }
        gc.fillText("START",x + 64,y +24,170);
    }

    static public void renderExitButton(double x, double y) {
        GraphicsContext gc = Main.game.getGc();
        double mouseX = Main.game.getPlayer().getMouseX();
        double mouseY = Main.game.getPlayer().getMouseY();
        if (mouseX >= x && mouseX <= x + 200 && mouseY >= y && mouseY <= y + 30){
            gc.setFill(Color.RED);
            gc.fillRoundRect(x,y,200,30,30,30);
            gc.setFill(Color.WHITE);
            gc.setFont(Font.font(30));
            if (Main.game.getControlState().isMouseLeft()) {
                Platform.exit();
            }
        }
        else{
            gc.setFill(Color.WHITE);
            gc.fillRoundRect(x,y,200,30,30,30);
            gc.setFill(Color.RED);
            gc.setFont(Font.font(30));
        }
        gc.fillText("EXIT",x + 75,y +24,170);
    }

    static public void renderMenuCursor(double x, double y) {
        GraphicsContext gc = Main.game.getGc();

        gc.drawImage(menuCursor, x, y);
    }

    public static void drawMenu() {
        renderMenuBackground();
        renderTitle(180,300);
        renderStartButton(300, 350);
        renderExitButton(300, 400);
        renderMenuCursor(Main.game.getPlayer().getMouseX(), Main.game.getPlayer().getMouseY());
    }

    public static void drawDeadScreen() {
        gc.save();
        gc.setFill(Color.RED);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
        gc.setFont(Font.font("Times New Roman", FontWeight.EXTRA_BOLD, 72));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.strokeText("WASTED", Main.horizontalRes / 2, Main.verticalRes / 2 - QuickView.gridSize);
        gc.fillText("WASTED", Main.horizontalRes / 2, Main.verticalRes / 2 - QuickView.gridSize);
        gc.restore();
    }

    //endregion ==============================

    //========================================
    //region UI Elements

    static public void drawCrosshair(double x, double y, double dir) {
        // Canvas coordinates
        double size = 5;
        gc.setFill(Color.WHITE);
        if (Main.game.getControlState().isMouseLeft()) {
            gc.setFill(Color.RED);
            size = 6;
        }
        double[] aX = {
                size * 1 * Math.cos(dir),
                size * 2 * Math.cos(dir + Math.PI / 2),
                size * 2.82 * Math.cos(dir + Math.PI * 3 / 4),
                size * 1 * Math.cos(dir + Math.PI),
                size * 2.82 * Math.cos(dir + Math.PI * 5 / 4),
                size * 2 * Math.cos(dir + Math.PI * 3 / 2)
        };
        double[] aY = {
                size * 1 * Math.sin(dir),
                size * 2 * Math.sin(dir + Math.PI / 2),
                size * 2.82 * Math.sin(dir + Math.PI * 3 / 4),
                size * 1 * Math.sin(dir + Math.PI),
                size * 2.82 * Math.sin(dir + Math.PI * 5 / 4),
                size * 2 * Math.sin(dir + Math.PI * 3 / 2)
        };
        for (int i = 0; i < 6; i++) {
            aX[i] += x;
            aY[i] += y;
        }
        gc.fillPolygon(aX, aY, 6);
    }

    public static void drawPlayerHealthBar(Player player) {
        gc.save();
        gc.setFill(Color.RED);
        gc.setStroke(Color.RED);
        gc.setLineWidth(2);
        gc.strokeRect(10, 10, 220, 20);
        gc.fillRect(12, 12, 216 * player.getHealthPoints() / player.getMaxHealth(), 16);
        gc.restore();
    }

    public static void drawShieldEndurance(Creature creature) {
        gc.save();
        gc.setFill(Color.LIGHTBLUE);
        Defend defend = (Defend) creature.getAbility(AbilityTypes.DEFEND);
        gc.fillRect(12, 22, 216 * defend.getHealth() / defend.getMaxHealth(), 6);
        gc.restore();
    }

    public static void drawAbilityStatus(Ability ability, int index) {
        double x = 12 + index * 42;
        double y = 42;
        double w = 32;
        double h = 32;
        gc.save();
        gc.setLineWidth(4);
        if (ability.isReady()) {
            gc.setStroke(Color.WHITESMOKE);
            gc.strokeArc(x, y, w, h, 90, 360, ArcType.OPEN);
        } else {
            double full = ability.getCooldown();
            double current = full - ability.getRemaining();
            double progress = current / full;
            gc.setStroke(Color.GREY);
            gc.setGlobalAlpha(0.25);
            gc.strokeArc(x, y, w, h, 90, 360, ArcType.OPEN);
            gc.setGlobalAlpha(1.0);
            gc.strokeArc(x, y, w, h, 90, progress * 360, ArcType.OPEN);
        }
        gc.restore();
    }

    public static void drawHealthBar(Creature creature, double offset) {
        double w = creature.getRadius() * 2.5 * QuickView.gridSize;
        double h = 5;
        double x = QuickView.toCanvasX(creature.getX()) - w / 2;
        double y = QuickView.toCanvasY(creature.getY()) - offset - h;
        gc.save();
        gc.setFill(Color.RED);
        gc.setStroke(Color.RED);
        gc.setLineWidth(1);
        gc.strokeRect(x, y, w, h);
        gc.fillRect(x, y, w * creature.getHealthPoints() / creature.getMaxHealth(), h);
        gc.restore();
    }

    public static void drawAllAmmo(Inventory backpack) {
        List<Consumable> items = backpack.getContents().stream()
                .filter(loot -> loot instanceof Consumable)
                .map(loot -> (Consumable) loot)
                .collect(Collectors.toList());
        for (int i = 0; i < items.size(); i++) {
            drawAmmo(items.get(i), i + 1);
        }
    }

    public static void drawAmmo(Consumable item, int index) {
        double x = 28 + index * 42;
        double y = 58;
        gc.save();
        gc.setFill(Color.RED);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
        gc.setFont(Font.font("Times New Roman", FontWeight.EXTRA_BOLD, 24));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.strokeText(String.format("%d", item.getAmmo()), x, y);
        gc.fillText(String.format("%d", item.getAmmo()), x, y);
        gc.restore();
    }

    public static void drawUI(Player player) {
        drawPlayerHealthBar(player);
        drawShieldEndurance(player);
        drawAbilityStatus(player.getAbility(AbilityTypes.DASH), 0);
        drawAbilityStatus(player.getAbility(AbilityTypes.HEAL), 1);
        drawAllAmmo(player.getInventory());
        drawCrosshair(player.getMouseX(),
                player.getMouseY(),
                player.getDirection());
    }

    //endregion ==============================

}
