import java.util.Arrays;

/**
 * A minimum heap is a partially structured priority queue. The structural requirement for a minimum heap is that
 * each child is larger than its parent node.
 *
 * @author Trym Hamer Gudvangen
 */
public class MinHeap {
    private int capacity = 10;
    private int size = 0;
    private Vertex[] vertices;

    public MinHeap() {
        vertices = new Vertex[capacity];
    }

    /**
     * This method gets the index of the parent node in the tree.
     * @param index Index of the node being evaluated, given as an int.
     * @return      Index of the parent node, given as an int.
     */
    private int getParentIndex(int index){
        return (index - 1) >> 1;
    }

    /**
     * This method retrieves the index of the left child of a given node.
     * @param index The parent node, given as an int.
     * @return      The index of the left child node, given as an int.
     */
    private int getLeftChild(int index){
        return (index << 1) + 1;
    }

    /**
     * This method retrieves the index of the right child of a given node.
     * @param index The parent node, given as an int.
     * @return      The index of the right child node, given as an int.
     */
    private int getRightChild(int index){
        return (index + 1) << 1;
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
        return getLeftChild(index) < size;
    }

    /**
     * This method checks if a given node has a right child node.
     * @param index The index of the node, given as an int.
     * @return      {@code true} if the node has a right child node; otherwise, {@code false}
     */
    private boolean hasRightChild(int index){
        return getRightChild(index) < size;
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
     * This method makes sure the vertices array, representing the heap, has enough space for all the nodes.
     * If not, then the array size is doubled (as this is the most efficient).
     */
    private void ensureCapacity(){
        if(size == capacity){
            capacity = capacity << 1;
            vertices = Arrays.copyOf(vertices, capacity);
        }
    }

    /**
     * This method returns the head of the heap, if it exists.
     * @return                              If head exists, return head node. Else, throw IllegalArgumentException.
     * @throws IllegalArgumentException     If the root vertex does not exist.
     */
    public Vertex peek() throws IllegalArgumentException{
        if(size == 0) throw new IllegalArgumentException("There is currently no root node.");
        return vertices[0];
    }

    /**
     * This method extracts (removes) the root node.
     * @return  The root node, represented as a Vertex.
     */
    private Vertex poll() throws IllegalArgumentException{
        if(size == 0) throw new IllegalArgumentException("There is currently no root node.");
        Vertex root = vertices[0];
        vertices[0] = vertices[--size];
        heapifyDown();
        return root;
    }

    /**
     * This method makes sure the heap structure is kept by starting at the last node and checking that it is correct.
     */
    private void heapifyUp(){
        int index = size - 1;
        while(hasParent(index) &&
                ((Previous)parent(index).nodeData()).distance() > ((Previous)vertices[index].nodeData()).distance()){
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
                    ((Previous)leftChild(index).nodeData()).distance() >
                            ((Previous)rightChild(index).nodeData()).distance()){
                smallerChild += 1;
            }
            if(((Previous)vertices[index].nodeData()).distance() < ((Previous)leftChild(index).nodeData()).distance()){
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
        ensureCapacity();
        vertices[size++] = vertex;
        heapifyUp();
    }

}
