package Interfaces;

public interface IBehaviour {
    void start();
    void pause();
    void stop();
    void reset();
    void update(double time);
    void think(double time);
}
