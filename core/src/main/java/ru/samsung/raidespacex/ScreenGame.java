package ru.samsung.raidespacex;

import static ru.samsung.raidespacex.Main.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

public class ScreenGame implements Screen {
    private final Main main;
    private final SpriteBatch batch;
    private final OrthographicCamera camera;
    private final Vector3 touch;
    private final BitmapFont font100lightGreen;
    private final BitmapFont font50white;

    Texture imgJoystick;
    Texture imgBG;
    Texture imgShipsAtlas;
    Texture imgShotsAtlas;
    TextureRegion[] imgShip = new TextureRegion[12];
    TextureRegion imgShot;
    TextureRegion[][] imgEnemy = new TextureRegion[4][12];
    TextureRegion[][] imgFragment = new TextureRegion[5][25];

    Sound sndExplosion;
    Sound sndBlaster;

    SpaceXButton btnBack;

    Space[] space = new Space[2];
    Ship ship;
    List<Enemy> enemies = new ArrayList<>();
    List<Shot> shots = new ArrayList<>();
    List<Fragment> fragments = new ArrayList<>();
    Player[] players = new Player[10];

    private long timeLastSpawnEnemy, timeSpawnEnemyInterval = 1500;
    private long timeLastSpawnShot, timeSpawnShotsInterval = 800;

    private int nFragments = 100;
    private boolean isGameOver;

    public ScreenGame(Main main) {
        this.main = main;
        batch = main.batch;
        camera = main.camera;
        touch = main.touch;
        font100lightGreen = main.font100lightGreen;
        font50white = main.font50white;
        Gdx.input.setInputProcessor(new SpaceXProcessor());

        imgJoystick = new Texture("joystick.png");
        imgBG = new Texture("bg2.jpg");
        imgShipsAtlas = new Texture("ships_atlas.png");
        imgShotsAtlas = new Texture("shots.png");
        imgShot = new TextureRegion(imgShotsAtlas, 0, 0, 100, 350);
        for (int i = 0; i < imgShip.length; i++) {
            imgShip[i] = new TextureRegion(imgShipsAtlas, (i<7?i:12-i)*400, 0, 400, 400);
        }
        for (int j = 0; j < imgEnemy.length; j++) {
            for (int i = 0; i < imgEnemy[j].length; i++) {
                imgEnemy[j][i] = new TextureRegion(imgShipsAtlas, (i<7?i:12-i)*400, (j+1)*400, 400, 400);
            }
        }
        int k = (int) Math.sqrt(imgFragment[0].length);
        int size = 400/k;
        for (int i = 0; i < imgFragment.length; i++) {
            for (int j = 0; j < imgFragment[i].length; j++) {
                if(i<imgFragment.length-1) {
                    imgFragment[i][j] = new TextureRegion(imgEnemy[i][0], j%k*size, j/k*size, size, size);
                } else {
                    imgFragment[i][j] = new TextureRegion(imgShip[0], j%k*size, j/k*size, size, size);
                }
            }
        }

        sndExplosion = Gdx.audio.newSound(Gdx.files.internal("explosion.mp3"));
        sndBlaster = Gdx.audio.newSound(Gdx.files.internal("blaster.mp3"));

        btnBack = new SpaceXButton(font50white, "x", 850, 1580);

        space[0] = new Space(0, 0);
        space[1] = new Space(0, SCR_HEIGHT);
        ship = new Ship(SCR_WIDTH/2, 200);

        for (int i = 0; i < players.length; i++) {
            players[i] = new Player();
        }
        loadLeaderBoard();
    }

    @Override
    public void show() {
        isGameOver = false;
        ship = new Ship(SCR_WIDTH/2, 200);
        main.player.clear();
        enemies.clear();
        fragments.clear();
        shots.clear();
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
        if (controls == ACCELEROMETER){
            ship.vx = -Gdx.input.getAccelerometerX()*5;
            ship.vy = -Gdx.input.getAccelerometerY()*5;
        }

        // события
        for (Space s: space) s.move();
        spawnEnemy();
        for (int i = enemies.size()-1; i >= 0; i--) {
            enemies.get(i).move();
            if(enemies.get(i).outOfScreen() || enemies.get(i).overlap(ship)){
                spawnFragments(enemies.get(i));
                enemies.remove(i);
                if(!isGameOver) gameOver();
            }
        }
        for (int i = shots.size()-1; i>=0; i--) {
            shots.get(i).move();
            if(shots.get(i).outOfScreen()) shots.remove(i);
        }
        for (int i = shots.size()-1; i>=0; i--) {
            for (int j = enemies.size()-1; j>=0; j--) {
                if(shots.get(i).overlap(enemies.get(j))){
                    if(--enemies.get(j).hp == 0) {
                        playerKillCounts(enemies.get(j));
                        spawnFragments(enemies.get(j));
                        enemies.remove(j);
                    }
                    shots.remove(i);
                    if (isSoundOn) sndExplosion.play();
                    break;
                }
            }
        }
        for (int i = fragments.size()-1; i>=0; i--) {
            fragments.get(i).move();
            if(fragments.get(i).outOfScreen()) fragments.remove(i);
        }
        if(!isGameOver) {
            ship.move();
            spawnShot();
        }

        // отрисовка
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for(Space s: space) batch.draw(imgBG, s.x, s.y, s.width, s.height);
        if(controls == JOYSTICK){
            batch.draw(imgJoystick, main.joystick.scrX(), main.joystick.scrY(), main.joystick.width, main.joystick.height);
        }
        for (Fragment f: fragments) {
            batch.draw(imgFragment[f.type][f.numImg], f.scrX(), f.scrY(), f.width/2, f.height/2, f.width, f.height, 1, 1, f.rotation);
        }
        for (Enemy e: enemies) {
            batch.draw(imgEnemy[e.type][e.phase], e.scrX(), e.scrY(), e.width, e.height);
        }
        for(Shot s: shots) {
            batch.draw(imgShot, s.scrX(), s.scrY(), s.width, s.height);
        }
        batch.draw(imgShip[ship.phase], ship.scrX(), ship.scrY(), ship.width, ship.height);
        font50white.draw(batch, "Score: "+main.player.score, 10, 1580);
        if(isGameOver){
            font100lightGreen.draw(batch, "GAME OVER", 0, 1300, SCR_WIDTH, Align.center, true);
            font50white.draw(batch, "score", 450, 1170, 150, Align.right, false);
            font50white.draw(batch, "kills", 600, 1170, 150, Align.right, false);
            for (int i = 0; i < players.length; i++) {
                font50white.draw(batch, i+1+" ", 140, 1100-i*80);
                font50white.draw(batch, players[i].name, 200, 1100-i*80);
                font50white.draw(batch, ""+players[i].score, 450, 1100-i*80, 150, Align.right, false);
                font50white.draw(batch, ""+players[i].kills, 600, 1100-i*80, 150, Align.right, false);
            }
        }
        /*for (int i = 0; i < ship.hp; i++) {
            batch.draw(imgShip[0], SCR_WIDTH-i*70-140, 1600-70, 60, 60);
        }*/
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
        imgShotsAtlas.dispose();
        imgShipsAtlas.dispose();
        imgJoystick.dispose();
        imgBG.dispose();
        sndExplosion.dispose();
        sndBlaster.dispose();
    }

    private void gameOver(){
        if (isSoundOn) sndExplosion.play();
        spawnFragments(ship);
        isGameOver = true;
        ship.x = -10000;
        sortLeaderBoard();
        saveLeaderBoard();
    }

    private void spawnEnemy(){
        if(TimeUtils.millis()>timeLastSpawnEnemy+timeSpawnEnemyInterval){
            timeLastSpawnEnemy = TimeUtils.millis();
            enemies.add(new Enemy());
        }
    }

    private void spawnShot(){
        if(TimeUtils.millis()>timeLastSpawnShot+timeSpawnShotsInterval){
            timeLastSpawnShot = TimeUtils.millis();
            shots.add(new Shot(ship.x-60, ship.y+20));
            shots.add(new Shot(ship.x+60, ship.y+20));
            if(isSoundOn) sndBlaster.play();
        }
    }

    private void spawnFragments(SpaceObject o){
        for (int i = 0; i < nFragments; i++) {
            fragments.add(new Fragment(o.x, o.y, o.type, imgFragment[0].length));
        }
    }

    private void playerKillCounts(Enemy e){
        main.player.kills++;
        main.player.killedType[e.type]++;
        main.player.score += e.price;
    }

    private void sortLeaderBoard(){
        if(main.player.score >= players[players.length-1].score) {
            players[players.length - 1].clone(main.player);
            for (int j = 0; j < players.length; j++) {
                for (int i = 0; i < players.length - 1; i++) {
                    if (players[i].score < players[i + 1].score) {
                        Player p = players[i];
                        players[i] = players[i + 1];
                        players[i + 1] = p;
                    }
                }
            }
        }
    }

    public void saveLeaderBoard(){
        Preferences prefs = Gdx.app.getPreferences("RaideSpaceXLeaderBoard");
        for (int i = 0; i < players.length; i++) {
            prefs.putString("name"+i, players[i].name);
            prefs.putInteger("kills"+i, players[i].kills);
            prefs.putInteger("score"+i, players[i].score);
            for (int j = 0; j < players[i].killedType.length; j++) {
                prefs.putInteger("killedType"+i+"."+j, players[i].killedType[j]);
            }
        }
        prefs.flush();
    }

    private void loadLeaderBoard(){
        Preferences prefs = Gdx.app.getPreferences("RaideSpaceXLeaderBoard");
        for (int i = 0; i < players.length; i++) {
            players[i].name = prefs.getString("name"+i, "Noname");
            players[i].kills = prefs.getInteger("kills"+i, 0);
            players[i].score = prefs.getInteger("score"+i, 0);
            for (int j = 0; j < players[i].killedType.length; j++) {
                players[i].killedType[j] = prefs.getInteger("killedType"+i+"."+j, 0);
            }
        }
    }

    public void clearLeaderBoard(){
        for (int i = 0; i < players.length; i++) {
            players[i].name = "Noname";
            players[i].kills = 0;
            players[i].score = 0;
            for (int j = 0; j < players[i].killedType.length; j++) {
                players[i].killedType[j] = 0;
            }
        }
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
            touch.set(screenX, screenY, 0);
            camera.unproject(touch);
            if(controls == SCREEN) {
                ship.touchScreen(touch);
            }
            if(controls == JOYSTICK) {
                if(main.joystick.isTouchInside(touch)) {
                    ship.touchJoystick(touch, main.joystick);
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
            touch.set(screenX, screenY, 0);
            camera.unproject(touch);
            if(controls == SCREEN) {
                ship.touchScreen(touch);
            }
            if(controls == JOYSTICK) {
                if(main.joystick.isTouchInside(touch)) {
                    ship.touchJoystick(touch, main.joystick);
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
