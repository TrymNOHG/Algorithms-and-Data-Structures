import java.io.*;
import java.util.*;

public class GraphData {

    public static void main(String[] args) throws IOException {
        Scanner input = new Scanner(System.in);
        String file = input.nextLine();
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(
                        Objects.requireNonNull(GraphData.class.getResourceAsStream(file))));
        Graph graph = new Graph();
        graph.newGraph(reader);

        System.out.println("Information about the graph from " + file);
        TreeGraph treeGraph = new TreeGraph(graph);

        treeGraph.DFSTrees();
        treeGraph.printSortedList(treeGraph.vertices);
        treeGraph.printComponents("");

        BufferedReader reader2 =
                new BufferedReader(new InputStreamReader(
                        Objects.requireNonNull(GraphData.class.getResourceAsStream(file))));

        Graph graphReverse = new Graph();
        graphReverse.graphTranspose(reader2);


        TreeGraph treeGraphReverse = new TreeGraph(graphReverse);

        treeGraphReverse.DFSTrees(treeGraph.sortByFinishTime(treeGraph.vertices));

        System.out.println("\nInformation about the transposed graph: ");
        treeGraphReverse.printSortedList(treeGraphReverse.vertices);
//        treeGraphReverse.printAllTrees();


        treeGraphReverse.printComponents(" strong");

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
    Object element;
    public Vertex(int id){
        this.id = id;
    }
    public Object getElement(){
        return element;
    }

    public int getId(){
        return id;
    }

}


class TreeGraph extends Graph {
    int numVertex, numEdges;
    Vertex[] vertices;
    boolean[] nodeVisited;
    final String SEPARATOR = "---------------------------------";

    LinkedList<List<Integer>> strongComponents = new LinkedList<>();

    public TreeGraph(Graph graph) {
        vertices = graph.vertices;
        numVertex = graph.numVertex;
        nodeVisited = new boolean[numVertex];
//        numEdges = numVertex - 1;
        DFSInit();
        //TODO: add numEdges
    }

    /**
     * 
     * @param vertices
     * @return
     */
    public Vertex[] sortByFinishTime(Vertex[] vertices){
        dualPivotQuickSort(vertices, 0, this.numVertex - 1);
        return vertices;
    }

    public LinkedList<Vertex[]> DFSTrees(){
        return DFSTrees(this.vertices);
    }


    public LinkedList<Vertex[]> DFSTrees(Vertex[] vertices){
        strongComponents.clear();

        LinkedList<Vertex[]> dfsTrees = new LinkedList<>();

        for(Vertex vertex : vertices){
            if(!nodeVisited[vertex.id]){
                ((DFSPrev)this.vertices[vertex.id].element).distance = 0;
                strongComponents.addLast(new ArrayList<>());
                DFSearch(this.vertices[vertex.id]);
                dfsTrees.add(this.deepCopyTree());
            }
        }
        return dfsTrees;
    }

    @Deprecated
    public void DFS(Vertex vertex){
        DFSInit();
        for(Vertex vertex1 : vertices){
          ((DFSPrev)vertex.element).distance = 0;
          DFSearch(vertex);
        }

    }

    public void DFSInit(){
        for(int i = numVertex; i-->0;){
            vertices[i].element = new DFSPrev();
        }
        DFSPrev.nullTime();
    }

    public void DFSearch(Vertex currentVertex){
        DFSPrev vertexElement = (DFSPrev)currentVertex.element;
        vertexElement.foundTime = DFSPrev.findTime();
        strongComponents.getLast().add(currentVertex.id);
        nodeVisited[currentVertex.id] = true;
        for(Edge e = currentVertex.edge; e != null; e = e.next){
            DFSPrev nextElement = (DFSPrev)e.to.element;
            if(nextElement.foundTime == 0){
                nodeVisited[e.to.id] = true;
                nextElement.previous = currentVertex;
                nextElement.distance = vertexElement.distance + 1;
                DFSearch(e.to);
            }
        }
        vertexElement.finishTime = DFSPrev.findTime();

    }

    public void printSortedList(Vertex[] vertices){
        System.out.println(SEPARATOR);
        System.out.println("List of nodes sorted by finish time: ");
        System.out.println("\nNode\tFinish Time");
        for(Vertex v : sortByFinishTime(vertices)){
            System.out.println(v.id + "\t\t\t" + ((DFSPrev)v.element).finishTime);
        }
        System.out.println("\n" + this.numVertex + " total nodes");
        System.out.println(SEPARATOR);
    }

//    public void printAllTrees(){
//        for(Vertex[] tree : this.trees){
//            printSortedList(tree);
//        }
//    }

    public void printComponents(String adjective){
        System.out.println(SEPARATOR);
        System.out.println("The" + adjective + " components of this graph are as follows: ");
        for(List<Integer> component : this.strongComponents){
            System.out.println(component);
        }
        System.out.println(SEPARATOR);
    }

    public Vertex[] deepCopyTree(){
        Vertex[] copiedTree = new Vertex[this.numVertex];
        System.arraycopy(this.vertices, 0, copiedTree, 0, this.numVertex);
        return copiedTree;
    }

    /**
     * Dual pivot
     * @param arr
     * @param low
     * @param high
     */
    private void dualPivotQuickSort(Vertex[] arr,
                                    int low, int high)
    {
        if (low < high)
        {

            // piv[] stores left pivot and right pivot.
            // piv[0] means left pivot and
            // piv[1] means right pivot
            int[] piv;
            piv = partition(arr, low, high);

            dualPivotQuickSort(arr, low, piv[0] - 1);
            dualPivotQuickSort(arr, piv[0] + 1, piv[1] - 1);
            dualPivotQuickSort(arr, piv[1] + 1, high);
        }
    }

    private int[] partition(Vertex[] arr, int low, int high) {
        if (((DFSPrev)arr[low].element).finishTime < ((DFSPrev)arr[high].element).finishTime)
            swap(arr, low, high);

        // p is the left pivot, and q
        // is the right pivot.
        int j = low + 1;
        int g = high - 1, k = low + 1,
                p = ((DFSPrev)arr[low].element).finishTime, q = ((DFSPrev)arr[high].element).finishTime;

        while (k <= g)
        {

            // If elements are less than the left pivot
            if (((DFSPrev)arr[k].element).finishTime > p)
            {
                swap(arr, k, j);
                j++;
            }

            // If elements are greater than or equal
            // to the right pivot
            else if (((DFSPrev)arr[k].element).finishTime <= q)
            {
                while (((DFSPrev)arr[g].element).finishTime < q && k < g)
                    g--;

                swap(arr, k, g);
                g--;

                if (((DFSPrev)arr[k].element).finishTime > p)
                {
                    swap(arr, k, j);
                    j++;
                }
            }
            k++;
        }
        j--;
        g++;

        // Bring pivots to their appropriate positions.
        swap(arr, low, j);
        swap(arr, high, g);

        // Returning the indices of the pivots
        // because we cannot return two elements
        // from a function, we do that using an array.
        return new int[] { j, g };
    }

    // Java program to implement
// dual pivot QuickSort
    private void swap(Vertex[] arr, int i, int j)
    {
        Vertex temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
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

        for (int i = 0; i < numEdge; ++i){
            st = new StringTokenizer(br.readLine());

            int fromNode = Integer.parseInt(st.nextToken());
            int toNode = Integer.parseInt(st.nextToken());
            vertices[fromNode].id = fromNode;

            vertices[fromNode].edge = new Edge(vertices[toNode], vertices[fromNode].edge);
        }
    }

     public void graphTranspose(BufferedReader br) throws IOException {
         StringTokenizer st = new StringTokenizer(br.readLine());
         numVertex = Integer.parseInt(st.nextToken());
         numEdge = Integer.parseInt(st.nextToken());

         vertices = new Vertex[numVertex];

         for(int i = 0; i < numVertex;++i) vertices[i] = new Vertex(i);

         for (int i = 0; i < numEdge; ++i){
             st = new StringTokenizer(br.readLine());

             int toNode = Integer.parseInt(st.nextToken());
             int fromNode = Integer.parseInt(st.nextToken());
             vertices[toNode].id = toNode;

             vertices[fromNode].edge = new Edge(vertices[toNode], vertices[fromNode].edge);
         }
     }

}

 class Previous{
    int distance;
    Vertex previous;
    static int infinity = 1000000000;
    public int getDistance(){return distance;}
    public Vertex getPrevious(){return previous;}

    public Previous(){
        distance = infinity;
    }
}

 class DFSPrev extends Previous{
    int foundTime, finishTime;
    static int time;
    static void nullTime(){time = 0;}
    static int findTime(){return ++time;}
}



