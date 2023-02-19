import java.awt.*;
import java.awt.geom.Line2D;

public class ColoredLine extends Line2D.Double {
    private final Color color;

    public ColoredLine(Line2D line, Color color) {
        super(line.getP1(), line.getP2());
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}

