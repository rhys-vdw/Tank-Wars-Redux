class RisingSound implements SoundGenerator {
  double next;
  double increment;

  public RisingSound(double initial, double increment) {
    this.next = initial;
    this.increment = increment;
  }

  @Override
  public double frequency() {
    return next += increment;
  }

  @Override
  public boolean finished() { return false; }
}
