/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.spellchecker;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import it.polito.tdp.spellchecker.model.Dictionary;
import it.polito.tdp.spellchecker.model.RichWord;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Dictionary model;
	private List<RichWord> words = new ArrayList<RichWord>();
	

    @FXML //ResourceBoundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML //URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML
    private ComboBox<String> cmbLanguage;

    @FXML
    private Label lbError;

    @FXML
    private Label lbTime;

    @FXML
    private TextArea txtResult;

    @FXML
    private TextField txtWords;

    @FXML
    void doClearText(ActionEvent event) {
    	this.txtResult.setText("");
    	this.txtWords.setText("");
    	this.lbTime.setText("Spell check completed in ...");
    	this.lbError.setText("Number of errors");
    }

    @FXML
    void doSpellCheck(ActionEvent event) {
    	//popolo il dizionario in base alla lingua del combobox
    	String lingua = this.cmbLanguage.getValue();
    	if(lingua == null) {
    		this.txtResult.setText("Seleziona la lingua per poter controllare lo spelling delle parole!");
    		return;
    	}
    	this.model.setLanguage(lingua);
    	this.model.loadDictionary(lingua);
    	//prendi il testo dal textField, rendilo minuscolo ed elimina la punteggiatura
    	String testo = this.txtWords.getText();
    	testo = testo.toLowerCase();
    	testo = testo.replaceAll("[.,\\/#!$%\\*;:{}=\\-_'~()\\[\\]\"?]", "");
    	String parole[] = testo.split(" ");
    	List<String> temp = new ArrayList<String>();
    	for(int i = 0; i < parole.length; i++) {
    		temp.add(parole[i]);
    	}
    	//eseguo il controllo dello spelling + conto quanto tempo viene impiegato
    	long start = System.currentTimeMillis();
    	//List<RichWord> words = this.model.spellCheckTest(temp);
    	//words = this.model.spellCheckTestLinear(temp);
    	words = this.model.spellCheckTestDichotomic(temp);
    	long end = System.currentTimeMillis();
    	//stampo a video le parole errate e le conto
    	int errate = 0;
    	this.txtResult.setText("");
    	for(RichWord rw : words) {
    		if(rw.isCorrect()==false) {
    			errate++;
    			this.txtResult.appendText(rw.getWord()+"\n");
    		}
    	}
    	this.lbError.setText("The text contains: "+errate+" errors");
    	this.lbTime.setText("Spell check completed in "+(end-start) +" milliseconds");
    }
    
    //predispongo il Controller per agire sul modello
    public void setModel(Dictionary model) {
    	//popolo la comboBox delle lingue possibili
        this.cmbLanguage.getItems().clear();
        this.cmbLanguage.getItems().add("English");
        this.cmbLanguage.getItems().add("Italian");
    	this.model = model;
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert cmbLanguage != null : "fx:id=\"cmbLanguage\" was not injected: check your FXML file 'Scene.fxml'.";
        assert lbError != null : "fx:id=\"lbError\" was not injected: check your FXML file 'Scene.fxml'.";
        assert lbTime != null : "fx:id=\"lbTime\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtWords != null : "fx:id=\"txtWords\" was not injected: check your FXML file 'Scene.fxml'.";
    }

}



