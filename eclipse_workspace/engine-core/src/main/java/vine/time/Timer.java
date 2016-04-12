package vine.time;

public interface Timer {

    void tick(float f);

    void start();

    void reset();

    void stop();

    float getElapsedTime();

    float getDuration();

    boolean isRunning();

    float getId();

}
