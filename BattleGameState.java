import java.util.ArrayList;
import java.util.Iterator;

/** This is the main game state. This handles the creation of tanks, handling
 *  of turns, firing of shots and updating scores.
 */
public class BattleGameState extends GameState {
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

  private interface BattleState {
    BattleState update(float deltaTime);
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
  
  @Override
  public void enter() {
    /* TODO: set width and height from GameOptions */
    generateDirt();
    /* load tanks */
    loadTanks();
    this.turnIterator = tanks.iterator();
    this.currentTank = turnIterator.next();
    this.battleState = new LowerTanksState();
    this.gameEnded = false;
  }

  @Override
  public GameState transition() {
    if (gameEnded) {
      return this.gameEndedState;
    }
    return null;
  }

  private class LowerTanksState implements BattleState {
    private Tank tank;
    private Iterator<Tank> tankIterator;

    public LowerTanksState() {
      this.tankIterator = tanks.iterator();
      this.tank = nextLivingTank(tankIterator);
    }

    @Override
    public BattleState update(float deltaTime) {
      if (dropTank(tank, NO_DAMAGE, deltaTime) == FINISHED) {
        this.tank = nextLivingTank(tankIterator);
        if (this.tank == null) {
          return new PlayerTurnState();
        }
      }
      return this;
    }
  }

  private class PlayerTurnState implements BattleState {
    private Tank tank;

    public PlayerTurnState() {
      assert countLivingTanks() > 1
          : countLivingTanks() + " tank(s) alive for player turn";

      this.tank = nextLivingTank(tanks.iterator());
    }

    @Override
    public BattleState update(float deltaTime) {
      /* no idea what happens here... keyboard input must be somehow passed to
         tank... either by logging keydown or by creating a new keydown
         method */
      return this;
      /* or return new ResolvingTurnState(); */
    }
  }

  private class ResolvingTurnState implements BattleState {
    //private WeaponFire weaponFire;

    public ResolvingTurnState(/*WeaponFire weaponFire*/) {
      //this.weaponFire = weaponFire;
    }
    
    @Override
    public BattleState update(float deltaTime) {
      //if (this.weaponFire.update() == FINISHED) {
        //return new CrumbleDirtState(this.weaponFire.getHitInfo());
      //} else {
        return this;
      //}
    }
  }

  private class CrumbleDirtState implements BattleState {
    //private HitInfo hitInfo;
    //public CrumbleDirtState(HitInfo hitInfo) {
      //this.hitInfo = hitInfo;
      //this.dirtFallen = false;
    //}
    @Override
    public BattleState update(float deltaTime) {
      /*
      if (crumbleSomeDirt() == FINISHED) {
        return new DropTanksState());
      }
      this.dirtFallen = true; */
      return this;
    }
  }

  private class DropTanksState implements BattleState {
    private Tank tank;
    private Iterator<Tank> tankIterator;

    public DropTanksState() {
      this.tankIterator = tanks.iterator();
      this.tank = nextLivingTank(tankIterator);
    }

    @Override
    public BattleState update(float deltaTime) {
      /* if there are no tanks to drop, end game */
      if (countLivingTanks() == 0)
        return new EndGameState();

      /* drop current tank */
      if (dropTank(tank, DAMAGE, deltaTime) == FINISHED) {
        this.tank = nextLivingTank(tankIterator);
        /* if there are no more tanks to drop, destroy dead tanks */
        if (this.tank == null) {
          return new DestroyDeadState();
        }
      }
      return this;
    }
  }

  /* Finds a single dead tank and destroys it */
  private class DestroyDeadState implements BattleState {
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
      return this;
    }
  }

  private class EndGameState implements BattleState {
    @Override
    public BattleState update(float deltaTime) {
      gameEnded = true;
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
  }

  @Override
  public void exit() {
    /* distribute earnings */
    /* update game history */
    /* update player stats */
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

  /** Increment a Tank iterator to the next living tank.
   *  @param tankIterator an iterator to a collection of Tank objects
   *  @return the Tank iterated to, or null if none exists
   */
  private Tank nextLivingTank(Iterator<Tank> tankIterator) {
    while (tankIterator.hasNext()) {
      Tank tank = tankIterator.next();
      if (tank.isAlive()) {
        return tank;
      }
    }
    return null;
  }

  private int countLivingTanks() {
    int living = 0;
    for (Tank t : this.tanks) {
      if (t.isAlive()) living++;
    }
    return living;
  }
}

