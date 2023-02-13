import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Paths;

import javafx.animation.AnimationTimer;
import javafx.application.Application;

import static javafx.application.Application.launch;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import javax.imageio.ImageIO;

import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

public class MovingCharacter extends Application {
    private ResizableCanvas canvas;
    private BufferedImage image;
    private int spriteIndex = 42;
    private boolean isJumping = false;

    private int startSpriteIndex = 42;
    private int endSpriteIndex = 50;
    private int xPos = 0;
    private int yPos = 0;
    private double deltaTimer = 0;

    private AffineTransform character;
    private boolean backwards = false;

    private BufferedImage[] tiles;

    @Override
    public void start(Stage stage) throws Exception {
        try {
            image = ImageIO.read(getClass().getResource("/images/sprite.png"));
            tiles = new BufferedImage[65];
            for (int i = 0; i < 65; i++)
                tiles[i] = image.getSubimage(64 * (i % 8), 64 * (i / 8), 64, 64);
        } catch (Exception e) {
            e.printStackTrace();
        }

        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(this::draw, mainPane);
        mainPane.setCenter(canvas);
        FXGraphics2D g2d = new FXGraphics2D(canvas.getGraphicsContext2D());
        new AnimationTimer() {
            long last = -1;

            @Override
            public void handle(long now) {
                if (last == -1)
                    last = now;
                update((now - last) / 1000000000.0);
                last = now;
                draw(g2d);
            }
        }.start();

        stage.setScene(new Scene(mainPane));
        stage.setTitle("Moving Character");
        stage.show();
        draw(g2d);
    }


    public void draw(FXGraphics2D graphics) {
        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.darkGray);
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());

        character = new AffineTransform();
        character.translate(xPos, yPos);

        canvas.setOnMousePressed(e -> mousePressed(e));

        if (backwards)
            character.scale(-1, 1);

        graphics.drawImage(tiles[spriteIndex], character, null);
    }


    public void update(double deltaTime) {

            deltaTimer += deltaTime;
            if (spriteIndex == endSpriteIndex)
                spriteIndex = startSpriteIndex;


            if (xPos > canvas.getWidth() - tiles[spriteIndex].getWidth()) {
                backwards = true;
            }
            if (xPos < tiles[spriteIndex].getWidth()) {
                backwards = false;
            }

            if (!backwards)
                xPos += 4;
            else
                xPos -= 4;


            if (deltaTimer > 0.06) {
                spriteIndex++;
                deltaTimer = 0;
            }
        if (isJumping){
            if (spriteIndex == 25){
                isJumping = false;
                spriteIndex = 42;


            }
            if (!backwards)
                xPos -= 4;
            else
                xPos += 4;
        }
    }

    private void mousePressed(MouseEvent e)
    {
        isJumping = true;
        spriteIndex = 17;
    }

    public static void main(String[] args) {
        launch(MovingCharacter.class);
    }

}
