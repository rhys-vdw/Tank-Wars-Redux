/** Thrown when GameController.setPixel() attempts to set a pixel value outside
 *  of the Processing screen.
 */
class SetPixelOutOfRangeException extends Exception {
  /** Contructor.
   *  @param x x-coordinate of pixel
   *  @param y y-coordinate of pixel
   */
  SetPixelOutOfRangeException(int x, int y) {
    super("Tried to draw pixel at (" + x + ", " + y + "), which is outside of drawing area.");
  }
}

