import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class TankController extends KeyAdapter {
  private Tank tank;
  private TurretMovement turretMovement;
  private PowerChange powerChange;
  private boolean rightPressed, leftPressed;
  private boolean upPressed, downPressed;
  private boolean finished;
  private float turretInterval;
  private float powerInterval;

  private final static float TURRET_SPEED = 80.0f;
  private final static float TURRET_DELAY = 1.0f / TURRET_SPEED;
  private final static float POWER_SPEED = 80.0f;
  private final static float POWER_DELAY = 1.0f / POWER_SPEED;

  private enum TurretMovement {
    Clockwise,
    Anticlockwise,
    Stationary
  }

  private enum PowerChange {
    Up,
    Down,
    Stationary
  }

  public TankController(Tank tank) {
    this.tank = tank;
    this.turretMovement = TurretMovement.Stationary;
    this.turretInterval = TURRET_DELAY;
    this.powerChange = PowerChange.Stationary;
    this.powerInterval = POWER_DELAY;
    this.finished = false;
  }

  public WeaponFire update(float deltaTime) {
    if (this.finished) {
      this.finished = false;
      return this.tank.fire();
    }

    this.turretInterval += deltaTime;
    while (this.turretInterval > TURRET_DELAY) {
      this.turretInterval -= TURRET_DELAY;
      if (this.turretMovement == TurretMovement.Clockwise) {
          this.tank.changeAngle(Tank.CLOCKWISE);
      } else if (this.turretMovement == TurretMovement.Anticlockwise) {
          this.tank.changeAngle(Tank.ANTICLOCKWISE);
      }
    }
    
    this.powerInterval += deltaTime;
    while (this.powerInterval > POWER_DELAY) {
      this.powerInterval -= POWER_DELAY;
      if (this.powerChange == PowerChange.Up) {
          this.tank.setPower(this.tank.getPower() + 1);
      } else if (this.powerChange == PowerChange.Down) {
          this.tank.setPower(this.tank.getPower() - 1);
      }
    }

    return null;
  }

  @Override
  public void keyPressed(KeyEvent e) {
    switch (e.getKeyCode()) {
      case KeyEvent.VK_RIGHT:
        this.rightPressed = true;
        this.turretMovement = TurretMovement.Clockwise;
        this.turretInterval = TURRET_DELAY;
        break;
      case KeyEvent.VK_LEFT:
        this.leftPressed = true;
        this.turretMovement = TurretMovement.Anticlockwise;
        this.turretInterval = TURRET_DELAY;
        break;
      case KeyEvent.VK_UP:
        this.upPressed = true;
        this.powerChange = PowerChange.Up;
        this.powerInterval = POWER_DELAY;
        break;
      case KeyEvent.VK_DOWN:
        this.downPressed = true;
        this.powerChange = PowerChange.Down;
        this.powerInterval = POWER_DELAY;
        break;
      case KeyEvent.VK_SPACE:
        this.finished = true;
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {
    switch (e.getKeyCode()) {
      case KeyEvent.VK_RIGHT:
        this.rightPressed = false;
        this.turretMovement = this.leftPressed ? TurretMovement.Anticlockwise
            : TurretMovement.Stationary;
        break;
      case KeyEvent.VK_LEFT:
        this.leftPressed = false;
        this.turretMovement = this.rightPressed ? TurretMovement.Clockwise
            : TurretMovement.Stationary;
        break;
      case KeyEvent.VK_UP:
        this.upPressed = false;
        this.powerChange = this.downPressed ? PowerChange.Down
            : PowerChange.Stationary;
        break;
      case KeyEvent.VK_DOWN:
        this.downPressed = false;
        this.powerChange = this.upPressed ? PowerChange.Up
            : PowerChange.Stationary;
        break;
      case KeyEvent.VK_SPACE:
        this.finished = true;
    }
  }
}

