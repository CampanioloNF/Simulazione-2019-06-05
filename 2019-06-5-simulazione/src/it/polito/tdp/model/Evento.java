package it.polito.tdp.model;


import java.time.LocalDateTime;


public class Evento implements Comparable<Evento>{

	
   public enum TipoEvento{
	   
	   CRIMINE,
	   POLIZIOTTO_LIBERO
   }
	
	
   private District distretto;
   private LocalDateTime ora;
   private String crimine;
   private TipoEvento tipo;

   
   public Evento(District distretto, LocalDateTime ora, String crimine) {
	super();
	this.distretto = distretto;
	this.ora = ora;
	this.crimine = crimine;
	this.tipo = TipoEvento.CRIMINE;
	
}

   public Evento(District distretto, LocalDateTime ora) {
		super();
		this.distretto = distretto;
		this.ora = ora;
		this.crimine = "";
		this.tipo = TipoEvento.POLIZIOTTO_LIBERO;
	}

public District getDistretto() {
	return distretto;
}

public LocalDateTime getOra() {
	return ora;
}

public String getCrimine() {
	return crimine;
}

public TipoEvento getTipo() {
	return tipo;
}

@Override
public int compareTo(Evento e) {
	// TODO Auto-generated method stub
	return this.ora.compareTo(e.ora);
}


   
}
