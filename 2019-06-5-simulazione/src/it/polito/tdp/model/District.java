package it.polito.tdp.model;

import com.javadocmd.simplelatlng.LatLng;

public class District {

	private int districtId;
	private LatLng centro;
	private int na;
	
	

	public District(int districtId, double lat, double lon) {
		super();
		this.districtId = districtId;
		this.centro = new LatLng(lat, lon);
		this.na=0;
	}

	public int getDistrictId() {
		return districtId;
	}

	public void setDistrictId(int districtId) {
		this.districtId = districtId;
	}

	public LatLng getCentro() {
		return centro;
	}

	public void setCentro(LatLng centro) {
		this.centro = centro;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + districtId;
		return result;
	}

	public int getNa() {
		return na;
	}

	public void setNa(int na) {
		this.na = na;
	}
	
	public void manda() {
		this.na--;
	}
	public void libera() {
		this.na++;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		District other = (District) obj;
		if (districtId != other.districtId)
			return false;
		return true;
	}
	
	public String toString() {
		return ""+this.districtId;
	}
	
}
