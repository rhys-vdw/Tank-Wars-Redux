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
  }

  /** Create the state objects that control the game.
   *  @return the initial state
   */
  public GameState initializeGameStates() {
    GameState battleGameState = new BattleGameState(this, this.width, this.height);
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
    this.gameState.update();
    /* update screen */
    this.gameState.render();
  }

  /** Returns the elapsed time between this frame and the last.
   *  @return frame length in ms
   */
  public int getDeltaTime() {
    return this.time.getDeltaTime();
  }

  /** Sets pixel color at specified coordinates
   *  @param x      the x coordinate of the pixel
   *  @param y      the y coordinate of the pixel
   *  @param color  new color for pixel hexadecimal RGB values
   */
  public void setPixel(int x, int y, int color) {
    this.pixels[y * this.width + x] = color;
  }
}

