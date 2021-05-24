package graphs;

import java.util.ArrayList;
import java.util.List;

import graphs.WeightedGraphs.FloydWarshallAllShortestPath;

public class newclass {
	public static void main(String[] args) {
		
		ArrayList<Integer> vertices = new ArrayList<>();
		// add vertices
        vertices.add(0);
        vertices.add(1);
        vertices.add(2);
        vertices.add(3);
        vertices.add(4);
        vertices.add(5);
        WeightedGraphs<Integer> graph = new WeightedGraphs<Integer>(vertices);
        graph.addEdge(0, 1, true, 2);
        graph.addEdge(0, 4, true, 1);
        graph.addEdge(1, 2, true, 3);
        graph.addEdge(2, 3, true, 6);
        graph.addEdge(4, 2, true, 2);
        graph.addEdge(4, 5, true, 4);
        graph.addEdge(5, 3, true, 1);
        
        FloydWarshallAllShortestPath f = graph.new FloydWarshallAllShortestPath();
        f.shortestPathBetweenVertices(0, 0);
        f.shortestPathBetweenVertices(0, 1);
        f.shortestPathBetweenVertices(0, 2);
        f.shortestPathBetweenVertices(0, 3);
        f.shortestPathBetweenVertices(0, 4);
        f.shortestPathBetweenVertices(0, 5);
	}

}
