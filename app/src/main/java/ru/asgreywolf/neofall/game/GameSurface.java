package ru.asgreywolf.neofall.game;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.SoundPool;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import ru.asgreywolf.neofall.App;
import ru.asgreywolf.neofall.R;
import ru.asgreywolf.neofall.StateListener.State;
import ru.asgreywolf.neofall.game.GameModel.Wall;
import ru.asgreywolf.neofall.gestures.GestureDetector;
import ru.asgreywolf.neofall.models.BackgroundModel;
import ru.asgreywolf.neofall.models.BonusModel;
import ru.asgreywolf.neofall.models.ForegroundModel;
import ru.asgreywolf.neofall.models.PipeModel;
import ru.asgreywolf.neofall.models.PlayerModel;
import ru.asgreywolf.neofall.models.TraceModel;
import ru.asgreywolf.neofall.models.WallModel;
import ru.asgreywolf.neofall.shaders.Texture;
import ru.asgreywolf.neofall.ui.UIView;

public class GameSurface extends GLSurfaceView implements GLSurfaceView.Renderer, GameView {
    final static int SOUND_WALL_SUCCESS = 0;
    final static int SOUND_CHANGE_FROM = 1;
    final static int SOUND_DIE = 2;
    public static int resolutionX;
    public static int resolutionY;
    private static int spaceX = resolutionX;
    private static int spaceY = resolutionY;
    int soundIds[] = new int[3];
    Vibrator vibrator;
    int nextWall = -1;
    Texture bonusTexture = null;
    int cachedGameId = -1;
    int cachedWall;
    double cachedTime;
    PlayerForm cachedForm;
    PlayerForm cachedPredict;
    int cachedDied;
    boolean cachedLiveBonus;
    private SoundPool soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
    private GameModel gm = new GameModel();
    private BackgroundModel background;
    private ForegroundModel foreground;
    private TraceModel trace;
    private PlayerModel player;
    private PipeModel pipe;
    private ArrayList<WallModel> walls = new ArrayList<>();
    private ArrayList<BonusModel> bonuses = new ArrayList<>();
    public GameSurface(Context context) {
        super(context);
        init(context);
    }
    public GameSurface(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public static double aspect() {
        return resolutionX * 1.0 / resolutionY;
    }

    public static double xToScreen(double x) {
        return 0.5 + (x - 0.5) * spaceX / resolutionX;
    }

    public static double yToScreen(double y) {
        return 0.5 + (y - 0.5) * spaceY / resolutionY;
    }

    public static double xSizeToScreen(double x) {
        return x * spaceX / resolutionX;
    }

    public static double ySizeToScreen(double y) {
        return y * spaceY / resolutionY;
    }

    private static void setResolution(int w, int h) {
        resolutionX = w;
        resolutionY = h;
        spaceX = resolutionX;
        spaceY = resolutionY;
        if (spaceY > spaceX * 16 / 9) {
            spaceY = spaceX * 16 / 9;
        }
        if (spaceX > spaceY * 9 / 16) {
            spaceX = spaceY * 9 / 16;
        }
    }

    private void init(Context context) {
        setEGLContextClientVersion(2);
        setRenderer(this);
        soundIds[SOUND_CHANGE_FROM] = soundPool.load(context, R.raw.change_form, 1);
        soundIds[SOUND_WALL_SUCCESS] = soundPool.load(context, R.raw.wall_success, 1);
        soundIds[SOUND_DIE] = soundPool.load(context, R.raw.die, 1);
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    private void vibrate() {
        if (App.getPreferences().getBoolean("VIBRO", true))
            vibrator.vibrate(200);
    }

    private void playSound(int id) {
        if (App.getPreferences().getBoolean("SOUND", true))
            soundPool.play(soundIds[id], 1, 1, 1, 0, 1.0f);
    }

    @Override
    public void draw(GameModel gm) {
        this.gm = gm;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void connect(final GameController controller) {
        setOnClickListener(controller);
        setOnTouchListener(new OnTouchListener() {
            GestureDetector detector = new GestureDetector(controller);

            @Override
            public boolean onTouch(View v, MotionEvent me) {
                double x = me.getX() / resolutionX;
                double y = me.getY() / resolutionY;
                if (trace != null && (me.getAction() == me.ACTION_DOWN || me.getAction() == me.ACTION_MOVE || me.getAction() == me.ACTION_UP)) {
                    if (x == 0.5 && y == 0.5) {
                        trace.x = x;
                    }
                    trace.x = x;
                    trace.y = y;
                }
                if (trace != null && me.getAction() == me.ACTION_DOWN) {
                    trace.clear = false;
                }
                if (trace != null && me.getAction() == me.ACTION_UP) {
                    trace.clear = true;
                }
                return detector.onTouch(v, me);
            }
        });
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        setResolution(width, height);
        player = new PlayerModel();
        background = new BackgroundModel();
        foreground = new ForegroundModel();
        trace = new TraceModel();
        pipe = new PipeModel();
        if (gm != null) {
            syncModels();
        }
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_favorite_border_white_48dp);
        bonusTexture = new Texture(bmp);
    }

    private void syncModels() {
        player.setForm(gm.playerForm);
        background.colorR = gm.playerForm.colorR();
        background.colorG = gm.playerForm.colorG();
        background.colorB = gm.playerForm.colorB();
        if (gm.predictedForm != PlayerForm.INVALID) {
            trace.colorR = gm.predictedForm.colorR();
            trace.colorG = gm.predictedForm.colorG();
            trace.colorB = gm.predictedForm.colorB();
        } else {
            trace.colorR = 1.0;
            trace.colorG = 0.84;
            trace.colorB = 0;
        }
        nextWall = -1;
        for (int i = 0; i < gm.walls.size(); i++) {
            if (i >= walls.size()) {
                walls.add(new WallModel());
            }
            Wall wall = gm.walls.get(i);
            double pos = wall.position - gm.time / gm.speed;
            if (nextWall == -1 && pos > 0) nextWall = i;
            walls.get(i).setPosition(pos);
            walls.get(i).setColor(wall.requiredForm.colorR(), wall.requiredForm.colorG(), wall.requiredForm.colorB());
        }

        for (int i = 0; i < gm.bonus.size(); i++) {
            if (i >= bonuses.size()) {
                bonuses.add(new BonusModel());
            }
            GameModel.Bonus bonus = gm.bonus.get(i);
            double pos = bonus.position - gm.time / gm.speed;
            bonuses.get(i).setPosition(pos);
            bonuses.get(i).setTexture(bonusTexture);
            if (bonus.picked) bonuses.get(i).setPosition(-9999);
        }
        pipe.setPosition(-gm.time / gm.speed);
    }

    void resetGame() {
        cachedTime = 0;
        cachedWall = 0;
        cachedGameId = gm.getId();
        cachedForm = PlayerForm.INVALID;
        cachedPredict = PlayerForm.INVALID;
        cachedDied = -1;
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        syncModels();
        if (gm.getId() != cachedGameId) resetGame();
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        if (background != null)
            background.render();
        if (player != null && gm.state == State.INGAME) {
            player.render();
            for (int i = 0; i < walls.size(); i++)
                walls.get(i).render();
            for (BonusModel bonus : bonuses)
                bonus.render();
            if (gm.time > cachedTime + 1) {
                UIView.setPoints(gm.time);
                UIView.setLiveBonus(gm.liveBonus);
                cachedTime = gm.time;
            }
            if (cachedWall < nextWall) {
                playSound(SOUND_WALL_SUCCESS);
                cachedWall = nextWall;
            }
            if (cachedForm != gm.playerForm) {
                playSound(SOUND_CHANGE_FROM);
                cachedForm = gm.playerForm;
            }
            if (cachedLiveBonus != gm.liveBonus) {
                if (gm.liveBonus)
                    playSound(SOUND_WALL_SUCCESS);
                else
                    playSound(SOUND_DIE);
                cachedLiveBonus = gm.liveBonus;
            }
            if (gm.predictedForm != cachedPredict) {
                if (gm.predictedForm != PlayerForm.INVALID)
                    vibrate();
                cachedPredict = gm.predictedForm;
            }
            if (pipe != null) pipe.render();
        }
        if (gm.state == State.DIED)
            if (cachedDied != gm.getId()) {
                playSound(SOUND_DIE);
                cachedDied = gm.getId();
            }
        if (foreground != null)
            foreground.render();
        if (trace != null)
            trace.render();
    }
}
