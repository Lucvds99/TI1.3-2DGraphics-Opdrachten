import org.jfree.fx.FXGraphics2D;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Point2D;

public class Renderable {
        private Area shape;
        private Point2D position;
        private float rotation;
        private float scale;
        private Color color;

        public Renderable(Area shape, Point2D position, float rotation, float scale, Color color)
        {
            this.shape = shape;
            this.position = position;
            this.rotation = rotation;
            this.scale = scale;
            this.color = color;
        }

        public void draw(FXGraphics2D g2d)
        {
            g2d.draw(getTransformedShape());
        }

        public Shape getTransformedShape()
        {
            return getTransform().createTransformedShape(shape);
        }

        public AffineTransform getTransform()
        {
            AffineTransform tx = new AffineTransform();
            tx.translate(position.getX(), position.getY());
            tx.rotate(rotation);
            tx.scale(scale,scale);
            return tx;
        }

    public Color getColor() {
        return color;
    }

    public Point2D getPosition(){
            return this.position;
    }

    public void setPosition(Point2D position){
            this.position = position;
    }

}

