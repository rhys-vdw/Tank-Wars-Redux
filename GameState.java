/** Abstract class for holding a state of the game (ie menu, weapon selection,
 *  battle).
 */
abstract class GameState {
  /** Called upon entering state. */
  abstract public void enter();

  /** Called every tick to check for a state transiton. If the state has
   *  finished, this should return the next state, a return value of null
   *  will cause this state to continue.
   *  @return the next state, or null.
   */
  abstract public GameState transition();

  /** This method should contain all the logic code for each tick. It is
   *  guaranteed to be executed before render().
   *  @param deltaTime the time elapsed in the previous frame, in s
   */ 
  abstract public void update(float deltaTime);
  
  /** This method should contain all the code that updates the screen. It is
   *  called after update().
   */
  abstract public void render();

  /** Called before transitioning to a new state. */
  abstract public void exit();
}

