package ru.asgreywolf.neofall.game;

import java.util.ArrayList;
import java.util.Random;

import ru.asgreywolf.neofall.StateListener.State;

public class GameModel {
    static Random rand = new Random();
    public State state;
    public PlayerForm playerForm = PlayerForm.SQUARE;
    public PlayerForm predictedForm = PlayerForm.INVALID;
    public double time = 0;
    public double len = 10000;
    public double speed = 1;
    public boolean liveBonus = false;
    public ArrayList<Wall> walls = new ArrayList<Wall>();
    public ArrayList<Bonus> bonus = new ArrayList<Bonus>();
    private int id = rand.nextInt();
    public GameModel() {
        int last = 3;
        walls.add(new Wall(3, PlayerForm.values()[rand.nextInt(3)]));
        while (last < len) {
            int r = last + rand.nextInt(4) + 1;
            if (r <= len)
                walls.add(new Wall(r, PlayerForm.values()[rand.nextInt(3)]));
            last = r;
        }
        for (int i = 0; i < 500; i++) {
            bonus.add(new LiveBonus(rand.nextInt((int) len)));
        }
    }

    public int getId() {
        return id;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object clone() {
        try {
            GameModel result = (GameModel) super.clone();
            result.walls = (ArrayList<Wall>) result.walls.clone();
            return result;
        } catch (Exception e) {
        }
        ;
        return null;
    }

    public static class Entity implements Comparable<Entity> {
        double position;

        Entity(double pos) {
            position = pos;
        }

        @Override
        public int compareTo(Entity o) {
            if (position < o.position) return -1;
            if (position == o.position) return 0;
            return 1;
        }
    }

    public static class Wall extends Entity {
        PlayerForm requiredForm;

        Wall(double pos, PlayerForm form) {
            super(pos);
            requiredForm = form;
        }
    }

    public static class Bonus extends Entity {
        public boolean picked = false;
        OnBonusListener listener;

        Bonus(double pos, OnBonusListener listener) {
            super(pos);
            this.listener = listener;
        }

        void pick(GameModel gm) {
            picked = true;
            listener.onBonus(this, gm);
        }

        static interface OnBonusListener {
            void onBonus(Bonus b, GameModel gm);
        }
    }

    public static class LiveBonus extends Bonus {
        final static OnBonusListener listener = new OnBonusListener() {
            @Override
            public void onBonus(Bonus b, GameModel gm) {
                gm.liveBonus = true;
            }
        };

        LiveBonus(double pos) {
            super(pos, listener);
        }
    }
}
