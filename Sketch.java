import processing.core.PApplet;
import processing.core.PFont;
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

  // Variables for animated background with image frames and fonts
  PImage[] frames;
  int numFrames = 8; 
  int currentFrame = 0;
  int frameDuration = 100; 
  int lastFrameChangeTime = 0;

  PFont titleFont;
  PFont menuFont;

  boolean showEnterText = true;
  int lastBlinkTime = 0;
  int blinkInterval = 500;

  static boolean isFinished = false;

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
      frames[i] = loadImage("Photos, GIFs, Videos, Music/menuframe" + i + ".png");
    }

    // Load custom fonts
    titleFont = createFont("Photos, GIFs, Videos, Music/The Centurion .ttf", 64);
    menuFont = createFont("Photos, GIFs, Videos, Music/CloisterBlack.ttf", 32, true);
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
        isFinished = true;
      }
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

    // Draw blinking menu and title text
    drawTextWithBorder("Heroes of Valoria", width / 2, height / 4, titleFont, 64, color(255), color(0));
   
    if (millis() - lastBlinkTime > blinkInterval) {
      showEnterText = !showEnterText;
      lastBlinkTime = millis();
    }

    if (showEnterText) {
      drawTextWithBorder("Press 'Enter' to Begin", width / 2, height / 2, menuFont, 32, color(255), color(0));
    }
  }

  void drawCutscene() {
    background(0);
     if (!CutsceneDisplayed) {
      Cutscene = new Cutscene();
      CutsceneDisplayed = true;
    }
  }

  void drawTextWithBorder(String text, float x, float y, PFont font, int size, int fillColor, int borderColor) {
    // Draw black borders alongside any text
    textFont(font);
    textSize(size);
    textAlign(CENTER, CENTER);

    fill(borderColor);
    text(text, x - 1, y);
    text(text, x + 1, y);
    text(text, x, y - 1);
    text(text, x, y + 1);
  
    fill(fillColor);
    text(text, x, y);
  }

  @Override
  public void keyPressed() {
    if (state == MENU && keyCode == ENTER) {
      state = CUTSCENE;
      cutsceneStartTime = millis();
    }
  }
}