package it.polito.tdp.spellchecker.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.io.*;

public class Dictionary {
	
	private List<String> dictionary;
	private String language;


	public Dictionary() {
		this.dictionary = new ArrayList<String>();
	}
	
	public void setLanguage(String l) { //da richiamare con la comboBox
		this.language = l;
	}
	
	public String getLanguage() {
		return this.language;
	}
	
	public void loadDictionary(String language) {
		this.language = language;
		this.dictionary.clear(); //per pulirlo qualora avessimo già inserito un dizionario in precedenza
		//devo caricare nel dizionario le parole del linguaggio selezionato con la comboBox	
		try {
			FileReader fr = new FileReader("src/main/resources/"+language+".txt");
			BufferedReader br = new BufferedReader(fr);
			String word;
			while ((word = br.readLine()) != null) {
				// Aggiungere parola alla struttura dati
				this.dictionary.add(word.toLowerCase());
			}
			br.close();
		}catch(IOException ioe) {
			System.out.println("Errore nella lettura del file");
		}			
	}

	public List<RichWord> spellCheckTest(List<String> inputTextList){
		//controlla se la parola è già presente nel dizionario: se sì, parola corretta, altrimenti errata
		List<RichWord> controllate = new ArrayList<RichWord>();
		for(String s : inputTextList) {
			boolean corretta = false;
			if(this.dictionary.contains(s)) {
				corretta = true;
			}
			controllate.add(new RichWord(s,corretta));
		}
		return controllate;
	}
	
	public List<RichWord> spellCheckTestLinear(List<String> inputTextList){ //ricerca lineare
		List<RichWord> controllate = new ArrayList<RichWord>();
		int ld = this.dictionary.size(); //numero di parole nel dizionario
		//String richWord = "";
		for(String ss : inputTextList) {
			boolean corretta = false;
			int i;
			for(i = 0; i < ld; i++) { //ricerco su tutto il dizionario
				String dizionario = this.dictionary.get(i);
				if(ss.equals(dizionario)) {
					corretta = true;
					System.out.println("Vi è una corrispondenza nel dizionario");
					break; //esci da qui se ho trovato una corrispondenza
				}
			}
			if(i == ld) {
				System.out.println("Il dizionario non presenta questa parola");
				corretta = false;
			}
			controllate.add(new RichWord(ss, corretta));
		}
		return controllate;
	}
	
	//questo metodo BLOCCA il programma quando lo runno
	public List<RichWord> spellCheckTestDichotomic(List<String> inputTextList){ //ricerca dicotomica
		int mid;
		List<RichWord> controllate = new ArrayList<RichWord>();
		for(String ss : inputTextList) {
			boolean corretta = false;
			int i = 0;
			int s = this.dictionary.size()-1;
			//ricerca finchè non ho trovato corrispondenza o se i = 0 ed s = i+1 o se i = s-1 ed s = dictionary.size()
			while( i <= s ) {
				mid = (i+s)/2;
				if(this.dictionary.get(mid).compareTo(ss) < 0) {
					i = mid +1;
				}
				else if(this.dictionary.get(mid).compareTo(ss) > 0) {
					s = mid -1;
				}
				else {
					corretta = true; //la parola è corretta
					break;
				}
			}
			if(i > s) { //no corrispondenza
				corretta = false;
				System.out.println("Parola non trovata nel dizionario");
			}
			controllate.add(new RichWord(ss, corretta));
		}
		
	//linear search (arrayList): 0 secondi (6 ms)---> non uso contains in questi metodi ma sì in quello normale
	//dichotomic search (arrayList): 0 secondi (1 ms)
	//linear search (linkedList): 9 secondi circa (9371 ms)
	//dichotomic search (linkedList): 0 secondi (14 ms)
		
		return controllate;
	}
}
