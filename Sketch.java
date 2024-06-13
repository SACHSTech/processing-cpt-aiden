import java.util.ArrayList;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import processing.core.PVector;

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
  int[] playerFrameCounts = {8, 8, 8, 8}; 
  int[] currentPlayerFrame = {0, 0, 0, 0};
  int playerFrameDuration = 100;
  int lastPlayerFrameChangeTime = 0; 
  int playerDirection = 0;
  float playerX = 150;
  float playerY = 150;
  float playerWidth = 64;
  float playerLength = 64;
  float playerSpeed = 2;
  // Player images for sword-swinging animations (0: right, 1: left, 2: up, 3: down)
  PImage[][] swordSwingingImages;
  int swordSwingingFrameCount = 6; 
  int[] currentSwordSwingingFrame = {0, 0, 0, 0}; 
  boolean isSwinging = false;
  int lastSwingingFrameChangeTime = 0;
  int swordSwingingFrameDuration = 100; 

  PImage overworldBackground;
  PImage borderTree;
  PImage river;

  PImage plankImage;
  boolean plankCollected = false;
  float plankX = 2240;
  float plankY = 1330;
  float plankWidth = 32;
  float plankHeight = 32;

  PImage monsterImg; // Image for the monster
  ArrayList<PVector> monsters; // List to store monster positions
  int maxMonsters = 25; // Total number of monsters
  double monsterSpeed = 1; // Speed of monsters
  float monsterImageSize = 32; // Size of the monster image
  final int CUT_DELAY = 500; // Delay in milliseconds after each cut
  final int MAX_CUTS = 3; // Maximum number of cuts before monster dies
  int[] monsterCuts;
  long[] lastMonsterCutTimes;

  int playerLives;
  PImage Lives;

  // Constants for cooldown
  int cooldownTime = 2000;
  long lastHitTime = 0;
  boolean inCooldown = false;


  @Override
  public void settings() {
    size(900, 500);

    playerLives = 5;
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

    // Initialize and load sword-swinging frames
    swordSwingingImages = new PImage[4][swordSwingingFrameCount];
    for (int i = 0; i < swordSwingingFrameCount; i++) {
      swordSwingingImages[0][i] = loadImage("Photos, GIFs, Videos, Music/swingright" + (i + 1) + ".png");
      swordSwingingImages[1][i] = loadImage("Photos, GIFs, Videos, Music/swingleft" + (i + 1) + ".png");
      swordSwingingImages[2][i] = loadImage("Photos, GIFs, Videos, Music/swingup" + (i + 1) + ".png");
      swordSwingingImages[3][i] = loadImage("Photos, GIFs, Videos, Music/swingdown" + (i + 1) + ".png");
    }

    overworldBackground = loadImage("Photos, GIFs, Videos, Music/world.png");
    borderTree = loadImage("Photos, GIFs, Videos, Music/bordertree.png");
    river = loadImage("Photos, GIFs, Videos, Music/water.png");
    plankImage = loadImage("Photos, GIFs, Videos, Music/plank.png");

    monsterImg = loadImage("download.png");
    monsters = new ArrayList<PVector>();
    monsterCuts = new int[maxMonsters];
    lastMonsterCutTimes = new long[maxMonsters];
    for (int i = 0; i < maxMonsters; i++) {
      monsterCuts[i] = 0;  // Initialize cuts for each monster to zero
      lastMonsterCutTimes[i] = 0;
    }
    spawnMonsters();

    Lives = loadImage("Photos, GIFs, Videos, Music/heart pixel art 254x254.png");
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

    float offsetX = width / 2 - playerX - 64 / 2;
    float offsetY = height / 2 - playerY - 64 / 2;

    pushMatrix();
    translate(offsetX, offsetY);

    image(overworldBackground, 0, 0);
    drawObstacles();
    handlePlayerMovement();

    // Draw plank if not collected
    if (!plankCollected) {
      image(plankImage, plankX, plankY, plankWidth, plankHeight);
    }

    // Reset sword-swinging state if animation is done
    if (isSwinging && millis() - lastSwingingFrameChangeTime > swordSwingingFrameDuration) {
      currentSwordSwingingFrame[playerDirection]++;
      if (currentSwordSwingingFrame[playerDirection] >= swordSwingingFrameCount) {
        isSwinging = false;
        currentSwordSwingingFrame[playerDirection] = 0;
      }
      lastSwingingFrameChangeTime = millis();
    }

    // Draw player hitbox
    float hitboxWidth = 40; 
    float hitboxHeight = 48; 
    float hitboxX = playerX + 14;
    float hitboxY = playerY + 16;

    // Loop through monsters
    for (int i = 0; i < monsters.size(); i++) {
      PVector monster = monsters.get(i);

      // Check if monster is cut 3 times
      if (monsterCuts[i] >= 3) {
        monsters.remove(i);
        monsterCuts[i] = 0; // Reset cuts for this monster
        lastMonsterCutTimes[i] = 0; // Reset last cut time for this monster
        continue; // Skip drawing this monster
      }

      // Calculate movement towards player
      float deltaX = playerX - monster.x;
      float deltaY = playerY - monster.y;
      float angle = atan2(deltaY, deltaX);

      // If monster is cut, stop its movement for half a second
      if (monsterCuts[i] > 0 && millis() - lastMonsterCutTimes[i] < 500) {
        // Do not update monster position
      } else {
        monster.x += cos(angle) * monsterSpeed;
        monster.y += sin(angle) * monsterSpeed;
      }

      // Draw monster image
      image(monsterImg, monster.x, monster.y, monsterImageSize, monsterImageSize);
      
      // Check collision between player hitbox and monster
      float monsterRadius = monsterImageSize / 2;
      float playerHitboxRadius = hitboxWidth / 2;

      // Calculate distance between player hitbox center and monster center
      float distance = dist(hitboxX + hitboxWidth / 2, hitboxY + hitboxHeight / 2, monster.x + monsterRadius, monster.y + monsterRadius);

      // Check if player hitbox and monster are touching
      if (distance < playerHitboxRadius + monsterRadius) {
        // Check if not in cooldown
        if (!inCooldown) {
          // Player hitbox touched the monster, decrement playerLives
          playerLives--;

          // Set cooldown timer
          inCooldown = true;
          lastHitTime = millis();
        }
      }
    }

    for (int i = 0; i < playerLives; i++) {
      float x = playerX - 280 - i * 30;
      float y = playerY - 200 ;
      fill(255);
      image(Lives, x, y, 20, 20);
    }

    if (millis() - lastHitTime > cooldownTime) {
      inCooldown = false;
    }

    if (playerLives <= 0) {
      background(255); 
      textSize(32);
      fill(0);
      text("Game Over", playerX, playerY);
    }
    popMatrix();
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
      if (plankCollected && y > 280 && y < 328) {
        continue; 
      }
      image(river, 1900, y, 16, 16);
    }
  }
  
  void handlePlayerMovement() {
    // Determine the current frame or resting image
    PImage currentPlayerImage;
    if (isSwinging) {
        currentPlayerImage = swordSwingingImages[playerDirection][currentSwordSwingingFrame[playerDirection]];
        // Set dimensions to 192x192 during swinging animation
        playerWidth = 192;
        playerLength = 192;
    } else if (upPressed || downPressed || leftPressed || rightPressed) {
        if (millis() - lastPlayerFrameChangeTime > playerFrameDuration) {
            currentPlayerFrame[playerDirection] = (currentPlayerFrame[playerDirection] + 1) % playerFrameCounts[playerDirection];
            lastPlayerFrameChangeTime = millis();
        }
        currentPlayerImage = playerImages[playerDirection][currentPlayerFrame[playerDirection]];
        // Reset dimensions to default size (64x64)
        playerWidth = 64;
        playerLength = 64;
    } else {
        currentPlayerFrame[playerDirection] = 0;
        playerWidth = 64;
        playerLength = 64;
        currentPlayerImage = playerRestImages[playerDirection];
    }
    // Calculate the offset for centering the player during the swing animation
    float offsetX = (playerWidth - 64) / 2;
    float offsetY = (playerLength - 64) / 2;

    // Additional vertical offset to make the swinging animation a bit higher
    float swingOffsetY = isSwinging ? 14 : 0;

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
      (nextPlayerX >= 1860 && nextPlayerX <= 1876 && nextPlayerY >= 0 && nextPlayerY <= 230)||
      (nextPlayerX >= 1860 && nextPlayerX <= 1876 && nextPlayerY >= 280 && nextPlayerY <= 600)||
      // Allow the player to move through the river path if the plank is collected
      (!plankCollected && nextPlayerX >= 1860 && nextPlayerX <= 1876 && nextPlayerY >= 230 && nextPlayerY <= 280)) {
      collisionDetected = true;
    }

    // Check for collision with plank
    if (!plankCollected && nextPlayerX + playerWidth > plankX && nextPlayerX < plankX + plankWidth &&
      nextPlayerY + playerLength > plankY && nextPlayerY < plankY + plankHeight) {
      plankCollected = true;
    // Add any additional logic for when the plank is collected
    } 

    // If no collision detected, update player position
    if (!collisionDetected) {
      playerX = nextPlayerX;
      playerY = nextPlayerY;
    }

    // Draw the player
    image(currentPlayerImage, playerX - offsetX, playerY - offsetY - swingOffsetY, playerWidth, playerLength);
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

  void spawnMonsters() {
    while (monsters.size() < maxMonsters) {
      float spawnX = random(50, 2320);
      float spawnY = random(50, 1400);
      PVector monsterPosition = new PVector(spawnX, spawnY);
      monsters.add(monsterPosition);
      
      // Initialize cuts for each new monster
      monsterCuts[monsters.size() - 1] = 0;
    }
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

      } else if (key == 'c' || key == 'C') {
        // Determine the direction vector based on playerDirection
        float dirX = 0, dirY = 0;
        if (playerDirection == 0) {
            dirX = 1; // Facing right
        } else if (playerDirection == 1) {
            dirX = -1; // Facing left
        } else if (playerDirection == 2) {
            dirY = -1; // Facing up
        } else if (playerDirection == 3) {
            dirY = 1; // Facing down
        }

        // Iterate through monsters to check if player is close enough to cut them
        for (int i = 0; i < monsters.size(); i++) {
            PVector monster = monsters.get(i);
            // Check if the monster is within cutting distance and in the right direction
            if (dist(playerX, playerY, monster.x, monster.y) < 70 &&
                ((dirX != 0 && (monster.x - playerX) * dirX > 0) || 
                 (dirY != 0 && (monster.y - playerY) * dirY > 0))) {
                // Increment monster cuts
                monsterCuts[i]++;
                // Record the time when this monster was last cut
                lastMonsterCutTimes[i] = millis();
            }
        }

        isSwinging = true;
        currentSwordSwingingFrame[playerDirection] = 0;
        lastSwingingFrameChangeTime = millis();
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