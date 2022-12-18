import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

/**
 *
 *
 * @author Leon Egeberg Hesthaug, Eirik Elvestad, og Trym Hamer Gudvangen
 */
public class CompressData {

    public static void main(String[] args) throws URISyntaxException, IOException {

        Scanner input = new Scanner(System.in);
        System.out.println("input file name: name.extension");
        String fileName = input.nextLine();
        URL resourceUrl1 = CompressData.class.getResource(fileName);
        File fileIn = new File(resourceUrl1.toURI());

        System.out.println("Input 1 for compression");
        System.out.println("Input 2 for unpacking");
        int alternative = input.nextInt();
        input.nextLine();

        if(alternative == 1){
            File fileOut = File.createTempFile(fileIn.getName() + "Compressed", ".txt", new File(fileIn.getParent()));
            File fileOutFinal = File.createTempFile(fileIn.getName() + "CompressedFinal", ".txt", new File(fileIn.getParent()));
            CompressFile.compress(fileIn, fileOut, fileOutFinal);
            fileOut.deleteOnExit();
        }
        else if(alternative == 2){
            System.out.println("Input desired output file type in this format: .pdf");
            String extension = input.nextLine();
            File unpackedFile = File.createTempFile(fileIn.getName() + "Unpacked", ".txt", new File(fileIn.getParent()));
            File unpackedFileFinal = File.createTempFile(fileIn.getName() + "UnpackedFinal", extension, new File(fileIn.getParent()));
            DecompressFile.decompress(fileIn, unpackedFile, unpackedFileFinal);
            unpackedFile.deleteOnExit();
        }
        else {
            System.out.println("Wrong input");
            System.exit(0);
        }

    }
}

class DecompressFile {
    public static void decompress(File fileIn, File fileOut, File fileOutFinal) throws IOException {
        Huffman.decompressData(fileIn, fileOut);
        try {
            LempelZiv.lzDecomp(fileOut, fileOutFinal);
        } catch (Exception e){

        }
    }

}

class CompressFile {
    public static void compress(File fileIn, File fileOut, File fileOutFinal) throws IOException {
        LempelZiv.lz(fileIn, fileOut);
        Huffman.compressData(fileOut, fileOutFinal);

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

class Huffman {
    static long totalSymbols = 0;
    public static void compressData(File file, File outputFile) throws IOException {
        DataInputStream dataIn = new DataInputStream(new FileInputStream(file));

        int[] symbols = new int[256];
        int totData = dataIn.available();

        for (int i = 0; i < totData; ++i) {
            int symb = dataIn.read();
            symbols[symb]++;
        }

        dataIn.close();

        PriorityQueue<Node> heap = new PriorityQueue<>(256, Comparator.comparingInt(node -> node.symbols));
        heap.addAll(createNodeList(symbols));

        Node tree = Node.huffTree(heap);
        tree.printOut(tree, "");

        FileInputStream fileIn = new FileInputStream(file);
        DataOutputStream compressedData = new DataOutputStream(new FileOutputStream(outputFile));

        //Write all the symbols' frequencies: first symb = char for [00000000] and holds the frequency
        for (int symbol : symbols) {
            compressedData.writeInt(symbol);
        }

        int bitstring;
        int j;
        int k = 0;
        long writeByte = 0L;
        List<Byte> bytes = new ArrayList<>();

        //Actually add all the characters from the text, through compromising using the huffman tree.
        for (int i = 0; i < totData; i++) {

            bitstring = Math.abs(fileIn.read()); //Bit-value of symbol
            String bitString = tree.bitString[bitstring]; //Get the associated huffman code.

            j = 0;

            while (j < bitString.length()) { //For every bit in the bitString

                if (bitString.charAt(j) == '1'){ //If the first bit is one
                    writeByte = ((writeByte << 1) | 1); //Then, Add one to the next bit of the long writeByte.
                }
                else{
                    writeByte = (writeByte << 1); //Else, add a zero
                }

                ++k;
                ++j;

                if (k == 8) { //Once writeByte has 8 bits
                    bytes.add((byte) writeByte); //then add a new Byte to the bytes array.
                    writeByte = 0L; //Restart the byte.
                    k = 0;          //Restart the length
                }
            }
        }


        while (k != 0 && k < 8) {
            k++;
            writeByte = (writeByte << 1);
        }

        bytes.add((byte) writeByte); //Add the final byte after adding trailing zeroes to the end.

        for (Byte bits : bytes) {
            compressedData.write(bits); //frequency -> final byte -> symbols -> final byte
        }

        fileIn.close();
        compressedData.close();

    }

    public static void decompressData(File inputFile, File outputFile) throws IOException {
        DataInputStream compressedData = new DataInputStream(new FileInputStream(inputFile));
        int[] symbols = new int[256];

        for (int i = 0; i < symbols.length; i++) {
            int frequency = compressedData.readInt();
            symbols[i] = frequency;
            totalSymbols += frequency;
        }


        PriorityQueue<Node> heap = new PriorityQueue<>(256, Comparator.comparingInt(node -> node.symbols));
        heap.addAll(createNodeList(symbols));
        Node tree = Node.huffTree(heap);

        DataOutputStream decompressedData = new DataOutputStream(new FileOutputStream(outputFile));

        byte currentByte;
        byte[] bytes = compressedData.readAllBytes();

        compressedData.close();


        int amountBytes = bytes.length;
        BitString bitString = new BitString(0, 0);

        for (int i = 0; i < amountBytes; i++) {
            currentByte = bytes[i];
            BitString byteString = new BitString(8, currentByte);
            bitString = BitString.combine(bitString, byteString);
            writeChar(tree, bitString, decompressedData);
        }

        compressedData.close();
        decompressedData.flush();
        decompressedData.close();
    }

    private static List<Node> createNodeList(int[] symbols) {
        List<Node> nodeList = new ArrayList<>();

        for (int i = 0; i < symbols.length; i++) {
            if (symbols[i] != 0) {
                nodeList.add(new Node((char) i, symbols[i], null, null));
            }
        }
        return nodeList;
    }

    private static void writeChar(Node tree, BitString bitString, DataOutputStream decompressedData) throws IOException {
        Node tempTree = tree;
        int i = 0;
        for (long j = 1L << bitString.length - 1; j != 0; j >>= 1) {
            i++;

            if ((bitString.bits & j) == 0){
                tempTree = tempTree.left;
            }
            else {
                tempTree = tempTree.right;
            }

            if (tempTree.isLeafNode()) {
                long charac = tempTree.letter;
                decompressedData.write((byte) charac);
                long tempLong = ~(0L);
                bitString.bits = (bitString.bits & tempLong); //Reset bits
                bitString.length = bitString.length - i; //
                i = 0;
                tempTree = tree;
                if(--totalSymbols == 0) {
                    return;
                }
            }
        }
    }
}


class Node{
    char letter;
    String [] bitString;
    Node left;
    Node right;
    int symbols;


    public Node(char character, int i, Node left, Node right){
        this.letter = character;
        bitString = new String[256];
        this.left = left;
        this.right = right;
        this.symbols = i;

    }
    public Node(){
        bitString = new String[256];
    }

    private static int nodeSum(Node node1, Node node2){
        return node1.symbols + node2.symbols;
    }

    public boolean isLeafNode(){
        return this.left == null;
    }

    public static Node huffTree(PriorityQueue<Node> heap){
        Node tree = new Node();

        while(heap.size() > 1){
            Node first = heap.poll();
            Node second = heap.poll();
            Node end = new Node('\0',nodeSum(first, second), second, first);
            heap.add(end);
            tree = end;
        }
        return tree;

    }

    public void printOut(Node root, String string) {
        if (root.left != null && root.right != null) {

            printOut(root.left, string+"0");
            printOut(root.right, string+"1");

        }
        else {
            bitString[root.letter] = string;
        }

    }
}

class BitString {
    int length;
    long bits;

    public BitString() {
    }

    public BitString(int len, long bits) {
        this.length = len;
        this.bits = bits;
    }

    public BitString(int len, byte bits) {
        this.length = len;
        this.bits = convertByteToLong(bits, len);
    }

    public static BitString combine(BitString s1, BitString s2) {
        BitString newByte = new BitString();
        newByte.length = s1.length + s2.length;
        if (newByte.length > 64) {
            System.out.println("For lang bitstreng, gÃ¥r ikke!");
            return null;
        }
        newByte.bits = s2.bits | (s1.bits << s2.length);
        return newByte;
    }

    public long convertByteToLong(byte bits, int length) { //Bring other method
        long result = 0;
        for (long i = 1L << length - 1; i != 0; i >>= 1) {
            if ((bits & i) == 0) {
                result = (result << 1);
            } else result = ((result << 1) | 1);
        }
        return result;
    }

}
