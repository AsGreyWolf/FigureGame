package ru.asgreywolf.neofall.game;

import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import ru.asgreywolf.neofall.App;
import ru.asgreywolf.neofall.game.GameModel.Wall;
import ru.asgreywolf.neofall.ui.UIController;

public class GameControllerImpl implements GameController {

    private static Handler requestDiedScreen = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            UIController.setState(State.DIED);
        }
    };
    Wall costyl = null;
    Thread gameThread;
    private GameView view;
    private GameModel gm = new GameModel();
    Runnable gameRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
            }
            gameloop:
            while (true) {
                synchronized (gm) {
                    handleCollisions();
                    gm.time += 30.0 / 1000;
                    view.draw(gm);
                }
                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }
    };

    public GameControllerImpl(GameView view) {
        this.view = view;
        view.connect(this);
        view.draw(gm);
    }

    private void die() {
        gm.playerForm = PlayerForm.TRIANGLE;
        SharedPreferences.Editor editor = App.getPreferences().edit();
        int score = (int) (gm.time * gm.speed);
        editor.putInt("lastScore", score);
        if (App.getPreferences().getInt("bestScore", -1) < score)
            editor.putInt("bestScore", score);
        editor.commit();
        requestDiedScreen.sendEmptyMessage(0);
    }

    private void handleCollisions() {
        double bottom = gm.speed * gm.time;
        double top = gm.speed * (gm.time + 30.0 / 1000);
        for (Wall w : gm.walls)
            if (w.position <= top && w.position >= bottom)
                if (w.requiredForm != gm.playerForm) {
                    if (gm.liveBonus)
                        gm.liveBonus = false;
                    else if (costyl != w)
                        die();
                    costyl = w;
                }

        for (GameModel.Bonus w : gm.bonus)
            if (w.position <= top && w.position >= bottom)
                if (!w.picked)
                    w.pick(gm);
    }

    @Override
    public void OnStateChange(State state) {
        if (state == State.INGAME && gm.state != State.PAUSE) {
            gm = new GameModel();
        }
        if (state == State.INGAME) {
            gameThread = new Thread(gameRunnable);
            gameThread.start();
        } else if (gameThread != null)
            gameThread.interrupt();
        gm.state = state;
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void onGesture(Gesture g, double posx, double posy) {
        synchronized (gm) {
            if (gm.state == State.INGAME) {
                switch (g) {
                    case SQUARE:
                        gm.playerForm = PlayerForm.SQUARE;
                        break;
                    case TRIANGLE:
                        gm.playerForm = PlayerForm.TRIANGLE;
                        break;
                    case CIRCLE:
                        gm.playerForm = PlayerForm.CIRCLE;
                        break;
                    case ANGLE:
                        break;
                }
            }
        }
    }

    @Override
    public void onPredict(Gesture g, double posx, double posy) {
        synchronized (gm) {
            if (gm.state == State.INGAME) {
                switch (g) {
                    case SQUARE:
                        gm.predictedForm = PlayerForm.SQUARE;
                        break;
                    case TRIANGLE:
                        gm.predictedForm = PlayerForm.TRIANGLE;
                        break;
                    case CIRCLE:
                        gm.predictedForm = PlayerForm.CIRCLE;
                        break;
                    default:
                        gm.predictedForm = PlayerForm.INVALID;
                        break;
                }
            }
        }
    }

}
