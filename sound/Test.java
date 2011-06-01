class Test {
  public static void main(String[] args) {
    SoundGenerator a = new RandomFrequency(200.0, 800.0);//RandomFrequency(700.0, 900.0);
    Sound.setSoundGenerator(a);
    while (true) {
      Sound.play(50);
    }
  }
}

