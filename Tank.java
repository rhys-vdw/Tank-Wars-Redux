public class Tank implements Drawable {
  private float angle;
  private int power;
  private int x, y;
  private int men;
  private Profile controller;
  
  private int targetAngle;
  private boolean clockwise;

  private final static int INITIAL_ANGLE = 45.0f;
  private final static int INITIAL_POWER = 100;
  private final static int INITIAL_MEN = 100;
  private final static float TURRET_MOVEMENT_SPEED = 5.0f;
  public final static boolean CLOCKWISE = true;
  public final static boolean ANTICLOCKWISE = false;

  public Tank(Profile controller, int x, int y) {
    this.controller = controller;
    this.x = x;
    this.y = y;
    this.angle = this.targetAngle = INITIAL_ANGLE;
    this.power = INITIAL_POWER;
    this.men = INITIAL_MEN;
    this.clockwise = CLOCKWISE;
  }

  /** Set a desired angle for the turret, and a rotation direction. This
   *  initiates the turret to move in the specified direction at a constant
   *  speed until it reaches the desired angle.
   *  @param angle the desired angle for the turret [-90, 90]
   *  @param clockwise the direction the turret should rotate
   */
  public void setAngle(int angle, boolean clockwise) {
    this.targetAngle = angle;
    this.clockwise = clockwise;
  }

  /** Specify the cannon strength.
   *  @param power the power with which a weapon will be fired.
   */
  public void setPower(int power) {
    this.power = power;
  }

  public void update(int deltaTime) {
    if (this.angle != this.targetAngle) {
      boolean leftOfTarget = angle < targetAngle;
      /* adjust angle towards target in specified rotation direction */
      angle += (this.clockwise ? 1 : -1) * TURRET_MOVEMENT_SPEED * deltaTime;
      /* if the turret has passed its target, set it to the target angle */
      if ((leftOfTarget && this.clockwise) && (angle > targetAngle)
          || ((!leftOfTarget && !this.clockwise) && (angle < targetAngle)) {
        angle = targetAngle;
      }
    }
  }
}

