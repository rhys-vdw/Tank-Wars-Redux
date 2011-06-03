import processing.core.*;

/** The GameController executes the game loop, updating game logic and
 *  graphics. The game controller refers to GameState objects to determine
 *  what task it is currently performing, and ensures that their methods will
 *  be executed in the correct order.
 */
public class GameController extends PApplet {
  private int width, height;
  private Time time;
  private GameState gameState;

  private final static String RENDER_MODE = PConstants.P2D; //JAVA2D;

  /** Create a new GameController component.
   *  @param width          width of the parent window
   *  @param height         height of the parent window
   *  @param initialState   game state to be executed when app starts
   */
  public GameController(int width, int height) {
    this.width = width;
    this.height = height - this.getBounds().y;
    this.time = new Time();
    this.gameState = initializeGameStates();
    this.gameState.enter();
  }

  /** Create the state objects that control the game.
   *  @return the initial state
   */
  public GameState initializeGameStates() {
    GameState battleGameState = new BattleGameState(this, this.width,
        this.height);
    return battleGameState;
  }

  @Override
  public void setup() {
    this.size(this.width, this.height, RENDER_MODE);
    this.background(0);
  }

  @Override
  public void draw() {
    /* update deltaTime */
    this.time.update();

    /* check if a state transition has occurred */
    GameState nextState = this.gameState.transition();
    if (nextState != null) {
      this.gameState.exit();
      this.gameState = nextState;
      this.gameState.enter();
    }

    /* run game logic */
    this.gameState.update(time.getDeltaTime());
    /* update pixels */
    this.gameState.render();
  }

  /** Sets pixel color at specified coordinates. Use this function rather than
   *  the built in processing functions, as it converts game co-ordinates (where
   *  the origin (0, 0) is the lower left corner of the screen) to processing
   *  screen co-ords, where the origin is in the top left corner.
   *  @param x      the x coordinate of the pixel
   *  @param y      the y coordinate of the pixel
   *  @param color  new color for pixel hexadecimal ARGB values
   */
  public void setPixel(int x, int y, int color) {
    this.pixels[(this.height - y - 1) * this.width + x] = color;
  }
}

