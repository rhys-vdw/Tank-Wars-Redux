import java.awt.*;
import java.awt.event.*;
import java.applet.Applet;

class Test extends Frame {
  private int width, height;
  private final static int DEFAULT_WIDTH = 640;
  private final static int DEFAULT_HEIGHT = 480;
  private final static String APPLICATION_TITLE = "Tank Wars Redux";

  public Test() {
    super(APPLICATION_TITLE);
    this.width = DEFAULT_WIDTH;
    this.height = DEFAULT_HEIGHT;

    this.setSize(width, height);
    this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
    this.setResizable(false);

    this.addWindowListener(new WindowAdapter() {
      public void WindowClosing(WindowEvent e) {
        System.out.println("Window closing.");
        System.exit(0);
      }
      public void WindowClosed(WindowEvent e) {
        System.out.println("Window closed");
        System.exit(0);
      }
      public void WindowIconified(WindowEvent e) {
        System.out.println("Window iconified");
        System.exit(0);
      }
    });

    Applet testDraw = new TestDraw(width, height);
    this.add(testDraw);
    testDraw.init();
  }

  public static void main(String[] args) {
    new Test().setVisible(true);
  }
}

