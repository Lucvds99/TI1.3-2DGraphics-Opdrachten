import com.sun.glass.events.KeyEvent;
import comon.Camera;
import comon.DebugDraw;
import comon.MousePicker;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.World;
import org.dyn4j.dynamics.joint.WeldJoint;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Transform;
import org.dyn4j.geometry.Vector2;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;


import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Vector;

public class JumpQueen extends Application {

    private ResizableCanvas canvas;

    private World world;
    private MousePicker mousePicker;
    private Camera camera;
    private boolean debugSelected = false;
    private FXGraphics2D g2d;

    private double spaceHold;
    private ArrayList<GameObject> gameObjects = new ArrayList<>();
    private boolean spaceKeyPressed = false;


    private final Body character = new Body();

    @Override
    public void start(Stage stage) throws Exception {

        BorderPane mainPane = new BorderPane();

        // Add debug button
        CheckBox showDebug = new CheckBox("Show debug");
        showDebug.setOnAction(e -> {
            debugSelected = showDebug.isSelected();
        });

        mainPane.setTop(showDebug);


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
        stage.setTitle("JumpQueen");
        stage.show();
        draw(g2d);

        canvas.requestFocus();
    }

    private void spaceKey() {
        System.out.println(spaceHold);
        if (spaceHold > 3){
            spaceHold = 3;
        }else {
            spaceHold += 0.5;
        }
    }

    private void spaceKeyRelease() {
        character.applyForce(new Vector2(0, 100 * spaceHold));
        spaceHold = 0;
    }

    private void rightArrow() {
        character.setLinearVelocity(new Vector2(5, character.getLinearVelocity().y));
        if (spaceHold > 3){
            spaceHold = 3;
        }else {
            spaceHold += 0.5;
        }
    }

    private void leftArrow() {
        character.setLinearVelocity(new Vector2(-5, character.getLinearVelocity().y));
        if (spaceHold > 3){
            spaceHold = 3;
        }else {
            spaceHold += 0.5;
        }
    }

    public void update(double deltaTime) {
        mousePicker.update(world, camera.getTransform((int) canvas.getWidth(), (int) canvas.getHeight()), 100);
        world.update(deltaTime);


        canvas.setOnKeyPressed(event -> {
            switch (event.getCode()){
                case LEFT:
                    leftArrow();
                    break;
                case RIGHT:
                    rightArrow();
                    break;
                case SPACE:
                    spaceKey();
                    break;
            }
        });

        canvas.setOnKeyReleased(event -> {
            if (event.getCode().equals(KeyCode.SPACE)){
                spaceKeyRelease();
            }
        });
        canvas.requestFocus();
        character.getTransform().setRotation(0);
    }

    public void draw(FXGraphics2D graphics) {
        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.gray);

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

        double bodyWidth = 1;
        double bodyHeight = 1;
        double spacing = -0.001;




        //floor
        for (int y = 0; y < 1; y++) {
            for (int x = 0; x < 16; x++) {
                Body boundryBlock = new Body();
                boundryBlock.addFixture(Geometry.createRectangle(bodyWidth,bodyHeight));
                boundryBlock.setMass(MassType.INFINITE);

                double xPos = -7 + x * (bodyWidth + spacing) + (y * 2 * (bodyWidth + spacing));
                double yPos = -5 + y * (bodyHeight + spacing);

                boundryBlock.getTransform().setTranslation(xPos, yPos);
                world.addBody(boundryBlock);

                gameObjects.add(new GameObject("wall.jpg", boundryBlock, new Vector2(0, 0), 0.13));
            }
        }

        //walls
        for (int x = 0; x <=1; x++) {
            for (int y = 0; y < 14; y++) {
                Body boundryBlock = new Body();
                boundryBlock.addFixture(Geometry.createRectangle(bodyWidth,bodyHeight));
                boundryBlock.setMass(MassType.INFINITE);
                double xPos;

                if (x == 0){
                    xPos = 8;
                }else {
                    xPos = -8;
                }

                double yPos = -5 + y * (bodyHeight + spacing);

                boundryBlock.getTransform().setTranslation(xPos, yPos);
                world.addBody(boundryBlock);

                gameObjects.add(new GameObject("wall.jpg", boundryBlock, new Vector2(0, 0), 0.13));
            }
        }

        //character
        character.addFixture(Geometry.createRectangle(0.5, 1));
        character.setMass(MassType.NORMAL);
        character.getTransform().setTranslation(0,0);
        character.getFixture(0).setRestitution(0.2);
        character.setAngularDamping(Double.MAX_VALUE);
        character.getFixture(0).setFriction(10);
              world.addBody(character);



        gameObjects.add(new GameObject("girl_standing.png", character, new Vector2(0,0), 0.2));
    }

    public static void main(String[] args) {
        launch(JumpQueen.class);
    }
}
