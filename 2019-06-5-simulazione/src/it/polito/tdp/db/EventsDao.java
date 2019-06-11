package it.polito.tdp.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.model.District;
import it.polito.tdp.model.Event;
import it.polito.tdp.model.Evento;


public class EventsDao {
	
	public List<Event> listAllEvents(){
		String sql = "SELECT * FROM events" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Event> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Event(res.getLong("incident_id"),
							res.getInt("offense_code"),
							res.getInt("offense_code_extension"), 
							res.getString("offense_type_id"), 
							res.getString("offense_category_id"),
							res.getTimestamp("reported_date").toLocalDateTime(),
							res.getString("incident_address"),
							res.getDouble("geo_lon"),
							res.getDouble("geo_lat"),
							res.getInt("district_id"),
							res.getInt("precinct_id"), 
							res.getString("neighborhood_id"),
							res.getInt("is_crime"),
							res.getInt("is_traffic")));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}

	public List<Integer> getAllAnni() {
		
		String sql = "SELECT year(reported_date) as anno FROM EVENTS GROUP BY year(reported_date) " ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Integer> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(res.getInt("anno"));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}

	public void loadVertex(Graph<District, DefaultWeightedEdge> grafo, Map<Integer, District> districtIdMap, int anno) {
	
		String sql = "SELECT district_id, SUM(geo_lat)/COUNT(*) AS cen_lat, SUM(geo_lon)/COUNT(*) AS cen_lon " + 
				"FROM EVENTS " + 
				"WHERE YEAR(reported_date) = ? " + 
				"GROUP BY district_id " ;
		
		try {
			Connection conn = DBConnect.getConnection() ;
            PreparedStatement st = conn.prepareStatement(sql) ;
            st.setInt(1, anno);
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				
				//evito i doppioni
				if(!districtIdMap.containsKey(res.getInt("district_id"))) {
					
					District district = new District(res.getInt("district_id"), 
							res.getDouble("cen_lat"), res.getDouble("cen_lon"));
					districtIdMap.put(district.getDistrictId(), district);
					grafo.addVertex(district);
					
				}
				
			}
			
			conn.close();
			

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		
	}

	public PriorityQueue<Evento> getEventi(LocalDate data, Map<Integer, District> districtIdMap) {
		String sql = "SELECT district_id, hour(reported_date) AS ora , minute(reported_date) AS minu, second(reported_date) AS sec,offense_category_id " + 
				"FROM EVENTS " + 
				"WHERE YEAR(reported_date) = ? AND MONTH(reported_date) = ? AND DAY(reported_date) = ? " ;
		
		PriorityQueue<Evento> queue = new PriorityQueue<Evento>();
		
		try {
			Connection conn = DBConnect.getConnection() ;
            PreparedStatement st = conn.prepareStatement(sql) ;
            
            st.setInt(1, data.getYear());
            st.setInt(2, data.getMonthValue());
            st.setInt(3, data.getDayOfMonth());
			
            ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				
				
				if(districtIdMap.containsKey(res.getInt("district_id"))) {
					
					queue.add(new Evento(districtIdMap.get(res.getInt("district_id")), 
							LocalDateTime.of(data, LocalTime.of(res.getInt("ora"), res.getInt("minu"), res.getInt("sec"))), res.getString("offense_category_id")));
					
				}
				
			}
			
			conn.close();
			return queue;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public District getDistrictMin(int anno, Map<Integer, District> districtIdMap) {
		String sql = "SELECT district_id, COUNT(*) AS cnt " + 
				"FROM EVENTS " + 
				"WHERE YEAR(reported_date) = ? " + 
				"GROUP BY district_id ORDER BY cnt" ;
		
		District partenza = null;
		
		try {
			Connection conn = DBConnect.getConnection() ;
            PreparedStatement st = conn.prepareStatement(sql) ;
            
            st.setInt(1, anno);
            
            ResultSet res = st.executeQuery() ;
			
			if(res.next()) {
				
				
				if(districtIdMap.containsKey(res.getInt("district_id"))) {
					partenza = districtIdMap.get(res.getInt("district_id"));
					conn.close();
					return partenza;
				}
				
			}
			
			conn.close();
			return partenza;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

}
