import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

/**
 * @
 */
public class CompressData {
    public static void main(String[] args) throws URISyntaxException, IOException{
        menu();
    }

    private static void menu() throws IOException, URISyntaxException {
//
//        URL resourceUrl1 = CompressData.class.getResource("diverse2.txt");
//        File fileIn = new File(resourceUrl1.toURI());
//        DataInputStream in = new DataInputStream(new FileInputStream(fileIn));
//
//        URL resourceUrl2 = CompressData.class.getResource("diverse2.txtCompressedFinal4336134511419686476.txtUnpackedFinal8633740773872670368.txt");
//        File fileIn2 = new File(resourceUrl2.toURI());
//        DataInputStream in2 = new DataInputStream(new FileInputStream(fileIn2));
//
//        File fileOut = File.createTempFile(fileIn.getName() + "Compressed", ".txt", new File(fileIn.getParent()));
//
//        File unpackedFileFinal = File.createTempFile(fileIn.getName() + "UnpackedFinal", ".txt", new File(fileIn.getParent()));
//
//
//        byte[] test = in.readAllBytes();
//
//        byte[] test2 = in2.readAllBytes();
//
//        for(int p = 0; p < test.length; p++){
//
//            if(test[p] != test2[p]){
//                System.out.println("--------");
//            }
//
//            System.out.println("index: " + p);
//            System.out.println("Array1: " + test[p] + "Char: " + (char) test[p]);
//            System.out.println("Array2: " + test2[p] + "Char: " + (char) test2[p]);
//
//        }
//
//        Huffman.compress(fileIn, fileOut);
//        Huffman.decompress(fileOut, unpackedFileFinal);
//
//
//        System.exit(0);

        Scanner input = new Scanner(System.in);
        System.out.println("input file name: name.extension");
        String fileName = input.nextLine();
        URL resourceUrl1 = CompressData.class.getResource(fileName);
        assert resourceUrl1 != null;
        File fileIn = new File(resourceUrl1.toURI());

        System.out.println("Input 1 for compression");
        System.out.println("Input 2 for unpacking");
        int alternative = input.nextInt();
        input.nextLine();

        if(alternative == 1){
            File fileOut = File.createTempFile(fileIn.getName() + "Compressed", ".txt", new File(fileIn.getParent()));
            File fileOutFinal = File.createTempFile(fileIn.getName() + "CompressedFinal", ".txt", new File(fileIn.getParent()));
            CompressFile.compress(fileIn, fileOut, fileOutFinal);
            //fileOut.deleteOnExit();
        }
        else if(alternative == 2){
            System.out.println("Input desired output file type in this format: .pdf");
            String extension = input.nextLine();
            File unpackedFile = File.createTempFile(fileIn.getName() + "Unpacked", extension, new File(fileIn.getParent()));
            File unpackedFileFinal = File.createTempFile(fileIn.getName() + "UnpackedFinal", ".txt", new File(fileIn.getParent()));
            DecompressFile.decompress(fileIn, unpackedFile, unpackedFileFinal);
            //unpackedFile.deleteOnExit();
        }
        else {
            System.out.println("Wrong input");
            System.exit(0);
        }
    }
}

class CompressFile {
    public static void compress(File fileIn, File fileOut, File fileOutFinal) throws IOException {
//        LempelZiv.lz(fileIn, fileOut);
        Huffman.compress(fileIn, fileOutFinal);

    }

}

class DecompressFile {
    public static void decompress(File fileIn, File fileOut, File fileOutFinal) throws IOException {
        Huffman.decompress(fileIn, fileOutFinal);
//        LempelZiv.lzDecomp(fileOut, fileOutFinal);
    }

}

class LempelZiv {

    static byte[] data;
    static byte[] buffer;
    static int length = 0;
    static int pos = 0;

    public static void lz(File inputFile, File fileOut) throws IOException{
        DataInputStream in = new DataInputStream(new FileInputStream(inputFile));
        DataOutputStream out = new DataOutputStream(new FileOutputStream(fileOut));

        buffer = new byte[1024*32];
        data = in.readAllBytes();

        for(int i = 0; i < data.length; i++){
            if(length > 32000){
                out.writeShort(length);
                for(int k = 0; k < length; k++){
                    out.write(buffer[(pos+k)%buffer.length]);
                }
                pos = (pos+length)%buffer.length;
                out.writeShort(0);
                out.writeShort(0);

                length = 0;

            }
            int bufferPos = inBuffer(data[i], i);
            if(bufferPos == -1){
                addToBuffer(data[i], i);
                length++;

            }
            else {

                int repeatLength = makeWord(bufferPos, i, i);
                int currentMaxIdx = bufferPos;

                while (true){
                    bufferPos = inBuffer(data[i], bufferPos-1);
                    if (bufferPos == -1) break;

                    int checkSize = makeWord(bufferPos, i, i);
                    if(repeatLength<checkSize){
                        repeatLength = checkSize;
                        currentMaxIdx = bufferPos;
                    }
                }

                if(repeatLength > 6){

                    out.writeShort(length);
                    for(int k = 0; k < length; k++){
                        out.write(buffer[(pos+k)%buffer.length]);
                    }
                    pos = (pos+length+repeatLength)%buffer.length;
                    length = 0;

                    out.writeShort(currentMaxIdx);
                    out.writeShort(repeatLength);

                    for(int j = 0; j<repeatLength; j++){
                        addToBuffer(data[i], i);
                        i++;
                    }
                    i--;
                }
                else {
                    addToBuffer(data[i], i);
                    length++;
                }
            }
        }
        out.writeShort(length);
        for(int k = 0; k < length; k++){
            out.write(buffer[(pos+k)%buffer.length]);
        }

        in.close();
        out.close();
    }

    public static int inBuffer(byte b, int pos){
        for(int i = pos%buffer.length; i>=0; i--){
            if (buffer[i] == b)
                return i;
        }
        return -1;
    }

    public static void addToBuffer(byte b, int pos){
        buffer[pos%buffer.length] = b;
    }

    public static int makeWord(int bufferPos, int bytePos, int posMax){
        byte bufferByte = buffer[bufferPos];
        byte dataByte = data[bytePos];
        int size = 0;
        while (dataByte == bufferByte && bytePos != data.length-1 && bufferPos < posMax){
            size++;
            dataByte = data[++bytePos];
            bufferByte = buffer[++bufferPos%buffer.length];


        }
        return size;
    }


    public static void lzDecomp(File inputFile, File fileout) throws IOException {
        DataInputStream in = new DataInputStream(new FileInputStream(inputFile));
        DataOutputStream out = new DataOutputStream(new FileOutputStream(fileout));


        pos = 0;
        buffer = new byte[1024*32];
        short start = in.readShort();
        data = new byte[start];
        in.readFully(data);
        out.write(data);

        for(int j = 0; j<start; j++){
            addToBuffer(data[j], j);
            pos++;
        }


        while (in.available()>0){
            short back = in.readShort();
            short copyAmount = in.readShort();

            if(copyAmount != 0){
                data = new byte[copyAmount];
            }

            int i = 0;

            for(int tempIndex = back; tempIndex < back + copyAmount; tempIndex++){
                byte index = buffer[tempIndex];
                data[i++] = index;
                addToBuffer(index, pos);
                pos++;
            }
            if(copyAmount != 0){
                out.write(data);
            }


            start = in.readShort();
            data = new byte[start];
            in.readFully(data);
            for(int j = 0; j<start; j++){
                addToBuffer(data[j], pos);
                pos++;
            }
            out.write(data);

        }
        in.close();
        out.close();

    }

}

//Check that the start of the compressedFile is being read. So after symbols and freq.
//Check that the first letter is actually turned into the correct symbol.

class Huffman {
    
    static List<Long[]> symbolFreq;
    static Heap heap;
    static LeafNode[] symbols;
    static byte[] fileData;
    static List<Byte> compressedFile;
    static List<Byte> decompressedFile;
    static int fileStart, totSymbols;
    static FrequencyTree tree;
    public static void compress(File inputFile, File outputFile) throws IOException {
        DataInputStream data = new DataInputStream(new FileInputStream(inputFile));

        initializeData(data);
        data.close();
        huffmanTree();
        compressFreqTable();
        compressFile();

        System.out.println(isHuffmanTreeValid());
        tree.printTree();

        DataOutputStream compressFile = new DataOutputStream((new FileOutputStream(outputFile)));

        compressFile.write(copyListToArr(compressedFile));
        compressFile.close();
    }

    public static void decompress(File inputFile, File outputFile) throws IOException {
        DataInputStream data = new DataInputStream(new FileInputStream(inputFile));
        initializeDecompressData(data);
        data.close();
        huffmanTree();
        totSymbols = (int) heap.peek().frequency();
        DataOutputStream decompressFile = new DataOutputStream(new FileOutputStream(outputFile));

        decompress();
        tree.printTree();


        decompressFile.write(copyListToArr(decompressedFile));
        decompressFile.close();
    }

    private static void initializeData(DataInputStream dataIn) throws IOException {
        symbolFreq = new ArrayList<>();
        fileData = dataIn.readAllBytes();
        for(byte symbols : fileData){
            boolean existsInList = false;
            for(Long[] symbolInfo : symbolFreq){
                if(symbolInfo[0] == symbols){
                    symbolInfo[1]++;
                    existsInList = true;
                }
            }
            if(!existsInList){
                symbolFreq.add(new Long[]{(long) symbols, 1L});
            }
        }
        symbols = new LeafNode[symbolFreq.size()];

        heap = new Heap(createFrequencyTree());
    }

    private static void initializeDecompressData(DataInputStream data) throws IOException {
        fileData = data.readAllBytes();
        fileStart = reconstructFreqTable();
        symbols = new LeafNode[symbolFreq.size()];
        heap = new Heap(createFrequencyTree());
    }

    private static int reconstructFreqTable(){
        int numSymbols = (int) (Bits.getUnsignedByte(fileData[0]) + 1);
        symbolFreq = new ArrayList<>();
        boolean byteSymbol = true;
        int i = 1;
        while(numSymbols > 0 || !byteSymbol){
            byte currentByte = fileData[i];
            if(byteSymbol){
                symbolFreq.add(new Long[]{(long) currentByte, 0L});
                byteSymbol = false;
                numSymbols--;
            }
            else{
                if(Bits.getUnsignedByte(currentByte) < 255){
                    byteSymbol = true;
                }
                symbolFreq.get(symbolFreq.size() - 1)[1] += Bits.getUnsignedByte(currentByte);
            }
            i++;
        }

        return i;
    }

    public static void compressFreqTable(){
        compressedFile = new ArrayList<>();
        compressedFile.add((byte)(symbolFreq.size() - 1));
        for(int i = 1; i <= symbolFreq.size(); i++){
            compressedFile.add((byte)(symbols[i-1].symbol));
            byte[] frequencyBytes = Bits.splitLong(symbols[i-1].frequency);
            for (byte frequencyByte : frequencyBytes) {
                compressedFile.add(frequencyByte);
            }
        }

    }

    public static FrequencyTree[] createFrequencyTree(){
        FrequencyTree[] frequencyTrees = new FrequencyTree[symbolFreq.size()];
        int i = 0;
        for(Long[] symbol : symbolFreq){
                LeafNode newSymbol = new LeafNode(symbol[0], symbol[1], null);
                symbols[i] = newSymbol;
                frequencyTrees[i] = new FrequencyTree(newSymbol);
                i++;
        }
        return frequencyTrees;
    }

    public static void huffmanTree(){
        while (heap.len > 1){
//            System.out.println(heap.printHeap());
            createSubTree();
        }
        addBinaryVal();
        tree = heap.peek();
    }

    public static void createSubTree(){
        FrequencyTree newTree;
        FrequencyTree rightSubTree = heap.poll();
        FrequencyTree leftSubTree = heap.poll();

        if(rightSubTree.frequency() < leftSubTree.frequency()){
            newTree = new FrequencyTree(rightSubTree, leftSubTree);
        }
        else{
            newTree = new FrequencyTree(leftSubTree, rightSubTree);
        }

        heap.add(newTree);
    }

    public static void addBinaryVal(){
        addBinaryVal(heap.peek().root);
    }

    private static void addBinaryVal(Node node){
        if(node.left != null){
            node.left.addZero(node.bitString);
            addBinaryVal(node.left);
        }
        if(node.right != null){
            node.right.addOne(node.bitString);
            addBinaryVal(node.right);
        }
    }

    //This is done in the order that they were placed in the LeafNode list.
    private static void compressFile() {
        StringBuilder sb = new StringBuilder();
        int bitsLeft = 8;
        int currentByteIndex = compressedFile.size();
        long symb;
        int symbLen;
        int temp2 = currentByteIndex;
        boolean r = true;
        compressedFile.add((byte)0b00000000);
        //Find a better way than a double for loop and if not, then escape when the letter found has been added.
        for(byte symbol : fileData){
            for(LeafNode leafNode : symbols){
                if(symbol == leafNode.symbol){
                    symbLen = Bits.numberOfBits(leafNode.bitString) - 1; //Number of bits to start with.
                    symb = (leafNode.bitString & ~(1L << symbLen)); //Start with all the bits from a symbol
                    int length = Bits.numberOfBits(symb);
                    for(int i = 0; i < symbLen - length; i++){
                        sb.append("0");
                    }
                    sb.append(Long.toBinaryString(Bits.getUnsignedByte((byte) symb)));

                    while(symbLen != 0) { //Add bits until you don't need to anymore
                        int bitsToMove;
                        if(symbLen < bitsLeft){
                            bitsToMove = bitsLeft - symbLen;
                            compressedFile.set(currentByteIndex,
                                    (byte)(compressedFile.get(currentByteIndex) | (symb << bitsToMove)));
                            bitsLeft -= symbLen;
                            symbLen = 0;
                        }
                        else{
                            bitsToMove = symbLen - bitsLeft;
                            byte temp = (byte) (symb >> bitsToMove);
                            compressedFile.set(currentByteIndex,
                                    (byte)(compressedFile.get(currentByteIndex) | temp));
                            symb =  Bits.getUnsignedByte((byte)(symb ^ (temp << bitsToMove)));
                            symbLen -= bitsLeft;
                            bitsLeft = 8;
                            currentByteIndex++;
                            compressedFile.add((byte)0b00000000);
                        }
                        //TODO: I think this can be optimized to not have two conditions
                    }
                }
            }
        }

        for(int j = temp2; j < compressedFile.size(); j++){
            for(int k = 0; k < (8 - Bits.numberOfBits(compressedFile.get(j))); k++){
                System.out.println(Bits.numberOfBits(compressedFile.get(j)));
                System.out.print("0");
            }
            System.out.print(Long.toBinaryString(Bits.getUnsignedByte(compressedFile.get(j))));
        }

//        System.out.println(sb.toString());
    }

    public static void decompress() {
        decompressedFile = new ArrayList<>();
        Node currentNode = tree.root;
        for(int i = fileStart; i < fileData.length; i++){
            for(int j = 8; j > 0; j--){
                if(currentNode instanceof LeafNode){
                    decompressedFile.add((byte)((LeafNode) currentNode).symbol);
                    j++;
                    if(--totSymbols == 0) break; //In case, the last byte had extra bits.
                    currentNode = tree.root;
                }else{
                    System.out.println(j);
                    if(Bits.getBitAtPos(j, fileData[i]) == 1){
                        currentNode = currentNode.right;
                    }
                    else{
                        currentNode = currentNode.left;
                    }
                }
            }
        }
    }
    public static byte[] copyListToArr(List<Byte> byteList) {
        byte[] finalCompression = new byte[byteList.size()];
        for(int i = 0; i < byteList.size(); i++){
            finalCompression[i] = byteList.get(i);
        }
        return finalCompression;
    }

    public static boolean isHuffmanTreeValid(){
        FrequencyTree frequencyTree = heap.peek();
        return isHuffmanTreeValid(frequencyTree.root);
    }

    private static boolean isHuffmanTreeValid(Node node){
        boolean leftSubTree = true, rightSubTree = true;
        if(node.left != null){
            if(node instanceof LeafNode) return false;
            leftSubTree = isHuffmanTreeValid(node.left);
        }
        if (node.right != null) {
            if(node instanceof LeafNode) return false;
            rightSubTree = isHuffmanTreeValid(node.right);

        }
        return (leftSubTree && rightSubTree);
    }

}


/**
 * This is a form for priority queue.
 */
class Heap{
    int len;
    int capacity = 10;
    FrequencyTree[] queue;

    public Heap(FrequencyTree[] nodes){
        this.queue = nodes;
        len = queue.length;
        createHeap(); //TODO: instead just use minsort...
    }

    /**
     * This method takes a tree, that may not have a heap structure at all, and makes it into a max heap.
     */
    public void createHeap(){
        int i = len/2;
        while(i-- > 0) heapifyDown(i); //Do not really need this if I am using the minSort method.
        minSort();
    }
    //TODO: check if I can remove stuff above!!!


    //This should sort first by frequency and then by whether it is a whole tree or one node.
    public void minSort(){
        FrequencyTree[] treeCopy = new FrequencyTree[len];
        System.arraycopy(this.queue, 0, treeCopy, 0, len);
        Arrays.sort(treeCopy, Comparator.comparing(FrequencyTree::frequency).thenComparing(FrequencyTree::isTree));
        System.arraycopy(treeCopy, 0, this.queue, 0, len);
    }


    /**
     * This method allows a FrequencyTree to be added to the heap.
     * @param tree    Tree to be added.
     */
    public void add(FrequencyTree tree){
        updateLength();
        queue[len - 1] = tree;
        minSort();
//        heapifyUp();
    }


    /**
     * This method makes sure the heap structure is kept by starting at the last node and checking that it is correct.
     */
    private void heapifyUp(){
        int index = len - 1;
        while(hasParent(index) && parent(index).frequency() > queue[index].frequency()){
            swap(getParentIndex(index), index);
            index = getParentIndex(index);
        }
    }

    /**
     * If a vertex breaks the max heap structure, then this method can fix the structure.
     * @param index The index of the vertex breaking the structure.
     */
    public void heapifyUp(int index){
        int parentIndex = getParentIndex(index);
        while(parentIndex > 0 && queue[index].frequency() < queue[parentIndex].frequency()){
            swap(index, parentIndex);
            index = parentIndex;
            parentIndex = getParentIndex(index);
        }
    }

    /**
     * This method makes sure the heap structure is kept by starting at the first node and checking that it is valid.
     */
    private void heapifyDown(){
        int index = 0;

        while(hasLeftChild(index)){
            int smallerChild = getLeftChild(index);
            if(hasRightChild(index) && leftChild(index).frequency() > rightChild(index).frequency()){
                smallerChild += 1;
            }
            if(queue[index].frequency() > queue[smallerChild].frequency()){
                swap(index, smallerChild);
                index = smallerChild;
            }
            else return;
        }
    }

    /**
     * If a vertex breaks the max heap structure, then this method can fix the structure.
     * @param index The index of the vertex breaking the structure.
     */
    public void heapifyDown(int index){
        int leftChildIndex = getLeftChild(index);
        if(leftChildIndex < len){
            int rightChildIndex = leftChildIndex + 1;
            if(rightChildIndex < len && queue[rightChildIndex].frequency() < queue[leftChildIndex].frequency()){
                leftChildIndex = rightChildIndex;
            }
            if(queue[leftChildIndex].frequency() < queue[index].frequency()){
                swap(index, leftChildIndex);
                heapifyDown(leftChildIndex);
            }
        }
    }

    /**
     * This method returns the head of the heap, if it exists.
     * @return                              If head exists, return head node. Else, throw IllegalArgumentException.
     * @throws IllegalArgumentException     If the root Node does not exist.
     */
    public FrequencyTree peek() throws IllegalArgumentException{
        if(len == 0) throw new IllegalArgumentException("There is currently no root node.");
        return queue[0];
    }

    /**
     * This method extracts (removes) the root FrequencyTree.
     * @return  The root tree, represented as a FrequencyTree.
     */
    public FrequencyTree poll() throws IllegalArgumentException{
        if(len == 0) throw new IllegalArgumentException("There is currently no root node.");
        FrequencyTree root = queue[0];
        queue[0] = queue[--len];
        heapifyDown();
        return root;
    }

    public void updateLength(){
        if(capacity == len++){
            capacity = capacity << 1;
            queue = Arrays.copyOf(this.queue, capacity);
        }
    }

    /**
     * This method swaps the queue at the given indices.
     * @param index1    Index of the first Node, represented as an int.
     * @param index2    Index of the second Node, represented as an int.
     */
    private void swap(int index1, int index2){
        FrequencyTree temp = queue[index1];
        queue[index1] = queue[index2];
        queue[index2] = temp;
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
     * This method retrieves the parent Node of the given Node.
     * @param index Index of the child node, represented as an int.
     * @return      Node of the parent node.
     */
    private FrequencyTree parent(int index){
        return queue[getParentIndex(index)];
    }

    /**
     * This method retrieves the left child Node of the given Node.
     * @param index Index of the parent node, represented as an int.
     * @return      Node of the left child node.
     */
    private FrequencyTree leftChild(int index){
        return queue[getLeftChild(index)];
    }

    /**
     * This method retrieves the right child Node of the given Node.
     * @param index Index of the parent node, represented as an int.
     * @return      Node of the right child node.
     */
    private FrequencyTree rightChild(int index){
        return queue[getRightChild(index)];
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
        for(int i = 0; i < len; i++){
            sb.append(queue[i]).append("    ");
        }
        return sb.toString();
    }
}


class FrequencyTree {
    Node root;

    public FrequencyTree(Node root) {
        this.root = root;
    }

    public FrequencyTree(FrequencyTree leftSubTree, FrequencyTree rightSubTree){
        long totalFrequency = leftSubTree.frequency() + rightSubTree.frequency();
        this.root = new Node(totalFrequency, leftSubTree.root, rightSubTree.root, null);
        leftSubTree.root.parent = this.root;
        rightSubTree.root.parent = this.root;
    }

    public long frequency(){
        return root.frequency;
    }

    public boolean isTree(){
        return root.left == null;
    }

    private int totHeight(){
        return height(this.root);
    }
    private int height(Node currentNode){
        if (currentNode == null){
            return -1;
        }

        int leftHeight = height(currentNode.left);
        int rightHeight = height(currentNode.right);

        if(leftHeight >= rightHeight) return leftHeight + 1;
        else return rightHeight + 1;
    }

    private int findDepth(Node node){
        int depth = -1;
        while(node != null){
            node = node.parent;
            depth++;
        }
        return depth;
    }

    //This method isn't perfect. The last line of printing is a bit off
    public void printTree(){
        StringBuilder sb = new StringBuilder();
        sb.append("========== Tree =========");
        if(this.root == null) throw new IllegalArgumentException("Tree has to have a root.");

        Stack<Node> stackOfNodes = new Stack<>();
        stackOfNodes.push(this.root);

        int currentLevel = 0;

        while(!stackOfNodes.empty()){
            Node node = stackOfNodes.pop();

            if(findDepth(node) != currentLevel){
                currentLevel++;
                sb.append("\n");

            }
            sb.append("          ".repeat(Math.max(0, totHeight() - currentLevel)));
            sb.append(node);

            if(node.left != null){
                stackOfNodes.add(0, node.left);
            }

            if(node.right != null){
                stackOfNodes.add(0, node.right);
            }
        }

        System.out.println(sb);
    }

    @Override
    public String toString() {
        return "FrequencyTree: " + root;
    }
}

class Node {
    long frequency;
    Node left;
    Node right;
    Node parent;
    long bitString = 0b1; //Would have to remove the first bit from each actual letter.

    public Node(long frequency, Node left, Node right, Node parent) {
        this.frequency = frequency;
        this.left = left;
        this.right = right;
        this.parent = parent;
    }

    public void addZero(long parentVal){
        bitString = (parentVal << 1);
    }

    public void addOne(long parentVal){
        bitString = (parentVal << 1) + 1;
    }

    public String getBitString(){
        return Long.toBinaryString(bitString).substring(1);
    }

    @Override
    public String toString() {
        return "Node: " + "frequency = " + frequency;
    }
}

class LeafNode extends Node {
    long symbol; //This has to be changed to some byte or array of bytes.

    public LeafNode(long symbol, long frequency, Node parent) {
        super(frequency, null, null, parent);
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return  Bits.getUnsignedByte((byte) symbol) + ", " + frequency + ", " + getBitString() + "      ";
    }
}

class Bits {

    /**
     * This method returns a long, consisting of the unsigned byte.
     * @param bits
     * @return
     */
    public static long getUnsignedByte(byte bits){
        return (bits & 0xFF);
    }

    /**
     * This method counts the number of bits of the given value.
     * @param val
     * @return
     */
    public static int numberOfBits(long val) {
        int numBits = 0;
        while (val > 0){
            numBits++;
            val = val >> 1;
        }
        return numBits;
    }

    /**
     * This method takes a long value and splits it into bytes, each max 255. If the last byte is 255, then it
     * add an empty byte.
     * @param val
     * @return
     */
    public static byte[] splitLong(long val){
        int numBytesNeeded = (int) (val / 255);
        byte[] valBytes = new byte[numBytesNeeded + 1];
        int size = valBytes.length - 1;
        for(int i = 0; i < size; i++){
            valBytes[i] = (byte)(255);
            val -= 255;
        }
        valBytes[size] = (byte)(val);

        return valBytes;
    }

    /**
     * This method returns the bit value at a given position in a byte. Bit values are 0 or 1.
     * @param position Position of bit in byte (1-8)
     * @param bits     Byte holding bit to be checked
     * @return         Value of bit
     */
    public static int getBitAtPos(int position, byte bits){
        int mask = 1 << (position - 1);
        return ((bits & mask) >> position - 1);
    }

}



/*
    Notes:

    Buffer

    Buffer is a region of memory used to temporarily hold data while it is being moved from one place to another.
    A buffer is used, for example, when moving data between processes within a computer.

    A buffer can be thought of as a storage area when there is a difference in processing speed. For example,
    if the data being read is slower than the data being produced, then the data being produced can be placed in
    a buffer, ready for the reading to occur once the reader is ready.

    A ByteBuffer is a buffer which provides for transferring bytes from a source to a destination.
    ByteBuffer provides methods to directly store and restore the bytes of primitive types such as short.
    A FileChannel is used for transferring data to and from a file to a ByteBuffer.
 */



/*
    How to process the bits associated with the huffman coding:

    The bits can be stored as long values : long symbol1 = 0b1101000010011 which is 13 bits.
    Since bytes only come in the form of 2^(2+x), 4, 8, 16, etc., the encoded symbols with longer than a specified
    byte have to be moved into the next index of a byte array.

    This moving of extra bits can be done through a process called bit masking.

    "A mask defines which bits you want to keep, and which bits you want to clear.
    Masking is the act of applying a mask to a value. This is accomplished by doing:
        - Bitwise ANDing in order to extract a subset of the bits in the value.
        - Bitwise ORing in order to set a subset of bits in the value.
        - Bitwise XORing in order to toggle a subset of bits in the value.

    One of the steps we first have to conduct is deciding what byte size is desirable. For simplicity's sake,
    we will default to the standard 8 bits byte.

    Next step, conduct the huffman encoding and store the bits as long values?
    At the end of this step, each symbol's bit representation should be stored in a byte[].

    If we keep the bits as strings, it would take a long time to process them into bytes. Therefore, it is desirable
    to convert them into actual binary values. With long, this is possible.

    Since huffman is the final part before compressing the whole file, we will need to save a few things:
        - The actual byte values of the characters, so decompressing works.
        - The associated character frequency, so the huffman tree can be rebuilt.
        - The compressed file.
 */