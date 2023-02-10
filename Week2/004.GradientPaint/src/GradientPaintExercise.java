import java.awt.*;
import java.awt.geom.*;
import java.lang.reflect.Array;

import javafx.application.Application;

import static javafx.application.Application.launch;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

public class GradientPaintExercise extends Application {
    private ResizableCanvas canvas;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);
        primaryStage.setScene(new Scene(mainPane));
        primaryStage.setTitle("GradientPaint");
        primaryStage.show();
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
    }


    public void draw(FXGraphics2D graphics)
    {
        graphics.scale(1,1);
        graphics.translate(1920/2, 1080/2);
        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.white);
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());


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



        RadialGradientPaint radialGradientPaint = new RadialGradientPaint((float)canvas.getWidth()/2,(float)canvas.getHeight()/2 , 600, fractions, colors);


        Area area = new Area(new Rectangle2D.Double(-80000,-80000, 80000, 80000));
        graphics.fill(area);
        graphics.setPaint(radialGradientPaint);
        graphics.draw(area);
    }


    public static void main(String[] args)
    {
        launch(GradientPaintExercise.class);
    }

}
