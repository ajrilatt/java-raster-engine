// Adam Rilatt
// 05 / 23 / 20
// Camera Class -- 3D Engine

/*
This class implements the Camera object, which
acts as the "player" and provides information to
the Screen to determine what should be rendered.
 */

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Camera implements KeyListener {

    public Vector position, lookDirection;
    double yaw;         // y-axis rotation

    Vector vUp = new Vector(0, 1, 0);
    boolean left, right, forward, back, up, down;
    double gameSpeed;


    /** Constructor. Creates the camera at the specified vector position and directs the camera view towards look. */
    public Camera(Vector pos, Vector look, double gS) {

        gameSpeed = gS;
        position = new Vector(pos);
        lookDirection = new Vector(look);

    }

    /** Default constructor. Assumes 60fps game speed. */
    public Camera() {

        this(new Vector(0, 0, 0), new Vector(0, 0, 1), 60.0);

    }



    public void keyPressed(KeyEvent key) {
        // Implements the KeyListener class, and is triggered on each key press.

        if (key.getKeyCode() == KeyEvent.VK_A)
            left = true;

        if (key.getKeyCode() == KeyEvent.VK_D)
            right = true;

        if (key.getKeyCode() == KeyEvent.VK_W)
            forward = true;

        if (key.getKeyCode() == KeyEvent.VK_S)
            back = true;

        if (key.getKeyCode() == KeyEvent.VK_SPACE)
            up = true;

        if (key.getKeyCode() == KeyEvent.VK_SHIFT)
            down = true;

        // TODO: remove debug
        if (key.getKeyCode() == KeyEvent.VK_G)
            System.out.println("position = " + position.toString() + ", looking at " + lookDirection.toString());

    }

    public void keyReleased(KeyEvent key) {
        // Implements the KeyListener class, and is triggered on each key released.

        if (key.getKeyCode() == KeyEvent.VK_A)
            left = false;

        if (key.getKeyCode() == KeyEvent.VK_D)
            right = false;

        if (key.getKeyCode() == KeyEvent.VK_W)
            forward = false;

        if (key.getKeyCode() == KeyEvent.VK_S)
            back = false;

        if (key.getKeyCode() == KeyEvent.VK_SPACE)
            up = false;

        if (key.getKeyCode() == KeyEvent.VK_SHIFT)
            down = false;

    }

    /** Based on current keyboard state, move the Camera. **/
    public void update() {

        if (up)
            position = Vector.add(position, Vector.multiply(vUp, 1.0));

        if (down)
            position = Vector.subtract(position, Vector.multiply(vUp, 1.0));

        if (forward)
            position = Vector.subtract(position, Vector.multiply(lookDirection, 1.0));

        if (back)
            position = Vector.add(position, Vector.multiply(lookDirection, 1.0));

        if (left)
            yaw -= 1.0 / gameSpeed;

        if (right)
            yaw += 1.0 / gameSpeed;

    }

    /** Sets the position of the camera to the coordinates of the specified Vector. */
    public void setPosition(Vector newPos) {

        position.x = newPos.x;
        position.y = newPos.y;
        position.z = newPos.z;

    }

    /** Sets the look direction of the camera to the direction of the specified unit vector. */
    public void setLookDirection(Vector look) {

        lookDirection.x = look.x;
        lookDirection.y = look.y;
        lookDirection.z = look.z;

    }

    // required to implement KeyListener
    public void keyTyped(KeyEvent e) {}
}
