import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class testTriangle {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setSize(1920, 1080);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new TrianglePanel());
        frame.setVisible(true);
    }

    static class TrianglePanel extends JPanel {
        int angle = 0;

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(Color.RED);
            g2d.fill(getTriangle());
            angle += 5;
            repaint();
        }

        private Polygon getTriangle() {
            int x[] = {960, 935, 985};
            int y[] = {540, 515, 515};
            Polygon triangle = new Polygon(x, y, 3);
            AffineTransform transform = new AffineTransform();
            transform.rotate(Math.toRadians(angle), 960, 540);
            return new Polygon(
                    triangle.xpoints,
                    triangle.ypoints,
                    3);
        }
    }
}
