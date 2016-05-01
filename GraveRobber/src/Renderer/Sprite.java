package Renderer;


import Enumerations.AnimationState;
import Game.Main;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Sprite {

    private String name;
    private double framerate;
    private ArrayList<Sequence> sequences;
    private ArrayList<Frame> frames;

    public Sprite(String path) {
        this.framerate = 10d; // we set this to a default value in case the config file doesn't specify it
        sequences = new ArrayList<>();
        frames = new ArrayList<>();

        Image source = null;

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String s;
            while ((s = br.readLine()) != null) {
                if (s.charAt(0) == '#') continue; // comment line skip
                String[] tokens = s.split("=");
                if (tokens[0].equals("name")) {
                    this.name = tokens[1];
                } else if (tokens[0].equals("image")) {
                    File imgPath = new File("resources/" + tokens[1]);
                    source = new Image(imgPath.toURI().toString());
                } else if (tokens[0].equals("framerate")) {
                    framerate = Double.parseDouble(tokens[1]);
                } else if (tokens[0].equals("frame")) {
                    String[] frameSettings = tokens[1].split(",");
                    int x = Integer.parseInt(frameSettings[0]);
                    int y = Integer.parseInt(frameSettings[1]);
                    int width = Integer.parseInt(frameSettings[2]);
                    int height = Integer.parseInt(frameSettings[3]);
                    int offsetX = Integer.parseInt(frameSettings[4]);
                    int offsetY = Integer.parseInt(frameSettings[5]);
                    frames.add(new Frame(source, x, y, width, height, offsetX, offsetY));
                } else if (tokens[0].equals("frames")) {
                    String[] params = tokens[1].split(",");
                    int rows = Integer.parseInt(params[6]);
                    int cols = Integer.parseInt(params[7]);
                    int startingX = Integer.parseInt(params[0]);
                    int startingY = Integer.parseInt(params[1]);
                    int width = Integer.parseInt(params[2]);
                    int height = Integer.parseInt(params[3]);
                    int offsetX = Integer.parseInt(params[4]);
                    int offsetY = Integer.parseInt(params[5]);
                    for (int i = 0; i < rows; i++) {
                        for (int j = 0; j < cols; j++) {
                            int currentLeft = startingX + j * width;
                            int currentTop = startingY + i * height;
                            frames.add(new Frame(source, currentLeft, currentTop, width, height, offsetX, offsetY));
                        }
                    }
                } else if (tokens[0].equals("sequence")) {
                    String[] params = tokens[1].split(",");
                    String name = params[0];
                    int nOfFrames = params.length - 3;
                    double fromDir = Double.parseDouble(params[1]);
                    double toDir = Double.parseDouble(params[2]);
                    Frame[] frames = new Frame[nOfFrames];
                    for (int i = 0; i < nOfFrames; i++) {
                        frames[i] = this.frames.get(Integer.parseInt(params[i+3]));
                    }
                    sequences.add(new Sequence(name, fromDir, toDir, frames));
                }
            }
        } catch (IOException e) {
            System.out.println("Exception caught");
        }
    }

    public double getFramerate() {
        return framerate;
    }

    public Image get(int index) {
        if (frames.size() > index)
            return frames.get(index).get();
        else return null;
    }
    public int getOX(int index) {
        if (frames.size() > index)
            return frames.get(index).getOX();
        else return 0;
    }
    public int getOY(int index) {
        if (frames.size() > index)
            return frames.get(index).getOY();
        else return 0;
    }

    public Image getFrame(String name, double dir, int index) {
        Image result;
        Optional<Sequence> sequence = sequences.stream()
                .filter(entry -> entry.getName().equals(name))
                .filter(entry -> entry.isDir(dir)).findFirst();
        if (sequence.isPresent()) {
            result = sequence.get().get(index).get();
        } else result = get(0); // return first frame in case the needed frame doesn't exist
        return result;
    }

    public Frame getFrame(int index) {
        if (frames.size() > index) return frames.get(index);
        else return frames.get(0);
    }

    public Sequence getSequence(String name, double dir) {
        Sequence result;
        Optional<Sequence> found = sequences.stream()
                .filter(entry -> entry.getName().equals(name))
                .filter(entry -> entry.isDir(dir)).findFirst();
        if (found.isPresent()) {
            result = found.get();
        } else result = new Sequence(name, 0, 0, new Frame[]{getFrame(0)}); // return sequence of first frame, if target not found
        return result;
    }

}
