package Renderer;


import Enumerations.AnimationState;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Sprite {
    /**
     * Да се напише програма, която съхранява следната информация:
     * <p>
     * - Път до изображение на харддиска
     * - Наименование
     * - Самото изображение в обект от тип javafx.scene.image.Image
     * - Скорост на анимацията (кадри в секунда)
     * - Списък с именувани поредици от фреймове
     * - За всяка поредица - начален индекс, краен индекс, координати от основното изображение, координати до центъра на
     * всеки фрейм от неговия горен ляв ъгъл
     * <p>
     * При подадедн вход, програмата трябва да може да върне следния отговор:
     * <p>
     * - Да зареди от харддиска текстов файл с информация за размера на всеки фрейм от основното изображение и да
     * извлече нужните данни за създаване на списъка с именувани поредици от кадри (анимации)
     * - Да зареди основното изображение
     * - При подадено име на поредица, индекс на кадъра и посока на обекта (ако има посока) - кадъра в подходящ формат
     * за изобразяване върху javafx.scene.canvas.GraphicsContext
     * - Информация за дължината на всяка именувана поредица, в брой кадри
     * - Информация за наличието на определена именувана поредица (true/false)
     * <p>
     * <p>
     * Примерен ред на изпълнение: програмата получава име на sprite sheet -> намира текстов файл с търсеното име в
     * папката с ресурси на играта -> зарежда файла и извлича нужната информация -> зарежда изображението, посочено във
     * файла -> създава масив от данни с информация за кадрите и именуваните поредици (отделните анимации)
     */
    public static void main(String[] args) {


    }

    private File path;
    private String name;
    private Image source;
    private double framerate;
    private ArrayList<AnimationState> sequences;
    private HashMap<AnimationState, Integer> start;
    private HashMap<AnimationState, Integer> end;
    private HashMap<AnimationState, Double> x;
    private HashMap<AnimationState, Double> y;
    private HashMap<AnimationState, Double> offsetX;
    private HashMap<AnimationState, Double> offsetY;
    private ArrayList<ImageView> frames;

    public Sprite(String path) {
        this.path = new File(path);
        source = new Image(path);
        framerate = 10d;
        sequences = new ArrayList<>();
        start = new HashMap<>();
        end = new HashMap<>();
        x = new HashMap<>();
        y = new HashMap<>();
        offsetX = new HashMap<>();
        offsetY = new HashMap<>();
        frames = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String s;
            while ((s = br.readLine()) != null) {
                System.out.println(s);
                String[] tokens = s.split("=");
                if (tokens[0].equals("name")) {
                    this.name = tokens[1];
                } else if (tokens[0].equals("framerate")) {
                    framerate = Double.parseDouble(tokens[1]);
                } else if (tokens[0].equals(("sequence"))) {
                    if (tokens[1].equals("name")) {
                        // Todo Parse AnimationState
                    } else if (tokens[1].equals("start")) {
                        // Todo Parse Start
                    } else if (tokens[1].equals("end")) {
                        // Todo Parse End
                    } else if (tokens[1].equals("x")) {
                        // Todo Parse x
                    } else if (tokens[1].equals("y")) {
                        // Todo Parse y
                    } else if (tokens[1].equals("offsetX")) {
                        // Todo Parse offsetX
                    } else if (tokens[1].equals("offsetY")) {
                        // Todo Parse offsetY
                    }

                }
            }

        } catch (IOException e) {
            System.out.println("Exception caught");
        }
    }
}
