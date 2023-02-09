import java.awt.*;
import java.awt.geom.*;

import javafx.application.Application;

import static javafx.application.Application.launch;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

public class YingYang extends Application {
    private ResizableCanvas canvas;

    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);
        primaryStage.setScene(new Scene(mainPane));
        primaryStage.setTitle("Ying Yang");
        primaryStage.show();
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
    }

    void draw(FXGraphics2D graphics) {
        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.white);
        Area cirkel = new Area(new Ellipse2D.Double(200, 150, 300, 300));
        //150 |75 200 + 150 - 75 = 350 - 75 = 275
        Area topCircel = new Area(new Ellipse2D.Double(275, 150, 150, 150));

        Area smallTopCircel = new Area(new Ellipse2D.Double(325, 200, 50, 50));

        Area smallBottomCircel = new Area(new Ellipse2D.Double(325, 350, 50, 50));

        Area buttomCirkel = new Area(new Ellipse2D.Double(275, 300, 150, 150));

        Area rectangle = new Area(new Rectangle2D.Double(350, 150, 150, 300));

        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());
        rectangle.subtract(topCircel);

        rectangle.intersect(cirkel);

        topCircel.add(cirkel);

        graphics.setColor(Color.black);
        graphics.draw(cirkel);
        graphics.draw(topCircel);
        graphics.fill(buttomCirkel);
        graphics.draw(buttomCirkel);
        graphics.fill(rectangle);
        graphics.draw(rectangle);
        graphics.fill(smallTopCircel);
        graphics.draw(smallTopCircel);

        graphics.setColor(Color.white);
        graphics.fill(smallBottomCircel);
        graphics.draw(smallBottomCircel);
    }

    public static void main(String[] args) {
        launch(YingYang.class);
    }

}
