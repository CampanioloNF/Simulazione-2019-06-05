package it.polito.tdp.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;
import it.polito.tdp.db.EventsDao;

public class Model {
	
	
	private EventsDao dao;
	private Graph<District, DefaultWeightedEdge> grafo;
	private Map<Integer, District> districtIdMap;
	private List<District> distretti ;
	private Simulatore sim;
	private District partenza;
	
	public Model() {
		this.dao = new EventsDao();
		this.sim = new Simulatore();
	}

	public List<Integer> getAnni() {
		
		return dao.getAllAnni();
	}

	public void creaGrafo(int anno) {
		
		//creo il grafo
		this.grafo = new SimpleWeightedGraph<District, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		//creo la mappa
		this.districtIdMap = new HashMap<Integer, District>();
		//carico i verici
		dao.loadVertex(grafo, districtIdMap, anno);
		
		//ora aggiungo gli archi 
	    this.distretti = new ArrayList<>(grafo.vertexSet()); 
		
		for(int i=0; i<distretti.size()-1; i++) {
			
			for(int j = i+1; j<distretti.size(); j++) {
			  
				District source = distretti.get(i);
				District target = distretti.get(j);
				Graphs.addEdge(grafo, source, target, LatLngTool.distance(source.getCentro(), target.getCentro(), LengthUnit.KILOMETER)); 
			}
		}
		
		this.partenza = dao.getDistrictMin(anno, districtIdMap);
		
	}
	
	public List<DistrictDistance> prendiAdiacenti(District partenza){
		
		if(grafo!=null) {
		
          List<DistrictDistance> ris = new LinkedList<>();
		
          for(District district : Graphs.neighborListOf(grafo, partenza))
                      ris.add(new DistrictDistance(partenza, district, grafo.getEdgeWeight(grafo.getEdge(partenza, district))));
          
          Collections.sort(ris);
          return ris;
		}
		
		return null;
	}

	public List<District> getDistrict() {
		
		if(grafo!=null) 
			return this.distretti;
		
		return null;
	}

	public int simulate(LocalDate data, int n) {
		
	  //cosa devo passare al simulatore? 
	  /*
	   * Lista di crimini commessi
	   *   
	   *    3 eventi
	   *    - crimine
	   *    - poliziotto liberato
	   *    
	   *    c'è un crimine --> posso mandare un poliziotto in tempo?
	   */
		
	  //al DAO chiedo (Data, distretto e tipologia di crimine)
		
		PriorityQueue<Evento> queue = dao.getEventi(data, districtIdMap);
 		
		sim.init(queue,n, partenza, grafo, this);
		sim.run();
		return sim.getMalGestiti();
	}
	

	public List<District> viciniPerDistanza(District district){
		List<District> ris =  new ArrayList<>();
		for(DistrictDistance dd : this.prendiAdiacenti(district))
			ris.add(dd.getTarget());	
		return ris;	
		
	}
}
