import java.lang.reflect.Constructor;

class Weapon {
  WeaponFire weaponFire;

  public Weapon(WeaponFire weaponFire) {
    this.weaponFire = weaponFire;
  }

  public String getName() {
    return this.weaponFire.getName();
  }

  public WeaponFire fire(Tank tank) {
    try {

      WeaponFire newFire = (WeaponFire) this.weaponFire.clone();
      newFire.fire(tank);
      return newFire;
    
    } catch (CloneNotSupportedException e) {
      System.out.println("Weapon firing failed.");
      e.printStackTrace();
      return null;
    }
  }
}

