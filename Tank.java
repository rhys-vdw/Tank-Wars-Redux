import java.lang.Math;
import processing.core.*;

public class Tank implements Drawable {
  private int power, angle;
  private float x, y;
  private float turretX, turretY;
  private float men;
  private Profile profile;
  private TankController controller;
  private int color;
  private PImage image;
  private PApplet game;
  private boolean alive;
  private Weapon weapon;
  
  private final static int INITIAL_POWER = 100;
  private final static int INITIAL_MEN = 100;
  private final static int INITIAL_ANGLE = 135;
  private final static int MAX_ANGLE = 270;
  private final static int MIN_ANGLE = 90;
  private final static int TURRET_RANGE = MAX_ANGLE - MIN_ANGLE;
  private final static float TURRET_LENGTH = 7.0f;
  public final static int TANK_WIDTH = 9;
  public final static int TANK_HEIGHT = 5;
  public final static int CENTER_POINT_X = 4;
  public final static int CENTER_POINT_Y = 4;
  //private final static float TURRET_SPEED = 5.0f;
  public final static boolean CLOCKWISE = true;
  public final static boolean ANTICLOCKWISE = false;

  /** Constructs a new tank.
   *  @param game       the main PApplet to which the graphics will be drawn 
   *  @param profile    the player or AI controlling this tank in its turn
   *  @param x          the tank's initial x co-ordinate 
   *  @param y          the tank's initial y co-ordinate 
   *  @param color      the tank's color, in hex ARGB
   */
  public Tank(PApplet game, Profile profile, int x, int y, int color) {
    this.game = game;
    this.profile = profile;
    this.controller = new TankController(this);
    this.x = x;
    this.y = y;
    this.color = color;
    this.angle = INITIAL_ANGLE;
    updateTurretPosition();
    this.power = INITIAL_POWER;
    this.men = INITIAL_MEN;
    this.image = createImage();
    this.alive = true;
    this.weapon = new Weapon(new LeadBall());
  }

  /** Create the sprite that represents the tank chassy.
   *  @return a sprite representing the tank
   */
  private PImage createImage() {
    PImage image;
    final boolean X = true;
    final boolean _ = false;
    final boolean[] tankPixels = {
       _,_,_,X,X,X,_,_,_,
       _,X,X,X,X,X,X,X,_,
       _,X,X,X,X,X,X,X,_,
       X,X,X,_,X,_,X,X,X,
       X,X,X,X,X,X,X,X,X
    };
    image = this.game.createImage(TANK_WIDTH, TANK_HEIGHT, PConstants.ARGB);
    image.loadPixels();
    for (int i = 0; i < tankPixels.length; i++) {
      image.pixels[i] = tankPixels[i] ? color : 0x00000000;
    }
    image.updatePixels();
    return image;
  }

  /** Adjust turret angle by one degree.
   *  @param clockwise the direction the turret should rotate
   */
  public void changeAngle(boolean clockwise) {
    if (clockwise) {
      this.angle--;
      if (this.angle < MIN_ANGLE) {
        this.angle += TURRET_RANGE;
      }
    } else {
      this.angle++;
      if (this.angle > MAX_ANGLE) {
        this.angle -= TURRET_RANGE;
      }
    }
    updateTurretPosition();
  }

  /** Updates the position of the end of the tank's cannon */
  private void updateTurretPosition() {
    this.turretX = (float) Math.sin(Math.toRadians(this.angle)) * TURRET_LENGTH;
    this.turretY = (float) Math.cos(Math.toRadians(this.angle)) * TURRET_LENGTH;
  }

  /** Fire current weapon */
  public WeaponFire fire() {
    return this.weapon.fire(this);
  }

  /** Returns angle of turret in degrees.
   *  @return angle of turret between MIN_ANGLE (left) and MAX_ANGLE (right)
   */
  public int getAngle() {
    return this.angle;
  }

  /** Specify the cannon strength.
   *  @param power the power with which a weapon will be fired.
   */
  public void setPower(int power) {
    this.power = power;
    System.out.println("power: " + power);
  }

  public int getPower() {
    return this.power;
  }

  /** Draw the tank at its position. */
  @Override
  public void draw() {
    /* draw chassy */
    this.game.image(this.image, this.x - CENTER_POINT_X,
        this.y - CENTER_POINT_Y);

    /* draw cannon */
    this.game.stroke(this.color);
    this.game.line(this.x, this.y, this.x + this.turretX,
        this.y + this.turretY);
  }

  /** Returns the horizontal position of the tank
   *  @return the X co-ordinate of the tank
   */
  public float getX() {
    return this.x;
  }

  /** Returns the vertical position of the tank
   *  @return the Y co-ordinate of the tank
   */
  public float getY() {
    return this.y;
  }

  /** Returns the horizontal screen position of the turret end
   *  @return the X co-ordinate of the turret end
   */
  public float getTurretX() {
    return this.x + this.turretX;
  }

  /** Returns the vertical screen position of the turret end
   *  @return the Y co-ordinate of the turret end
   */
  public float getTurretY() {
    return this.y + this.turretY;
  }

  /** Move the tank in the level
   *  @param x the horizontal distance of the move
   *  @param y the vertical distance of the move
   */
  public void move(float x, float y) {
    this.x += x;
    this.y += y;
  }

  /** Sets the vertical position of the tank
   *  @param y the new Y co-ordinate of the tank
   */
  public void setY(float y) {
    this.y = y;
  }

  /** Returns the number of men remaining in tank
   *  @return tank's number of men/health
   */
  public int getMen() {
    return Math.max((int) Math.ceil(this.men), 0);
  }

  /** Returns whether the tank is alive or not
   *  @return true if the tank has a least one man
   */
  public boolean isAlive() {
    return this.alive;
  }

  /** Set tank to dead */
  public void kill() {
    this.alive = false;
  }

  /** Take damage.
   *  @param damage the number of men who die
   */
  public void damage(float damage) {
    this.men -= damage;
  }

  public TankController getController() {
    return this.controller;
  }
}

