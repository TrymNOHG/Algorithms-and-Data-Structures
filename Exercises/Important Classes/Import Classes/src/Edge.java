/**
 * This class represents an edge between two vertices on a graph. The edge also contains the next edge connected
 * to the same node, if it exists. Therefore, an edge object will also act as a linked list between the different
 * edges attached to the same vertex.
 *
 * @author Trym Hamer Gudvangen
 */
public class Edge {
    private Edge next;
    private Vertex to;

    /**
     * This constructor creates an edge using the next edge and the vertex attached to the end of this edge as parameters.
     * @param next  The next edge connected to the same start node.
     * @param to    The vertex connected to {@code this} edge.
     */
    public Edge(Edge next, Vertex to) {
        this.next = next;
        this.to = to;
    }

    /**
     * This method returns the next edge connected to the same start node.
     * @return  Next Edge object.
     */
    public Edge next(){
        return next;
    }

    /**
     * This method returns the vertex attached to the current edge.
     * @return  Vertex attached to the current edge.
     */
    public Vertex to(){
        return to;
    }
}
