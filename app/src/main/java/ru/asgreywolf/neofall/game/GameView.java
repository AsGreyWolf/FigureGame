package ru.asgreywolf.neofall.game;

public interface GameView {
    void connect(GameController controller);

    void draw(GameModel model);
}
