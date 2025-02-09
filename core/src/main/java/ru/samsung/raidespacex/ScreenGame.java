package ru.samsung.raidespacex;

import static ru.samsung.raidespacex.Main.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

public class ScreenGame implements Screen {

    private final float joystickWidth = 380, joystickHeight = 380;
    private final float joystickX = joystickWidth/2, joystickY = joystickHeight/2;

    private final Main main;
    private final SpriteBatch batch;
    private final OrthographicCamera camera;
    private final Vector3 touch;
    private final BitmapFont font;

    Texture imgJoystick;
    Texture imgBG;
    Texture imgShipsAtlas;
    TextureRegion[] imgShip = new TextureRegion[12];

    SpaceXButton btnBack;

    Ship ship;
    Space[] space = new Space[2];

    public ScreenGame(Main main) {
        this.main = main;
        batch = main.batch;
        camera = main.camera;
        touch = main.touch;
        font = main.fontLightGreen;
        Gdx.input.setInputProcessor(new SpaceXProcessor());

        imgJoystick = new Texture("joystick.png");
        imgBG = new Texture("bg2.jpg");
        imgShipsAtlas = new Texture("ships_atlas.png");
        for (int i = 0; i < imgShip.length; i++) {
            imgShip[i] = new TextureRegion(imgShipsAtlas, (i<7?i:12-i)*400, 0, 400, 400);
            /*if(i<7) imgShip[i] = new TextureRegion(imgShipsAtlas, i*400, 0, 400, 400);
            else imgShip[i] = new TextureRegion(imgShipsAtlas, (12-i)*400, 0, 400, 400);*/
        }

        btnBack = new SpaceXButton(font, "x", 850, 1580);

        space[0] = new Space(0, 0);
        space[1] = new Space(0, SCR_HEIGHT);
        ship = new Ship(SCR_WIDTH/2, 200);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        // касания
        if(Gdx.input.justTouched()){
            touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touch);

            if(btnBack.hit(touch)){
                main.setScreen(main.screenMenu);
            }
        }

        // события
        for (Space s: space) s.move();
        /*for (int i = 0; i < space.length; i++) {
            space[i].move();
        }*/
        if (contols == ACCELEROMETER){
            ship.vx = -Gdx.input.getAccelerometerX()*2;
            ship.vy = -Gdx.input.getAccelerometerY()*2;
        }
        ship.move();

        // отрисовка
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for(Space s: space) batch.draw(imgBG, s.x, s.y, s.width, s.height);
        batch.draw(imgShip[ship.phase], ship.scrX(), ship.scrY(), ship.width, ship.height);
        if(contols == JOYSTICK_LEFT){
            batch.draw(imgJoystick, 0, 0, joystickWidth, joystickHeight);
        }
        btnBack.font.draw(batch, btnBack.text, btnBack.x, btnBack.y);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    class SpaceXProcessor implements InputProcessor{

        @Override
        public boolean keyDown(int keycode) {
            return false;
        }

        @Override
        public boolean keyUp(int keycode) {
            return false;
        }

        @Override
        public boolean keyTyped(char character) {
            return false;
        }

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            if(contols == SCREEN) {
                touch.set(screenX, screenY, 0);
                camera.unproject(touch);
                ship.touch(touch);
            }
            if(contols == JOYSTICK_LEFT) {
                touch.set(screenX, screenY, 0);
                camera.unproject(touch);
                if(Math.pow(touch.x-joystickWidth/2,2) + Math.pow(touch.y-joystickWidth/2,2) <= Math.pow(joystickWidth/2,2)) {
                    ship.vx = (touch.x-joystickWidth/2)/10;
                    ship.vy = (touch.y-joystickWidth/2)/10;
                }
            }
            return false;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            ship.stop();
            return false;
        }

        @Override
        public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
            return false;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            if(contols == SCREEN) {
                touch.set(screenX, screenY, 0);
                camera.unproject(touch);
                ship.touch(touch);
            }
            if(contols == JOYSTICK_LEFT) {
                touch.set(screenX, screenY, 0);
                camera.unproject(touch);
                if(Math.pow(touch.x-joystickX,2) + Math.pow(touch.y-joystickY,2) <= Math.pow(joystickWidth/2,2)) {
                    ship.vx = (touch.x-joystickX)/10;
                    ship.vy = (touch.y-joystickY)/10;
                }
            }
            return false;
        }

        @Override
        public boolean mouseMoved(int screenX, int screenY) {
            return false;
        }

        @Override
        public boolean scrolled(float amountX, float amountY) {
            return false;
        }
    }
}
