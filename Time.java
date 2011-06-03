
class Time {
  private long previousTime;
  private int deltaTime;

  public Time() {
    this.previousTime = System.currentTimeMillis();
    this.deltaTime = 0;
  }

  /** Updates deltaTime. This should be called once per game tick.
   */
  public void update() {
    long currentTime = System.currentTimeMillis();
    this.deltaTime = (int) (currentTime - previousTime);
    this.previousTime = currentTime;
  }

  /** Returns the duration of the last frame in ms.
   *  @return deltaTime in s
   */
  public float getDeltaTime() {
    return this.deltaTime / 1000;
  }
}

