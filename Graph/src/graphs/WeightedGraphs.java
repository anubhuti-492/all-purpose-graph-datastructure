package graphs;
/**
 * @author Anubhuti Srivastava
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

class NODE<T>{
	T vertex;
	T distanceFromNode;
	// weight of the edge to Vertex from distanceFromNode; 
	Integer edgeWeight;
	public NODE(T vertex, T distanceFromNode, Integer edgeWeight) {
		this.vertex = vertex;
		this.distanceFromNode = distanceFromNode;
		this.edgeWeight = edgeWeight;
	}
}

public class WeightedGraphs<T> extends Graph<T>{
	
	final int INF = 99999;
	Map<String, Integer> weightedEdgeMap;
	
	public WeightedGraphs(List<T> vertices) {
		super(vertices);
		weightedEdgeMap = new HashMap<>();
	}
	
	public void addEdge(T u, T v, boolean isDirected, Integer weightedEdge) {
		
		 addEdge(u, v, isDirected);
		 
		 String edgePath = nodeToIndex.get(u)+"-"+nodeToIndex.get(v);
		 weightedEdgeMap.put(edgePath, weightedEdge);
		 
	}
	public List<T> topoSort() {
		if(isCyclic()) {
			System.out.println("The graph contains a cycle OR is undircted.");
			return new ArrayList<>();
		}
		int V = getVertexCount();
		List<T> TOPOLOGICAL_SORT = new ArrayList<>();
		int[] INDEGREE = new int[V];
		int INDEX = 0;
		// counting degree 
		for(T node: graph.keySet()) {
			List<T> nodes = graph.get(node);
			for(T vertex: nodes) {
				INDEGREE[nodeToIndex.get(vertex)]++;
			}
		}
		
		Queue<Integer> BFS_QUEUE = new LinkedList<>();
		
		for(T node: graph.keySet()) {
			if(INDEGREE[nodeToIndex.get(node)]==0) {
				BFS_QUEUE.offer(nodeToIndex.get(node));
			}
		}
		while(!BFS_QUEUE.isEmpty()) {
			Integer vertex = BFS_QUEUE.poll();
			TOPOLOGICAL_SORT.add(indexToNode.get(vertex));
			T node = indexToNode.get(vertex);
			for(T ver: graph.get(node)) {
				INDEGREE[nodeToIndex.get(ver)]--;
				if(INDEGREE[nodeToIndex.get(ver)]==0) {
					BFS_QUEUE.offer(nodeToIndex.get(ver));
				}
			}
		}
		return TOPOLOGICAL_SORT;
		
	}
	class FloydWarshallAllShortestPath {
		int V; int[][] dist;
		FloydWarshallAllShortestPath(){
			 System.out.println("Constructor called");
			 int V = getVertexCount();
			 dist = new int[V][V];
			 
			 for(int i=0;i<V;i++) {
				 for(int j=0;j<V;j++) {
					 if(i==j) {
						 continue;
					 }
					 if(!weightedEdgeMap.containsKey(i+"-"+j)) {
						 dist[i][j] = INF;
					 } else {
						 dist[i][j] = weightedEdgeMap.get(i+"-"+j);
					 }
				 }
			 }
			 
			 for(int k=0;k<V;k++) {
				 for(int i=0;i<V;i++) {
					 for(int j=0;j<V;j++) {
						 if(dist[i][k]+dist[k][j]<dist[i][j]) {
							 dist[i][j] = dist[i][k]+dist[k][j];
						 }
					 }
				 }
			 }
		}
		
		public void shortestPathBetweenVertices(T source, T destination) {
			int y = dist[nodeToIndex.get(source)][nodeToIndex.get(destination)];
			System.out.println(y);
		}
		
		public void display(int[][] dist) {
			
			 for(int i=0;i<V;i++) {
				 for(int j=0;j<V;j++) {
					 if(dist[i][j]==INF) {
						 System.out.print("INF ");
					 } else 
					 System.out.print(dist[i][j]+" ");
				 }
				 System.out.println("");
			 }
		}
		
	}
	
	public boolean isCyclic() {
		return doesCycleExist();
	}
	
	public boolean isGraphBipartite() {
		return isBipartite();
	}
	
	public void DFS() {
		DFSTraversal();
	}
	 
}



