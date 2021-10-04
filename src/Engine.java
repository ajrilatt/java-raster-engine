// Adam Rilatt
// 05 / 17 / 20
// Main Engine Class -- 3D Engine

/*
This program follows a javidx9 tutorial in order to implement a
simple 3-dimensional rasterization engine.
 */

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.Timer;

public class Engine extends JFrame implements Runnable, ActionListener {

    // settings variables
    private final double GAME_SPEED = 60.0;
    private final int SCREEN_WIDTH  = 800;
    private final int SCREEN_HEIGHT = 600;
    private final double FOV = 90.0;
    private final double FAR_PLANE  = 10000.0;
    private final double NEAR_PLANE = 0.1;

    // normal initializer variables -- do not touch, user!
    private final double ASPECT_RATIO = (double)SCREEN_HEIGHT / (double)SCREEN_WIDTH;
    private final Timer timer = new Timer((int)((1 / GAME_SPEED) * 1000), this);
    private Screen screen;
    private Camera cam;
    private ArrayList<Mesh> sceneObjects;

    /** Constructor. Initializes everything needed for engine operation. */
    public Engine() {

        // init JFrame
        super();

        // init scene data
        sceneObjects = new ArrayList<Mesh>();

        Mesh defaultCube = new Mesh();
        defaultCube.loadFromFile("teapot.obj");
        sceneObjects.add(defaultCube);

        // camera setup
        cam = new Camera(new Vector(0, 0, 0),
                         new Vector(0, 0, 1),
                         GAME_SPEED);
        addKeyListener(cam);

        // JFrame setup
        Container pane = getContentPane();
        screen = new Screen(SCREEN_WIDTH, SCREEN_HEIGHT, Color.BLACK,
                            ASPECT_RATIO, FOV, FAR_PLANE, NEAR_PLANE,
                            GAME_SPEED, cam);
        pane.add(screen);
        pack();

        setResizable(false);
        setTitle("3D Game Engine");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBackground(Color.BLACK);
        setLocationRelativeTo(null);
        setVisible(true);

        timer.start();

        requestFocus();

    }

    /** Triggers once every 1 / GAME_SPEED seconds. Refreshes the screen. */
    @Override
    public void actionPerformed(ActionEvent e) {

        // update camera position
        cam.update();

        // update screen
        screen.addTris(sceneObjects.get(0).tris());
        repaint();

    }

    /** Creates a new Engine which will automatically run itself. */
    public static void main(String[] args) {

        Engine gameSession = new Engine();

    }

    // required to implement Runnable
    public void run() {}

}
