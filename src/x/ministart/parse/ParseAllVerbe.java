/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package x.ministart.parse;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import x.ministart.sys.SwingUI;
import x.ministart.sys.SwingWorkerExecutor;
import x.ministart.utils.Chrono;

/**
 *
 * @author packard-bell
 */
public class ParseAllVerbe {
   
	
	private SwingUI ui;
	
	public ParseAllVerbe(final SwingUI ui) {
		this.ui = ui;
		
		SwingWorker<Integer, Object> swingWorker = new ParseParag();

		SwingWorkerExecutor.instance().execute(swingWorker);

		swingWorker.addPropertyChangeListener( new PropertyChangeListener() {
			@Override
			public  void propertyChange(PropertyChangeEvent evt) {
				if ("progress".equals(evt.getPropertyName())) {
					if (ui.progressBar != null){
						ui.progressBar.setValue((Integer)evt.getNewValue());
					}
				}
			}

		});	
	}
	String doc1=" ";
	public class ParseParag extends SwingWorker<Integer, Object> {
		private Chrono chrono = new Chrono();

		@Override
		protected Integer doInBackground() throws Exception {
			Document doc = ui.main_txtarea.getDocument();
			
			ui.statusInfo.setText("Parse Parag - running");
			ui.main_txtarea.setDocument(new DefaultStyledDocument());
			this.chrono.start();

			int 
				original_txt_lenght = doc.getLength(),
                    		insertion_count = 0,
				parse_ind = 0;
			try {
				String start_balise = "<p>";
				String end_balise = "</p>";
                                boolean openTag = false;
                                String x=doc.getText(parse_ind, doc.getLength());
                                       
                                      String []tab=x.split(" ");
                                      
                                  for (int i = 0; i < tab.length; i++) {
                                String mot = tab[i];
                                char terminaison= mot.charAt(mot.length()-1);
                                      System.out.println(" "+terminaison);
                                      if (mot.contains("ons")||(mot.contains("ez"))||(mot.contains("ent"))
                                              ||(mot.contains("es"))||mot.contains("is")||(mot.contains("issons"))||(mot.contains("issez"))
                                              ||(mot.contains("issent"))||(mot.contains("it") )) {
                                        doc1=doc1+start_balise+ mot+end_balise;    
                                      }else{
                                           doc1=doc1+mot;
                                      }
                                }
                                  doc.insertString(0, doc1, null);
    
                                      
                               /*       
				for (int original_txt_id = 0; original_txt_id < original_txt_lenght; ++original_txt_id) {
				 	setProgress( (original_txt_id+1) * 100 / original_txt_lenght);// update the progress
                                      
					char s_char = doc.getText(parse_ind, 1).charAt(0);
                                       
					if (!((s_char == '.')||(s_char==',')) ) {
                                            
						if (openTag)
							++parse_ind;
						else{
                                                  
							doc.insertString(parse_ind, start_balise , null);
							++insertion_count;
							parse_ind += start_balise.length() + 1;
							openTag = true;
						}
					}else{
						if (openTag){
                                                        
							doc.insertString(parse_ind+1, end_balise , null);
							++insertion_count;
							parse_ind += end_balise.length()+1;
							openTag = false;
						}else{
							++parse_ind;
						}
					}
                                        
                               
				}
				if (openTag){
					doc.insertString(doc.getLength(), end_balise , null);
					++insertion_count;

                                  openTag = false;
				}*/

			} catch (BadLocationException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
                                
			}

			ui.statusInfo.setText (" Parse Parag - sending to text area");
			//ui.main_txtarea.setDocument(doc);
                         ui.main_txtarea.setText(doc1);
			return insertion_count;
		}
		
		@Override
		protected void done(){
			int strParsed = 0;
			try {
				strParsed = get();
				this.chrono.stop();
				ui.statusInfo.setText("Document parsed..." + strParsed+ " insertions [" + this.chrono.displayInterval() + "]");
			}catch(Exception e){

				JOptionPane.showMessageDialog(ui, 
						"Error :\n" + e.getLocalizedMessage(),
						"Parse", 
						JOptionPane.ERROR_MESSAGE);
				ui.statusInfo.setText("Parse error on \"ParseParag\".");
			}
		}

	}
}
