// Adam Rilatt
// 05 / 17 / 20
// Screen Class -- 3D Engine

/*
This class extends the standard JPanel in order
to draw to the screen. The Engine refreshes this at
a constant rate, sending raw triangle data to be processed by
the Screen and scaled into 2D screen space.
 */

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.CopyOnWriteArrayList;

public class Screen extends JPanel {

    // because the Engine object tends to update the
    // list of triangles as the Screen attempts to draw them,
    // a thread-safe version of ArrayList is used. While slower,
    // it is synchronized and will not throw a ConcurrentModificationException.
    private CopyOnWriteArrayList<Triangle> trisToDraw;
    private ArrayList<Triangle> projectedTris;
    private ArrayList<Triangle> tris3D;
    private Matrix matProj;
    private Matrix rotZ;
    private Matrix rotX;
    private Camera camera;
    private Vector lightDirection;
    private int width, height;
    private double time;
    private double gameSpeed;

    /** Default constructor. */
    public Screen(int w, int h, Color c, double aspectRatio, double fov,
                  double farPlane, double nearPlane, double gS, Camera cam) {

        setPreferredSize(new Dimension(w, h));
        width = w;
        height = h;

        trisToDraw = new CopyOnWriteArrayList<Triangle>();
        projectedTris = new ArrayList<Triangle>();
        setBackground(c);

        time = 0;
        gameSpeed = gS;

        // the camera instance variable always references the Camera object created
        // by the Engine
        camera = cam;

        // create simple light source for the scene. -1 is used for the direction because
        // tris should be illuminated most when their surface normal aligns with the lighting direction;
        // thus, (0, 0, -1) represents a light shining towards +Z.
        lightDirection = new Vector(1, -1, -1);
        lightDirection.normalize();

        // initialize the projection matrix used to convert 3D points to 2D coordinates
        matProj = Matrix.makeProjection(fov, aspectRatio, nearPlane, farPlane);

    }


    /** Add a list of triangles to the screen draw queue. */
    public void addTris(ArrayList<Triangle> t) {

        trisToDraw.clear();
        trisToDraw.addAll(t);

    }

    /** Each update of paintComponent will draw to the screen. */
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        time += (1 / gameSpeed);

        // clear the previous frame
        projectedTris.clear();
        g.setColor(new Color(25, 25, 25));
        g.fillRect(0, 0, width, height);

        // all rotations and translations in 3D space
        // are combined into one world matrix
        Matrix rotZ = Matrix.makeRotationZ(time);
        Matrix rotX = Matrix.makeRotationX(time * 0.5);
        Matrix trans = Matrix.makeTranslation(0,0, 6.0);
        Matrix world = Matrix.multiply(rotX, rotZ); // order is important! rotate, then translate
        world = Matrix.multiply(world, trans);

        // TODO: remove if camera fails
        // define the camera matrix based on current camera position

        Vector up     = new Vector(0, 1, 0);
        Vector target = Vector.add(camera.position, camera.lookDirection);
        Matrix camRot = Matrix.makeRotationY(camera.yaw);
        Vector newCamDir = Matrix.multiplyVector(camRot, target);
        newCamDir.normalize();
        camera.setLookDirection(newCamDir);
        Matrix cam = Matrix.makePointAt(camera.position, target, up);

        for (Triangle t : trisToDraw) {

            Vector vA = new Vector(t.vects()[0]);
            Vector vB = new Vector(t.vects()[1]);
            Vector vC = new Vector(t.vects()[2]);

            // translate and rotate the vectors by the world matrix
            vA = Matrix.multiplyVector(world, vA);
            vB = Matrix.multiplyVector(world, vB);
            vC = Matrix.multiplyVector(world, vC);

            // perform cross product of the two lines to obtain the triangle's surface normal,
            // then normalize it into [-1, 1]
            Vector normalLine1 = Vector.subtract(vB, vA);
            Vector normalLine2 = Vector.subtract(vC, vA);
            Vector normal = Vector.cross(normalLine1, normalLine2);
            normal.normalize();

            // only draw the triangle if its surface normal faces toward the camera
            Vector cameraToTri = Vector.subtract(vA, camera.position);
            if (Vector.dot(cameraToTri, normal) < 0.0) {

                // Illuminate the triangle with a customizable function
                double lightDotTri = Math.max(0.0, Vector.dot(lightDirection, normal));
                Color lightColor = lightScale(t.getColor(), lightDotTri, 2.0, 1, 0.0);

                // TODO: Remove if camera fails
                // run the tri through the camera matrix to apply the 'camera view' effect
                vA = Matrix.multiplyVector(cam, vA);
                vB = Matrix.multiplyVector(cam, vB);
                vC = Matrix.multiplyVector(cam, vC);

                // run the current tri through the projection matrix to
                // convert from 3D to 2D coordinates
                vA = Matrix.multiplyVector(matProj, vA);
                vB = Matrix.multiplyVector(matProj, vB);
                vC = Matrix.multiplyVector(matProj, vC);
                vA = Vector.divide(vA, vA.w);
                vB = Vector.divide(vB, vB.w);
                vC = Vector.divide(vC, vC.w);

                // normalize the vectors into screen space [-1, +1],[-1, +1],
                // and round them into integer coordinates representing pixels
                vA.x = (int)((vA.x + 1) * 0.5 * width);
                vA.y = (int)((vA.y + 1) * 0.5 * height);
                vB.x = (int)((vB.x + 1) * 0.5 * width);
                vB.y = (int)((vB.y + 1) * 0.5 * height);
                vC.x = (int)((vC.x + 1) * 0.5 * width);
                vC.y = (int)((vC.y + 1) * 0.5 * height);

                Triangle projected = new Triangle(vA, vB, vC, lightColor);

                projectedTris.add(projected);

            }

        }

        // triangles have been projected to screen space, but must be ordered
        // from back-to-front for a proper render. This is just an approximation
        // and may be replaced with a proper ordering method later.
        Collections.sort(projectedTris);
        Collections.reverse(projectedTris);

        // draw the projected and sorted triangles
        for (Triangle t : projectedTris) {

            drawTriangle(t, g, 'f');

        }

        g.dispose();

    }

    /** Draws a triangle to the screen in one of several modes.
     * @param mode  'w' to draw a wireframe, 'f' to fill with color.
     */
    private void drawTriangle(Triangle t, Graphics g, char mode) {

        // all vectors should have been adjusted to 2D screen space,
        // so only the X and Y coordinates will be used
        g.setColor(t.getColor());
        Vector[] v = t.vects();

        // wireframe
        if (mode == 'w')
            g.drawPolygon(new int[] {(int)v[0].x, (int)v[1].x, (int)v[2].x},
                new int[] {(int)v[0].y, (int)v[1].y, (int)v[2].y},
                3);

        // fill
        else if (mode == 'f') {

            g.fillPolygon(new int[] {(int)v[0].x, (int)v[1].x, (int)v[2].x},
                    new int[] {(int)v[0].y, (int)v[1].y, (int)v[2].y},
                    3);
        }

    }

    /** Modifies how a triangle's color reacts to light. */
    public Color lightScale(Color shade, double dot, double curve, double whitepoint, double blackpoint) {

        // https://www.desmos.com/calculator/sfxc1zt1y0

        int newR = (int)(Math.max(0.0, Math.min(255.0, Math.pow(dot, curve) * shade.getRed()   * whitepoint + blackpoint)));
        int newG = (int)(Math.max(0.0, Math.min(255.0, Math.pow(dot, curve) * shade.getGreen() * whitepoint + blackpoint)));
        int newB = (int)(Math.max(0.0, Math.min(255.0, Math.pow(dot, curve) * shade.getBlue()  * whitepoint + blackpoint)));

        return new Color(newR, newG, newB);

    }

}
