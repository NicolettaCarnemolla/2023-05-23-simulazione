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
	private ArrayList<People> dreamTeam;
	private double salarioDreamTeam;
	
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
			//Oppure
			//int grado = Graphs.neighborListOf(this.grafo,p).size();
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
	
	public void getDreamTeam(int anno) {
		this.dreamTeam = new ArrayList<People>();
		this.salarioDreamTeam = 0.0;
		List<People> rimanenti = new ArrayList<>(this.grafo.vertexSet());
		List<People> playersinattivi = new ArrayList<>(this.grafo.vertexSet());

		
		ricorsione(anno,new ArrayList<People>(), rimanenti);
			
		}
	

	private void ricorsione(int anno,ArrayList<People> parziale, List<People> rimanenti ) {
		// TODO Auto-generated method stub
		//Condizione di terminazione
		if(rimanenti.isEmpty()) {
			double salario = getSalarioTotale(parziale,anno);
			if(salario > this.salarioDreamTeam) {
				this.salarioDreamTeam = salario;
				this.dreamTeam = new ArrayList<>(parziale);
			}
			return;
		}
		
		//Aggiungiamo giocatori:
		List<People> compagnisquadra = Graphs.neighborListOf(this.grafo, rimanenti.get(0));
		compagnisquadra.add(rimanenti.get(0));
		List<People> nuovorimanenti = new ArrayList<>(rimanenti);
		nuovorimanenti.removeAll(compagnisquadra);
		
		for(People p : compagnisquadra) {
			parziale.add(p);
			nuovorimanenti.removeAll(Graphs.neighborListOf(grafo, p));
			nuovorimanenti.remove(p);
			ricorsione(anno,parziale,nuovorimanenti);
			parziale.remove(parziale.size()-1);
		}
	}

	private double getSalarioTotale(ArrayList<People> players, int anno) {
		// TODO Auto-generated method stub
		double salariotot = 0.0;
		for(People p : players) {
			double salario = this.dao.getSalario(p, anno);
			salariotot += salario;
		}
		return salariotot;
	}
	public List<People> getDream(){
		return this.dreamTeam;
		
	}
	
	public double getsala() {
		return salarioDreamTeam;
		
	}
 }
