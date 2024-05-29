import processing.core.PApplet;
import processing.core.PImage;

public class Sketch extends PApplet {
    
/**
  * A class that 
  * @author A. Razack
  *
  */

  // Define game states and variables for cutscenes
  final int OPENING = 0;
  final int MENU = 1;
  final int CUTSCENE = 2;
  final int GAME = 3;
    
  int state = OPENING;
    
  // Exstablish variables for the cutscene time
  int cutsceneStartTime;
  int openingDuration = 17000; 
  int cutsceneDuration = 21000; 

  Opening Opening;
  boolean OpeningDisplayed = false;

  Cutscene Cutscene;
  boolean CutsceneDisplayed = false;

  // Variables for animated background with image frames
  PImage[] frames;
  int numFrames = 5; 
  int currentFrame = 0;
  int frameDuration = 100; 
  int lastFrameChangeTime = 0;

  @Override
  public void settings() {
    size(900, 500);
  }

  @Override
  public void setup() {
    cutsceneStartTime = millis();

    // Initialize and load image frames
    frames = new PImage[numFrames];
    for (int i = 0; i < numFrames; i++) {
      frames[i] = loadImage("menuframe" + i + ".png");
    }
  }

  @Override
  public void draw() {
    switch (state) {
    case OPENING:
      drawOpening();
      if (millis() - cutsceneStartTime > openingDuration) {
        state = MENU;
        if (Opening!= null) {
          Opening.close();  
        }
      }
      break;

    case MENU:
      drawMenu();
      break;

    case CUTSCENE:
      drawCutscene();
      if (millis() - cutsceneStartTime > cutsceneDuration) {
        state = GAME;
        if (Cutscene!= null) {
          Cutscene.close();  
        }
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
     // Draw animated background using image frames
     if (millis() - lastFrameChangeTime > frameDuration) {
      currentFrame = (currentFrame + 1) % numFrames;
      lastFrameChangeTime = millis();
    }
    image(frames[currentFrame], 0, 0, width, height);
    fill(255);
    textSize(32);
    textAlign(CENTER, CENTER);
    text("Press 'Enter' to Start Next Cutscene", width / 2, height / 2);
  }

  void drawCutscene() {
    background(0);
     if (!CutsceneDisplayed) {
      Cutscene = new Cutscene();
      CutsceneDisplayed = true;
    }
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
      state = CUTSCENE;
      cutsceneStartTime = millis();
    }
  }
}