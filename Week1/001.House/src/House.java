import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;

import javax.sound.sampled.Line;
import java.awt.geom.Line2D;

public class House extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Canvas canvas = new Canvas(1920, 1080);
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
        primaryStage.setScene(new Scene(new Group(canvas)));
        primaryStage.setTitle("House");
        primaryStage.show();
    }


    public void draw(FXGraphics2D graphics) {
        //floor
        graphics.draw(new Line2D.Double(200, 500, 500, 500));
        //right wall
        graphics.draw(new Line2D.Double(500, 300, 500, 500));
        //left wall
        graphics.draw(new Line2D.Double(200, 300, 200, 500));
        //left roof
        graphics.draw(new Line2D.Double(200, 300, 350, 100));
        //right roof
        graphics.draw(new Line2D.Double(500, 300, 350, 100));

        //inner parts of house
        //left side door
        graphics.draw(new Line2D.Double(240, 500, 240, 380));
        //right side door
        graphics.draw(new Line2D.Double(300, 500, 300, 380));
        //top side door
        graphics.draw(new Line2D.Double(240, 380, 300, 380));
        // left side window
        graphics.draw(new Line2D.Double(325, 450, 325, 350));
        // right side window
        graphics.draw(new Line2D.Double(475, 450, 475, 350));
        //bottom side window
        graphics.draw(new Line2D.Double(325, 450, 475, 450 ));
        //top side window
        graphics.draw(new Line2D.Double(325, 350, 475, 350));
    }



    public static void main(String[] args) {
        launch(House.class);
    }

}
