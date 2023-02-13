import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;

import javafx.application.Application;

import static javafx.application.Application.launch;

import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

public class BlockDrag extends Application {

    ResizableCanvas canvas;
    private Point2D mouse = new Point2D.Double(0,0);

    private ArrayList<Renderable> renderables = new ArrayList<>();
    private double dragX;
    private double dragY;

    private Renderable selectedBlock;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);
        primaryStage.setScene(new Scene(mainPane));
        primaryStage.setTitle("Block Dragging");
        primaryStage.show();

        canvas.setOnMousePressed(e -> mousePressed(e));
        canvas.setOnMouseReleased(e -> mouseReleased(e));
        canvas.setOnMouseDragged(e -> mouseDragged(e));

        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
    }


    public void draw(FXGraphics2D graphics)
    {
        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.white);
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());

        for (Renderable renderable : renderables) {

            graphics.setColor(renderable.getColor());
            graphics.fill(renderable.getTransformedShape());
            renderable.draw(graphics);
        }


    }


    public static void main(String[] args)
    {
        launch(BlockDrag.class);
    }

    private void mousePressed(MouseEvent e)
    {
        mouse = new Point2D.Double(e.getX(), e.getY());
        for (Renderable block : renderables) {
            if (block.getTransformedShape().contains(e.getX(), e.getY())) {
                selectedBlock = block;
                System.out.println(block);
                dragX = mouse.getX() - selectedBlock.getPosition().getX();
                dragY = mouse.getY() - selectedBlock.getPosition().getY() ;
                break;
            }
        }
    }

    private void mouseReleased(MouseEvent e)
    {
        this.selectedBlock = null;
    }

    private void mouseDragged(MouseEvent e)
    {
        if (selectedBlock != null) {
            selectedBlock.setPosition(new Point2D.Double(e.getX() + dragX, e.getY() + dragY));
        }
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
    }

    public void init() {
        int width = 100;
        int height = 100;
        for (int i = 0; i <50; i++){
            double x =  (Math.random() * 1820);
            double y =  (Math.random() * 980);

            Area block = new Area(new Rectangle2D.Double(-50, -50 , width, height));
            Point2D point2D = new Point2D.Double(x , y );
            Color color = (Color.getHSBColor((float) (Math.random() * 256), 0.7f, 1));

            Renderable renderable = new Renderable(block, point2D, 0, 1, color );
            renderables.add(renderable);
        }
    }

}
