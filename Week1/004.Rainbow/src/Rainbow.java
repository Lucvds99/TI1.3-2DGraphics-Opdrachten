import java.awt.*;
import java.awt.geom.*;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;

public class Rainbow extends Application {
    Canvas canvas = new Canvas();
    @Override
    public void start(Stage primaryStage) throws Exception {
        canvas = new Canvas(1920, 1080);
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
        primaryStage.setScene(new Scene(new Group(canvas)));
        primaryStage.setTitle("Rainbow");
        primaryStage.show();
    }



    public void draw(FXGraphics2D graphics) {

        //sets the origin to middle
        graphics.translate(1920/2,1000);
        graphics.scale(1,-1);

        // the amount of steps its going to take with ur for loop
        float resolution = (float)0.0001;
        //  with radius in and out you set the with of the arch
        float radiusBinnen = (float) 600;
        float radiusBuiten = (float) 700;

        //the increment is the amount of color spread over the sphere
        float increment = ((float)500.0/((float)Math.PI)*(float)(Math.PI));
        for(float i = 0; i < Math.PI; i+= resolution) {
            graphics.setColor(Color.getHSBColor(i*increment/500f, 1, 1));
            float x1 = radiusBinnen * (float)Math.cos(i);
            float y1 = radiusBinnen * (float)Math.sin(i);
            float x2 = radiusBuiten * (float)Math.cos(i);
            float y2 = radiusBuiten * (float)Math.sin(i);
            graphics.draw(new Line2D.Double(x1,y1,x2,y2));
        }


    }
    
    
    
    public static void main(String[] args) {
        launch(Rainbow.class);
    }

}
