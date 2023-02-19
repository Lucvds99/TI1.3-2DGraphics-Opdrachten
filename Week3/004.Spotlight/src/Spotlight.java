import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import javafx.application.Application;

import static javafx.application.Application.launch;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import javax.xml.crypto.dsig.Transform;

import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;
import org.omg.CosNaming.BindingIterator;

public class Spotlight extends Application {
    private ResizableCanvas canvas;
    FXGraphics2D g2d;

    private Point2D mouse = new Point2D.Double(0,0);

    Shape clippedShape;
    boolean shapeIsSelected = false;
    private double dragX;
    private double dragY;
    private ArrayList<ColoredLine> lines = new ArrayList<>();
    private boolean init = false;

    @Override
    public void start(Stage stage) throws Exception {

        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);
        g2d = new FXGraphics2D(canvas.getGraphicsContext2D());

        new AnimationTimer() {
            long last = -1;

            @Override
            public void handle(long now) {
                if (last == -1)
                    last = now;
                update((now - last) / 1000000000.0);
                last = now;
                draw(g2d);
            }
        }.start();

        stage.setScene(new Scene(mainPane));
        stage.setTitle("Spotlight");
        stage.show();

        canvas.setOnMousePressed(e -> mousePressed(e));
        canvas.setOnMouseReleased(e -> mouseReleased(e));
        canvas.setOnMouseDragged(e -> mouseDragged(e));

        draw(g2d);
    }

    private void mouseDragged(MouseEvent e) {
        if (shapeIsSelected){
            AffineTransform tx = new AffineTransform();
            tx.translate(e.getX(), e.getY());
            tx.createTransformedShape(clippedShape);
        }
    }
    private void mouseReleased(MouseEvent e) {
        shapeIsSelected = false;
    }
    private void mousePressed(MouseEvent e) {
        mouse = new Point2D.Double(e.getX(), e.getY());
        if (clippedShape.getBounds().contains(mouse)){
            shapeIsSelected = true;

            System.out.println(shapeIsSelected);
            dragX = mouse.getX() - clippedShape.getBounds().x;
            dragY = mouse.getY() - clippedShape.getBounds().y;
        }
    }
    public void draw(FXGraphics2D graphics) {

        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.white);
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());
        for (ColoredLine line : lines) {
            graphics.setColor(line.getColor());
            graphics.draw(line);
        }

        // shape that will be clipped
        clippedShape = new Ellipse2D.Double(100, 100, 200, 200);


        graphics.draw(clippedShape);
        graphics.clip(clippedShape);


    }

    public void init() {
        for (int i = 0; i < 1000; i++) {
            Line2D line = new Line2D.Double((int) (Math.random() * 1920), ((int) (Math.random() * 1080)), (int) (Math.random() * 1920), (int) (Math.random() * 1080));

            Color color = Color.getHSBColor((float) (Math.random() * 256), 0.7f, 1);
//            Color color = Color.black;
            lines.add(new ColoredLine(line, color));
        }


    }

    public void update(double deltaTime) {

    }


    public static void main(String[] args) {
        launch(Spotlight.class);
    }

}
