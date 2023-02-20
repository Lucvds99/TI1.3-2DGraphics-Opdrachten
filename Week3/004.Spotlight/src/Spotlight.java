import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import javafx.application.Application;

import static javafx.application.Application.launch;

import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

public class Spotlight extends Application {
    private ResizableCanvas canvas;
    FXGraphics2D g2d;

    private Point2D mouse = new Point2D.Double(0, 0);

    private AffineTransform tx = new AffineTransform();

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
            @Override
            public void handle(long now) {draw(g2d);}
        }.start();

        stage.setScene(new Scene(mainPane));
        stage.setTitle("Spotlight");
        stage.show();

        canvas.setOnMouseMoved(e -> mouseMoved(e));

        draw(g2d);
    }

    private void mouseMoved(MouseEvent e) {
        dragX = e.getX();
        dragY = e.getY();
    }

    public void draw(FXGraphics2D graphics) {

        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.white);
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());

        Shape clippedShape = new Ellipse2D.Double(dragX - 100, dragY - 100, 200, 200);

        graphics.setClip(clippedShape);
        graphics.draw(clippedShape);

        for (ColoredLine line : lines) {
            graphics.setColor(line.getColor());
            graphics.draw(line);
        }
        graphics.setClip(null);
    }

    public void init() {
        for (int i = 0; i < 1000; i++) {
            Line2D line = new Line2D.Double((int) (Math.random() * 1920), ((int) (Math.random() * 1080)), (int) (Math.random() * 1920), (int) (Math.random() * 1080));

            Color color = Color.getHSBColor((float) (Math.random() * 256), 0.7f, 1);
            lines.add(new ColoredLine(line, color));
        }
    }

    public static void main(String[] args) {
        launch(Spotlight.class);
    }

}
