// Adam Rilatt
// 05 / 17 / 20
// Mesh Class -- 3D Raster Engine

/*
This class implements a Mesh object,
which holds a list of Triangles to be drawn by
the Engine. Meshes represent "objects" in the Engine world.
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Mesh {

    private ArrayList<Triangle> triangles;
    private String mapFilePath;

    /** Default constructor. */
    public Mesh() {

        triangles = new ArrayList<Triangle>();

    }

    /** Constructor. Attempts to read .obj data from a filepath. */
    public boolean loadFromFile(String filePath) {

        triangles = new ArrayList<Triangle>();
        mapFilePath = filePath;

        // While reading the .obj file, the Mesh will build
        // a list of Vertices. The file will then list indexes
        // into that array for the Mesh to build Triangles from.
        ArrayList<Vector> vectors = new ArrayList<Vector>();

        try {

            Scanner reader = new Scanner(new File(mapFilePath));
            String currentLine;
            String[] strNums;

            while (reader.hasNext()) {

                currentLine = reader.nextLine();
                strNums = currentLine.split(" ");

                // in .obj, 'v' indicates that the rest of the line is a vertex
                if (strNums[0].equals("v")) {

                    vectors.add(new Vector(
                            Double.parseDouble(strNums[1]),
                            Double.parseDouble(strNums[2]),
                            Double.parseDouble(strNums[3])
                    ));

                }
                // 'f' indicates a face, comprised of vertex data listed in the
                // file and referenced by index
                else if (strNums[0].equals("f")) {

                    triangles.add(new Triangle(
                            vectors.get(Integer.parseInt(strNums[1]) - 1),
                            vectors.get(Integer.parseInt(strNums[2]) - 1),
                            vectors.get(Integer.parseInt(strNums[3]) - 1)
                    ));

                }
                else {

                    continue;

                }

            }


        } catch (FileNotFoundException e) {

            e.printStackTrace();
            return false;

        }

        // .obj file successfully loaded
        return true;

    }

    /** Returns a list of all triangles in the Mesh. */
    public ArrayList<Triangle> tris() {

        return triangles;

    }

}
