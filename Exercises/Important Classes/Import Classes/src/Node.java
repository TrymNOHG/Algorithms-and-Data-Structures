/**
 * This class represents a node containing an element and a next node.
 *
 * @author Trym Hamer Gudvangen
 */
public class Node {
    private Object element;
    private Node next;

    /**
     * This constructor creates a node using the element attached and the next node.
     * @param element   The value attached to the node, represented using an Object.
     * @param next      The node connected to this node, represented using a Node object.
     */
    public Node(Object element, Node next) {
        this.element = element;
        this.next = next;
    }

    /**
     * This method retrieves the element of the node.
     * @return  Element of the node, given as an Object.
     */
    public Object getElement(){
        return element;
    }

    /**
     * This method retrieves the next node.
     * @return  Next node, given as a Node object.
     */
    public Node next(){
        return next;
    }
}
