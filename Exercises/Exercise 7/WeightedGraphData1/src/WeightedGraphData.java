import java.io.*;
import java.util.*;

/**
 * @author Leon Egeberg Hesthaug, Eirik Elvestad, and Trym Hamer Gudvangen
 */
public class WeightedGraphData {

    public static void main(String[] args) throws IOException {
        Scanner input = new Scanner(System.in);
        System.out.println("What file do you want to test?");
        String file = input.nextLine();
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(
                        Objects.requireNonNull(WeightedGraphData.class.getResourceAsStream(file))));
        Graph graph = new Graph();
        graph.newGraph(reader);

        System.out.println("Information about the graph from " + file);

        Tree tree = new Tree(graph);

        tree.dijkstra(1);

        System.out.println(tree.getPathFromNode());

    }
}

class WeightedEdge extends Edge {
    int weight;

    public WeightedEdge(Vertex v, Edge nxt, int weight) {
        super(v, nxt);
        this.weight = weight;
    }
}

class Edge {
    Edge next;
    Vertex to;
    public Edge(Vertex v, Edge nxt){
        to = v;
        next = nxt;
    }
}

class Vertex {
    Edge edge;
    int id;
    Object nodeData;
    public Vertex(int id){
        this.id = id;
    }

}

class Tree extends Graph {

    int numVertex, numEdge;
    Vertex[] vertices;

    Heap heap;

    public Tree(Graph graph) {
        this.numVertex = graph.numVertex;
        this.numEdge = graph.numEdge;
        this.vertices = graph.vertices;
        heap = new Heap();
    }

    public void dijkstra(int id){
        initPrev(getVertexById(id));
        fillMinHeap();
        for(int i = numVertex; i > 1; i--){
            Vertex vertex = heap.getMinNode();
            for(WeightedEdge edge = (WeightedEdge)vertex.edge; edge != null; edge = (WeightedEdge) edge.next){
                optimizeDist(vertex, edge);
            }
            heap.minSort();
        }
    }

    public void fillMinHeap(){
        for(Vertex vertex : vertices){
            heap.add(vertex);
        }
    }

    public void optimizeDist(Vertex vertex, WeightedEdge edge){
        Previous node = (Previous)vertex.nodeData;
        Previous nextNode = (Previous)edge.to.nodeData;
        int newDist = node.distance + edge.weight;
        if(nextNode.distance > newDist){
            nextNode.distance = newDist;
            nextNode.previous = vertex;
        }
    }

    public Vertex getVertexById(int id){
        for(Vertex vertex : vertices){
            if(vertex.id == id) return vertex;
        }
        return null;
    }


    public String getPathFromNode(){
        StringBuilder sb =  new StringBuilder();
        for(int i = 0; i < this.vertices.length; i++){
            sb.append(getPathFromNode(i)).append("\n");
        }
        return sb.toString();
    }
    public String getPathFromNode(int id){
        Vertex startNode = getVertexById(id);
        StringBuilder sb = new StringBuilder();
        sb.append("Shortest path from node ").append(1).append(" to node ").append(startNode.id)
                .append(" [Distance = ");

        if(((Previous)startNode.nodeData).distance == Previous.infinity){
            sb.append("INFINITY] : Unreachable");
            return sb.toString();
        }

        sb.append(((Previous)startNode.nodeData).distance).append("]").append(" : ").append(startNode.id);

        int length = sb.length() - String.valueOf(startNode.id).length();
        while(((Previous)startNode.nodeData).previous != null){
            startNode = ((Previous)startNode.nodeData).previous;
            sb.insert(length, startNode.id + "->");
        }

        return sb.toString();
    }


    public void initPrev(Vertex rootVertex){
        for(Vertex vertex : vertices){
            vertex.nodeData = new Previous();
        }
        ((Previous)rootVertex.nodeData).distance = 0;
    }
}

class Graph {
    int numVertex, numEdge;
    Vertex[] vertices;

    public void newGraph(BufferedReader br) throws IOException {
        StringTokenizer st = new StringTokenizer(br.readLine());
        numVertex = Integer.parseInt(st.nextToken());
        numEdge = Integer.parseInt(st.nextToken());

        vertices = new Vertex[numVertex];

        for(int i = 0; i < numVertex;++i) vertices[i] = new Vertex(i);

        initNodeDate(vertices);

        for (int i = 0; i < numEdge; ++i){
            st = new StringTokenizer(br.readLine());

            int fromNode = Integer.parseInt(st.nextToken());
            int toNode = Integer.parseInt(st.nextToken());
            int weight = Integer.parseInt(st.nextToken());
            ((Previous)vertices[fromNode].nodeData).distance = fromNode;

            vertices[fromNode].edge = new WeightedEdge(vertices[toNode], vertices[fromNode].edge, weight);
        }
    }

    public void initNodeDate(Vertex[] vertices){
        for(Vertex vertex : vertices){
            vertex.nodeData = new Previous();
        }
    }
}

class Previous{
    int distance;
    Vertex previous;
    static int infinity = Integer.MAX_VALUE;
    public int getDistance(){return distance;}
    public Vertex getPrevious(){return previous;}

    public Previous(){
        distance = infinity;
    }
}

/**
 * This is a form for priority queue.
 */
class Heap{
    int len;
    int capacity = 10;
    Vertex[] vertices;

    public Heap(Vertex[] vertices){
        this.vertices = vertices;
        len = vertices.length;
    }

    public Heap(){
        this.vertices = new Vertex[capacity];
    }

//    /**
//     * This method takes a tree, that may not have a heap structure at all, and makes it into a max heap.
//     */
//    public void createMaxHeap(){
//        int i = len/2;
//        while(i-- > 0) fixMaxHeap(i);
//    }

    /**
     * This method takes a tree and makes it into a min heap.
     */
    public void createMinHeap(){
        int i = len/2;
        while(i-- > 0) fixDownMinHeap(i);
    }

//    /**
//     * This method sorts the min heap.
//     */
//    public void minHeapSort(){
//        createMaxHeap();
//        int length = len;
//        while(len > 1){
//            Vertex vertex = getMaxNode();
//            vertices[len] = vertex;
//        }
//        len = length;
//    }

//    /**
//     * This method sorts the max heap.
//     */
//    public void maxHeapSort(){
//        createMinHeap();
//        int length = len;
//        while(len > 1){
//            Vertex vertex = getMinNode();
//            vertices[len] = vertex;
//        }
//        len = length;
//    }

    public void updateLength(){
        if(capacity == len++){
            capacity = capacity << 1;
            vertices = Arrays.copyOf(this.vertices, capacity);
        }
    }

//    /**
//     * This method inserts a new node into a max heap.
//     * @param vertex The node to be inserted, represented using the Vertex object.
//     */
//    public void insertNewNodeMaxHeap(Vertex vertex){
//        updateLength();
//        int maxIndex = len;
//        vertices[maxIndex] = vertex;
//        increasePriorityMaxHeap(maxIndex, 0);
//    }

    /**
     * This method inserts a new node into a min heap.
     * @param vertex The node to be inserted, represented using the Vertex object.
     */
    public void insertNewNodeMinHeap(Vertex vertex){
        updateLength();
        vertices[len - 1] = vertex;
        increasePriorityMinHeap(len - 1, 0);
    }

//    /**
//     * This method increases the priority of a max heap and fixes the heap.
//     * @param index     The node to increase priority for, given as an int.
//     * @param priority  The change in priority.
//     */
//    public void increasePriorityMaxHeap(int index, int priority){
//        int parentIndex;
//        ((Previous)vertices[index].nodeData).distance += priority;
//        while(index > 0 && ((Previous)vertices[index].nodeData).distance >
//                ((Previous)vertices[parentIndex = getParentIndex(index)].nodeData).distance){
//            fixMaxHeap(parentIndex);
//            index = parentIndex;
//        }
//    }

    /**
     * This method increases the priority of a min heap and fixes the heap.
     * @param index     The node to increase priority for, given as an int.
     * @param priority  The change in priority.
     */
    public void increasePriorityMinHeap(int index, int priority){
        int parentIndex;
        ((Previous)vertices[index].nodeData).distance -= priority;
        while(index > 0 && ((Previous)vertices[index].nodeData).distance <
                ((Previous)vertices[parentIndex = getParentIndex(index)].nodeData).distance){
            heapifyUp();
            index = parentIndex;
        }
    }

//    /**
//     * This method decrease the priority of a max heap and fixes the heap.
//     * @param index     The node to increase priority for, given as an int.
//     * @param priority  The change in priority.
//     */
//    public void decreasePriorityMaxHeap(int index, int priority){
//        ((Previous)vertices[index].nodeData).distance -= priority;
//        fixMaxHeap(index);
//    }

    /**
     * This method decreases the priority of a min heap and fixes the heap.
     * @param index     The node to increase priority for, given as an int.
     * @param priority  The change in priority.
     */
    public void decreasePriorityMinHeap(int index, int priority){
        ((Previous)vertices[index].nodeData).distance += priority;
        heapifyUp();
    }

//    /**
//     * If a vertex breaks the max heap structure, then this method can fix the structure.
//     * @param index The index of the vertex breaking the structure.
//     */
//    public void fixMaxHeap(int index){
//        int leftChildIndex = getLeftChild(index);
//        if(leftChildIndex < len){
//            int rightChildIndex = leftChildIndex + 1;
//            if(rightChildIndex < len && ((Previous)vertices[rightChildIndex].nodeData).distance >
//                    ((Previous)vertices[leftChildIndex].nodeData).distance){
//                leftChildIndex = rightChildIndex;
//            }
//            if(((Previous)vertices[leftChildIndex].nodeData).distance > ((Previous)vertices[index].nodeData).distance){
//                swapVertices(index, leftChildIndex);
//                fixMaxHeap(leftChildIndex);
//            }
//        }
//    }

    public void minSort(){
        Vertex[] vertices1 = new Vertex[len];
        System.arraycopy(vertices, 0, vertices1, 0, len);
        capacity = 10;
        len = 0;
        vertices = new Vertex[capacity];
        for(Vertex vertex : vertices1){
            add(vertex);
        }
    }

    /**
     * If a vertex breaks the max heap structure, then this method can fix the structure.
     * @param index The index of the vertex breaking the structure.
     */
    public void fixDownMinHeap(int index){
        int leftChildIndex = getLeftChild(index);
        if(leftChildIndex < len){
            int rightChildIndex = leftChildIndex + 1;
            if(rightChildIndex < len && ((Previous)vertices[rightChildIndex].nodeData).distance <
                    ((Previous)vertices[leftChildIndex].nodeData).distance){
                leftChildIndex = rightChildIndex;
            }
            if(((Previous)vertices[leftChildIndex].nodeData).distance < ((Previous)vertices[index].nodeData).distance){
                swap(index, leftChildIndex);
                fixDownMinHeap(leftChildIndex);
            }
        }
    }

    /**
     * If a vertex breaks the max heap structure, then this method can fix the structure.
     * @param index The index of the vertex breaking the structure.
     */
    public void fixUpMinHeap(int index){
        int parentIndex = getParentIndex(index);
        while(parentIndex > 0 && ((Previous)vertices[index].nodeData).distance
                < ((Previous)vertices[parentIndex].nodeData).distance){
            swap(index, parentIndex);
            index = parentIndex;
            parentIndex = getParentIndex(index);
        }
    }

    /**
     * This method checks if a given node has a parent node.
     * @param index The index of the node, given as an int.
     * @return      {@code true} if the node has a parent; otherwise, {@code false}
     */
    private boolean hasParent(int index){
        return getParentIndex(index) >= 0;
    }

    /**
     * This method checks if a given node has a left child node.
     * @param index The index of the node, given as an int.
     * @return      {@code true} if the node has a left child node; otherwise, {@code false}
     */
    private boolean hasLeftChild(int index){
        return getLeftChild(index) < len;
    }

    /**
     * This method checks if a given node has a right child node.
     * @param index The index of the node, given as an int.
     * @return      {@code true} if the node has a right child node; otherwise, {@code false}
     */
    private boolean hasRightChild(int index){
        return getRightChild(index) < len;
    }

    /**
     * This method retrieves the parent vertex of the given vertex.
     * @param index Index of the child node, represented as an int.
     * @return      Vertex of the parent node.
     */
    private Vertex parent(int index){
        return vertices[getParentIndex(index)];
    }

    /**
     * This method retrieves the left child vertex of the given vertex.
     * @param index Index of the parent node, represented as an int.
     * @return      Vertex of the left child node.
     */
    private Vertex leftChild(int index){
        return vertices[getLeftChild(index)];
    }

    /**
     * This method retrieves the right child vertex of the given vertex.
     * @param index Index of the parent node, represented as an int.
     * @return      Vertex of the right child node.
     */
    private Vertex rightChild(int index){
        return vertices[getRightChild(index)];
    }

    /**
     * This method swaps the vertices at the given indices.
     * @param index1    Index of the first vertex, represented as an int.
     * @param index2    Index of the second vertex, represented as an int.
     */
    private void swap(int index1, int index2){
        Vertex temp = vertices[index1];
        vertices[index1] = vertices[index2];
        vertices[index2] = temp;
    }


    /**
     * This method returns the head of the heap, if it exists.
     * @return                              If head exists, return head node. Else, throw IllegalArgumentException.
     * @throws IllegalArgumentException     If the root vertex does not exist.
     */
    public Vertex peek() throws IllegalArgumentException{
        if(len == 0) throw new IllegalArgumentException("There is currently no root node.");
        return vertices[0];
    }

    /**
     * This method extracts (removes) the root node.
     * @return  The root node, represented as a Vertex.
     */
    private Vertex poll() throws IllegalArgumentException{
        if(len == 0) throw new IllegalArgumentException("There is currently no root node.");
        Vertex root = vertices[0];
        vertices[0] = vertices[--len];
        heapifyDown();
        return root;
    }

    /**
     * This method makes sure the heap structure is kept by starting at the last node and checking that it is correct.
     */
    private void heapifyUp(){
        int index = len - 1;
        while(hasParent(index) &&
                ((Previous)parent(index).nodeData).distance > ((Previous)vertices[index].nodeData).distance){
            swap(getParentIndex(index), index);
            index = getParentIndex(index);
        }
    }

    /**
     * This method makes sure the heap structure is kept by starting at the first node and checking that it is valid.
     */
    private void heapifyDown(){
        int index = 0;

        while(hasLeftChild(index)){
            int smallerChild = getLeftChild(index);
            if(hasRightChild(index) &&
                    ((Previous)leftChild(index).nodeData).distance >
                            ((Previous)rightChild(index).nodeData).distance){
                smallerChild += 1;
            }
            if(((Previous)vertices[index].nodeData).distance < ((Previous)leftChild(index).nodeData).distance){
                swap(index, smallerChild);
                index = smallerChild;
            }
            else return;
        }
    }

    /**
     * This method allows a vertex to be added to the heap.
     * @param vertex    Vertex to be added.
     */
    public void add(Vertex vertex){
        updateLength();
        vertices[len - 1] = vertex;
        heapifyUp();
    }


//    /**
//     * This is relevant to max heaps only.
//     * @return Max node, which is the root node.
//     */
//    public Vertex getMaxNode(){
//        Vertex maxNode = vertices[0];
//        vertices[0] = vertices[--len];
//        fixMaxHeap(0);
//        return maxNode;
//    }


    /**
     * This is relevant to min heaps only.
     * @return Min node, which is the root node.
     */
    public Vertex getMinNode(){
        Vertex minNode = vertices[0];
        vertices[0] = vertices[--len];
        heapifyDown();
        return minNode;
    }

    /**
     * This method gets the index of the parent node in the tree.
     * @param index Index of the node being evaluated, given as an int.
     * @return      Index of the parent node, given as an int.
     */
    public int getParentIndex(int index){
        return (index - 1)>>1;
    }

    /**
     * This method retrieves the index of the left child of a given node.
     * @param index The parent node, given as an int.
     * @return      The index of the left child node, given as an int.
     */
    public int getLeftChild(int index){
        return 1 + (index<<1);
    }

    /**
     * This method retrieves the index of the right child of a given node.
     * @param index The parent node, given as an int.
     * @return      The index of the right child node, given as an int.
     */
    public int getRightChild(int index){
        return 1 << index + 1;
    }

    /**
     * This method creates a string with all the values of the heap concatenated.
     * @return String presenting the values of the nodes in the heap in order.
     */
    public String printHeap(){
        StringBuilder sb = new StringBuilder();
        for(Vertex vertex : vertices){
            sb.append(((Previous)vertex.nodeData).distance).append("    ");
        }
        return sb.toString();
    }
}
