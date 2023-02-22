import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Point {
    private Point2D position;
    private boolean xFlipped;
    private boolean yFlipped;
    private ArrayList<Point2D> history = new ArrayList<>();

    public Point(Point2D position, Boolean isXflipped, Boolean isYflipped) {
        this.xFlipped = isXflipped;
        this.yFlipped = isYflipped;
        this.position = position;
        this.history.add(position);
    }

    public double getX() {
        return this.position.getX();
    }

    public double getY() {
        return this.position.getY();
    }

    public void setX(int x) {
        this.position.setLocation(x, this.position.getY());
        this.history.add(this.position);
        if (this.history.size() > 10) {
            this.history.remove(0);
        }
    }

    public void setY(int y) {
        this.position.setLocation(this.position.getX(), y);
        this.history.add(this.position);
        if (this.history.size() > 10) {
            this.history.remove(0);
        }
    }

    public void setPosition(Point2D point2D) {
        this.position = point2D;
        this.history.add(this.position);
        if (this.history.size() > 10) {
            this.history.remove(0);
        }
    }

    public void setxFlipped(boolean xFlipped) {
        this.xFlipped = xFlipped;
    }

    public void setyFlipped(boolean yFlipped) {
        this.yFlipped = yFlipped;
    }

    public Point2D getPosition() {
        return position;
    }

    public boolean isxFlipped() {
        return xFlipped;
    }

    public boolean isyFlipped() {
        return yFlipped;
    }

    public ArrayList<Point2D> getHistory() {
        return history;
    }
}
