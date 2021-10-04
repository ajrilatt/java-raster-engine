// Adam Rilatt
// 05 / 17 / 20
// Vector Class -- 3D Engine

/*
This class implements a Vector object,
which holds an X, Y, and Z coordinate in 3D space.
 */

public class Vector {

    public double x, y, z, w;

    /** Adds Vector 2 to Vector 1 and returns the resulting vector. */
    public static Vector add(Vector v1, Vector v2) {

        return new Vector(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z);

    }

    /** Subtracts Vector 2 from Vector 1 and returns the resulting vector. */
    public static Vector subtract(Vector v1, Vector v2) {

        return new Vector(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);

    }

    /** Returns the dot product of the two input Vectors.
     *  Always normalize the Vectors before performing this action. */
    public static double dot(Vector v1, Vector v2) {

        return v1.x * v2.x + v1.y * v2.y + v1.z * v2.z;

    }

    /** Multiplies a vector by a multiplicand and returns the resulting vector. */
    public static Vector multiply(Vector v, double m) {

        return new Vector(v.x * m, v.y * m, v.z * m);

    }

    /** Divides a vector by a divisor and returns the resulting vector. */
    public static Vector divide(Vector v, double div) {

        Vector quotient = new Vector(v);
        if (div != 0) {

            quotient.x /= div;
            quotient.y /= div;
            quotient.z /= div;

        }

        return quotient;

    }

    /** Returns the cross product of the two input Vectors. */
    public static Vector cross(Vector vector1, Vector vector2) {

        return new Vector(vector1.y * vector2.z - vector1.z * vector2.y,
                vector1.z * vector2.x - vector1.x * vector2.z,
                vector1.x * vector2.y - vector1.y * vector2.x);

    }

    /** Constructor. Copies the Vector's values and returns a new Vector. */
    public Vector(Vector v) {

        this(v.x, v.y, v.z, v.w);


    }

    /** Default constructor. Initializes the vector to (0, 0, 0). */
    public Vector() {

        this(0.0, 0.0, 0.0, 1.0);

    }

    public Vector(double initialX, double initialY, double initialZ) {

        this(initialX, initialY, initialZ, 1);

    }

    /** Constructor. Initializes the Vector to X, Y, Z. */
    public Vector(double initialX, double initialY, double initialZ, double initialW) {

        x = initialX; y = initialY; z = initialZ; w = initialW;

    }

    /** Returns the length of the Vector. */
    public double length() {

        return Math.sqrt(Vector.dot(this, this));

    }

    /** Normalizes this Vector to the range [-1, 1]. */
    public void normalize() {

        double l = this.length();
        if (l != 0.0) {

            x /= l;
            y /= l;
            z /= l;

        }

    }

    @Override
    /** toString method. Returns the X, Y, and Z coordinates of the Vector. */
    public String toString() {

        String s = String.format("(%f, %f, %f)", x, y, z);
        return s;

    }

}
