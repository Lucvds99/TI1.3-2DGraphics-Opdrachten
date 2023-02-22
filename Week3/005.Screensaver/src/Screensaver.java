import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;

import javafx.animation.AnimationTimer;
import javafx.application.Application;

import static javafx.application.Application.launch;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import javax.imageio.ImageIO;

import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;
import sun.reflect.generics.tree.Tree;

public class Screensaver extends Application {
    private ResizableCanvas canvas;

    private ArrayList<Point> points = new ArrayList<>();

    private ArrayList<Line2D> oldLines = new ArrayList<>();

    private int offset  = 2;
    private double deltaTimer;


    @Override
    public void start(Stage stage) throws Exception
    {

        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);
        FXGraphics2D g2d = new FXGraphics2D(canvas.getGraphicsContext2D());
        new AnimationTimer() {
            long last = -1;

            @Override
            public void handle(long now)
            {
                if (last == -1)
                    last = now;
                update((now - last) / 1000000000.0, g2d);
                last = now;
                draw(g2d);
            }
        }.start();

        stage.setScene(new Scene(mainPane));
        stage.setTitle("Screensaver");
        stage.show();
        draw(g2d);
    }


    public void draw(FXGraphics2D graphics) {
        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.black);
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());

        graphics.setColor(Color.white);
        graphics.draw(new Rectangle2D.Double(0, 0, 1920, 1080));

        graphics.setColor(Color.magenta);

        int numPoints = points.size();
        for (int i = 0; i < numPoints; i++) {
            Point startPoint = points.get(i);
            Point endPoint = points.get((i + 1) % numPoints);

            Line2D.Double line = new Line2D.Double((int) startPoint.getX(), (int) startPoint.getY(), (int) endPoint.getX(), (int) endPoint.getY());

            graphics.draw(line);

            oldLines.add(line);
            if (oldLines.size()>800){
                for (int j = 0; j < 4; j++) {
                    oldLines.remove(oldLines.remove(j));
                }
            }
            for (int j = 0; j < oldLines.size(); j+=5) {
                graphics.draw(oldLines.get(j));
            }
        }
    }

    public void init()
    {
        points.add(new Point(new Point2D.Double(200, 200), true, true));
        points.add(new Point(new Point2D.Double(600, 200), true, false));
        points.add(new Point(new Point2D.Double(600, 600), false, true));
        points.add(new Point(new Point2D.Double(200, 600), false, false));
    }

    public void update(double deltaTime, FXGraphics2D graphics)
    {
        deltaTimer += deltaTime;
        if (deltaTimer>0.006){
            for (Point point : points) {

                //set the X flipped
                if (point.getX()>=1920)
                    point.setxFlipped(true);
                if (point.getX()<=0)
                    point.setxFlipped(false);

                //set the Y flipped
                if (point.getY()>=1080)
                    point.setyFlipped(true);
                if (point.getY()<=0)
                    point.setyFlipped(false);

                //apply offset
                if (point.isxFlipped())
                    point.setX((int) (point.getX()-offset));
                else
                    point.setX((int) (point.getX()+offset));
                if (point.isyFlipped())
                    point.setY((int) (point.getY()-offset));
                else
                    point.setY((int) (point.getY()+offset));

             //reset timer
            deltaTimer = 0;
            }
        }
    }

    public static void main(String[] args)
    {
        launch(Screensaver.class);
    }

}
