package it.polito.tdp.baseball.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.baseball.db.BaseballDAO;

public class Model {
	
	BaseballDAO dao = new BaseballDAO();
	Graph<People,DefaultEdge> grafo;
	List<People> vertici;
	Map<String,People> idMap;
	
	public List<Integer> getAllYears(){
		return dao.readAllYears();
	}
	
	public void creaGrafo(int anno,double salario) {
		idMap = new HashMap<>();
		//Creazione grafo:
		grafo = new SimpleGraph<>(DefaultEdge.class);
		//Creazione vertici
		vertici = dao.readAllPlayersWithCondition(anno,salario);
		Graphs.addAllVertices(grafo, vertici);
		for(People p : vertici) {
			idMap.put(p.getPlayerID(), p);
		}
		//Creazione archi:
		List<Arco> archi = dao.readAllArchi(anno, salario, idMap);
		for(Arco a : archi) {
			grafo.addEdge(a.p1, a.p2);
		}
	}
	
	public int getVertexsize() {
		return grafo.vertexSet().size();
	}
	
	public int getEdgessize() {
		return grafo.edgeSet().size();
	}
	
	public List<Grado_associato> getGradoMax() {
		int max=0;
		List<Grado_associato> people_grado = new ArrayList<>();
		for(People p : vertici) {
			int grado= grafo.degreeOf(p);
			if(grado> max) {
				people_grado.clear();
				max = grado;
				
			}
			if(people_grado.isEmpty() || grado == max) {
				people_grado.add(new Grado_associato(p,max));
			}
		}
		
		
		return people_grado;
	}
	
	public int getConnessesize() {
		ConnectivityInspector<People, DefaultEdge> connesse = new ConnectivityInspector<>(this.grafo);
		return connesse.connectedSets().size();
	}
}