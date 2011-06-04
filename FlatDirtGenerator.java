class FlatDirtGenerator implements DirtGenerator {
  @Override
  public void generate(boolean[][] dirt, int width, int height) {
    /* fill the lower half of the map */
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
          dirt[x][y] = y > height / 2;
      }
    }
  }
}
