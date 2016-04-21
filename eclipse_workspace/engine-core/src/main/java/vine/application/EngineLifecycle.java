package vine.application;

public interface EngineLifecycle
{
    void create();

    void start();

    void resume();

    void pause();

    void stop();

    void destroy();
}
