import java.util.Random;

class RandomFrequency implements SoundGenerator {
  Random random;
  double min, max;

  public RandomFrequency(double min, double max) {
    this.random = new Random();
    this.min = min;
    this.max = max;
  }

  @Override
  public double frequency() {
    double frequency = random.nextDouble() * this.max + this.min;
    return frequency;
  }

  @Override
  public boolean finished() { return false; }
}

