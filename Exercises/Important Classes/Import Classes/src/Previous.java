/**
 * The Previous class represents a linked list of vertices attached. It, therefore, also contains a distance
 * which can be used to find the distance between two nodes.
 *
 * @author Trym Hamer Gudvangen
 */
public class Previous {
    private int distance;
    private Vertex previous;
    static int infinity = Integer.MAX_VALUE;

    /**
     * The constructor defaults the distance to infinity.
     */
    public Previous(){
        distance = infinity;
    }

    /**
     * This method retrieves the distance of the previous object.
     * @return Distance between two nodes, given as an int.
     */
    public int distance(){
        return distance;
    }

    /**
     * This method retrieves the previous node.
     * @return Previous Vertex.
     */
    public Vertex previous(){
        return previous;
    }

}


