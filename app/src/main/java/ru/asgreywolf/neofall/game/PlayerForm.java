package ru.asgreywolf.neofall.game;

public enum PlayerForm {
    SQUARE(0.0f), TRIANGLE(1.0f), CIRCLE(2.0f), DEFAULT(0.0f), INVALID(-1.0f);;

    private final float value;

    private PlayerForm(float value) {
        this.value = value;
    }

    public float value() {
        return value;
    }

    public double colorR() {
        switch (this) {
            case SQUARE:
                return 1.0;
            case TRIANGLE:
                return 0.0;
            case CIRCLE:
                return 0.0;
            default:
                return 0.0;
        }
    }

    public double colorG() {
        switch (this) {
            case SQUARE:
                return 0.0;
            case TRIANGLE:
                return 1.0;
            case CIRCLE:
                return 0.0;
            default:
                return 0.0;
        }
    }

    public double colorB() {
        switch (this) {
            case SQUARE:
                return 0.0;
            case TRIANGLE:
                return 0.0;
            case CIRCLE:
                return 1.0;
            default:
                return 0.0;
        }
    }

    public int color() {
        return (0xff << 24) | ((int) (0xff * colorR()) << 16) | ((int) (0xff * colorG()) << 8)
                | ((int) (0xff * colorB()));
    }
}
