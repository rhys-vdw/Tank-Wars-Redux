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

  private final static String RENDER_MODE = PConstants.P2D;
  private final static int FRAME_RATE = 60;

  /** Create a new GameController component.
   *  @param width          width of the parent window
   *  @param height         height of the parent window
   *  @param initialState   game state to be executed when app starts
   */
  public GameController(int width, int height) {
    this.width = width;
    this.height = height - this.getBounds().y;
    this.time = new Time();
    enterState(initializeGameStates());
  }

  /** Create the state objects that control the game.
   *  @return the initial state
   */
  private GameState initializeGameStates() {
    GameState battleGameState = new BattleGameState(this, this.width,
        this.height, null);
    return battleGameState;
  }

  private void enterState(GameState nextState) {
    if (this.gameState != null) {
      this.removeKeyListener(this.gameState);
      this.gameState.exit();
    }
    this.gameState = nextState;
    this.gameState.enter();
    this.addKeyListener(this.gameState);
  }

  @Override
  public void setup() {
    this.frameRate(FRAME_RATE);
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
      enterState(nextState);
    }

    /* run game logic */
    this.gameState.update(time.getDeltaTime());
    /* update pixels */
    this.gameState.render();
  }

  /** Sets pixel color at specified coordinates. Use this function rather than
   *  accessig PApplet.pixels[] directly or using set(), as it has bounds
   *  checking.
   *  
   *  @param x      the x coordinate of the pixel
   *  @param y      the y coordinate of the pixel
   *  @param color  new color for pixel hexadecimal ARGB values
   *
   *  @exception SetPixelOutOfRangeException tried to set a pixel outside of the
   *                drawing area
   */
  public void setPixel(int x, int y, int color) throws SetPixelOutOfRangeException {
    if (x >= 0 && x < this.width && y >= 0 && y < this.height) {
      this.pixels[y * this.width + x] = color;
    } else {
      throw new SetPixelOutOfRangeException(x, y);
    }
  }
}

