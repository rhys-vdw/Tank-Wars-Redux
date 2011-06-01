import processing.core.*;
import java.util.Random;

class TestDraw extends PApplet {
  private int width, height;
  private float backgroundShade;
  private Time time;
  private int timeSinceLastDraw;

  private final static float DEFAULT_BACKGROUND_SHADE = 10.8f;

  public TestDraw(int width, int height) {
    this.width = width;
    this.height = height;
    this.backgroundShade = DEFAULT_BACKGROUND_SHADE;
    this.time = new Time();
    this.timeSinceLastDraw = 0;
  }

  @Override
  public void setup() {
    int titleBarHeight = this.getBounds().y; 
    this.size(this.width, this.height - titleBarHeight);
  }

  @Override
  public void draw() {
    this.background(backgroundShade);
    Random random = new Random();
    this.time.update();
    this.timeSinceLastDraw += this.time.deltaTime;

    /*
    if (timeSinceLastDraw > 100) {
      timeSinceLastDraw -= 100;
      */
      for (int i = 0; i < 20; i++) {
        this.drawCircle(random.nextFloat() * this.width,
                        random.nextFloat() * this.height,
                        random.nextFloat() * 20 + 5);
      }
    //}
  }

  private void drawCircle(float x, float y, float radius) {
    this.ellipse((int) x, (int) y, (int) radius * 2, (int) radius * 2);
  }
}

