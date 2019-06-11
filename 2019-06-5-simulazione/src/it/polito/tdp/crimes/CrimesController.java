/**
 * Sample Skeleton for 'Crimes.fxml' Controller Class
 */

package it.polito.tdp.crimes;

import java.net.URL;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.model.District;
import it.polito.tdp.model.DistrictDistance;
import it.polito.tdp.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class CrimesController {

	private Model model;
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxAnno"
    private ComboBox<Integer> boxAnno; // Value injected by FXMLLoader

    @FXML // fx:id="boxMese"
    private ComboBox<Month> boxMese; // Value injected by FXMLLoader

    @FXML // fx:id="boxGiorno"
    private ComboBox<Integer> boxGiorno; // Value injected by FXMLLoader

    @FXML // fx:id="btnCreaReteCittadina"
    private Button btnCreaReteCittadina; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimula"
    private Button btnSimula; // Value injected by FXMLLoader

    @FXML // fx:id="txtN"
    private TextField txtN; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    private int annoSelezionato;
    @FXML
    void doCreaReteCittadina(ActionEvent event) {
    	
    	txtResult.clear();
    	
    	if(boxAnno.getValue()!=null) {
    		
    		int anno = boxAnno.getValue();
    		annoSelezionato = anno;
    		model.creaGrafo(anno);
    		
    		for(District district : model.getDistrict()) {
    			txtResult.appendText("I vicini del distretto "+district.getDistrictId()+" sono:\n");
    			for(DistrictDistance vicino : model.prendiAdiacenti(district))
    				txtResult.appendText("  -"+vicino.getTarget()+"  "+(double)Math.round(vicino.getDistance()*100)/100+" km\n");
    		}	
    		
   		List<Month> mesi = new ArrayList<>();
   		for(int i = 1; i<=12; i++)
   			mesi.add(Month.of(i));
   		
   		boxMese.getItems().addAll(mesi);
   		
   		List<Integer> giorni = new ArrayList<>();
   		for(int i=1; i<=31; i++)
   			giorni.add(i);
   		
   		boxGiorno.getItems().addAll(giorni);
   		
    		
    	}else {
    		txtResult.appendText("Si prega di inserire un anno");
    		return;
    	}
    	
    }

    @FXML
    void doSimula(ActionEvent event) {

    	//controlli 
    	txtResult.clear();
    	
    	if(boxAnno.getValue()!= null ) {
    		
    		if(boxAnno.getValue()==annoSelezionato) {
    			
    			if(boxMese.getValue()!=null) {
    				if(boxGiorno.getValue()!=null) {
    				
    					
    					   LocalDate data;
    					   int n;
    					   
    					   try {
    						
    						 n = Integer.parseInt(txtN.getText());
    						 data = LocalDate.of(annoSelezionato, boxMese.getValue(), boxGiorno.getValue());
    						
    					}catch(DateTimeException dte) {
    						txtResult.appendText("Inserisci una data valida");
    						return;
    					}catch(NumberFormatException nfe) {
    						txtResult.appendText("Si prega di inserire un numero INTERO N");
    						return;
    					}
    					 
    					   txtResult.appendText("Gli eventi mal gestiti sono: " + model.simulate(data, n));
    					
    				   
    				}
    				else 
    					txtResult.appendText("Si prega di selezionare un giorno");
    				
    			}
    			else 
					txtResult.appendText("Si prega di selezionare un mese");
				
    			
    		}
    		else
    			txtResult.appendText("Nel caso si volesse cambiare anno premere nuovamente 'Rete Cittadina'");
    	}
    	else 
    		txtResult.appendText("Si prega di selezionare un anno");
    	
    	
    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxAnno != null : "fx:id=\"boxAnno\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert boxMese != null : "fx:id=\"boxMese\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert boxGiorno != null : "fx:id=\"boxGiorno\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert btnCreaReteCittadina != null : "fx:id=\"btnCreaReteCittadina\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert txtN != null : "fx:id=\"txtN\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Crimes.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    }
    
   public void caricaBoxAnno() {
	   this.boxAnno.getItems().addAll(model.getAnni());
   }
}
