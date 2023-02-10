import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.*;
import java.lang.reflect.Array;

import javafx.application.Application;

import static javafx.application.Application.launch;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

public class GradientPaintExercise extends Application {
    private ResizableCanvas canvas;
    private boolean translated = false;
    private Point2D mouse = new Point2D.Double(0,0);

    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);


        mainPane.setCenter(canvas);
        canvas.setOnMouseDragged(event -> {
            mouse = new Point2D.Double(event.getX(), event.getY());
            draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
        });
        primaryStage.setScene(new Scene(mainPane));
        primaryStage.setTitle("GradientPaint");
        primaryStage.show();
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
    }


    public void draw(FXGraphics2D graphics) {
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());
        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.white);
        if (!translated) {
            graphics.scale(1, 1);
            graphics.translate(1920 / 2, 1080 / 2);
            translated = true;
        }


        float[] fractions = new float[]{
                0.44f,
                0.77f,
                0.99f
        };


        Color[] colors = new Color[]{
                Color.CYAN,
                Color.MAGENTA,
                Color.gray
        };

        Rectangle2D.Double rect = new Rectangle2D.Double(-1000, -1000, 1000, 1000);

        RadialGradientPaint radialGradientPaint = new RadialGradientPaint(mouse, 600f, mouse, fractions, colors, MultipleGradientPaint.CycleMethod.NO_CYCLE);

        graphics.setPaint(radialGradientPaint);
        graphics.fill(rect);
        graphics.draw(rect);

    }


    public static void main(String[] args) {
        launch(GradientPaintExercise.class);
    }

}
