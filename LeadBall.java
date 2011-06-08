import java.lang.Math;
import processing.core.*;

public class LeadBall extends WeaponFire {
  private float x, y;
  private float velocityX, velocityY;
  private float accelerationX, accelerationY;

  private final static String NAME = "Lead Ball";
  private final static int MISSILE_COLOR = 0xFFFFFFFF;
  private final static float GRAVITY = 100.0f;

  public void fire(Tank tank) {
    this.x = tank.getTurretX();
    this.y = tank.getTurretY();
    this.accelerationX = 0.0f;
    this.accelerationY = GRAVITY;
    this.velocityX = (float) Math.sin(Math.toRadians(tank.getAngle())) * tank.getPower();
    this.velocityY = (float) Math.cos(Math.toRadians(tank.getAngle())) * tank.getPower();
  }

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public HitInfo update(BattleGameState battle, float deltaTime) {
    this.velocityX += this.accelerationX * deltaTime;
    this.velocityY += this.accelerationY * deltaTime;
    this.x += this.velocityX * deltaTime;
    this.y += this.velocityY * deltaTime;
    /* TODO: check collision with tanks
    if (battle.checkCollision(this.x, this.y) {

    } else */
    /* check if missile has hit dirt */
    if (battle.checkDirt((int) this.x, (int) this.y)) {
      return new HitInfo((int) this.x, (int) this.y);
    }
    return null;
  }

  @Override
  public void draw(PApplet game) {
    game.stroke(MISSILE_COLOR);
    game.point(this.x, this.y);
  }
}
