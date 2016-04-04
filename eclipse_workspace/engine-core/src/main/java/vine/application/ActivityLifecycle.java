package vine.application;

public interface ActivityLifecycle {
    void create();

    void start();

    void resume();

    void pause();

    void stop();

    void destroy();
}
