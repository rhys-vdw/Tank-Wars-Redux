class SingleNote implements SoundGenerator {
  double frequency;

  public SingleNote(double frequency) {
    this.frequency = frequency;
  }

  @Override
  public double frequency() {
    return frequency;
  }

  @Override
  public boolean finished() {
    return false;
  }
}
