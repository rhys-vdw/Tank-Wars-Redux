
public class BattleGameState extends GameState {
  private GameController game;
  private int mapWidth, mapHeight;
  private boolean[][] dirt;
  private int skyColor;
  private int dirtColor;

  private final static int DEFAULT_SKY_COLOR = 0x0000FF00;
  private final static int DEFAULT_DIRT_COLOR = 0x00FF0000;

  /* TODO:
  private enum BattleState { PlayerTurn, ResolvingShot, CollapsingDirt }
  private BattleState battleState;
  private final static INITIAL_BATTLE_STATE = BattleState.PlayerTurn;
  */

  public BattleGameState(GameController game, int mapWidth, int mapHeight) {
    this.game = game;
    this.dirt = new boolean[mapWidth][mapHeight];
    this.skyColor = DEFAULT_SKY_COLOR;
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
  public void update() {
    /* check battle state and act accordingly */
  }

  @Override
  public void render() {
    game.ellipse(50, 50, 100, 200);
    //game.fill(100);

    /* render dirt */
    game.loadPixels();
    for (int y = 0; y < mapHeight; y++) {
      for (int x = 0; x < mapWidth; x++) {
        game.pixels[x * mapWidth + y] = game.color(255, 0, 0, 255);
        //game.setPixel(x, y, game.color(1, 0, 0));
        //game.setPixel(x, y, (this.dirt[x][y] ? game.color(0, 0, 1) : game.color(0, 1, 0)));
        //this.dirtColor : this.skyColor));
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
