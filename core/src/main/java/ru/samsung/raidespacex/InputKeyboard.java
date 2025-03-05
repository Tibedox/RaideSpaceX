package ru.samsung.raidespacex;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

/**
 * 1. копируем класс InputKeyboard.java в пакет приложения,
 *    копируем в assets атлас изображений кнопок keys.png
 * 2. в поле главного класса создаём ссылку
 *    InputKeyboard keyboard;
 * 3. в методе create создаём объект
 *    keyboard = new InputKeyboard(font, SCR_WIDTH, SCR_HEIGHT, 12);
 * 4. когда требуется включить клавиатуру, вызываем
 *    keyboard.start();
 *    клавиатура работает до нажатия Enter, после нажатия выключится сама
 * 5. в методе render передаём в клавиатуру координаты касания и если touch вернул true,
 *    то завершаем ввод и передаём введённый текст в переменную name, а клавиатура исчезает
 *    if (keyboard.touch(touch.x, touch.y)) name = keyboard.getText();
 * 6. в batch рисуем клавиатуру, она будет рисоваться только после вызова keyboard.start()
 *    keyboard.draw(batch);
 * 7. в методе dispose удаляем объект
 *    keyboard.dispose();
 */
public class InputKeyboard {
    String keysFileName = "keys.png";
    private final BitmapFont font;

    private final float x, y; // координаты
    private final float keyboardWidth, keyboardHeight; // ширина и высота всей клавиатуры
    private final float keyWidth, keyHeight; // ширина и высота каждой кнопки
    private final float padding = 0; // расстояние между кнопками
    private final int enterTextLength; // длина вводимого текста

    boolean isKeyboardShow;
    private boolean endOfEdit;

    private String text = ""; // вводимый текст
    // текст на кнопках
    private static final String LETTERS_EN_CAPS = "1234567890-~QWERTYUIOP+?^ASDFGHJKL;'`ZXCVBNM<> |"; // английский без shift
    private static final String LETTERS_EN_LOW  = "!@#$%:&*()_~qwertyuiop[]^asdfghjkl:'`zxcvbnm,. |"; // английский c shift
    private static final String LETTERS_RU_CAPS = "1234567890-~ЙЦУКЕНГШЩЗХЪ^ФЫВАПРОЛДЖЭ`ЯЧСМИТЬБЮЁ|"; // русский без shift
    private static final String LETTERS_RU_LOW  = "!@#$%:&*()_~йцукенгшщзхъ^фывапролджэ`ячсмитьбюё|"; // русский с shift
    private String letters = LETTERS_EN_CAPS;

    private final Texture imgAtlasKeys; // все изображения кнопок
    private final TextureRegion imgEditText; // поле ввода
    private final TextureRegion imgKeyUP, imgKeyDown; // кнопка выпуклая/вдавленная
    private final TextureRegion imgKeyBS, imgKeyEnter, imgKeyCL, imgKeySW; // картинки управляющих кноп

    private long timeStartPressKey, timeDurationPressKey = 150; // длительность надавливания кнопки
    private int keyPressed = -1; // код нажатой кнопки
    private final Array<Key> keys = new Array<>(); // список всех кноп

    public InputKeyboard(BitmapFont font, float scrWidth, float scrHeight, int enterTextLength){
        this.font = font;
        this.enterTextLength = enterTextLength; // количество вводимых символов

        imgAtlasKeys = new Texture(keysFileName);
        imgKeyUP = new TextureRegion(imgAtlasKeys, 0, 0, 256, 256);
        imgKeyDown = new TextureRegion(imgAtlasKeys, 256, 0, 256, 256);
        imgEditText = new TextureRegion(imgAtlasKeys, 256*2, 0, 256, 256);
        imgKeyBS = new TextureRegion(imgAtlasKeys, 256*3, 0, 256, 256);
        imgKeyEnter = new TextureRegion(imgAtlasKeys, 256*4, 0, 256, 256);
        imgKeyCL = new TextureRegion(imgAtlasKeys, 256*5, 0, 256, 256);
        imgKeySW = new TextureRegion(imgAtlasKeys, 256*6, 0, 256, 256);

        // задаём параметры клавиатуры
        keyboardWidth = scrWidth/21f*20;
        keyboardHeight = scrHeight/5f*3;
        x = (scrWidth- keyboardWidth)/2;
        y = keyboardHeight +scrHeight/30f;
        keyWidth = keyboardWidth/13;
        keyHeight = keyboardHeight/5;
        createKBD();
    }

    // создание кнопок клавиатуры по рядам
    private void createKBD(){
        int j = 0;
        for (int i = 0; i < 12; i++, j++)
            keys.add(new Key(i*keyWidth+x+keyWidth/2, y-keyHeight*2, keyWidth-padding, keyHeight-padding, letters.charAt(j)));

        for (int i = 0; i < 13; i++, j++)
            keys.add(new Key(i*keyWidth+x, y-keyHeight*3, keyWidth-padding, keyHeight-padding, letters.charAt(j)));

        for (int i = 0; i < 12; i++, j++)
            keys.add(new Key(i*keyWidth+x+keyWidth/2, y-keyHeight*4, keyWidth-padding, keyHeight-padding, letters.charAt(j)));

        for (int i = 0; i < 11; i++, j++)
            keys.add(new Key(i*keyWidth+x+keyWidth, y-keyHeight*5, keyWidth-padding, keyHeight-padding, letters.charAt(j)));
    }

    // задаём/меняем раскладку символов на всех кнопках
    private void setCharsKBD() {
        int j = 0;
        for (int i = 0; i < 12; i++, j++)
            keys.get(j).letter = letters.charAt(j);

        for (int i = 0; i < 13; i++, j++)
            keys.get(j).letter = letters.charAt(j);

        for (int i = 0; i < 12; i++, j++)
            keys.get(j).letter = letters.charAt(j);

        for (int i = 0; i < 11; i++, j++)
            keys.get(j).letter = letters.charAt(j);
    }

    // рисуем клавиатуру и вводимый текст
    public void draw(SpriteBatch batch){
        if(isKeyboardShow) {
            // рисуем кнопки
            for (int i = 0; i < keys.size; i++) {
                drawImgKey(batch, i, keys.get(i).x, keys.get(i).y, keys.get(i).width, keys.get(i).height);
            }
            // рисуем вводимый текст
            batch.draw(imgEditText, 2 * keyWidth + x + keyWidth / 2, y - keyHeight, keyboardWidth - 5 * keyWidth - padding, keyHeight);
            font.draw(batch, text, 2 * keyWidth + x + keyWidth / 2, keys.get(0).letterY + keyHeight, keyboardWidth - 5 * keyWidth - padding, Align.center, false);
        }
    }

    // рисуем каждую кнопку
    private void drawImgKey(SpriteBatch batch, int i, float x, float y, float width, float height){
        float dx, dy;
        if(keyPressed == i){ // если нажата, то рисуем нажатую кнопку
            batch.draw(imgKeyDown, x, y, width, height);
            dx = 2;
            dy = -2;
            if(TimeUtils.millis() - timeStartPressKey > timeDurationPressKey){
                keyPressed = -1;
            }
        } else { // рисуем отжатую кнопку
            dx = 0;
            dy = 0;
            batch.draw(imgKeyUP, x, y, width, height);
        }

        // выводим символы на кнопки
        switch (letters.charAt(i)) {
            case '~': batch.draw(imgKeyBS, x+dx, y+dy, width, height); break; // backspace
            case '^': batch.draw(imgKeyEnter, x+dx, y+dy, width, height); break; // enter
            case '`': batch.draw(imgKeyCL, x+dx, y+dy, width, height); break; // caps lock
            case '|': batch.draw(imgKeySW, x+dx, y+dy, width, height); break; // ru/en switcher
            default: // все прочие символы
                font.draw(batch, ""+keys.get(i).letter, keys.get(i).letterX+dx, keys.get(i).letterY+dy);
        }
    }

    // проверяем, куда нажали
    public boolean touch(float tx, float ty){
        if(isKeyboardShow) {
            for (int i = 0; i < keys.size; i++) {
                if (!keys.get(i).hit(tx, ty).isEmpty()) {
                    keyPressed = i;
                    setText(i);
                    timeStartPressKey = TimeUtils.millis();
                }
            }
            // окончание редактирования ввода (нажата кнопка enter)
            if (endOfEdit) {
                endOfEdit = false;
                isKeyboardShow = false;
                return true;
            }
        }
        return false;
    }
    // проверяем, куда нажали - перегрузка
    public boolean touch(Vector3 t){
        if(isKeyboardShow) {
            for (int i = 0; i < keys.size; i++) {
                if (!keys.get(i).hit(t.x, t.y).isEmpty()) {
                    keyPressed = i;
                    setText(i);
                    timeStartPressKey = TimeUtils.millis();
                }
            }
            // окончание редактирования ввода (нажата кнопка enter)
            if (endOfEdit) {
                endOfEdit = false;
                isKeyboardShow = false;
                return true;
            }
        }
        return false;
    }

    // обработка нажатия кнопок
    private void setText(int i){
        switch (letters.charAt(i)) {
            case '~': // backspace
                if(!text.isEmpty()) text = text.substring(0, text.length() - 1);
                break;
            case '^': // enter
                if(text.isEmpty()) break;
                endOfEdit = true;
                break;
            case '`': // caps lock
                if(letters.charAt(12) == 'Q') letters = LETTERS_EN_LOW;
                else if(letters.charAt(12) == 'q') letters = LETTERS_EN_CAPS;
                else if(letters.charAt(12) == 'Й') letters = LETTERS_RU_LOW;
                else if(letters.charAt(12) == 'й') letters = LETTERS_RU_CAPS;
                setCharsKBD();
                break;
            case '|': // ru/en switcher
                if(letters.charAt(12) == 'й') letters = LETTERS_EN_LOW;
                else if(letters.charAt(12) == 'Й') letters = LETTERS_EN_CAPS;
                else if(letters.charAt(12) == 'q') letters = LETTERS_RU_LOW;
                else if(letters.charAt(12) == 'Q') letters = LETTERS_RU_CAPS;
                setCharsKBD();
                break;
            default: // ввод символов
                if(text.length() < enterTextLength) text += letters.charAt(i);
                //if(text.length() == 1 && letters.equals(LETTERS_EN_CAPS)) letters = LETTERS_EN_LOW;
                //if(text.length() == 1 && letters.equals(LETTERS_RU_CAPS)) letters = LETTERS_RU_LOW;
                setCharsKBD();
        }
    }

    // выдача отредактированного текста
    public String getText() {
        return text;
    }

    // класс отдельной кнопки виртуальной клавиатуры
    private class Key {
        float x, y;
        float width, height;
        char letter; // символ на кнопке
        float letterX, letterY; // координаты вывода символа

        private Key (float x, float y, float width, float height, char letter) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.letter = letter;
            letterX = x + width/3;
            letterY = y + height - (height - font.getCapHeight())/2;
        }

        private String hit(float tx, float ty){
            if (x<tx && tx<x+width && y<ty && ty<y+height) {
                return "" + letter;
            }
            return "";
        }
    }

    public void start(){
        isKeyboardShow = true;
    }

    public void dispose(){
        imgAtlasKeys.dispose();
    }
}
