import java.awt.event.KeyListener;

/** Abstract class for holding a state of the game (ie menu, weapon selection,
 *  battle).
 */
interface GameState extends KeyListener {
  /** Called upon entering state. */
  void enter();

  /** Called every tick to check for a state transiton. If the state has
   *  finished, this should return the next state, a return value of null
   *  will cause this state to continue.
   *  @return the next state, or null.
   */
  GameState transition();

  /** This method should contain all the logic code for each tick. It is
   *  guaranteed to be executed before render().
   *  @param deltaTime the time elapsed in the previous frame, in s
   */ 
  void update(float deltaTime);
  
  /** This method should contain all the code that updates the screen. It is
   *  called after update().
   */
  void render();

  /** Called before transitioning to a new state. */
  void exit();
}

