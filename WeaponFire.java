import processing.core.*;

abstract class WeaponFire implements Cloneable {
  abstract public String getName();
  abstract public void fire(Tank tank);
  abstract public HitInfo update(BattleGameState battle, float deltaTime);
  abstract public void draw(PApplet game);
  @Override
  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }
}

