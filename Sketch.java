import processing.core.PApplet;
import processing.core.PImage;

public class Sketch extends PApplet {
    
    
  /**
   * Called once at the beginning of execution, put your size all in this method
   */

  // Define game states and variables for cutscenes
  final int OPENING = 0;
  final int MENU = 1;
  final int CUTSCENE1 = 2;
  final int GAME = 3;
    
  int state = OPENING;
  PImage cutscene1;
    
  // Exstablish variables for the cutscene time
  int cutsceneStartTime;
  int openingDuration = 16500; 
  int cutscene1Duration = 5000; 

  Opening Opening;
  boolean OpeningDisplayed = false;

  @Override
  public void settings() {
    size(900, 500);
  }

  @Override
  public void setup() {
    cutscene1 = loadImage("Photos, GIFs, Videos, Music/night-sky-glows-with-iridescent-deep-space-generative-ai.jpg");
    cutsceneStartTime = millis();
  }

  @Override
  public void draw() {
    switch (state) {
    case OPENING:
      drawOpening();
      if (millis() - cutsceneStartTime > openingDuration) {
        state = MENU;
        if (Opening != null) {
          Opening.close();  
        }
      }
      break;

    case MENU:
      drawMenu();
      break;

    case CUTSCENE1:
      drawCutscene1();
      if (millis() - cutsceneStartTime > cutscene1Duration) {
        state = GAME;
      }
      break;

    case GAME:
      drawGame();
      break;
    }
  }

  void drawOpening() {
    background(0);
    if (!OpeningDisplayed) {
      Opening = new Opening();
      OpeningDisplayed = true;
    }
  }

  void drawMenu() {
    background(100);
    fill(255);
    textSize(32);
    textAlign(CENTER, CENTER);
    text("Press 'Enter' to Start Next Cutscene", width / 2, height / 2);
  }

  void drawCutscene1() {
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
      state = CUTSCENE1;
      cutsceneStartTime = millis();
    }
  }
}