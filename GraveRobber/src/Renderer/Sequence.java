package Renderer;

import java.util.ArrayList;

public class Sequence {

    String name;
    final ArrayList<Frame> frames;
    final double fromDir;
    final double toDir;

    public Sequence(String name, double fromDir, double toDir, Frame... frames) {
        this.name = name;
        this.frames = new ArrayList<>();
        this.fromDir = fromDir;
        this.toDir = toDir;
        for (Frame frame : frames) {
            this.frames.add(frame);
        }
    }

    public String getName() {
        return name;
    }

    public Frame get(int index) {
        return frames.get(index);
    }

    public int length() {
        return frames.size();
    }

    public boolean isDir(double dir) {
        while (dir > Math.PI) dir -= 2 * Math.PI;
        if (dir >= fromDir && dir < toDir) return true;
        if (dir >= 0) {
            if (fromDir < 0 && dir < toDir && toDir > 0) return true;
            else if (fromDir > 0 && dir >= fromDir && toDir < 0) return true;
            else return false;
        } else {
            if (fromDir > 0 && dir < toDir && toDir < 0) return true;
            else if (fromDir < 0 && dir >= fromDir && toDir > 0) return true;
            else return false;
        }
    }

}
