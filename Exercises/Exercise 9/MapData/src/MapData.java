import java.io.*;
import java.util.*;

/**
 * @author Leon Egeberg Hesthaug, Eirik Elvestad, and Trym Hamer Gudvangen
 */
public class MapData {

    public static void main(String[] args) throws IOException {
        Scanner input = new Scanner(System.in);

        System.out.println("Node file: ");
        String fileVert = input.nextLine();

        System.out.println("Edge file: ");
        String fileEdge = input.nextLine();

        BufferedReader readerVert =
                new BufferedReader(new InputStreamReader(
                        Objects.requireNonNull(MapData.class.getResourceAsStream(fileVert))));
        BufferedReader readerEdge =
                new BufferedReader(new InputStreamReader(
                        Objects.requireNonNull(MapData.class.getResourceAsStream(fileEdge))));

        Graph graph = new Graph();
        graph.newGraph(readerVert, readerEdge);
        readerVert.close();
        readerEdge.close();

        //TODO: while loop so that you don't have to read from file every time.

        while (true){
        System.out.println("1. Find path");
        System.out.println("2. Find 8 closest landmarks");
        System.out.println("3. Preprocess for ALT algorithm");

        int choice = input.nextInt();
        input.nextLine();

        switch (choice) {
            case 1 -> {
                System.out.println("From location/node id: ");
                int locationFrom = input.nextInt();
                System.out.println("To location/node id: ");
                int locationTo = input.nextInt();
                System.out.println("1. Dijkstra");
                System.out.println("2. ALT");
                int type = input.nextInt();

                if (type == 1) {
                    //Dijkstra
                    long startTime = System.nanoTime();
                    int nodesPicked = graph.dijkstra(locationFrom);
                    long endTime = System.nanoTime();
                    double time = (((double) endTime - (double) startTime) / (double) 1000000000);
                    System.out.println(graph.getCoordinatesFromPath(locationTo));
                    int pathTime = ((Previous) graph.vertices[locationTo].nodeData).distance / 100;
                    int hours = pathTime / 3600;
                    int minutes = (pathTime % 3600) / 60;
                    int seconds = pathTime % 60;
                    System.out.println("The route takes: " + hours + " hours " + minutes + " minutes and " + seconds + " seconds");
                    System.out.println("Algorithm time in seconds: " + time);
                    System.out.println("Nodes picked from queue: " + nodesPicked);
                } else if (type == 2) {
                    //ALT
                    //read table files:
                    input.nextLine();
                    System.out.println("fromLandmark file: ");
                    String fromLand = input.nextLine();
                    BufferedReader readerfromLand =
                            new BufferedReader(new InputStreamReader(
                                    Objects.requireNonNull(MapData.class.getResourceAsStream(fromLand))));
                    StringTokenizer fromSt = new StringTokenizer(readerfromLand.readLine());
                    int numVertex = Integer.parseInt(fromSt.nextToken());
                    int numLands = Integer.parseInt(fromSt.nextToken());
                    int[][] fromTable = new int[numVertex][numLands];

                    for (int i = 0; i < numVertex; i++) {
                        fromSt = new StringTokenizer(readerfromLand.readLine());
                        for (int j = 0; j < numLands; j++) {
                            int dist = Integer.parseInt(fromSt.nextToken());
                            fromTable[i][j] = dist;
                        }
                    }

                    System.out.println("toLandmark file: ");
                    String toLand = input.nextLine();
                    BufferedReader readerToLand =
                            new BufferedReader(new InputStreamReader(
                                    Objects.requireNonNull(MapData.class.getResourceAsStream(toLand))));
                    StringTokenizer toSt = new StringTokenizer(readerToLand.readLine());
                    int numVertex2 = Integer.parseInt(toSt.nextToken());
                    int numLands2 = Integer.parseInt(toSt.nextToken());
                    int[][] toTable = new int[numVertex2][numLands2];

                    for (int i = 0; i < numVertex2; i++) {
                        toSt = new StringTokenizer(readerToLand.readLine());
                        for (int j = 0; j < numLands2; j++) {
                            int dist = Integer.parseInt(toSt.nextToken());
                            toTable[i][j] = dist;
                        }
                    }
                    long startTime = System.nanoTime();
                    int nodesPicked = graph.ALT(fromTable, toTable, locationFrom, locationTo);
                    long endTime = System.nanoTime();
                    double time = (((double) endTime - (double) startTime) / (double) 1000000000);
                    System.out.println(graph.getCoordinatesFromPath(locationTo));
                    int pathTime = ((Previous) graph.vertices[locationTo].nodeData).distance / 100;
                    int hours = pathTime / 3600;
                    int minutes = (pathTime % 3600) / 60;
                    int seconds = pathTime % 60;
                    System.out.println("The route takes: " + hours + " hours " + minutes + " minutes and " + seconds + " seconds");
                    System.out.println("Algorithm time in seconds: " + time);
                    System.out.println("Nodes picked from queue: " + nodesPicked);


                }
            }
            case 2 -> {
                //Closest 8 landmarks to a location:
                System.out.println("Landmarks file: ");
                String fileLand = input.nextLine();
                BufferedReader readerLand =
                        new BufferedReader(new InputStreamReader(
                                Objects.requireNonNull(MapData.class.getResourceAsStream(fileLand))));
                System.out.println("Location/node id: ");
                int loc = input.nextInt();
                System.out.println(""" 
                        1 Stedsnavn
                         
                        2 Bensinstasjon
                         
                        4 Ladestasjon
                         
                        8 Spisested
                                                             
                        16 Drikkested
                                                             
                        32 Overnattingssted 
                        """);
                int landType = input.nextInt();
                long startTime = System.nanoTime();
                Vertex[] rest = graph.closest8(loc, landType, readerLand);
                long endTime = System.nanoTime();
                double time = (((double) endTime - (double) startTime) / (double) 1000000000);
                System.out.println("Algorithm time in seconds: " + time);
                for (Vertex v : rest) {
                    System.out.println(v.latitude + ", " + v.longitude + "\n");
                }
            }
            case 3 -> {
                //Make preprocessed landmark table for future use in ALT:
                int[] landmarksId = {3007953, 448362, 3781862};
                int[][] fromLandmarkTable = graph.landmarkTable(landmarksId);
                BufferedReader readerVert2 =
                        new BufferedReader(new InputStreamReader(
                                Objects.requireNonNull(MapData.class.getResourceAsStream(fileVert))));
                BufferedReader readerEdge2 =
                        new BufferedReader(new InputStreamReader(
                                Objects.requireNonNull(MapData.class.getResourceAsStream(fileEdge))));

                File fromLandmarksFile = File.createTempFile("fromLandmarks", ".txt", new File(System.getProperty("user.dir")));
                BufferedWriter buffer = new BufferedWriter(new FileWriter(fromLandmarksFile));
                buffer.write(graph.vertices.length + " " + landmarksId.length);
                buffer.newLine();
                System.out.println(fromLandmarkTable.length);
                for (int[] ints : fromLandmarkTable) {
                    for (int j = 0; j < landmarksId.length; j++) {
                        buffer.write(ints[j] + " ");
                    }
                    buffer.newLine();
                }


                buffer.close();

                graph = new Graph();
                graph.newGraphReverse(readerVert2, readerEdge2);
                int[][] toLandmarkTable = graph.landmarkTable(landmarksId);
                File toLandmarksFile = File.createTempFile("toLandmarks", ".txt", new File(System.getProperty("user.dir")));
                BufferedWriter buffer2 = new BufferedWriter(new FileWriter(toLandmarksFile));
                buffer2.write(graph.vertices.length + " " + landmarksId.length);
                buffer2.newLine();
                for (int i = 0; i < toLandmarkTable.length; i++) {
                    for (int j = 0; j < landmarksId.length; j++) {
                        buffer2.write(toLandmarkTable[i][j] + " ");
                    }
                    buffer2.newLine();
                }
                buffer2.close();
            }
        }
    }
    }
}


class Graph {
    int numVertex, numEdge;
    Vertex[] vertices;

    public void newGraph(BufferedReader vertBr, BufferedReader edgeBr) throws IOException {
        StringTokenizer vertSt = new StringTokenizer(vertBr.readLine());
        numVertex = Integer.parseInt(vertSt.nextToken());

        vertices = new Vertex[numVertex];

        for (int i = 0; i < numVertex; ++i){
            vertSt = new StringTokenizer(vertBr.readLine());

            int id = Integer.parseInt(vertSt.nextToken());
            double latitude = Double.parseDouble(vertSt.nextToken());
            double longitude = Double.parseDouble(vertSt.nextToken());
            vertices[i] = new Vertex(id, latitude, longitude);
        }

        initNodeDate(vertices);

        StringTokenizer edgeSt = new StringTokenizer(edgeBr.readLine());
        numEdge = Integer.parseInt(edgeSt.nextToken());

        for (int i = 0; i < numEdge; ++i){
            edgeSt = new StringTokenizer(edgeBr.readLine());

            int fromNode = Integer.parseInt(edgeSt.nextToken());
            int toNode = Integer.parseInt(edgeSt.nextToken());
            int weight = Integer.parseInt(edgeSt.nextToken());
            ((Previous)vertices[fromNode].nodeData).distance = fromNode;

            vertices[fromNode].edge = new WeightedEdge(vertices[toNode], vertices[fromNode].edge, weight);
        }
    }

    public void newGraphReverse(BufferedReader vertBr, BufferedReader edgeBr) throws IOException {
        StringTokenizer vertSt = new StringTokenizer(vertBr.readLine());
        numVertex = Integer.parseInt(vertSt.nextToken());

        vertices = new Vertex[numVertex];

        for (int i = 0; i < numVertex; ++i){
            vertSt = new StringTokenizer(vertBr.readLine());

            int id = Integer.parseInt(vertSt.nextToken());
            double latitude = Double.parseDouble(vertSt.nextToken());
            double longitude = Double.parseDouble(vertSt.nextToken());
            vertices[i] = new Vertex(id, latitude, longitude);
        }

        initNodeDate(vertices);

        StringTokenizer edgeSt = new StringTokenizer(edgeBr.readLine());
        numEdge = Integer.parseInt(edgeSt.nextToken());

        for (int i = 0; i < numEdge; ++i){
            edgeSt = new StringTokenizer(edgeBr.readLine());

            int toNode = Integer.parseInt(edgeSt.nextToken());
            int fromNode = Integer.parseInt(edgeSt.nextToken());
            int weight = Integer.parseInt(edgeSt.nextToken());
            ((Previous)vertices[fromNode].nodeData).distance = fromNode;

            vertices[fromNode].edge = new WeightedEdge(vertices[toNode], vertices[fromNode].edge, weight);
        }
    }

    public Vertex[] closest8(int location, int type,  BufferedReader reader) throws IOException {
        Map<Integer, Integer> landmarks = new HashMap<>();
        Vertex[] closest = new Vertex[8];

        Comparator<Vertex> Comp = Comparator.comparingInt(o -> ((Previous) o.nodeData).distance);
        TreeSet<Vertex> set = new TreeSet<>(Comp);

        StringTokenizer st = new StringTokenizer(reader.readLine());
        numVertex = Integer.parseInt(st.nextToken());

        for (int i = 0; i < numVertex; ++i) {
            st = new StringTokenizer(reader.readLine());

            int id = Integer.parseInt(st.nextToken());
            int code = Integer.parseInt(st.nextToken());
            landmarks.put(id, code);
        }

        dijkstra(location);

        for (Map.Entry<Integer, Integer> code : landmarks.entrySet()){
            if((code.getValue() & type) == type){
                Vertex match = vertices[code.getKey()];
                set.add(match);
            }

        }

        for (int i = 0; i < 8; i++){
            closest[i] = set.pollFirst();
        }
        return closest;

    }

    public void initNodeDate(Vertex[] vertices){
        for(Vertex vertex : vertices){
            vertex.nodeData = new Previous();
        }
    }

    public int dijkstra(int id){
        int count = 0;
        initPrev(vertices[id]);
        PriorityQueue<Vertex> priQue = new PriorityQueue<>(10,
                Comparator.comparingInt(o -> ((Previous) o.nodeData).distance));
        priQue.add(vertices[id]);
        for(int i = numVertex; i > 1; i--){
            if(priQue.isEmpty()) break;
            Vertex vertex = priQue.poll();
            count++;
            for(WeightedEdge edge = (WeightedEdge)vertex.edge; edge != null; edge = (WeightedEdge) edge.next){
                boolean opt = optimizeDist(vertex, edge);
                if(opt){ //Increasing priority of min heap (with PriorityQueue)
                    priQue.remove(edge.to);
                    priQue.add(edge.to);
                }
            }
        }
        return count;
    }

    public int ALT(int[][] fromLandmark, int[][] toLandmark, int startNodeId, int endNodeId){
        int nodesPicked = 0;
        boolean reachedGoal = false;

        initPrev(vertices[startNodeId]);
        PriorityQueue<Vertex> priQue = new PriorityQueue<>(10,
                Comparator.comparingInt(vertex ->
                        ((Previous) vertex.nodeData).distance + ((Previous) vertex.nodeData).distFromGoal));

        priQue.add(vertices[startNodeId]);

        while (!reachedGoal){
            if(priQue.isEmpty()) break;
            Vertex vertex = priQue.poll();
//            System.out.println(vertex.latitude + ", " + vertex.longitude);
            nodesPicked++;
            if(vertex.id == endNodeId) break;
            //if the vertex found is the goal node, break;
            for(WeightedEdge edge = (WeightedEdge)vertex.edge; edge != null; edge = (WeightedEdge) edge.next){

                if(((Previous)edge.to.nodeData).distFromGoal == Previous.infinity){ //Is just set one time for each node.
                        estimateDistFromGoal(fromLandmark, toLandmark, edge.to, endNodeId);
                        priQue.add(edge.to);
                }
                boolean opt = optimizeDist(vertex, edge);
                if(opt){ //Increasing priority of min heap (with PriorityQueue)
                    priQue.remove(edge.to);
                    priQue.add(edge.to);
                }

            }
        }

        return nodesPicked;
    }

    /**
     * This is the calculation for finding the estimated distance from a given node to the end node.
     * @param fromLandmark
     * @param toLandmark
     * @param vertex
     * @param goalNodeNr
     * @return
     */
    private void estimateDistFromGoal(int[][] fromLandmark, int[][] toLandmark, Vertex vertex, int goalNodeNr){
        int totalBestEst = 0;
        for(int i = 0; i < fromLandmark[0].length; i++){
            int landmarkEst = fromLandmark[goalNodeNr][i] - fromLandmark[vertex.id][i];
            if(landmarkEst < 0) landmarkEst = 0;
            int landmarkEst2 = toLandmark[vertex.id][i] - toLandmark[goalNodeNr][i];
            if(landmarkEst2 < 0) landmarkEst2 = 0;

            int landmarkBestEst = Math.max(landmarkEst, landmarkEst2);
            totalBestEst = Math.max(totalBestEst, landmarkBestEst);
        }
        ((Previous)vertex.nodeData).distFromGoal =  totalBestEst;

    }



    /**
     * A table is created with the distance from or to a landmark from a given node. The table has the format:
     * int[node nr][landmark nr]
     * @param landmarksId
     * @return
     */
    public int[][] landmarkTable(int[] landmarksId){
        int[][] table = new int[vertices.length][landmarksId.length];

        for(int i = 0; i < landmarksId.length; i++){
            dijkstra(landmarksId[i]);
            for(int j = 0; j < vertices.length; j++){
                table[j][i] = ((Previous)vertices[j].nodeData).distance;
            }
        }
        return table;
    }

    public boolean optimizeDist(Vertex vertex, WeightedEdge edge){
        Previous node = (Previous)vertex.nodeData;
        Previous nextNode = (Previous)edge.to.nodeData;
        int newDist = node.distance + edge.weight;
        if(nextNode.distance > newDist){
            nextNode.distance = newDist;
            nextNode.previous = vertex;
            return true;
        }
        return false;
    }

    public String getCoordinatesFromPath(int id){
        Vertex startNode = vertices[id];
        StringBuilder sb = new StringBuilder();

        int count = 0;
        while(((Previous)startNode.nodeData).previous != null){
            startNode = ((Previous)startNode.nodeData).previous;
            if(count%4 == 0){
                sb.append(startNode.latitude).append(", ").append(startNode.longitude).append("\n");
            }
            count++;
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
    double latitude;
    double longitude;
    Object nodeData;
    public Vertex(int id, double latitude, double longitude){
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
    }

}

class Previous{
    int distance;
    int distFromGoal;
    Vertex previous;
    static int infinity = Integer.MAX_VALUE;
    public int getDistance(){return distance;}
    public Vertex getPrevious(){return previous;}

    public Previous(){
        distance = infinity;
        distFromGoal = infinity;
    }
}
