
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Objects;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.World;
import org.dyn4j.dynamics.joint.PinJoint;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.Mass;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

import javax.imageio.ImageIO;

public class AngryBirds extends Application {

    private ResizableCanvas canvas;
    private World world;
    private MousePicker mousePicker;

    private  PinJoint catapultJoint;
    private Camera camera;

    private boolean isAttatched;
    private boolean debugSelected = false;

    private BufferedImage backgroundImage;

    private FXGraphics2D g2d;
    private ArrayList<GameObject> gameObjects = new ArrayList<>();

    @Override
    public void start(Stage stage) throws Exception {

        BorderPane mainPane = new BorderPane();

        // Add debug button
        javafx.scene.control.CheckBox showDebug = new CheckBox("Show debug");
        showDebug.setOnAction(e -> {
            debugSelected = showDebug.isSelected();
        });

        mainPane.setTop(showDebug);

        backgroundImage = ImageIO.read(Objects.requireNonNull(getClass().getResource("background.png")));

        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);
        g2d = new FXGraphics2D(canvas.getGraphicsContext2D());

        camera = new Camera(canvas, g -> draw(g), g2d);
        mousePicker = new MousePicker(canvas);

        new AnimationTimer() {
            long last = -1;

            @Override
            public void handle(long now) {
                if (last == -1) {
                    last = now;
                }
                update((now - last) / 1.0e9);
                last = now;
                draw(g2d);
            }
        }.start();

        stage.setScene(new Scene(mainPane, 1920, 1080));
        stage.setTitle("Angry Birds");
        stage.show();
        draw(g2d);
    }

    public void update(double deltaTime) {
        mousePicker.update(world, camera.getTransform((int) canvas.getWidth(), (int) canvas.getHeight()), 100);
        world.update(deltaTime);

        if (catapultJoint.getAnchor1().getAngleBetween(catapultJoint.getAnchor2()) < 0 ){
            world.removeJoint(catapultJoint);
        }
    }

    public void draw(FXGraphics2D graphics) {
        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.white);

        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());

        AffineTransform originalTransform = graphics.getTransform();

        graphics.setTransform(camera.getTransform((int) canvas.getWidth(), (int) canvas.getHeight()));
        graphics.scale(1, -1);

        for (GameObject go : gameObjects) {
            go.draw(graphics);
        }

        if (debugSelected) {
            graphics.setColor(Color.blue);
            DebugDraw.draw(graphics, world, 100);
        }
        graphics.setTransform(originalTransform);
    }

    public void init() {
        world = new World();
        world.setGravity(new Vector2(0, -9.8));

        Body floor = new Body();
        floor.addFixture(Geometry.createRectangle(80, 1));
        floor.getTransform().setTranslation(0, -5);
        floor.setMass(MassType.INFINITE);
        world.addBody(floor);
        gameObjects.add(new GameObject("", floor, new Vector2(0,0), 1));

        double bodyWidth = 0.25;
        double bodyHeight = 0.25;
        double spacing = 0.05; // spacing between each body

        for(int y = 0; y < 10; y++) {
            for (int x = 0; x < 20-y; x++) {
                Body box = new Body();
                box.addFixture(Geometry.createRectangle(bodyWidth, bodyHeight));

                box.setMass(new Mass(new Vector2(0,0), 0.00001,1.0));

                // Calculate the translation for this body
                double xPos = -2 + x * (bodyWidth + spacing) + (y * 0.5 * (bodyWidth + spacing));
                double yPos = -4.5 +  y * (bodyHeight + spacing);

                box.getTransform().setTranslation(xPos, yPos);
                world.addBody(box);

                gameObjects.add(new GameObject("block.png", box, new Vector2(0,0), 0.1));
            }
        }

        Body catapult = new Body();
        catapult.getTransform().setTranslation(-8, -3);
        catapult.setMass(MassType.INFINITE);
        world.addBody(catapult);
        gameObjects.add(new GameObject("", catapult, new Vector2(0,0), 1));

        Body bird = new Body();
        bird.addFixture(Geometry.createCircle(0.15));
        bird.getTransform().setTranslation(-8,-3);
        bird.setMass(MassType.NORMAL);
        bird.getFixture(0).setRestitution(0.20);
        world.addBody(bird);
        gameObjects.add(new GameObject("redBird.png", bird, new Vector2(0,0), 0.05));

        catapultJoint = new PinJoint(bird, catapult.getTransform().getTranslation(), 10, 0, 5);
        catapultJoint.setTarget(catapult.getTransform().getTranslation());
        catapultJoint.setCollisionAllowed(false);
        world.addJoint(catapultJoint);

        isAttatched = true;
    }

    public static void main(String[] args) {
        launch(AngryBirds.class);
    }
}
