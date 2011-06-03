
/** This is the main game state. This handles the creation of tanks, handling
 *  of turns, firing of shots and updating scores.
 */
public class BattleGameState extends GameState {
  private GameController game;
  private int mapWidth, mapHeight;
  private boolean[][] dirt;
  private int dirtColor;

  private final static int BACKGROUND_COLOR = 0x382428;
  private final static int DEFAULT_DIRT_COLOR = 0xFF00A800;

  /* TODO:
  private enum BattleState { PlayerTurn, ResolvingShot, CollapsingDirt }
  private BattleState battleState;
  private final static INITIAL_BATTLE_STATE = BattleState.PlayerTurn;
  */

  public BattleGameState(GameController game, int mapWidth, int mapHeight) {
    this.game = game;
    this.mapWidth = mapWidth;
    this.mapHeight = mapHeight;
    this.dirt = new boolean[mapWidth][mapHeight];
    this.dirtColor = DEFAULT_DIRT_COLOR;
    /* this.battleState = INITIAL_BATTLE_STATE */
  }
  
  @Override
  public void enter() {
    generateDirt();
    /* load tanks */
    /* place tanks */
  }

  @Override
  public GameState transition() { return null; }

  @Override
  public void update(float deltaTime) {
    /* check battle state and act accordingly */
  }

  @Override
  public void render() {
    /* set background color */
    game.background(BACKGROUND_COLOR);
    /* render dirt */
    game.loadPixels();
    for (int y = 0; y < mapHeight; y++) {
      for (int x = 0; x < mapWidth; x++) {
        if (this.dirt[x][y])
          game.setPixel(x, y, this.dirtColor);
      }
    }
    game.updatePixels();
  }

  @Override
  public void exit() {
    /* distribute earnings */
    /* update game history */
    /* update player stats */
  }
  
  private void generateDirt() {
    /* fill the lower half of the map */
    for (int y = 0; y < mapHeight; y++) {
      for (int x = 0; x < mapWidth; x++) {
        dirt[x][y] = y < mapHeight / 2;
      }
    }
  }
}
