import processing.core.PApplet;
import processing.core.PImage;

  public class Sketch extends PApplet {
    
    
    /**
     * Called once at the beginning of execution, put your size all in this method
     */

    // Define game states and variables for cutscenes
    final int OPENING = 0;
    final int MENU = 1;
    final int CUTSCENE2 = 2;
    final int GAME = 3;
    
    int state = OPENING;

    PImage opening;
    PImage cutscene1;
    
    // Exstablish variables for the cutscene time
    int cutsceneStartTime;
    int cutsceneDuration = 5000; 

    public static void main(String[] args) {
        String[] processingArgs = {"MySketch"};
        Sketch mySketch = new Sketch();
        PApplet.runSketch(processingArgs, mySketch);
    }

    @Override
    public void settings() {
        size(800, 600);
    }

    @Override
    public void setup() {
        opening = loadImage("cutscene1.gif");
        cutscene1 = loadImage("cutscene2.gif");
        
        cutsceneStartTime = millis();
    }

    @Override
    public void draw() {
        switch (state) {
            case OPENING:
                drawCutscene1();
                if (millis() - cutsceneStartTime > cutsceneDuration) {
                    state = MENU;
                }
                break;
            case MENU:
                drawMenu();
                break;
            case CUTSCENE2:
                drawCutscene2();
                if (millis() - cutsceneStartTime > cutsceneDuration) {
                    state = GAME;
                }
                break;
            case GAME:
                drawGame();
                break;
        }
    }

    void drawCutscene1() {
        background(0);
        image(cutscene1, 0, 0, width, height);
    }

    void drawMenu() {
        background(100);
        fill(255);
        textSize(32);
        textAlign(CENTER, CENTER);
        text("Press 'Enter' to Start Next Cutscene", width / 2, height / 2);
    }

    void drawCutscene2() {
        background(0);
        image(cutscene1, 0, 0, width, height);
    }

    void drawGame() {
        background(50);
        fill(255);
        textSize(32);
        textAlign(CENTER, CENTER);
        text("Game Screen", width / 2, height / 2);
    }

    @Override
    public void keyPressed() {
        if (state == MENU && keyCode == ENTER) {
            state = CUTSCENE2;
            cutsceneStartTime = millis();
        }
    }
  }