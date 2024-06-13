public class Plank {
    PImage plankImage;
    float x;
    float y;
    boolean collected;

    public Plank(float x, float y) {
        this.x = x;
        this.y = y;
        this.plankImage = loadImage("Photos, GIFs, Videos, Music/plank.png");
        this.collected = false;
    }

    public void display() {
        if (!collected) {
            image(plankImage, x, y);
        }
    }

    public boolean checkCollision(float playerX, float playerY, float playerWidth, float playerLength) {
        // Check collision with player
        if (!collected && playerX + playerWidth > x && playerX < x + plankImage.width &&
            playerY + playerLength > y && playerY < y + plankImage.height) {
            collected = true;
            return true;
        }
        return false;
    }

    public boolean isCollected() {
        return collected;
    }
}
