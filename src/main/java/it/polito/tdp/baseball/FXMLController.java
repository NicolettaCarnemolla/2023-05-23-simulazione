package it.polito.tdp.baseball;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;


import it.polito.tdp.baseball.model.Model;
import it.polito.tdp.baseball.model.People;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnConnesse;

    @FXML
    private Button btnCreaGrafo;

    @FXML
    private Button btnDreamTeam;

    @FXML
    private Button btnGradoMassimo;

    @FXML
    private TextArea txtResult;

    @FXML
    private TextField txtSalary;

    @FXML
    private TextField txtYear;

    
    
    @FXML
    void doCalcolaConnesse(ActionEvent event) {
    	this.txtResult.appendText("Ci sono "+model.getConnessesize()+" componenti connesse.");
    }

    
    
    @FXML
    void doCreaGrafo(ActionEvent event) {
    	int anno = Integer.parseInt(this.txtYear.getText());
    	if(!model.getAllYears().contains(anno)) {
    		this.txtResult.setText("Inserisci un anno valido!");
    	}
    	double salariounitario = Double.parseDouble(this.txtSalary.getText());
    	double salario = salariounitario*1000000;
    	model.creaGrafo(anno, salario);
    	this.txtResult.setText("Grafo creato!\nCi sono "+model.getVertexsize()+" vertici. \nCi sono "+model.getEdgessize()+" archi.");
    	this.btnConnesse.setDisable(false);
    	this.btnGradoMassimo.setDisable(false);
    }

    
    @FXML
    void doDreamTeam(ActionEvent event) {

    }

    
    @FXML
    void doGradoMassimo(ActionEvent event) {

    	this.txtResult.setText("Nodo di grado max: \n"+model.getGradoMax());
    }

    
    @FXML
    void initialize() {
        assert btnConnesse != null : "fx:id=\"btnConnesse\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnDreamTeam != null : "fx:id=\"btnDreamTeam\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnGradoMassimo != null : "fx:id=\"btnGradoMassimo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtSalary != null : "fx:id=\"txtSalary\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtYear != null : "fx:id=\"txtYear\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    }

}
