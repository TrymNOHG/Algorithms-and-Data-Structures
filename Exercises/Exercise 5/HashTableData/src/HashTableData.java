import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

/**
 * @author Leon Egeberg Hesthaug, Eirik Elvestad, og Trym Hamer Gudvangen
 */
public class HashTableData {

    public static void main(String[] args) {

        textKeyExercise();

        intKeyExercise();


    }

    private static void textKeyExercise(){
        Scanner input = new Scanner(System.in);
        int numLines = 0;
        String[] words;
        try {
            FileHandler file = new FileHandler("navn");
            numLines = file.findNumLines();
            words = file.getWords();
        } catch (IOException e){
            throw new RuntimeException("There was a fault in the file you represented");
        }


        LinkedHashTable hashTable = new LinkedHashTable(numLines);

        for(String name: words){
            hashTable.add(name);
        }
        System.out.println("Antall kollisjoner: " + hashTable.getCollisions());
        System.out.println("Antall kollisjoner per person: " + hashTable.collPerPers());

        System.out.println("Lastfaktor: " + hashTable.findLoadFactor());


        System.out.println("Hvem ønsker du å finne?");
        String person = input.nextLine();

        System.out.println("Eksisterer denne personen i tabellen: " + hashTable.existsInTable(person));

        System.out.println("Hvem ønsker du å finne?");
        person = input.nextLine();
        System.out.println("Eksisterer denne personen i tabellen: " + hashTable.existsInTable(person) + "\n");
    }

    private static void intKeyExercise(){

        HashTable hashTable = new HashTable(10000000);
        HashMap<Integer, Integer> hashMap = new HashMap<>(10000000);
        NumberArray numberArray = new NumberArray(10000000, hashTable.getHashTableSize() * 100);


        long startTime = System.nanoTime();

        for(int randomNum : numberArray.getUnorderedArr()){
            hashTable.add(randomNum);
        }

        long endTime = System.nanoTime();
        final long ourTimeTaken = endTime - startTime;

        System.out.println("Antall kollisjoner: " + hashTable.getCollisions());
        System.out.println("Antall kollisjoner per person: " + hashTable.collisionPerPerson());

        System.out.println("Lastfaktor: " + hashTable.findLoadFactor());
        System.out.println("Tid for vår hash: " + ourTimeTaken/1000000 + " ms");

        startTime = System.nanoTime();
        for(int randomNum : numberArray.getUnorderedArr()){
            hashMap.put(randomNum, randomNum);
        }
        endTime = System.nanoTime();
        final long javaTimeTaken = endTime - startTime;

        System.out.println("Tid for Java HashMap: " + javaTimeTaken/1000000  + " ms");
        
    }


}

/**
 * Oppgave 1: Hashtabell med tekstnøkkel
 */
class LinkedHashTable{

    private int collisions = 0;
    private final DoubleLinkedList<?>[] hashTable;
    private final int numElements;

    public LinkedHashTable(int numInput) {
        this.hashTable = new DoubleLinkedList[nextPrime(numInput)];
        this.numElements = numInput;
    }

    public int hashObject(Object object){
        String objectString = object.toString();
        int hashVal = 0;
        for(int i = 0; i < objectString.length(); i++){
            hashVal = (hashVal + objectString.charAt(i)) * 11;
        }
        return hashVal;
    }

    public void add(Object obj){
        int hashedObject = hashObject(obj);

        int hashIndex = getHashTableIndex(hashedObject);

        if(hashTable[hashIndex] == null) this.hashTable[hashIndex] = new DoubleLinkedList<>();
        else this.collisions++;

        this.hashTable[hashIndex].addFirst(obj);
    }

    public Nodule get(Object obj){
        int hashCode = hashObject(obj);
        int hashIndex = getHashTableIndex(hashCode);

        DoubleLinkedList<?> wordList = this.hashTable[hashIndex];
        if(wordList == null) return null;
        return wordList.getByValue(obj);
    }

    public boolean existsInTable(String word){
        return this.get(word) != null;
    }


    private int nextPrime(int numSize){
        while(!isPrime(numSize)) numSize++;
        return numSize;
    }

    private boolean isPrime(int num){
        for(int i = 2; i < Math.sqrt(num) + 1; i++){
            if(num % i == 0) return false;
        }
        return true;
    }

    public int getHashTableIndex(int key){
        if (key % this.hashTable.length < 0) return (key % this.hashTable.length) + this.hashTable.length;
        return key % this.hashTable.length;
    }

    public int indicesFilled(){
        int indicesFilled = 0;
        for(DoubleLinkedList<?> list : this.hashTable){
            if(list != null) indicesFilled++;
        }
        return indicesFilled;
    }

    public double findLoadFactor(){
        return (double) indicesFilled()/ this.hashTable.length;
    }

    public double collPerPers(){
        return (double) collisions / numElements;
    }

    public int getCollisions() {
        return collisions;
    }

    public DoubleLinkedList<?>[] getHashTable() {
        return hashTable;
    }

    public int getNumElements() {
        return numElements;
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
        while(!node.getElement().equals(element)){
            if(node.getNext() == null) return null;
            node = node.getNext();

        }
        return node;
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

class FileHandler{

    private File file;
    private String[] words;
    private Path path;

    public FileHandler(String fileName) throws IOException {
        this.path = Path.of("src/" + fileName + ".txt");
        this.file = new File(String.valueOf(path));
        setStringArr();
        findNumLines();
    }

    public int findNumLines() throws IOException{
        int numLines = 0;
        try (BufferedReader reader = Files.newBufferedReader(this.path)) {
            while (reader.readLine() != null) numLines++;
        } catch (IOException e) {
            throw new IOException("There was a fault in the file you represented");
        }
        return numLines;
    }

    public void setStringArr() throws IOException {
        try(Scanner scanner = new Scanner(this.file)){
            this.words = new String[findNumLines()];
            int index = 0;
            while(scanner.hasNextLine()) this.words[index++] = scanner.nextLine();
        } catch (IOException e) {
            throw new IOException("There was a fault in the file you represented");
        }
    }

    public File getFile() {
        return this.file;
    }

    public String[] getWords() {
        return this.words;
    }

    public Path getPath() {
        return this.path;
    }
}

/**
 * Oppgave 2: Hashtabell med helltallsnøkkel
 */
class HashTable{

    private int collisions = 0;
    private final int hashTableSize;
    private final int[] hashTable;
    private final int numElements;

    public HashTable(int numInput) {
        this.hashTableSize = nextPrime((int) (numInput * 1.35)); // 35% overhead
        this.hashTable = new int[hashTableSize];
        this.numElements = numInput;
    }

    public int hashNumber(int number){
        if (number % this.hashTable.length < 0) return (number % this.hashTable.length) + this.hashTable.length;
        return number % this.hashTable.length;
    }

    public int hashNumber2(int number){
        if (number % this.hashTable.length < 0) return (number % (this.hashTable.length - 1)) + this.hashTable.length;
        return (number % (this.hashTable.length - 1)) + 1;
    }

    public void add(int number){

        int hashedNum = hashNumber(number);
        if(hashTable[hashedNum] == 0) this.hashTable[hashedNum] = number;
        else hashTable[probe(hashedNum)] = number;
    }

    private int probe(int number){
        int jumpVal = hashNumber2(number);
        while (this.hashTable[number] != 0){
            this.collisions++;
            number = hashNumber(number + jumpVal);
        }
        return number;
    }

    private int nextPrime(int numSize){
        while(!isPrime(numSize)) numSize++;
        return numSize;
    }

    private boolean isPrime(int num){
        for(int i = 2; i < Math.sqrt(num) + 1; i++){
            if(num % i == 0) return false;
        }
        return true;
    }

    public double findLoadFactor(){
        return (double) numElements/ this.hashTable.length;
    }

    public double collisionPerPerson(){
        return (double) collisions / numElements;
    }

    public int getCollisions() {
        return collisions;
    }

    public int getHashTableSize() {
        return hashTableSize;
    }

    public int getNumElements() {
        return numElements;
    }
}

/**
 * En mer generell dobbel hash metode for objekter
 */
class GeneralHashTable{

    private int collisions = 0;
    private final int hashTableSize;
    private final Object[] hashTable;
    private final int numElements;

    public GeneralHashTable(int numInput) {
        this.hashTableSize = nextPrime((int) (numInput * 1.3)); // 25% overhead
        this.hashTable = new Object[hashTableSize];
        this.numElements = numInput;
    }

    public int hashObject(Object object){
        String objectString = object.toString();
        int hashVal = 0;
        for(int i = 0; i < objectString.length(); i++){
            hashVal = (hashVal + objectString.charAt(i)) * 11;
        }
        return hashVal;
    }

    public int hashObject2(Object object){
        String objectString = object.toString();
        if(Integer.valueOf(objectString) == 0 ||Integer.valueOf(objectString) == this.hashTable.length){
            objectString = String.valueOf(this.hashTable.length/2);
        }
        int hashVal = 0;
        for(int i = 0; i < objectString.length(); i++){
            hashVal = (hashVal + objectString.charAt(i)) * 43;
        }
        return hashVal;
    }

    public void add(Object obj){
        int hashedObject = hashObject(obj);

        int hashIndex = getHashTableIndex(hashedObject);

        if(hashTable[hashIndex] == null) this.hashTable[hashIndex] = obj;
        else hashTable[probe(hashIndex, obj)] = obj;
    }

    private int probe(int index, Object obj){
        int jumpVal = hashObject2(obj);
        while (this.hashTable[index] != null){
            this.collisions++;
            index = getHashTableIndex(index + jumpVal);
        }
        return index;
    }


    private int nextPrime(int numSize){
        while(!isPrime(numSize)) numSize++;
        return numSize;
    }

    private boolean isPrime(int num){
        for(int i = 2; i < Math.sqrt(num) + 1; i++){
            if(num % i == 0) return false;
        }
        return true;
    }

    public int getHashTableIndex(int key){
        if (key % this.hashTable.length < 0) return (key % this.hashTable.length) + this.hashTable.length;
        return key % this.hashTable.length;
    }

    public double findLoadFactor(){
        return (double) numElements/ this.hashTable.length;
    }

    public double collisionPerPerson(){
        return (double) collisions / numElements;
    }

    public int getCollisions() {
        return collisions;
    }

    public int getHashTableSize() {
        return hashTableSize;
    }

    public int getNumElements() {
        return numElements;
    }
}

/**
 * This class handles the creation of a random, unordered array of numbers.
 */
class NumberArray{
    private final Random random = new Random(System.currentTimeMillis());
    private int[] unorderedArr;
    final private int sumOfArrayValuesBefore;

    public NumberArray(int sizeOfArr, int randomBound){
        createRandomArr(sizeOfArr, randomBound);
        this.sumOfArrayValuesBefore = getSumOfArrayValues();
    }

    /**
     * This method fills the unordered array with random numbers (0 to 100) with a specified number of indices.
     * @param sizeOfArr The desired length of the array, represented as an int.
     */
    public void createRandomArr(int sizeOfArr, int randomBound){
        this.unorderedArr = new int[sizeOfArr];
        for (int i = 0; i < sizeOfArr; i++) {
            this.unorderedArr[i] = random.nextInt(1, randomBound);
        }
    }

    /**
     * This method checks if the input array is properly sorted from lowest to highest value.
     * @return    If the array has an index bigger than the preceding index it returns {@code false}, else {@code true}.
     */
    public boolean isArrOrdered() {
        for(int i = 0; i < this.unorderedArr.length - 1; i++){
            if(this.unorderedArr[i] > this.unorderedArr[i+1]){
                System.out.println("The array is not sorted and this test has therefore failed.");
                return false;
            }
        }
        System.out.println("The array is sorted and this test was successful!");
        return true;
    }

    /**
     * This method returns the sum of all the elements of the object array.
     * @return Sum of the elements of the given array, represented as an int.
     */
    public int getSumOfArrayValues() {
        int sumOfElements = 0;
        for(int element : this.unorderedArr){
            sumOfElements += element;
        }
        return sumOfElements;
    }

    public int[] getUnorderedArr() {
        return unorderedArr;
    }

    public int getSumOfArrayValuesBefore() {
        return sumOfArrayValuesBefore;
    }
}


/*
    Best result from prime factors 11 and 43:

    Number of collisions: 10151586
    Number of collisions per number: 1.0151586
    Lastfaktor: 0.7999998080000461

 */