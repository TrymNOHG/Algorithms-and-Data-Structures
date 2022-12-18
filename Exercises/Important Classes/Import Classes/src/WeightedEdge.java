/**
 * This class represents an edge in a graph with a weight attached to it. The edge also contains the next edge connected
 * to the same node, if it exists. Therefore, an edge object will also act as a linked list between the different
 * edges attached to the same vertex.
 *
 * @author Trym Hamer Gudvangen
 */
public class WeightedEdge extends Edge{
    private int weight;

    /**
     * This constructor represents an edge between two vertices with a given weight.
     * @param next      The next edge connected to the same start node.
     * @param to        The vertex connected to {@code this} edge.
     * @param weight    The weight of edge, given as an int.
     */
    public WeightedEdge(Edge next, Vertex to, int weight) {
        super(next, to);
        this.weight = weight;
    }

    /**
     * This method retrieves the weight of an edge.
     * @return Weight, given as an int.
     */
    public int getWeight(){
        return weight;
    }
}
