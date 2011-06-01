
class Time {
  long previousTime;
  int deltaTime;

  public Time() {
    previousTime = System.currentTimeMillis();
    deltaTime = 0;
  }

  /** Updates deltaTime. This should be called once per game tick.
   */
  public void update() {
    long currentTime = System.currentTimeMillis();
    this.deltaTime = (int) (currentTime - previousTime);
    this.previousTime = currentTime;
  }

  /** Returns the duration of the last frame in ms.
   *  @return deltaTime in ms
   */
  public int getDeltaTime() {
    return this.deltaTime;
  }
}
