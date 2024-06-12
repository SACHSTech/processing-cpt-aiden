import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;

public class Sketch extends PApplet {
    
/**
  * A class that runs my game
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

  boolean upPressed = false;
  boolean downPressed = false;
  boolean leftPressed = false;
  boolean rightPressed = false;

  // Player images for different directions (0: right, 1: left, 2: up, 3: down)
  PImage[] playerImages;
  int playerDirection = 0;
  float playerX = 150;
  float playerY = 150;
  float playerWidth = 100;
  float playerLength = 100;
  float playerSpeed = 3;

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
    
    // Initialize and load player frames
    playerImages = new PImage[4];
    playerImages[0] = loadImage("Photos, GIFs, Videos, Music/walkingright1.png");
    playerImages[1] = loadImage("Photos, GIFs, Videos, Music/walkingleft1.png");
    playerImages[2] = loadImage("Photos, GIFs, Videos, Music/walkingup1.png");
    playerImages[3] = loadImage("Photos, GIFs, Videos, Music/walkingdown1.png");
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

  void drawGame() {
    background(32);
    image(playerImages[playerDirection], playerX, playerY, playerWidth, playerLength);

    if (upPressed) {
      playerY -= playerSpeed;
    }
    if (downPressed) {
      playerY += playerSpeed;
    }
    if (leftPressed) {
      playerX -= playerSpeed;
    }
    if (rightPressed) {
      playerX += playerSpeed;
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
  // start cutscene when enter key is pressed
  public void keyPressed() {
    if (state == MENU && keyCode == ENTER) {
      state = CUTSCENE;
      cutsceneStartTime = millis();
    }

    if (state == GAME) {
      // Update player position and direction based on key press
      if (keyCode == UP) {
        upPressed = true;
        playerDirection = 2;
      } else if (keyCode == DOWN) {
        downPressed = true;
        playerDirection = 3;
      } else if (keyCode == LEFT) {
        leftPressed = true;
        playerDirection = 1;
      } else if (keyCode == RIGHT) {
        rightPressed = true;
        playerDirection = 0;
      }
    }
  }

  public void keyReleased() {
    if (state == GAME) {
      if (keyCode == UP) {
        upPressed = false;
      }
      else if (keyCode == DOWN) {
        downPressed = false;
      }
      else if (keyCode == LEFT) {
        leftPressed = false;
     }
      else if (keyCode == RIGHT) {
       rightPressed = false;
      }
    }
  }
}