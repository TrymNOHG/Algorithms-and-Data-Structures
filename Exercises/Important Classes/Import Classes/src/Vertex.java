/**
 * This class represents a vertex on a graph. It, therefore, has information about the attached edges, its id value,
 * and node data.
 *
 * @author Trym Hamer Gudvangen
 */
public class Vertex {
    private Edge edge;
    private int id;
    private Object nodeData;

    /**
     * This constructor creates a vertex object using the edges attached and an id.
     * @param edge  Edges attached, given as an Edge object.
     * @param id    The identification value for the node, given as an int.
     */
    public Vertex(Edge edge, int id) {
        this.edge = edge;
        this.id = id;
    }

    /**
     * This method retrieves the edge of the vertex.
     * @return  Edge of the vertex, given as an Edge object.
     */
    public Edge edge() {
        return edge;
    }

    /**
     * This method retrieves the id of the vertex.
     * @return  The id of the vertex, represented using an int.
     */
    public int id() {
        return id;
    }

    /**
     * This method retrieves the data relevant to the node.
     * @return  Node data, given as an Object object.
     */
    public Object nodeData() {
        return nodeData;
    }
}
