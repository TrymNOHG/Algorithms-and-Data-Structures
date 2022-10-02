import java.util.Scanner;

/**
 * Exercise 4
 * @author Leon Egeberg Hesthaug, Eirik Elvestad og Trym Hamer Gudvangen
 */
public class ElementStorage {

    static final int ARITHMETIC_CHOICE = 1;
    static final int BINARY_TREE_CHOICE = 2;
    static final int EXIT_CHOICE = 3;
    static final String SUBTRACTION_SYMBOL = "-";
    static final String ADDITION_SYMBOL = "+";

    static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        while(true){
            System.out.println("For the arithmetic exercise, press 1.\n" +
                    "For the binary tree exercise, press 2.\n" +
                    "To exit, press 3");
            int choice = input.nextInt();
            switch (choice){
                case ARITHMETIC_CHOICE -> userInterfaceArithmetic();
                case BINARY_TREE_CHOICE -> userInterfaceBinaryTree();
                case EXIT_CHOICE -> System.exit(0);
                default -> System.out.println("Invalid input! Try again.");
            }

        }


    }

    private static void userInterfaceArithmetic(){
        System.out.println("Type the operator for the operation you would like to use: \n" +
                "+ for addition\n- for subtraction");
        input.nextLine();

        String operationChoice = input.nextLine();

        System.out.println("First number");
        String firstNum = input.nextLine();
        System.out.println("Second number");
        String secondNum = input.nextLine();

        LinkedNumber linkedNumber = new LinkedNumber(firstNum);

        switch (operationChoice){
            case SUBTRACTION_SYMBOL -> {
                linkedNumber.subtractLinkedNumber(secondNum);
                System.out.println(firstNum +  " - " + secondNum + " = " + linkedNumber);
            }
            case ADDITION_SYMBOL -> {
                linkedNumber.addLinkedNumber(secondNum);
                System.out.println(firstNum +  " + " + secondNum + " = " + linkedNumber);
            }
            default -> System.out.println("Invalid choice! Try again.");
        }
    }

    private static void userInterfaceBinaryTree(){
        input.nextLine();

        System.out.println("Write a sentence to get a Binary tree");
        String treeString = input.nextLine();

        WordTree wordTree = new WordTree(treeString);

        wordTree.printTree();
    }

}

/**
 * Oppgave 4-5 a og b
 */
class LinkedNumber{


    private DoubleLinkedList<Integer> linkedNumber;

    public LinkedNumber(String number) {
        this.linkedNumber = createLinkedNumber(number);
    }

    public DoubleLinkedList<Integer> createLinkedNumber(String number){
        DoubleLinkedList<Integer> tempLinkedNumber = new DoubleLinkedList<>();
        for(int i = 0; i < number.length(); i++){

            if (number.charAt(i) == '-' && i == 0){
                tempLinkedNumber.addLast(Integer.parseInt(String.valueOf(number.charAt(i) + number.charAt(i + 1))));
                i++;
            }
            else tempLinkedNumber.addLast(Integer.parseInt(String.valueOf(number.charAt(i))));
        }
        return tempLinkedNumber;
    }

    //TODO: extra challenge Fix subtract negative numbers
    public void subtractLinkedNumber(DoubleLinkedList<Integer> otherNumber){

        boolean negative = false;
        makeLinkedNumberSameSize(this.linkedNumber, otherNumber);

        int i = 0;

        Nodule otherNum = otherNumber.getHead();
        Nodule thisNum = this.linkedNumber.getHead();

        while((int)otherNum.getElement() == (int)thisNum.getElement() && (int)otherNum.getElement() != 0
                && this.linkedNumber.size() > 1){
            otherNum = otherNum.getNext();
            thisNum = thisNum.getNext();
        }
        if((int)otherNum.getElement() > (int)thisNum.getElement()){
            DoubleLinkedList<Integer> tempNum = new DoubleLinkedList<>(otherNumber);
            otherNumber = this.linkedNumber;
            this.linkedNumber = tempNum;
            negative = true;
        }

        while (i < this.linkedNumber.size()){
            int thisDigitNum = this.linkedNumber.get(i);
            int otherDigitNum = otherNumber.get(i);

            int newDigitNum;

            if (otherDigitNum > thisDigitNum && i != 0){

                int indexOfNearestNum = i - findPreviousWholeNum(this.linkedNumber.getNode(i));

                this.linkedNumber.set(indexOfNearestNum,  this.linkedNumber.get(indexOfNearestNum) - 1);

                for(int j = indexOfNearestNum + 1; j < i; j++){
                    this.linkedNumber.set(j,  9);
                }
                newDigitNum = 10 + thisDigitNum - otherDigitNum;

            }
            else newDigitNum = thisDigitNum - otherDigitNum;

            this.linkedNumber.set(i, newDigitNum);

            i++;
        }

        shrinkLinkedNumber(linkedNumber);

        if(negative){
            this.linkedNumber.addFirst("-");
        }
    }

    public void subtractLinkedNumber(String otherNumber){
        subtractLinkedNumber(createLinkedNumber(otherNumber));
    }

    public void addLinkedNumber(DoubleLinkedList<Integer> otherNumber) {

        makeLinkedNumberSameSize(this.linkedNumber, otherNumber);

        int carryOver = 0;
        int i = 1;
        while (i <= this.linkedNumber.size()) {
            int thisDigitNum = this.linkedNumber.get(this.linkedNumber.size() - i);
            int otherDigitNum = otherNumber.get(otherNumber.size() - i) + carryOver;

            int newDigitNum;

            if (thisDigitNum + otherDigitNum >= 10){
                carryOver = 1;
                newDigitNum = (thisDigitNum + otherDigitNum) - 10;
            }
            else{
                carryOver = 0;
                newDigitNum = thisDigitNum + otherDigitNum;
            }

            this.linkedNumber.set(this.linkedNumber.size() - i, newDigitNum);

            i++;
        }


        if(carryOver == 1){
            this.linkedNumber.addFirst(1);
        }

        shrinkLinkedNumber(linkedNumber);

    }

    public void addLinkedNumber(String otherNumber){
        addLinkedNumber(createLinkedNumber(otherNumber));
    }

    private void expandLinkedNumber(DoubleLinkedList<?> linkedNum, int size){
        while(linkedNum.size() < size){
            linkedNum.addFirst(0);
        }
    }

    private void shrinkLinkedNumber(DoubleLinkedList<?> linkedNum){
        while((int) linkedNum.getHead().getElement() == 0 && linkedNum.size() > 1){
            linkedNum.deleteElement(linkedNum.getHead());
        }
    }

    private void makeLinkedNumberSameSize(DoubleLinkedList<Integer> linkedNum1, DoubleLinkedList<Integer> linkedNum2){
        expandLinkedNumber(linkedNum1, linkedNum2.size());
        expandLinkedNumber(linkedNum2, linkedNum1.size());
    }

    private int findPreviousWholeNum(Nodule node){
        Nodule nodeNum = node;
        int i = 1;
        while((int) nodeNum.getPrev().getElement() == 0){
            nodeNum = nodeNum.getPrev();
            i++;
        }
        return i;
    }

    @Override
    public String toString() {
        return linkedNumber.toString();
    }
}

class DoubleLinkedList<E>{

    private Nodule head = null;
    private Nodule tail = null;
    private int size = 0;

    public DoubleLinkedList(DoubleLinkedList<?> doubleLinkedList) {
        this.head = doubleLinkedList.getHead();
        this.tail = doubleLinkedList.getTail();
        this.size = doubleLinkedList.size();
    }

    public DoubleLinkedList() {
    }

    public void addFirst(Object element){
        Nodule newHead = new Nodule(element, null, this.head);

        if(this.head == null) this.tail = newHead;
        else this.head.setPrevious(newHead);

        this.head = newHead;
        size++;
    }


    public void addLast(Object value){
        Nodule newTail = new Nodule(value, this.tail, null);

        if(this.tail == null) this.head = newTail;
        else this.tail.setNext(newTail);

        this.tail = newTail;

        size++;
    }

    public Nodule deleteElement(Nodule node) {
        if (node.getPrev() != null) {
            node.getPrev().setNext(null);
            this.tail = node.getPrev();
        }
        if (node.getNext() != null) {
            node.getNext().setPrevious(null);
            this.head = node.getNext();
        }

        --size;

        return node;
    }

    public void set(int index, Object element){
        this.getNode(index).setElement(element);
    }

    public void insertNode(int index, Nodule currentNode, Object element){
        Nodule newNode = new Nodule(element, currentNode, currentNode.getNext());
        if(currentNode.getNext() != null){
            currentNode.getNext().setPrevious(newNode);
        }

        getNode(index).setNext(newNode);
    }


    public E get(int index){
        Nodule node = this.head;
        int i = 0;
        while(i++ < index && node.getNext() != null){
            node = node.getNext();
        }
        return (E) node.getElement();
    }

    public Nodule getNode(int index){
        Nodule node = this.head;
        int i = 0;
        while(i++ < index){
            node = node.getNext();
        }
        return node;
    }

    public Nodule getLast(){
        return getNode(this.size);
    }

    public Nodule getByValue(Object element){
        Nodule node = this.head;
        while(node.getNext().getElement() != element){
            node = node.getNext();
        }
        return node.getNext();
    }

    public Nodule getHead() {
        return this.head;
    }

    public Nodule getTail() {
        return tail;
    }

    public int size() {
        return size;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        Nodule nextElement = this.getHead();
        while (nextElement != null) {
            sb.append(nextElement.getElement());
            nextElement = nextElement.getNext();
        }

        return sb.toString();
    }

}

class Nodule{

    private Object element;
    private Nodule next;
    private Nodule previous;



    public Nodule(Object object, Nodule prev, Nodule next) {
        this.element = object;
        this.next = next;
        this.previous = prev;
    }

    public Object getElement(){
        return this.element;
    }


    public Nodule getNext(){
        return this.next;
    }

    public Nodule getPrev(){
        return this.previous;
    }

    public void setElement(Object element){
        this.element = element;
    }

    public void setNext(Nodule next) {
        this.next = next;
    }

    public void setPrevious(Nodule previous) {
        this.previous = previous;
    }
}



/**
 * Deloppgave 2
 */
class WordTree {

    private String[] words;
    private Tree tree = new Tree();

    public WordTree(String words) {
        this.words = words.split(" ");
        tree.insertAllNodes(this.words);
    }

    public void printTree(){
        System.out.println("Alphabetically sorted binary tree: \n");
        tree.printThroughOrderTraversal();
    }
}



/**
 * Defines tree
 */
class Tree {
    TreeNode root;
    int numNodes = 0;

    public Tree(){
        root = null;
    }

    public boolean treeEmpty(){
        return root == null;
    }

    public int findDepth(TreeNode node){
        int depth = -1;
        while(node != null){
            depth++;
            node = node.parent;
        }
        return depth;
    }

    public int findHeight(TreeNode node){
        if(node == null) return -1;
        else{
            int leftHeight = findHeight(node.left);
            int rightHeight = findHeight(node.right);
            if(leftHeight >= rightHeight) return leftHeight + 1;
            else return rightHeight + 1;
        }
    }

    public int findHeight(){
        return findHeight(root);
    }

    public void insert(String word){
        String key = word;
        TreeNode node = root;

        if(treeEmpty()){
            root = new TreeNode(word,null,null,null);
            numNodes++;
            return;
        }

        String compareKey = node.element;

        TreeNode parent = null;

        while(node != null){
            parent = node;
            compareKey = node.element;
            if(key.compareToIgnoreCase(compareKey) < 0) node = node.left;
            else node = node.right;
        }

        if (key.compareToIgnoreCase(compareKey) < 0) parent.left = new TreeNode(word, parent, null, null);
        else parent.right = new TreeNode(word, parent, null, null);
        numNodes++;
    }

    public void insertAllNodes(String[] words){
        for(String word : words){
            insert(word);
        }
    }

    public void printThroughOrderTraversal(){
        TreeNode[][] orderedList = orderByLevelTraversal();


        for(int level = 0; level <= this.findHeight(); level++){
            printLevel(orderedList[level]);
        }
    }

    public void printLevel(TreeNode[] nodesInLevel){
        String defaultSpace = "   ";
        StringBuilder sb = new StringBuilder();
        int width = 36;

        int nodesPrinted = 0;
        for(int i = 0; i < width; i++){
            if(i % (width / (nodesInLevel.length + 1)) == 0 && nodesPrinted < nodesInLevel.length && i != 0) {
                if(nodesInLevel[nodesPrinted] == null) sb.append(defaultSpace);
                else sb.append(nodesInLevel[nodesPrinted].element);
                nodesPrinted++;
            }
            else sb.append(defaultSpace);
        }


        System.out.println(sb);
        System.out.println("");
    }

    public TreeNode[][] orderByLevelTraversal(){
        TreeNode[][] orderedTree = new TreeNode[this.findHeight() + 1][this.numNodes];
        orderedTree[0] = new TreeNode[1];
        orderedTree[0][0] = this.root;

        for(int level = 1; level <= this.findHeight(); level++){
            orderedTree[level] = new TreeNode[1 << level];
            int index = 0;
            
            for(int numNodes = 0; numNodes < orderedTree[level - 1].length; numNodes++){
                if(orderedTree[level - 1][numNodes] != null){
                    orderedTree[level][index] = orderedTree[level - 1][numNodes].left;
                    orderedTree[level][index + 1] = orderedTree[level - 1][numNodes].right;
                    index += 2;
                }
                else{
                    orderedTree[level][index] = null;
                    orderedTree[level][index + 1] = null;
                    index += 2;
                }
            }
        }
        
        return orderedTree;
    }

}

/**
 * Defines TreeNode
 */
class TreeNode {
    String element;
    TreeNode left;
    TreeNode right;
    TreeNode parent;

    public TreeNode(String word, TreeNode parent, TreeNode left, TreeNode right){
        this.element = word;
        this.parent = parent;
        this.left = left;
        this.right = right;
    }
}
