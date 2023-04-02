import comon.Camera;
import comon.DebugDraw;
import comon.MousePicker;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.BlockingDeque;

public class JumpQueen extends Application {

    private final Body character = new Body();
    GameObject characterObject;
    private ResizableCanvas canvas;
    private World world;
    private MousePicker mousePicker;
    private Camera camera;
    private boolean debugSelected = false;
    private FXGraphics2D g2d;
    private double spaceHold;
    private ArrayList<GameObject> gameObjects = new ArrayList<>();
    private boolean isMovingRight = false;
    private long leftArrowDownTime;
    private long rightArrowDownTime;
    //keys
    private boolean isLeftArrowPressed;
    private boolean isRightArrowPressed;
    private boolean isSpaceBarPressed;
    private boolean wasLeftArrowPressed = false;
    private boolean wasRightArrowPressed = false;
    private long elapsedTime;



    private BufferedImage jumping;
    private BufferedImage standing;
    private BufferedImage flying;

    public static void main(String[] args) {
        launch(JumpQueen.class);
    }

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
                if (last == -1)
                    last = now;
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

    private void jumpLeft() {
        characterObject.flipLeft();
        if (character.getLinearVelocity().y == 0) {
            elapsedTime = leftArrowDownTime;

            System.out.println("Left arrow key held down for " + elapsedTime + " ms");
            if (elapsedTime > 0) {
                elapsedTime = elapsedTime / 5;
                character.applyForce(new Vector2(-100 + -elapsedTime, -elapsedTime));
            }
            leftArrowDownTime = 0;
            elapsedTime = 0;
        }
    }

    private void jumpRight() {
        characterObject.flipRight();
        if (character.getLinearVelocity().y == 0) {
            elapsedTime = rightArrowDownTime;

            System.out.println("Right arrow key held down for " + elapsedTime + " ms");
            if (elapsedTime > 0) {
                elapsedTime = elapsedTime / 5;
                character.applyForce(new Vector2(100 + elapsedTime, -elapsedTime));
            }
            elapsedTime = 0;
            rightArrowDownTime = 0;
        }
    }

    public void update(double deltaTime) {
        mousePicker.update(world, camera.getTransform((int) canvas.getWidth(), (int) canvas.getHeight()), 100);
        world.update(deltaTime);
        character.getTransform().setRotation(0);

        //select right image on movement.
        if(!(character.getLinearVelocity().y < 0.5 && character.getLinearVelocity().x < 0.5)){
            characterObject.setImage(flying);
        } else if (isRightArrowPressed || isLeftArrowPressed) {
            characterObject.setImage(jumping);
        } else{
            characterObject.setImage(standing);
        }

        System.out.println(character.getLinearVelocity().y);
        System.out.println(character.getLinearVelocity().x);
        System.out.println("//////////////////////");

        canvas.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case LEFT:
                    isLeftArrowPressed = true;
                    leftArrowDownTime += System.currentTimeMillis() / 1e10;
                    break;
                case RIGHT:
                    isRightArrowPressed = true;
                    rightArrowDownTime += System.currentTimeMillis() / 1e10;
                    break;
            }
        });

        canvas.setOnKeyReleased(event -> {
            switch (event.getCode()) {
                case LEFT:
                    isLeftArrowPressed = false;
                    jumpLeft();
                    break;
                case RIGHT:
                    isRightArrowPressed = false;
                    jumpRight();
                    break;
            }
        });

        // Move character left or right based on arrow keys
        if (isLeftArrowPressed) {
            if (leftArrowDownTime < 20)
                jumpLeft();
        } else if (isRightArrowPressed) {
            if (rightArrowDownTime < 20)
                jumpRight();
        }
        // Jump left or right when arrow key is released
        if (!isLeftArrowPressed && wasLeftArrowPressed) {
            jumpLeft();
        }
        if (!isRightArrowPressed && wasRightArrowPressed) {
            jumpRight();
        }
        // Update arrow key status for next frame
        wasLeftArrowPressed = isLeftArrowPressed;
        wasRightArrowPressed = isRightArrowPressed;
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

        try {
            jumping = ImageIO.read(getClass().getResource("girl_jumping.png"));
            standing = ImageIO.read(getClass().getResource("girl_standing.png"));
            flying = ImageIO.read(getClass().getResource("girl_flying.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //floor
        for (int y = 0; y < 1; y++) {
            for (int x = 0; x < 16; x++) {
                Body boundryBlock = new Body();
                boundryBlock.addFixture(Geometry.createRectangle(bodyWidth, bodyHeight));
                boundryBlock.setMass(MassType.INFINITE);

                double xPos = -7 + x * (bodyWidth + spacing) + (y * 2 * (bodyWidth + spacing));
                double yPos = -5 + y * (bodyHeight + spacing);

                boundryBlock.getTransform().setTranslation(xPos, yPos);
                world.addBody(boundryBlock);

                gameObjects.add(new GameObject("wall.jpg", boundryBlock, new Vector2(0, 0), 0.13));
            }
        }

        //walls
        for (int x = 0; x <= 1; x++) {
            for (int y = 0; y < 14; y++) {
                Body boundryBlock = new Body();
                boundryBlock.addFixture(Geometry.createRectangle(bodyWidth, bodyHeight));
                boundryBlock.setMass(MassType.INFINITE);
                boundryBlock.getFixture(0).setRestitution(1);
                double xPos;

                if (x == 0) {
                    xPos = 8;
                } else {
                    xPos = -8;
                }

                double yPos = -5 + y * (bodyHeight + spacing);

                boundryBlock.getTransform().setTranslation(xPos, yPos);
                world.addBody(boundryBlock);

                gameObjects.add(new GameObject("wall.jpg", boundryBlock, new Vector2(0, 0), 0.13));
            }
        }

        //levels
        lvl1();

        //character
        character.addFixture(Geometry.createRectangle(0.5, 1));
        character.setMass(MassType.NORMAL);
        character.getTransform().setTranslation(-6, -4);
        character.getFixture(0).setRestitution(0.2);
        character.setAngularDamping(Double.POSITIVE_INFINITY);
        character.getFixture(0).setFriction(10);
        world.addBody(character);
        characterObject = new GameObject("", character, new Vector2(0, 0), 0.2);

        gameObjects.add(characterObject);


//        character.addFixture(Geometry.createRectangle(0.5, 1));
//        character.setMass(MassType.NORMAL);
//        character.getTransform().setTranslation(-7, 1);
//        character.getFixture(0).setRestitution(0.2);
//        character.setAngularDamping(Double.POSITIVE_INFINITY);
//        character.getFixture(0).setFriction(10);
//        world.addBody(character);
//        characterObject = new GameObject("", character, new Vector2(0, 0), 0.2);
//
//        gameObjects.add(characterObject);
    }
    public void lvl1() {
        double bodyWidth = 1;
        double bodyHeight = 1;
        double spacing = -0.001;

        // level 1
        for (int y = 0; y < 1; y++) {
            for (int x = 0; x < 12; x++) {
                Body boundryBlock = new Body();
                boundryBlock.addFixture(Geometry.createRectangle(bodyWidth, bodyHeight / 2));
                boundryBlock.setMass(MassType.INFINITE);

                if (x == 1 )
                    continue;

                double xPos = -7 + x * (bodyWidth + spacing) + (y * 2 * (bodyWidth + spacing));
                double yPos = -2 + y * (bodyHeight + spacing);
                boundryBlock.getTransform().setTranslation(xPos, yPos);
                world.addBody(boundryBlock);
                gameObjects.add(new GameObject("wall.jpg", boundryBlock, new Vector2(0, 0), 0.13, 2));
            }
        }

        //land platform
        Body addedBlock = new Body();
        addedBlock.addFixture(Geometry.createRectangle(bodyWidth, bodyHeight / 2));
        addedBlock.setMass(MassType.INFINITE);
        addedBlock.getTransform().setTranslation(-9  * (bodyWidth + spacing) + ( 2 * (bodyWidth + spacing)), 0  * (bodyHeight + spacing));
        world.addBody(addedBlock);
        gameObjects.add(new GameObject("wall.jpg", addedBlock, new Vector2(0, 0), 0.13, 2));

        //anti bounce platform
        Body antiBouncePlatform = new Body();
        antiBouncePlatform.addFixture(Geometry.createRectangle(0.1, bodyHeight));
        antiBouncePlatform.setMass(MassType.INFINITE);
        world.addBody(antiBouncePlatform);
        antiBouncePlatform.getTransform().setTranslation(-9.4  * (bodyWidth + spacing) + ( 2 * (bodyWidth + spacing)), 1  * (bodyHeight + spacing));
        gameObjects.add(new GameObject("", antiBouncePlatform, new Vector2(0, 0), 0.13));

        //end guards for finish
        Body endGuard = new Body();
        endGuard.addFixture(Geometry.createRectangle(0.1, bodyHeight * 2));
        endGuard.setMass(MassType.INFINITE);
        world.addBody(endGuard);
        endGuard.getFixture(0).setRestitution(0);
        endGuard.getFixture(0).setFriction(Float.MAX_VALUE);
        endGuard.getTransform().setTranslation(-0.5 * (bodyWidth + spacing) + ( 2 * (bodyWidth + spacing)), 7 * (bodyHeight + spacing));
        gameObjects.add(new GameObject("", endGuard, new Vector2(0,0), 0,13));

        //end platform
        Body endplatform = new Body();
        endplatform.addFixture(Geometry.createRectangle(bodyWidth, bodyHeight / 2));
        endplatform.setMass(MassType.INFINITE);
        endplatform.getFixture(0).setFriction(Float.MAX_VALUE);
        world.addBody(endplatform);
        endplatform.getTransform().setTranslation(-1  * (bodyWidth + spacing) + ( 2 * (bodyWidth + spacing)), 3  * (bodyHeight + spacing));
        gameObjects.add(new GameObject("wall.jpg", endplatform, new Vector2(0, 0), 0.13, 2));

        for (int y = 0; y < 1; y++) {
            for (int x = 0; x < 9; x++) {
                Body boundryBlock = new Body();
                boundryBlock.addFixture(Geometry.createRectangle(bodyWidth, bodyHeight / 2));
                boundryBlock.setMass(MassType.INFINITE);

                double xPos = -3 + x * (bodyWidth + spacing) + (y * 2 * (bodyWidth + spacing));
                double yPos = 1 + y * (bodyHeight + spacing);

                boundryBlock.getTransform().setTranslation(xPos, yPos);
                boundryBlock.getFixture(0).setFriction(0);
                world.addBody(boundryBlock);

                gameObjects.add(new GameObject("wall.jpg", boundryBlock, new Vector2(0, 0), 0.13, 2));
            }
        }
    }
}
