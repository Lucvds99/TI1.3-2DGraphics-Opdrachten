import java.awt.*;
import java.awt.geom.*;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;

public class Spiral extends Application {
    Canvas canvas = new Canvas();
    @Override
    public void start(Stage primaryStage) throws Exception {
        canvas = new Canvas(1920, 1080);
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
        primaryStage.setScene(new Scene(new Group(canvas)));
        primaryStage.setTitle("Spiral");
        primaryStage.show();
    }

    public void draw(FXGraphics2D graphics) {
        graphics.translate(canvas.getWidth()/2, canvas.getHeight()/2);
        graphics.scale( 1, -1);

        graphics.setColor(Color.red);
        graphics.drawLine(0,0,1000,0);
        graphics.setColor(Color.green);
        graphics.drawLine(0,0,0,1000);
        graphics.setColor(Color.black);

        float scale = 50;
        float n = 0.1f;
        float LastX = 0;
        float LastY = 0;

        for (float i = 0; i < 100; i+=0.1f) {
            float x = (float)(n * i * Math.cos(i));
            float y = (float)(n * i * Math.sin(i));

            graphics.draw(new Line2D.Double(x * scale, y * scale, LastX * scale, LastY * scale));
            LastX = x;
            LastY = y;
        }


    }


    
    public static void main(String[] args) {
        launch(Spiral.class);
    }

}
