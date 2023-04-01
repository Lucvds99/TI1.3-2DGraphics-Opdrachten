import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Vector2;
import org.jfree.fx.FXGraphics2D;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class GameObject {

    private Body body;
    private BufferedImage image;
    private Color color;
    private Vector2 offset;
    private double scale;
    private AffineTransform tx;
    private boolean flipped;

    public GameObject(String imageFile, Body body, Vector2 offset, double scale) {
        this.body = body;
        this.offset = offset;
        this.scale = scale;
        try {
            image = ImageIO.read(getClass().getResource(imageFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Body getBody() {
        return body;
    }

    public void setImage(String imageFile) {
        try {
            this.image = ImageIO.read(getClass().getResource(imageFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public GameObject(String imageFile, Body body, Vector2 offset, double scale, Color color) {
        this.body = body;
        this.offset = offset;
        this.scale = scale;
        try {
            image = ImageIO.read(getClass().getResource(imageFile));
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.color = color;
    }

    public void draw(FXGraphics2D g2d) {
        if (image == null) {
            return;
        }

        tx = new AffineTransform();
        tx.translate(body.getTransform().getTranslationX() * 100, body.getTransform().getTranslationY() * 100);
        tx.rotate(body.getTransform().getRotation());

        tx.translate(offset.x, offset.y);

        if (flipped)
            tx.scale(-this.scale, -this.scale);
        else
            tx.scale(this.scale, -this.scale);

        if (color != null){
            g2d.setColor(color);
        }

        tx.translate(-image.getWidth() / 2, -image.getHeight() / 2);

        g2d.drawImage(image, tx, null);
    }

    public void flipLeft() {
        flipped = true;
    }

    public void flipRight() {
       flipped = false;
    }
}
