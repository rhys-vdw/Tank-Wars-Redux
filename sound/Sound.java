import javax.sound.sampled.*;
import java.nio.ByteBuffer;
import java.io.ByteArrayInputStream;

class Sound {
  private static SoundGenerator soundGenerator;
  private static AudioFormat audioFormat;
  private static SourceDataLine line;
  private static double volume;

  private final static float SAMPLE_RATE = 44100f;
  private final static int SAMPLE_BITS = Byte.SIZE;
  private final static int SAMPLE_BYTES = SAMPLE_BITS / 8;
  private final static int CHANNELS = 1;
  private final static boolean SIGNED = true;
  private final static boolean BIG_ENDIAN = true;
  private final static double MAX_AMPLITUDE = Byte.MAX_VALUE;
  private final static double MIN_AMPLITUDE = Byte.MIN_VALUE;
  private final static int MAX_DURATION_MS = 3000;
  private final static double DEFAULT_VOLUME = 0.1;

  private final static String EXCEPTION_MSG = "Exception caught: ";
  static {
    Sound.soundGenerator = null;
    Sound.audioFormat = new AudioFormat(SAMPLE_RATE, SAMPLE_BITS, CHANNELS,
        SIGNED, BIG_ENDIAN);
    try {
      Sound.initializeLine();
    } catch(LineUnavailableException e) {
      System.out.println(EXCEPTION_CAUGHT + e.getMessage());
      e.printStackTrace();
    }
    Sound.volume = DEFAULT_VOLUME;
  }

  /** Set the volume level. Values in range [0, 1] are valid.
   *  @param volume the desired volume level
   */
  public static void setVolume(double volume) {
    Sound.volume = volume; 
  }

  /** Initializes the SourceDataLine member.
   */
  private static void initializeLine() throws LineUnavailableException {
    Sound.line = AudioSystem.getSourceDataLine(audioFormat);
    Sound.line.open(audioFormat);
    Sound.line.start();
  }

  /** Set the SoundGenerator to query when playing a note. Set to null to stop
   *  playback.
   *  @param soundGenerator SoundGenerator used to determine frequency
   */
  public static void setSoundGenerator(SoundGenerator soundGenerator) {
    Sound.stopPlaying();
    Sound.soundGenerator = soundGenerator;
    Sound.line.start();
  }

  /** Clears the audio in the buffer. This should be called when changing the
   *  SoundGenerator.
   */
  private static void stopPlaying() {
    if (Sound.line.isActive()) {
      Sound.line.stop(); /* TODO: test */
    }
    Sound.line.flush();
  }

  /** Set current frequency being played. The frequency to be played is
   *  determined by the specified SoundGenerator. If none has been specified, no
   *  sound will be played.
   */
  public static void play(int duration) {
    if (Sound.soundGenerator != null) {
      if (Sound.soundGenerator.finished()) {
        Sound.stopPlaying();
        Sound.soundGenerator = null;
      } else {
        duration = Math.min(duration, MAX_DURATION_MS); 
        byte[] wave = Sound.createSquareWave(soundGenerator.frequency(),
            duration);
        Sound.line.write(
            createSquareWave(soundGenerator.frequency(), wave.length),
            0, wave.length);
      }
    }
  }

  /** Generate a square wave pattern.
   *  @param frequency  the frequency of the square wave, in Hz
   *  @param duration   the length of time the tone will play for, in ms
   *  @return           a square soundwave in signed PCM format
   */
  private static byte[] createSquareWave(double frequency, int duration) {
    int samplesInPeriod = (int) Math.ceil(SAMPLE_RATE / frequency);
    int halfSamplesInPeriod = (int) samplesInPeriod / 2;
    int totalSamples = duration * (int) Math.ceil(SAMPLE_RATE / 1000);
    ByteBuffer data = ByteBuffer.allocate(totalSamples * SAMPLE_BYTES);

    for (int i = 0; i < totalSamples; i++) {
      int index = i * SAMPLE_BYTES;
      byte value = (byte) (Sound.volume
          * (i % samplesInPeriod < halfSamplesInPeriod ? MIN_AMPLITUDE
          : MAX_AMPLITUDE));
      data.put(index, value);
    }

    return data.array();
  }
}

