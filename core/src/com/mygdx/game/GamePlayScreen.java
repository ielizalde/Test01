package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

import java.awt.Color;

/**
 * Created by Ivan on 02/03/15.
 */
public class GamePlayScreen implements Screen {

    final MainScreen game;
    private Stage stage;
    private int Level;

    Texture img;
    Image imgBack;

    private BitmapFont font;
    private TextureAtlas AtlasHeader;
    private Skin SkinHeader;
    private TextureAtlas AtlasOrange;
    private TextureRegion RegionOrange;
    private Animation AnimationOrange;
    private float ScreenWidth, ScreenHeight;

    Image HeaderImage;
    String HeaderName;
    int Lives = 5;
    int Special = 0;
    private int Score;

    BubbleArray bubbles;

    public GamePlayScreen(final MainScreen gam, int lev) {
        game = gam;
        Level = lev;



        img = new Texture("Settings/background.png");
        imgBack = new Image(img);
        imgBack.setBounds(0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

        AtlasHeader = new TextureAtlas("GamePlay/colors.txt");
        SkinHeader = new Skin();
        SkinHeader.addRegions(AtlasHeader);
        font = game.getFont(16);

        ScreenWidth = Gdx.graphics.getWidth();
        ScreenHeight = Gdx.graphics.getHeight();

        stage = new Stage();
        //stage.clear();

        TextField.TextFieldStyle style = new TextField.TextFieldStyle(game.getFont(16),com.badlogic.gdx.graphics.Color.BLUE,null,null,null);
        final TextField txtScore = new TextField("0",style);
        txtScore.setPosition(100,game.calcSize(1850,false));

        Gdx.input.setInputProcessor(new InputAdapter() {
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                int pointedX = screenX;
                int pointedY = Gdx.graphics.getHeight() - screenY;
                java.util.Iterator<Bubble> i = bubbles.items.iterator();
                while (i.hasNext()) {
                    Bubble b = i.next();
                    if (!b.Exploted) {
                        float bIX = b.Position.x;
                        float bFX = b.Position.x + b.sizeX;
                        if ((pointedX >= bIX) && (pointedX <= bFX)) {
                            float bIY = b.Position.y;
                            float bFY = b.Position.y + b.sizeY;
                            if ((pointedY >= bIY) && (pointedY <= bFY)) {
                                if (b.TipoFruta != Bubble.Fruta.DOUBLE){
                                    Score += b.Explode();
                                    txtScore.setText(String.valueOf(Score));
                                } else {
                                    if (b.tappedUno) {
                                        Score += b.Explode();
                                        txtScore.setText(String.valueOf(Score));
                                    } else {
                                        b.tappedUno = true;
                                    }
                                }
                            }
                        }
                    }
                }
                return true;
            }});

        HeaderName = "levels-color-000" + Level;
        HeaderImage = new Image();
        HeaderImage.setDrawable(SkinHeader.getDrawable(HeaderName));
        HeaderImage.setBounds(0,ScreenHeight - game.calcSize(138,false),
                game.calcSize(1080,true),game.calcSize(138,false));

        stage.addActor(imgBack);
        stage.addActor(HeaderImage);
        stage.addActor(txtScore);


        bubbles = new BubbleArray();

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.draw();

        game.batch.begin();

        float d = Gdx.graphics.getDeltaTime();
        if (bubbles.items.size() > 0) {
            java.util.Iterator<Bubble> i = bubbles.items.iterator();
            while (i.hasNext()) {
                Bubble b = i.next();
                b.update(d);
                if (b.Position.y < -(game.calcSize(306, false))) {
                    i.remove();
                } else {
                    if (!b.ExplotedAndFinished){
                        b.sizeX = game.calcSize(b.RegionBubble.getRegionWidth(),true);
                        b.sizeY = game.calcSize(b.RegionBubble.getRegionHeight(),false);
                        game.batch.draw(b.RegionBubble, b.Position.x, b.Position.y, b.sizeX, b.sizeY);
                    }
                }
            }
        }

        game.batch.end();

    }
    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
        Timer.schedule(new Task(){
            @Override
            public void run() {
                bubbles.createNew(game.calcSize(1080,false),game.calcSize(1980,false),Level);
            }}, 0,(2 / (Level*0.75f)));
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    public void dispose() {
    }

}
