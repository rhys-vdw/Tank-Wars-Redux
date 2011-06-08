import java.util.ArrayList;
import java.util.Iterator;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/** This is the main game state. This handles the creation of tanks, handling
 *  of turns, firing of shots and updating scores.
 */
public class BattleGameState implements GameState {
  private GameController game;
  private int mapWidth, mapHeight;
  private boolean[][] dirt;
  private int dirtColor;
  private DirtGenerator dirtGenerator;
  private ArrayList<Tank> tanks;
  private boolean gameEnded;

  /* records whether the last crumble actually did anything */
  private boolean crumbleOccurred;

  /* the next tank to have a turn */
  private Tank currentTank;
  private Iterator<Tank> turnIterator;

  private GameState gameEndedState;

  /* constants */
  private final static boolean FINISHED = true;
  private final static boolean NOT_FINISHED = false;
  private final static boolean DAMAGE = true;
  private final static boolean NO_DAMAGE = false;

  /* options TODO: create game options class */
  private final static int BACKGROUND_COLOR = 0xFF382428;
  private final static int DEFAULT_DIRT_COLOR = 0xFF00A800;
  private final static float FALL_SPEED = 700.0f;
  private final static int PLAYERS = 4;

  private abstract class BattleState extends KeyAdapter {
    abstract public BattleState update(float deltaTime);
    public void render() { /* empty */ };
  }
  private BattleState battleState;

  public BattleGameState(GameController game, int mapWidth, int mapHeight,
      GameState gameEndedState) {
    this.game = game;
    this.mapWidth = mapWidth;
    this.mapHeight = mapHeight;
    this.gameEndedState = gameEndedState;
    this.dirt = new boolean[mapWidth][mapHeight];
    this.dirtColor = DEFAULT_DIRT_COLOR;
    this.dirtGenerator = new FlatDirtGenerator();
    this.tanks = new ArrayList<Tank>();
  }
  
  /* GAME STATE METHODS */

  @Override
  public void enter() {
    /* TODO: set width and height from GameOptions */
    generateDirt();
    /* load tanks */
    loadTanks();
    this.turnIterator = tanks.iterator();
    this.currentTank = null;
    this.battleState = new LowerTanksState();
    this.gameEnded = false;
  }

  @Override
  public GameState transition() {
    return gameEnded ? this.gameEndedState : null;
  }

  private class LowerTanksState extends BattleState {
    private Tank tank;
    private Iterator<Tank> tankIterator;

    public LowerTanksState() {
      this.tankIterator = tanks.iterator();
      this.tank = this.tankIterator.next();
    }

    @Override
    public BattleState update(float deltaTime) {
      if (dropTank(tank, NO_DAMAGE, deltaTime) == FINISHED) {
        if (this.tankIterator.hasNext())
          this.tank = tankIterator.next();
        else {
          return new PlayerTurnState();
        }
      }
      return this;
    }
  }

  @Override
  public void update(float deltaTime) {
    this.battleState = this.battleState.update(deltaTime);
  }

  @Override
  public void render() {
    /* set background color */
    this.game.background(BACKGROUND_COLOR);
    /* render dirt */
    this.game.loadPixels();
    for (int y = 0; y < mapHeight; y++) {
      for (int x = 0; x < mapWidth; x++) {
        if (this.dirt[x][y])
          try {
            this.game.setPixel(x, y, this.dirtColor);
          } catch (SetPixelOutOfRangeException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
          }
      }
    }
    this.game.updatePixels();

    /* draw tanks */
    for (Tank t : this.tanks) {
      t.draw();
    }

    /* render state info */
    this.battleState.render();
  }

  @Override
  public void exit() {
    /* distribute earnings */
    /* update game history */
    /* update player stats */
  }
  
  /* KEY LISTENER METHODS */

  @Override
  public void keyPressed(KeyEvent e) {
    this.battleState.keyPressed(e);
  }

  @Override
  public void keyReleased(KeyEvent e) {
    this.battleState.keyReleased(e);
  }
  
  @Override
  public void keyTyped(KeyEvent e) {
    this.battleState.keyTyped(e);
  }

  /* BATTLE STATES */

  private class PlayerTurnState extends BattleState {
    private TankController controller;

    public PlayerTurnState() {
      incrementTurn();
      this.controller = currentTank.getController();
    }

    @Override
    public BattleState update(float deltaTime) {
      WeaponFire weaponFire = this.controller.update(deltaTime);
      if (weaponFire != null) {
        return new ResolvingTurnState(weaponFire);
      }
      return this;
    }

    @Override
    public void keyPressed(KeyEvent e) {
      this.controller.keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
      this.controller.keyReleased(e);
    }
  }

  private class ResolvingTurnState extends BattleState {
    private WeaponFire weaponFire;

    public ResolvingTurnState(WeaponFire weaponFire) {
      System.out.println("Entering ResolvingTurnState");
      this.weaponFire = weaponFire;
    }
    
    @Override
    public BattleState update(float deltaTime) {
      HitInfo hitInfo = this.weaponFire.update(BattleGameState.this, deltaTime);
      if (hitInfo != null) {
        return new CrumbleDirtState(hitInfo);
      } else {
        return this;
      }
    }

    @Override
    public void render() {
      this.weaponFire.draw(game);
    }
  }

  private class CrumbleDirtState extends BattleState {
    private HitInfo hitInfo;
    //private boolean dirtFallen;

    public CrumbleDirtState(HitInfo hitInfo) {
      this.hitInfo = hitInfo;
      //this.dirtFallen = false;
    }

    @Override
    public BattleState update(float deltaTime) {
      /*
      if (crumbleSomeDirt() == FINISHED) {
        return new DropTanksState());
      }
      this.dirtFallen = true;
      return this;
      */
      return new DropTanksState();
    }
  }

  private class DropTanksState extends BattleState {
    private Tank tank;
    private Iterator<Tank> tankIterator;

    public DropTanksState() {
      this.tankIterator = tanks.iterator();
      this.tank = tankIterator.next();
    }

    @Override
    public BattleState update(float deltaTime) {
      /* if there are no tanks to drop, end game */
      if (countLivingTanks() == 0)
        return new EndGameState();

      /* drop current tank */
      if (dropTank(tank, DAMAGE, deltaTime) == FINISHED) {
        if (tankIterator.hasNext()) {
          do {
            this.tank = tankIterator.next();
          } while (this.tank.isAlive() && tankIterator.hasNext());
        }
        /* if there are no more tanks to drop, destroy dead tanks */
        if (!tankIterator.hasNext()) {
          return new DestroyDeadState();
        }
      }
      return this;
    }
  }

  /* Finds a single dead tank and destroys it */
  private class DestroyDeadState extends BattleState {
    //private Death death;

    public DestroyDeadState() {
      //this.death = null;
      for (Tank t : tanks) {
        if (t.isAlive() && t.getMen() == 0) {
          //this.death = Death.spawnRandom(t);
          t.kill();
          break;
        }
      }
    }

    @Override
    public BattleState update(float deltaTime) {
      //if (this.death == null) {
        /* continue game if there are at least two players */
        //if (countLivingTanks() <= 1) {
          //return new PlayerTurnState();
        //} 
        //return new EndGameState();
      //}
      //if (death.update(deltaTime) == FINISHED) {
        //death.getHitInfo();
        //return new CrumbleState(hitInfo);
      //}
      //return this;
      return new PlayerTurnState();
    }
  }

  private class EndGameState extends BattleState {
    @Override
    public BattleState update(float deltaTime) {
      gameEnded = true;
      return this;
    }
  }

  /* MISCELLANY */

  /** Check if there is dirt at specified x, y co-ordinates */
  public boolean checkDirt(int x, int y) {
    return this.dirt[x][y];
  }

  /** Initialize the dirt for the level, using specified DirtGenerator */
  private void generateDirt() {
    dirtGenerator.generate(dirt, mapWidth, mapHeight);
  }

  /** Determine the position of each tank. */
  private void loadTanks() {
    /* TODO: Correctly emulate Tank Wars' positioning */
    int tankSeperation = this.mapWidth / (PLAYERS + 1);

    for (int i = 0; i < PLAYERS; i++) {
      this.tanks.add(new Tank(game, null, tankSeperation * (i + 1), 0,
          0xFFFF0000));
    }
  }

  /** Drop a tank until it can go no further down.
   *  TODO: This should probably be hidden within the tank class... Or even a
   *        more general physics class from whic tank can inherit.
   *  @param tank      the tank to be dropped
   *  @param damage    true (DAMAGE) if falling damage to be dealt, or false
   *                   (NO_DAMAGE) otherwise
   *  @param deltaTime delta time in s
   *  @return          true (FINISHED) or false (NOT_FINISHED)
   */
  private boolean dropTank(Tank tank, boolean damage, float deltaTime) {
    /* TODO: more advanced collision algorithm... Consider attaching colliders
     *       to tanks/missiles */
    float distance = FALL_SPEED * deltaTime;
    boolean finished = NOT_FINISHED;
    /* check if movement will hit dirt or the bottom of the level */
    for (int i = 0;
        (i < (int) distance) && (i + (int) tank.getY() < mapHeight);
        i++) {
      if (dirt[(int) tank.getX()][(int) tank.getY() + i]) {
        distance = i;
        finished = FINISHED;
        break;
      }
    }
    /* move the tank down */
    tank.move(0.0f, distance);
    /* deal falling damage */
    if (damage) tank.damage(distance);
    return finished;
  }

  /** Increment a Tank iterator to the next living tank. TODO: this comment is wrong
   *  @param tankIterator an iterator to a collection of Tank objects
   *  @return the Tank iterated to, or null if none exists
   */
  private void incrementTurn() {
    Tank tank = null;
    do {
      if (!this.turnIterator.hasNext()) {
        this.turnIterator = this.tanks.iterator();
      }
      tank = turnIterator.next(); 
      /* completed a full loop without finding a match */
      if (tank == this.currentTank) {
        System.out.println("NO LIVING TANKS HOLY SHIT!");
      }
    } while (!tank.isAlive());

    this.currentTank = tank;
  }

  private int countLivingTanks() {
    int living = 0;
    for (Tank t : this.tanks) {
      if (t.isAlive()) living++;
    }
    return living;
  }
}

