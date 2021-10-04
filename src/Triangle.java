// Adam Rilatt
// 05 / 17 / 20
// Triangle Class -- 3D Raster Engine

/*
This class implements a Triangle object,
which holds three Vectors. Triangles are the simplest
primitive shape and will be used to draw everything
in this engine. Vectors should be listed in clockwise
order so that the Engine can calculate their normals
consistently.
 */

import java.awt.Color;

public class Triangle implements Comparable<Triangle> {

    private Vector[] vectors;
    private Color color;

    /** Constructor. Note that the three vertices should be listed
     *  in clockwise order. */
    public Triangle(Vector a, Vector b, Vector c, Color col) {

        // note that the winding order of vertices should be clockwise.
        vectors = new Vector[3];
        vectors[0] = a;
        vectors[1] = b;
        vectors[2] = c;
        color = col;
    }

    /** Default constructor. Currently used when reading from files. */
    public Triangle(Vector a, Vector b, Vector c) {

        this(a, b, c, Color.WHITE);

    }

    /** Returns an array containing the three vertices of the triangle. */
    public Vector[] vects() {

        return vectors;

    }

    /** Returns the color that the triangle should be drawn with. */
    public Color getColor() {

        return color;

    }

    /** Returns the average Z component of the Triangle's three vertices. */
    public double averageZ() {

        return (vectors[0].z + vectors[1].z + vectors[2].z) / 3.0;

    }

    @Override
    /** toString. Returns the vector data for each vertex of the triangle. */
    public String toString() {

        String s = "";

        s += "New triangle:";
        s += String.format("(%f, %f, %f), (%f, %f, %f), (%f, %f, %f)\n",
                vects()[0].x, vects()[0].y, vects()[0].z,
                vects()[1].x, vects()[1].y, vects()[1].z,
                vects()[2].x, vects()[2].y, vects()[2].z);

        return s;

    }

    @Override
    /** Implements Comparable. Used by sorting algorithms to sort
     *  triangles based on the average Z component of their vertices. */
    public int compareTo(Triangle t) {

        double myZ = this.averageZ();
        double newZ = t.averageZ();

        if (myZ > newZ)
            return 1;
        else if (myZ == newZ)
            return 0;
        else
            return -1;

    }

}
