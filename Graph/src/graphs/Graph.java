package graphs;
import java.util.*;
/**
 * @author Anubhuti Srivastava
 */
class Subsets {
	int parent;
	int rank;
}

public class Graph<T> {
	/**
	 T is the generic data type. 
	 The data type needs to be user defined. (It can be ANY object.)
	 We will use HashMaps to store the the vertex and the corresponding
	 adjacency list of nodes.
	 */
	// All nodes will be of the same type and hence only one generic type T is used
	Map<T, List<T>> graph;
	Map<Integer,T> indexToNode;
	Map<T,Integer> nodeToIndex;
	Queue<T> queue;
	Boolean bidirectional;
	Integer V_INDEX;
	Subsets[] subsets; Integer[] coloring; 
	boolean[] visited;
	
	public Graph(List<T> vertices) {
		graph = new HashMap<>();
		bidirectional = true;
		indexToNode = new HashMap<>();
		nodeToIndex = new HashMap<>();
		V_INDEX = 0;
		for(int i=0;i<vertices.size();i++) {
			T vertex = vertices.get(i);
			graph.put(vertex, new ArrayList<T>());
			indexToNode.put(V_INDEX, vertex);
			nodeToIndex.put(vertex, V_INDEX);
			V_INDEX++;
		}
		
	}
    public void addVertex(T v) {
    	if(graph.get(v).size()==0) {
    		graph.put(v, new ArrayList<T>());
			indexToNode.put(V_INDEX, v);
			nodeToIndex.put(v, V_INDEX);
			V_INDEX++;
    	}
    }
	 public Integer getVertexCount() {
		 return graph.keySet().size();
	 }
	 
	 public void addEdge(T u, T v, boolean isDirected) {
		 /*
		  * adding an edge from vertex u of type T to vertex v of type T.
		  * isDirected will be defined by the user.
		  * If true, the edge will be directed or else undirected.
		  */
		 if(isDirected) {
			 bidirectional = false;
		 }
		 if(!graph.containsKey(u)) {
			 graph.put(u, new ArrayList<T>());
				indexToNode.put(V_INDEX, u);
				nodeToIndex.put(u, V_INDEX);
				V_INDEX++;
		 }
		 
		 if(!graph.containsKey(v)) {
			 graph.put(v, new ArrayList<T>());
				indexToNode.put(V_INDEX, v);
				nodeToIndex.put(v, V_INDEX);
				V_INDEX++;
		 }
		 
		 graph.get(u).add(v);
		 
	 }
	 public boolean hasEdge(T u, T v) {
		 if(graph.containsKey(u)) {
			 return false;
		 }
		 if(graph.get(u).contains(v)) {
			 return true;
		 }
		 return false;
	 }
	 
	 public boolean isBipartite() {
		 queue = new LinkedList<T>();
		 int vertices = getVertexCount();
		 if(vertices<1) return true;
		 coloring = new Integer[vertices];
		 visited = new boolean[vertices];
		 
		 for(T node: graph.keySet()) {
			 if(coloring[nodeToIndex.get(node)]==0) {
				 boolean flag = isBipartiteUtil(node);
				 if(!flag) return flag;
			 }
		 }
		 return false;
	 }
	 public boolean isBipartiteUtil(T ver) {
		 coloring[nodeToIndex.get(ver)] = 1;
		 queue.offer(ver);
		 while(!queue.isEmpty()) {
			 T node = queue.poll();
			 int color = coloring[nodeToIndex.get(node)];
			 List<T> vertice = graph.get(node);
			 for(T vertex: vertice) {
                 if(vertex.equals(node)) continue;
				 int index = nodeToIndex.get(vertex);
				 if(coloring[index]!=0 && coloring[index]==color) {
                     return false;
				 } else if(coloring[index]!=0 && coloring[index]!=color){
                     continue;
                 }
                 if(color==1){
                     coloring[index] = 2;
                 } else {
                     coloring[index] = 1;
                 }
                 queue.offer(vertex);
			 }
		 }
		 return true;
	 }
	 public boolean doesCycleExist() {
		 
		 if(bidirectional) return cycleInTheUnidrectedGraph();
		 
		return cycleInTheDirectedGraph();
		 
	 }
	 
	public boolean cycleInTheUnidrectedGraph() {
			return isCyclic();
	}

	private boolean isCyclic() {
		Integer vertices = getVertexCount();
		
		subsets = new Subsets[vertices];
		
		// an array of parent + rank 
		
		for(int v=0;v<vertices;v++) {
			subsets[v] = new Subsets();
			subsets[v].parent = v;
			subsets[v].rank = 0;
		}
		
		// finding absolute parent of each edge from source to vertex.
		
		Map<T, List<T>> graph = this.graph;
		for(T node: graph.keySet()) {
			for(T vertex: graph.get(node)) {
				// node -> vertex     
				Integer x = find(subsets, nodeToIndex.get(node));
				Integer y = find(subsets, nodeToIndex.get(vertex));
				// absolute parent of source is x 
				// absolute parent of destination is y 
				if(x==y) {
					return true;
				}
				// union 
				Union(subsets, x, y);
				
			}
		}
		
		return false;
	}
	public void Union(Subsets[] subsets, Integer node, Integer vertex) {
		// union by rank 
		Integer nodeParent = find(subsets, node);
		Integer vertexParent = find(subsets, vertex);
		// if the rank is different, 
		if(subsets[nodeParent].rank < subsets[vertexParent].rank) {
			subsets[nodeParent].parent = vertexParent;
		} else if (subsets[nodeParent].rank > subsets[vertexParent].rank) {
			subsets[vertexParent].parent = nodeParent;
		} else {
			subsets[nodeParent].parent = vertexParent;
			subsets[vertexParent].rank++;
		}
		
	}

	public  Integer find(Subsets[] subsets, Integer node) {
		if((subsets[node].parent)!=node) {
			// setting the link directly to the absolute parent.
			// this is PATH COMPRESSION
			subsets[node].parent = find(subsets, subsets[node].parent);
		}
		return subsets[node].parent;
	}
	 
	private boolean cycleInTheDirectedGraph() {
		Integer vertices = getVertexCount();
		boolean[] visited = new boolean[vertices];
		boolean[] visiting = new boolean[vertices];
		for(T node:graph.keySet()) {
			Integer indexOfNode = nodeToIndex.get(node);
			System.out.println(indexOfNode);
			if(!visited[indexOfNode]) {
				boolean status = searchForCycle(visited, visiting, node);
				if(status) return true;
			}
		}
		return false;
	}
	public boolean searchForCycle(boolean[] visited, boolean[] visiting, T node) {
		
		Integer nodeOfIndex = nodeToIndex.get(node);
		visited[nodeOfIndex] = true;
		visiting[nodeOfIndex] = true;
		
		List<T> childrenOfNode = graph.get(node);
		
		for(T child: childrenOfNode) {
			
			Integer indexOfNode = nodeToIndex.get(child);
			if(!visited[indexOfNode]) {
				boolean isCycle = searchForCycle(visited, visiting, child);
				if(isCycle) return true;
				
			} else {
				
				if(visiting[indexOfNode]) {
					/*
					 * a back edge has been found.
					 */
					return true; 
				}
			}
		}
		visiting[nodeOfIndex] = false;
		
		return false;
	}
	public void DFSTraversal() {
		Integer vertices = getVertexCount();
		boolean[] visited = new boolean[vertices];
		System.out.println("DFS TRAVERSAL OF THE GRAPH:");
		for(T node: graph.keySet()) {
			Integer indexOfNode = nodeToIndex.get(node);
			if(!visited[indexOfNode]) {
				DFS(node, visited);
			}
		}
	}
	
	public void DFS(T node, boolean[] visited) {
		System.out.print(node+" ");
		visited[nodeToIndex.get(node)] = true;
		List<T> nodes = graph.get(node);
		for(T vertices: nodes) {
			if(!visited[nodeToIndex.get(vertices)]) {
				DFS(vertices, visited);
			}
		}
	}
}


