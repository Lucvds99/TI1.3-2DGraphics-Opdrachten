import org.jfree.fx.FXGraphics2D;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.Random;

public class DistanceConstraint implements Constraint {

    private double distance;
    private double setDistance;
    private double adjustmentDistance;
    private double distanceDiffrence;

    private Color color;
    private Particle a;
    private Particle b;

    public DistanceConstraint(Particle a, Particle b) {
        this(a, b, a.getPosition().distance(b.getPosition()));
    }

    public DistanceConstraint(Particle a, Particle b, double distance) {
        this.color = Color.GREEN;
        this.a = a;
        this.b = b;
        this.distance = distance;
        this.setDistance = distance;
    }

    public Color getColor(double distanceDifference) {
        if (distanceDifference <= 0) {
            return Color.RED;
        } else {
            double factor = distanceDifference / setDistance;
            int greenValue = (int) (255 * (1 - factor));
            int redValue = (int) (255 * factor);

            if (greenValue > 255)
                greenValue = 255;
            else if (greenValue < 0) {
                greenValue = 0;
            }
            if (redValue > 255)
                redValue = 255;
            else if (redValue<0) {
                redValue = 0;
            }

            return new Color(redValue, greenValue, 0);
        }
    }

    @Override
    public void satisfy() {
        double currentDistance = a.getPosition().distance(b.getPosition());

        adjustmentDistance = (currentDistance - distance) / 2;

        Point2D BA = new Point2D.Double(b.getPosition().getX() - a.getPosition().getX(), b.getPosition().getY() - a.getPosition().getY());
        double length = BA.distance(0, 0);
        if (length > 0.0001) {
            BA = new Point2D.Double(BA.getX() / length, BA.getY() / length);
        } else {
            BA = new Point2D.Double(1, 0);
        }

        a.setPosition(new Point2D.Double(a.getPosition().getX() + BA.getX() * adjustmentDistance,
                a.getPosition().getY() + BA.getY() * adjustmentDistance));
        b.setPosition(new Point2D.Double(b.getPosition().getX() - BA.getX() * adjustmentDistance,
                b.getPosition().getY() - BA.getY() * adjustmentDistance));
    }

    @Override
    public void draw(FXGraphics2D g2d) {
        g2d.setColor(getColor(adjustmentDistance));
        g2d.draw(new Line2D.Double(a.getPosition(), b.getPosition()));
    }
}

