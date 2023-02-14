import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.Buffer;

import javafx.animation.AnimationTimer;
import javafx.application.Application;

import static com.sun.javafx.scene.control.skin.Utils.getResource;
import static javafx.application.Application.launch;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

public class FadingImage extends Application {
    private ResizableCanvas canvas;
    private BufferedImage memeImage1;
    private BufferedImage memeImage2;

    
    @Override
    public void start(Stage stage) throws Exception {

        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);
        FXGraphics2D g2d = new FXGraphics2D(canvas.getGraphicsContext2D());

        try {
            memeImage1 = ImageIO.read(getClass().getResource("/memePicture1.jpg"));
            memeImage2 = ImageIO.read(getClass().getResource("/memePicture2.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        new AnimationTimer() {
            long last = -1;
            @Override
            public void handle(long now) {
		if(last == -1)
                    last = now;
		update((now - last) / 1000000000.0);
		last = now;
		draw(g2d);
            }
        }.start();
        
        stage.setScene(new Scene(mainPane));
        stage.setTitle("Fading image");
        stage.show();
        draw(g2d);
    }
    
    
    public void draw(FXGraphics2D graphics) {
        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.white);
        graphics.clearRect(0, 0, (int)canvas.getWidth(), (int)canvas.getHeight());

        AffineTransform affineTransform = new AffineTransform();
        affineTransform.scale(1,1);
        graphics.drawImage(memeImage2, affineTransform, null);

    }
    

    public void update(double deltaTime) {
	
    }

    public static void main(String[] args) {
        launch(FadingImage.class);
    }

}
