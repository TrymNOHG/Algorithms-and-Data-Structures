import java.util.*;

public class MapData {
    public static void main(String[] args) {
    }
}

/**
 * <p>
 * Dijkstra's algorithm is used in order to construct the shortest path from one node to another node. This may be done
 * by creating a minimum spanning tree.
 * </p>
 *
 * <p>
 * The algorithm starts by taking in a positively weighted graph of nodes. Between the various nodes, there are edges. A person decides
 * the root node for the algorithm and may specify a desired end-node.
 * </p>
 *
 * <p>
 * The algorithm sets the root node to have a distance of 0 and all other nodes to have a distance of infinity (in
 * programming this can be done with an arbitrarily large number). It, then, creates a min heap containing all the
 * nodes and using the distance as the priority value.
 * </p>
 */
class Dijkstra {

    /**
     * A set containing all the nodes in a weighted graph.
     */
    Node[] graph;

    public Dijkstra(Node[] graph) {
        this.graph = graph;
    }

    public void dijkstra(Node root){
        initDist(root);
        MinHeap minHeap = new MinHeap(graph);
        while(minHeap.len > 0){
            Node currentMinNode = minHeap.poll();
            for (WeightedEdge e = (WeightedEdge)currentMinNode.edge; e.next != null; e = (WeightedEdge) e.to){
                findMinDistance(e.next, e);
            }
        }
    }

    public void initDist(Node root){
        for (Node node: graph) {
            node.dist = 1000000000;
            node.previous = null;
        }
        root.dist = 0;
    }

    public void findMinDistance(Node node, WeightedEdge edge){
        int nodeDist = node.dist;
        int prevNodeDist = edge.next.dist;
        if(nodeDist > prevNodeDist + edge.weight){
            node.previous = edge.next;
            node.dist = prevNodeDist + edge.weight;
        }
    }


}

//You could stop the algorithm once the path you desire has been found, instead of creating a whole tree.

class MinHeap {
    int len = 0;
    int capacity = 10;
    Node[] nodes;

    public MinHeap() {
    }

    public MinHeap(Node[] nodes) {
        this.nodes = nodes;
        createHeap();
    }

    public void createHeap(){
        int length = len/2;
        while (length-- > 0) heapifyDown(length);
    }

    public void updateCapacity(){
        if(len == capacity) capacity <<= 1;
    }

    public void add(Node node){
        updateCapacity();
        nodes[len++] = node;
        heapifyUp(len);
    }

    public void heapifyUp(int pos){
        int parentIndex = parent(pos);
        if(nodes[parentIndex] != null){
            if(nodes[parent(pos)].dist > nodes[pos].dist){
                swap(pos, parentIndex);
                heapifyUp(parentIndex);
            }
        }
    }

    public void heapifyDown(int pos){
        int leftChildIndex = leftChild(pos);
        if(nodes[leftChildIndex] != null){

            int rightChildIndex = leftChildIndex + 1;

            if(nodes[rightChildIndex] != null){
                leftChildIndex = nodes[leftChildIndex].dist > nodes[rightChildIndex].dist
                        ? leftChildIndex : rightChildIndex;
            }

            if(nodes[pos].dist > nodes[leftChildIndex].dist){
                swap(pos, leftChildIndex);
                heapifyDown(leftChildIndex);
            }
        }
    }

    public Node peek(){
        if(len == 0) throw new NoSuchElementException("There is no root node");
        return nodes[0];
    }

    public Node poll(){
        if(len == 0) throw new NoSuchElementException("There is no root node");
        Node root = nodes[0];
        nodes[0] = nodes[len--];
        heapifyDown(0);
        return root;
    }

    public int parent(int position){
        return position >> 1;
    }

    public int rightChild(int position){
        return (1 + position) << 1;
    }

    public int leftChild(int position){
        return 1 + position << 1;
    }

    public boolean hasParent(int index){
        return nodes[parent(index)] != null;
    }

    public boolean hasLeft(int index){
        return nodes[leftChild(index)] != null;
    }

    public boolean hasRight(int index){
        return nodes[rightChild(index)] != null;
    }

    private void swap(int firstNode, int secondNode){
        Node temp = nodes[firstNode];
        nodes[firstNode] = nodes[secondNode];
        nodes[secondNode] = temp;
    }

}

class WeightedEdge extends Edge{
    int weight;

    public WeightedEdge(Edge to, Node next, int weight) {
        super(to, next);
        this.weight = weight;
    }

}

class Edge {
    Edge to;
    Node next;

    public Edge(Edge to, Node next) {
        this.to = to;
        this.next = next;
    }
}

class Node {
    Object element;
    Edge edge;

    int dist = 1000000000;

    /**
     * This variable links together all the nodes from the given node to the start node.
     */
    Node previous;

    public Node(Object element, Edge edge) {
        this.element = element;
        this.edge = edge;
    }
}



