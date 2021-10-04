// Adam Rilatt
// 05 / 17 / 20
// Matrix Class -- 3D Engine

/*
This class implements the Matrix object,
which will be used to manipulate Vectors.
 */

public class Matrix {

    public double m[][];
    private int columns, rows;

    /** Constructor. */
    public Matrix(int nCols, int nRows) {

        m = new double[nCols][nRows];
        columns = nCols;
        rows    = nRows;

    }

    /** Default constructor. Initializes a 4x4 matrix. */
    public Matrix() {

        this(4, 4);

    }

    /** Multiplies a vector by a matrix and returns the resulting vector. */
    public static Vector multiplyVector(Matrix ma, Vector v) {

        Vector out = new Vector();

        out.x = v.x * ma.m[0][0] + v.y * ma.m[1][0] + v.z * ma.m[2][0] + v.w * ma.m[3][0];
        out.y = v.x * ma.m[0][1] + v.y * ma.m[1][1] + v.z * ma.m[2][1] + v.w * ma.m[3][1];
        out.z = v.x * ma.m[0][2] + v.y * ma.m[1][2] + v.z * ma.m[2][2] + v.w * ma.m[3][2];
        out.w = v.x * ma.m[0][3] + v.y * ma.m[1][3] + v.z * ma.m[2][3] + v.w * ma.m[3][3];

        return out;

    }

    /** Constructs a standard identity matrix which will only extract multiplied values. */
    public static Matrix makeIdentity() {

        Matrix ma = new Matrix();
        ma.m[0][0] = 1.0;
        ma.m[1][1] = 1.0;
        ma.m[2][2] = 1.0;
        ma.m[3][3] = 1.0;
        return ma;

    }

    /** Constructs a rotation matrix for the X axis based on a radian angle.
     *  This should be biased in a different way than other rotation matrices
     *  to avoid gimbal lock. */
    public static Matrix makeRotationX(double angleRad) {

        Matrix rotX = new Matrix();
        rotX.m[0][0] = 1.0;
        rotX.m[1][1] = Math.cos(angleRad);
        rotX.m[1][2] = Math.sin(angleRad);
        rotX.m[2][1] = -Math.sin(angleRad);
        rotX.m[2][2] = Math.cos(angleRad);
        rotX.m[3][3] = 1.0;
        return rotX;

    }

    /** Constructs a rotation matrix for the Y axis based on a radian angle.
     *  This should be biased in a different way than other rotation matrices
     *  to avoid gimbal lock. */
    public static Matrix makeRotationY(double angleRad) {

        Matrix rotY = new Matrix();
        rotY.m[0][0] = Math.cos(angleRad);
        rotY.m[0][2] = Math.sin(angleRad);
        rotY.m[1][1] = 1.0;
        rotY.m[2][0] = -Math.sin(angleRad);
        rotY.m[2][2] = Math.cos(angleRad);
        rotY.m[3][3] = 1.0;

        return rotY;

    }

    /** Constructs a rotation matrix for the Z axis based on a radian angle.
     *  This should be biased in a different way than other rotation matrices
     *  to avoid gimbal lock. */
    public static Matrix makeRotationZ(double angleRad) {

        Matrix rotZ = new Matrix();
        rotZ.m[0][0] = Math.cos(angleRad);
        rotZ.m[0][1] = Math.sin(angleRad);
        rotZ.m[1][0] = -Math.sin(angleRad);
        rotZ.m[1][1] = Math.cos(angleRad);
        rotZ.m[2][2] = 1.0;
        rotZ.m[3][3] = 1.0;
        return rotZ;

    }

    /** Constructs a translation matrix that will move Vectors by x, y, and z in their respective directions. */
    public static Matrix makeTranslation(double x, double y, double z) {

        Matrix trans = Matrix.makeIdentity();
        trans.m[3][0] = x;
        trans.m[3][1] = y;
        trans.m[3][2] = z;
        return trans;

    }

    /** Constructs a standard projection matrix based on field-of-view, screen aspect ratio, and Z-depth. */
    public static Matrix makeProjection(double fov, double aspectRatio, double nearPlane, double farPlane) {

        double invFOV = 1 / Math.tan((fov * 0.5) / 180.0 * Math.PI);
        Matrix matProj = new Matrix(4, 4);
        matProj.m[0][0] = aspectRatio * invFOV;
        matProj.m[1][1] = invFOV;
        matProj.m[2][2] = farPlane / (farPlane - nearPlane);
        matProj.m[2][3] = 1.0;
        matProj.m[3][2] = (-farPlane * nearPlane) / (farPlane - nearPlane);
        matProj.m[3][3] = 0.0;
        return matProj;

    }

    /** Helper method for makePointAt and makeLookAt methods. */
    private static Vector[] pointCoordsHelper(Vector position, Vector target, Vector up) {

        // calculate new forward vector
        Vector newForward = Vector.subtract(target, position);
        newForward.normalize();

        // calculate new up vector
        Vector a = Vector.multiply(newForward, Vector.dot(up, newForward));
        Vector newUp = Vector.subtract(up, a);
        newUp.normalize();

        // calculate new right vector
        Vector newRight = Vector.cross(newForward, newUp);

        Vector[] vs = {newForward, newUp, newRight};
        return vs;

    }

    /** Constructs a point-at matrix which transforms vectors multiplied by it to point at the target vector.
     *  This allows for world movement relative to the camera. */
    public static Matrix makePointAt(Vector position, Vector target, Vector up) {

        Vector[] vs = pointCoordsHelper(position, target, up);
        Vector newForward = vs[0], newUp = vs[1], newRight = vs[2];

        Matrix pointAt = new Matrix();
        pointAt.m[0][0] = newRight.x;
        pointAt.m[0][1] = newRight.y;
        pointAt.m[0][2] = newRight.z;
        pointAt.m[1][0] = newUp.x;
        pointAt.m[1][1] = newUp.y;
        pointAt.m[1][2] = newUp.z;
        pointAt.m[2][0] = newForward.x;
        pointAt.m[2][1] = newForward.y;
        pointAt.m[2][2] = newForward.z;
        pointAt.m[3][0] = position.x;
        pointAt.m[3][1] = position.y;
        pointAt.m[3][2] = position.z;
        pointAt.m[3][3] = 1.0;

        return pointAt;

    }

    /** Creates the inverse matrix of makePointAt, if needed. */
    public static Matrix makeLookAt(Vector position, Vector target, Vector up) {

        Vector[] vs = pointCoordsHelper(position, target, up);
        Vector newForward = vs[0], newUp = vs[1], newRight = vs[2];

        Matrix lookAt = new Matrix();
        lookAt.m[0][0] = newForward.x;
        lookAt.m[0][1] = newUp.x;
        lookAt.m[0][2] = newRight.x;
        lookAt.m[1][0] = newForward.y;
        lookAt.m[1][1] = newUp.y;
        lookAt.m[1][2] = newRight.y;
        lookAt.m[2][0] = newForward.z;
        lookAt.m[2][1] = newUp.z;
        lookAt.m[2][2] = newRight.z;
        lookAt.m[3][0] = -(Vector.dot(position, newForward));
        lookAt.m[3][1] = -(Vector.dot(position, newUp));
        lookAt.m[3][2] = -(Vector.dot(position, newRight));
        lookAt.m[3][3] = 1.0;

        return lookAt;

    }

    /** Multiplies two matrices together and returns their product. */
    public static Matrix multiply(Matrix m1, Matrix m2) {

        Matrix product = new Matrix();
        for (int i = 0; i < 4; i++)
            for (int k = 0; k < 4; k++)
                product.m[k][i] = m1.m[k][0] * m2.m[0][i] + m1.m[k][1] * m2.m[1][i] +
                                  m1.m[k][2] * m2.m[2][i] + m1.m[k][3] * m2.m[3][i];

        return product;

    }

}
