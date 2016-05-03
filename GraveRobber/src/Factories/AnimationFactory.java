package Factories;

import Renderer.Animation;
import Renderer.Sprite;

import java.util.HashMap;

public class AnimationFactory {

    // Sprites are shared among animations, to conserve memory
    private static HashMap<String, Sprite> sprites;

    public static void init() {
        sprites = new HashMap<>();

        // TODO instead of listing files here, load all matching config files from /resources
        sprites.put("Player", new Sprite("./resources/warrior.ini"));
        sprites.put("GIANT_RAT", new Sprite("./resources/rat.ini"));
        sprites.put("SKELETON", new Sprite("./resources/skeleton.ini"));
    }

    public static Animation getAnimation(String name) {
        return makeAnimation(name, 0.0);
    }

    public static Animation getAnimation(String name, double startingDirection) {
        return makeAnimation(name, startingDirection);
    }

    private static Animation makeAnimation(String name, double startingDirection) {
        return new Animation(sprites.get(name), startingDirection);
    }

}
