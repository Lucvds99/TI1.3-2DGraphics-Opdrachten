import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;

import javafx.application.Application;

import static javafx.application.Application.launch;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

public class Rainbow extends Application {
    private ResizableCanvas canvas;

    @Override
    public void start(Stage stage) throws Exception
    {
        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);
        stage.setScene(new Scene(mainPane));
        stage.setTitle("Rainbow");
        stage.show();
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
    }


    public void draw(FXGraphics2D graphics)
    {
        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.white);
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());

        graphics.translate(canvas.getWidth()/2, canvas.getHeight()/2);

        graphics.scale(1,1);

        String[] string = new String[]{"s","u","p"," ","B","o","z","o"};

        ArrayList<Shape> letterShapes = new ArrayList<>();

        double turnIndex = Math.PI / string.length;
        Font font = new Font("Arial", Font.PLAIN, 30);

        for (int i = 0; i < string.length; i++ ) {
            letterShapes.add(font.createGlyphVector(graphics.getFontRenderContext(), string[i]).getOutline());
            AffineTransform transform = new AffineTransform();
            transform.rotate((Math.PI/(string.length - 1) * i)-(0.5 * Math.PI));
            transform.translate(0,-100);
            graphics.setColor(Color.getHSBColor(1f/ (float)string.length * i, 1, 1));
            graphics.fill(transform.createTransformedShape(letterShapes.get(i)));
        }
    }


    public static void main(String[] args)
    {
        launch(Rainbow.class);
    }

}
