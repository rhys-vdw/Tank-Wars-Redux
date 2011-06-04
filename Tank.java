public class Tank implements Drawable {
  private float angle;
  private int power;
  private int x, y;
  private int men;
  private Profile controller;
  private int color;
  
  /* angle that turret is moving to */
  private int targetAngle;
  /* direction that turret is rotating */
  private boolean clockwise;

  private final static int INITIAL_POWER = 100;
  private final static int INITIAL_MEN = 100;
  private final static int INITIAL_ANGLE = 45.0f;
  private final static float MAX_ANGLE = 90.0f;
  private final static float MIN_ANGLE = -90.0f;
  private final static float TURRET_RANGE = MAX_ANGLE - MIN_ANGLE;
  private final static float TURRET_SPEED = 5.0f;
  public final static boolean CLOCKWISE = true;
  public final static boolean ANTICLOCKWISE = false;

  private bool[][] tankPixels;

  private loadTankPixels() {
    final bool X = true;
    final bool _ = false;
    this.tankPixels = {
        {_,_,_,X,X,X,_,_,_},
        {_,X,X,X,X,X,X,X,_},
        {_,X,X,X,X,X,X,X,_},
        {X,X,X,_,X,_,X,X,X},
        {X,X,X,X,X,X,X,X,X}
     };
  }

  public Tank(Profile controller, int x, int y, int color) {
    this.controller = controller;
    this.x = x;
    this.y = y;
    this.color = color;
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
    /* TODO: Throw exception if angle is out of range. */
    this.targetAngle = angle;
    this.clockwise = clockwise;
  }

  /** Specify the cannon strength.
   *  @param power the power with which a weapon will be fired.
   */
  public void setPower(int power) {
    this.power = power;
  }

  /** Updates rotation of turret. To be called every tick.
   *  @param deltaTime the duration of the previous frame, in seconds
   */
  public void update(float deltaTime) {
    if (this.angle != this.targetAngle) {
      boolean leftOfTarget = angle < targetAngle;
      /* adjust angle towards target in specified rotation direction */
      angle += (this.clockwise ? 1 : -1) * TURRET_SPEED * deltaTime;
      /* if the turret has passed its target, set it to the target angle */
      /* TODO: this will not work */
      if ((leftOfTarget && this.clockwise) && (angle > targetAngle)
          || ((!leftOfTarget && !this.clockwise) && (angle < targetAngle)) {
        angle = targetAngle;
      }
      /* if angle is below horizontal, flip */
      if (angle < MIN_ANGLE) {
        angle += TURRET_RANGE;
      } else if (angle > MAX_ANGLE) {
        angle -= TURRET_RANGE;
      }
    }
  }
  
  @Override
  public void draw(PApplet game) {
    game.loadPixels();
  }

}

