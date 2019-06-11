package it.polito.tdp.model;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;

import javax.print.attribute.standard.MediaSize.NA;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

public class Simulatore {

	private PriorityQueue<Evento> queue;
	private District partenza;
	private int na; //numero agentu
	private Graph<District, DefaultWeightedEdge> grafo;
	private Model model;
	//parametri interni
	private Random rand;

	private Duration inter_crime = Duration.ofHours(2);
	private List<Duration> inter_other_crime;  
	private int maxDistance = 15; //km velocita(1km/min)*tempoMax(15min)
 	private int malGestiti; 
 //	private Map<District, Integer> naDis;
 	
	public void init(PriorityQueue<Evento> queue, int n, District partenza, Graph<District, DefaultWeightedEdge> grafo, Model model) {
		
		this.queue = queue;
		this.na = n;
		this.partenza = partenza;
		this.grafo = grafo;
		this.malGestiti = 0;
		this.model = model;
		
		this.rand = new Random();
		this.inter_other_crime = new ArrayList<>();
		 inter_other_crime.add(Duration.ofHours(1));
		 inter_other_crime.add(Duration.ofHours(2));
		
	    for(District dis : grafo.vertexSet())
			 dis.setNa(0);
		 
		this.partenza.setNa(na);
	}
	
	public void run() {
		
		while(!queue.isEmpty()) {
			
			Evento ev = queue.poll();
			District district = ev.getDistretto();
			
			//due tipi di evento --> Crimine e poliziotto libero
			
			switch(ev.getTipo()) {
			
			case CRIMINE:
			
			//vedo che crimine è 
				
				Duration durata = inter_crime;
				
				
				if(ev.getCrimine().compareTo("all_other_crimes")==0) 
					durata = inter_other_crime.get(rand.nextInt(2));
			
			    //Controllo se c'è un poliziotto nel distretto
				
				  if(district.getNa()>0) {
					district.manda();
				}
				
				else {
					
					//cerco tra i vicini
					
					boolean malGestito = true;
					
					for(DistrictDistance vicino : model.prendiAdiacenti(district)) {
					
						if(vicino.getTarget().getNa()>0) {
						
							vicino.getTarget().manda();
						
							if(vicino.getDistance()<=maxDistance) {
							queue.add(new Evento(district, ev.getOra().plusMinutes((long) vicino.getDistance())));	
							}
     						else {
							 queue.add(new Evento(district, ev.getOra().plus(durata).plusMinutes((long) vicino.getDistance())));
							 malGestito = false;
     						}
							break;
							
     						}
							
						}
					
					if(malGestito)
						malGestiti++;
					
					}
					
				break;
				
			case POLIZIOTTO_LIBERO:
				//caso molto semplice -> in quel distretto ci sarà un poliziotto in più
				district.libera();
				break;
			
			}
			
		}
		
	}

	public int getMalGestiti() {
		return malGestiti;
	}
	
	
	
	
	
}
