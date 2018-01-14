package ru.asgreywolf.neofall.models;

import ru.asgreywolf.neofall.game.GameSurface;
import ru.asgreywolf.neofall.shaders.WallShader;

/**
 * Created by asgreywolf on 3/5/17.
 */

public class PipeModel extends Model{
    private static WallShader s = null;
    private double position = 0;

    public PipeModel() {
        super();
        double sizex = GameSurface.xSizeToScreen(0.6);
        double sizey = GameSurface.ySizeToScreen(0.9*9/16);
        double X = sizex / 2;
        double x = -sizex / 2;
        double Y = 1.0;
        double y = GameSurface.yToScreen(0.9) - sizey;
        addQuad(0,x,X,y,Y,0);
        addQuad(1,0,1,0,1);
    }

    public void setPosition(double pos) {
        position = pos;
    }

    @Override
    public void render() {
        if (position < -1.0 || position >= 3.0)
            return;
        if (s == null)
            s = new WallShader();
        s.position = -position;
        s.colorR = 0;
        s.colorG = 0;
        s.colorB = 0;
        s.use();
        super.render();
    }
}
