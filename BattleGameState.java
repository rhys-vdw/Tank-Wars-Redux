import java.util.ArrayList;

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

  /* TODO: consider using an Iterator<Tank> in conjunction with a Tank currentTank */
  private int currentTank;

  private final static int BACKGROUND_COLOR = 0xFF382428;
  private final static int DEFAULT_DIRT_COLOR = 0xFF00A800;

  private final static boolean FALLING = true;
  private final static boolean LANDED = false;

  private final static boolean DAMAGE = true;
  private final static boolean NO_DAMAGE = false;

  private final static float FALL_SPEED = 200.0f;

  /* TODO: replace with GameOptions class */
  private final static int PLAYERS = 2;

  /** The state of the battle at present. */
  private enum BattleState {
      /** initially placing the tanks on the ground */
      LoweringTanks,
      /** player is setting up a shot */
      PlayerTurn,
      /** updating missile movement/weapon behaviour */
      ResolvingShot,
      /** dropping suspended dirt */
      CollapsingDirt,
      /** dropping tanks that are not supported by dirt */
      DroppingTanks
  }
  private BattleState battleState;

  public BattleGameState(GameController game, int mapWidth, int mapHeight) {
    this.game = game;
    this.mapWidth = mapWidth;
    this.mapHeight = mapHeight;
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
    this.battleState = BattleState.LoweringTanks;
    this.currentTank = 0;
  }

  @Override
  public GameState transition() { return null; }

  @Override
  public void update(float deltaTime) {
    /* check battle state and act accordingly */
    switch (this.battleState) {
      case LoweringTanks:
        if (lowerTank(this.tanks.get(currentTank), NO_DAMAGE, deltaTime) == LANDED) {
          if (this.currentTank == this.tanks.size() - 1) {
            this.battleState = BattleState.PlayerTurn;
          } else {
            currentTank++;
          }
        }
        break;
    }
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
      this.tanks.add(new Tank(game, null, tankSeperation * (i + 1), 10, 0xFFFF0000));
    }
  }

  /** Drop a tank until it can go no further down.
   *  @param tank   the tank to be dropped
   *  @return       true (FALLING) if falling, false (LANDED) if done
   */
  private boolean lowerTank(Tank tank, boolean damage, float deltaTime) {
    /* TODO: more advanced collision algorithm... Consider attaching colliders
     *       to tanks/missiles */
    float distance = FALL_SPEED * deltaTime;
    boolean landed = FALLING;
    /* check if movement will hit dirt or the bottom of the level */
    for (int i = 0;
        (i < (int) distance) && (i + (int) tank.getY() < mapHeight);
        i++) {
      if (dirt[(int) tank.getX()][(int) tank.getY() + i]) {
        distance = i;
        landed = LANDED;
        break;
      }
    }
    /* move the tank down */
    tank.move(0.0f, distance);
    /* deal falling damage */
    if (damage) tank.damage(distance);
    return landed;
  }
}
