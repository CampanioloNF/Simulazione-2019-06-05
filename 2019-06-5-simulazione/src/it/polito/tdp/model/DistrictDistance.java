package it.polito.tdp.model;

public class DistrictDistance  implements Comparable <DistrictDistance>{

	private District source;
	private District target;
	private double distance;
	
	public DistrictDistance(District source, District target, double distance) {
		super();
		this.source = source;
		this.target = target;
		this.distance = distance;
	}

	public District getSource() {
		return source;
	}

	public District getTarget() {
		return target;
	}

	public double getDistance() {
		return distance;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((source == null) ? 0 : source.hashCode());
		result = prime * result + ((target == null) ? 0 : target.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DistrictDistance other = (DistrictDistance) obj;
		if (source == null) {
			if (other.source != null)
				return false;
		} else if (!source.equals(other.source))
			return false;
		if (target == null) {
			if (other.target != null)
				return false;
		} else if (!target.equals(other.target))
			return false;
		return true;
	}

	@Override
	public int compareTo(DistrictDistance dd) {
		
		return (int) (this.distance - dd.distance);
	}
	
	
}
