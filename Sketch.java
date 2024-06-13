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
  PImage[][] playerImages;
  PImage[] playerRestImages;
  int[] playerFrameCounts = {8, 8, 8, 8}; // Number of animation frames (2-9) for each direction
  int[] currentPlayerFrame = {0, 0, 0, 0}; // Current frame index for each direction
  int playerFrameDuration = 100; // Duration for each frame of animation
  int lastPlayerFrameChangeTime = 0; // Last time the player frame was changed
  int playerDirection = 0;
  float playerX = 150;
  float playerY = 150;
  float playerWidth = 64;
  float playerLength = 64;
  float playerSpeed = 2;

  PImage overworldBackground;
  PImage borderTree;
  PImage river;
  Plank plank;


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
    playerImages = new PImage[4][];
    playerRestImages = new PImage[4];
    
    for (int i = 0; i < 4; i++) {
      playerImages[i] = new PImage[playerFrameCounts[i]];
    }

    // Load walking frames (2-9) for each direction
    for (int i = 0; i < playerFrameCounts[0]; i++) {
      playerImages[0][i] = loadImage("Photos, GIFs, Videos, Music/walkingright" + (i + 2) + ".png");
    }
    for (int i = 0; i < playerFrameCounts[1]; i++) {
      playerImages[1][i] = loadImage("Photos, GIFs, Videos, Music/walkingleft" + (i + 2) + ".png");
    }
    for (int i = 0; i < playerFrameCounts[2]; i++) {
      playerImages[2][i] = loadImage("Photos, GIFs, Videos, Music/walkingup" + (i + 2) + ".png");
    }
    for (int i = 0; i < playerFrameCounts[3]; i++) {
      playerImages[3][i] = loadImage("Photos, GIFs, Videos, Music/walkingdown" + (i + 2) + ".png");
    }

    // Load resting frames (1) for each direction
    playerRestImages[0] = loadImage("Photos, GIFs, Videos, Music/walkingright1.png");
    playerRestImages[1] = loadImage("Photos, GIFs, Videos, Music/walkingleft1.png");
    playerRestImages[2] = loadImage("Photos, GIFs, Videos, Music/walkingup1.png");
    playerRestImages[3] = loadImage("Photos, GIFs, Videos, Music/walkingdown1.png");

    overworldBackground = loadImage("Photos, GIFs, Videos, Music/world.png");
    borderTree = loadImage("Photos, GIFs, Videos, Music/bordertree.png");
    river = loadImage("Photos, GIFs, Videos, Music/water.png");
    plank = new Plank(2240, 1330);
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
    // Clear the screen to a black background
    background(0);

    float offsetX = width / 2 - playerX - playerWidth / 2;
    float offsetY = height / 2 - playerY - playerLength / 2;

    pushMatrix();
    translate(offsetX, offsetY);

    image(overworldBackground, 0, 0);
    drawObstacles();
    handlePlayerMovement();

    popMatrix();

    // Draw the plank if it's not collected
    if (!plank.isCollected()) {
      plank.display();
    }
  }
  
  void drawObstacles() {
    // Draw rectangles along the top and bottom sides
    for (int x = 0; x < overworldBackground.width; x += 32) {
      if (!(x >= 2200 ))
      image(borderTree, x, 0, 32, 51); // Top side
      image(borderTree, x, overworldBackground.height - 51, 32, 51); // Bottom side
    }
    
    // Draw rectangles along the left and right sides
    for (int y = 0; y < overworldBackground.height; y += 51) {
      image(borderTree, 0, y, 32, 51); // Left side
      image(borderTree, overworldBackground.width - 32, y, 32, 51); // Right side
    }

    // Draw rectangles at (356, 20) to (356, 360) every 20 pixels
    for (int y = 20; y <= 360; y += 20) {
      image(borderTree, 356, y, 32, 51);
    }

    // Draw rectangles at (700, 320) to (700, 660) every 20 pixels
    for (int y = 320; y <= 660; y += 20) {
      image(borderTree, 700, y, 32, 51);
    }

    // Draw rectangles at (700, 660) to (0, 660) every 20 pixels
    for (int x = 700; x >= 0; x -= 20) {
      image(borderTree, x, 660, 32, 51);
    }

    // Draw rectangles at (0, 840) to (720, 840) every 20 pixels
    for (int x = 0; x <= 720; x += 20) {
      image(borderTree, x, 840, 32, 51);
    }

    // Draw rectangles at (720, 840) to (720, 1020) every 20 pixels
    for (int y = 840; y <= 1020; y += 20) {
      image(borderTree, 720, y, 32, 51);
    }

    // Draw rectangles at (380, 1020) to (380, 1240) every 20 pixels
    for (int y = 1020; y <= 1240; y += 20) {
      image(borderTree, 380, y, 32, 51);
    }

    // Draw rectangles at (380, 1240) to (1040, 1240) every 20 pixels
    for (int x = 380; x <= 1040; x += 20) {
      image(borderTree, x, 1240, 32, 51);
    }

    // Draw rectangles at (1040, 20) to (1040, 1240) every 20 pixels
    for (int y = 20; y <= 1240; y += 20) {
      image(borderTree, 1040, y, 32, 51);
    }

    // Draw rectangles at (1680, 620) to (2340, 620) every 20 pixels
    for (int x = 1680; x <= 2340; x += 20) {
      image(borderTree, x, 620, 32, 51);
    }

    // Draw rectangles at (1360, 820) to (1360, 1240) every 20 pixels
    for (int y = 820; y <= 1240; y += 20) {
      image(borderTree, 1360, y, 32, 51);
    }

    // Draw rectangles at (1360, 1240) to (2340, 1240) every 20 pixels
    for (int x = 1360; x <= 2340; x += 20) {
      image(borderTree, x, 1240, 32, 51);
    }

    // Draw sqaures at (1360, 820) to (1360, 1240) every 20 pixels (Skip drawing at (2240, 1330) if plank is collected)
    for (int y = 0; y <= 608; y += 16) {
      if (!(plank.isCollected() && playerX >= 2240 && playerX <= 2256 && playerY >= 1320 && playerY <= 1336)) {
          image(river, 1900, y, 16, 16);
      }
    }
  }
  
  void handlePlayerMovement() {
    // Determine the current frame or resting image
    PImage currentPlayerImage;
    if (upPressed || downPressed || leftPressed || rightPressed) {
      if (millis() - lastPlayerFrameChangeTime > playerFrameDuration) {
        currentPlayerFrame[playerDirection] = (currentPlayerFrame[playerDirection] + 1) % playerFrameCounts[playerDirection];
        lastPlayerFrameChangeTime = millis();
      }
      currentPlayerImage = playerImages[playerDirection][currentPlayerFrame[playerDirection]];
    } 
    else {
      currentPlayerFrame[playerDirection] = 0;
      currentPlayerImage = playerRestImages[playerDirection];
    }

    // Check collisions with obstacles before updating player position
    float nextPlayerX = playerX;
    float nextPlayerY = playerY;

    if (upPressed) {
        nextPlayerY -= playerSpeed;
    }
    if (downPressed) {
        nextPlayerY += playerSpeed;
    }
    if (leftPressed) {
        nextPlayerX -= playerSpeed;
    }
    if (rightPressed) {
        nextPlayerX += playerSpeed;
    }

    // Check for collisions with each obstacle
    boolean collisionDetected = false;

    // Collision detection with side borders
    for (int x = 0; x < overworldBackground.width; x += 32) {
      if (!(x >= 2200)) {
        // Top side
        if (nextPlayerX + playerWidth > x && nextPlayerX < x + 32) {
          if (nextPlayerY + playerLength > 0 && nextPlayerY < 51) {
            collisionDetected = true;
          }
        }
        // Bottom side
        if (nextPlayerX + playerWidth > x && nextPlayerX < x + 32) {
          if (nextPlayerY + playerLength > overworldBackground.height - 51 && nextPlayerY < overworldBackground.height) {
            collisionDetected = true;
          }
        }
      }
    }

    // Left side
    for (int y = 0; y < overworldBackground.height; y += 51) {
      if (nextPlayerX + playerWidth > 0 && nextPlayerX < 32) {
        if (nextPlayerY + playerLength > y && nextPlayerY < y + 51) {
          collisionDetected = true;
        }
      }
    }

  // Right side
  for (int y = 0; y < overworldBackground.height; y += 51) {
    if (nextPlayerX + playerWidth > overworldBackground.width - 32 && nextPlayerX < overworldBackground.width) {
      if (nextPlayerY + playerLength > y && nextPlayerY < y + 51) {
        collisionDetected = true;
      }
    }
  }

  if ((nextPlayerX >= 316 && nextPlayerX <= 360 && nextPlayerY >= 20 && nextPlayerY <= 360) ||
    (nextPlayerX >= 660 && nextPlayerX <= 704 && nextPlayerY >= 270 && nextPlayerY <= 660) ||
    (nextPlayerX >= 0 && nextPlayerX <= 704 && nextPlayerY >= 610 && nextPlayerY <= 660) ||
    (nextPlayerX >= 0 && nextPlayerX <= 724 && nextPlayerY >= 780 && nextPlayerY <= 850) ||
    (nextPlayerX >= 680 && nextPlayerX <= 724 && nextPlayerY >= 850 && nextPlayerY <= 1020) ||
    (nextPlayerX >= 340 && nextPlayerX <= 384 && nextPlayerY >= 980 && nextPlayerY <= 1240) ||
    (nextPlayerX >= 360 && nextPlayerX <= 1050 && nextPlayerY >= 1180 && nextPlayerY <= 1240)||
    (nextPlayerX >= 1640 && nextPlayerX <= 2340 && nextPlayerY >= 560 && nextPlayerY <= 630)||
    (nextPlayerX >= 1320 && nextPlayerX <= 2340 && nextPlayerY >= 1180 && nextPlayerY <= 1250)||
    (nextPlayerX >= 1320 && nextPlayerX <= 1360 && nextPlayerY >= 760 && nextPlayerY <= 1250)||
    (nextPlayerX >= 1000 && nextPlayerX <= 1050 && nextPlayerY >= 0 && nextPlayerY <= 1240)||
    (nextPlayerX >= 1860 && nextPlayerX <= 1876 && nextPlayerY >= 0 && nextPlayerY <= 600)) {
    collisionDetected = true;
  }

  // If no collision detected, update player position
  if (!collisionDetected) {
    playerX = nextPlayerX;
    playerY = nextPlayerY;
  }

  // Draw the player
  image(currentPlayerImage, playerX, playerY, playerWidth, playerLength);
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
    else if (state == CUTSCENE && keyCode == ENTER) {
      state = GAME;
      if (Cutscene != null) {
        Cutscene.close();
      }
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

    // Check for collecting the plank
    if (!plank.isCollected() && playerX >= 2240 && playerX <= 2256 && playerY >= 1320 && playerY <= 1336) {
      plank.collected = true;
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