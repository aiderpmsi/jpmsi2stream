grammar Pmsi;

options {
  language = Java;
}

@header {
package aider.org.pmsi.parser;

import org.apache.log4j.Logger;
}

@lexer::header {
package aider.org.pmsi.parser;

}

@lexer::members {
}

@members {

  /**
   * Collecteur utilisé dans cette classe
   */
  public PmsiCollecteur collecteur = null;
  
  /**
   * Etat de succès ou d'échec d'import du fichier
   */
  private boolean State = true;

  /**
   * Stocke la liste des erreurs
   */
  private ArrayList<String> errors = new ArrayList<String>();

  /**
   * Logger de la classe
   */
  //@SuppressWarnings("unused")
  private static final Logger logger = Logger.getLogger(PmsiParser.class);

  /**
   * Constructeur
   * @param lexer
   * @param my_col
   */
  public HPRIMSParser(TokenStream input, HPRIMSCollecteur collecteur) {
    this(input);
    this.collecteur = collecteur;
  }

  /**
   * Retourne la liste des erreurs de la liste de parsing
   * @return
   */
  public ArrayList<String> getErrors() {
    return errors;
  }
  
  /**
   * Formate une erreur sous forme de chaine de caract�res
   * @param err
   * @return
   */
  public String formatError(RecognitionException err) {
    return getErrorHeader(err) + " / " + getErrorMessage(err, this.getTokenNames());
  }

  /**
   * La reconnaissance d'une erreur dans le parser ajoute cette erreur dans la liste des erreurs
   * @param tokenNames
   * @param e
   */
  public void displayRecognitionError(String[] tokenNames, RecognitionException e) {
    String msg = formatError(e);
    errors.add(msg);
    setStateSuccess(false);
  }
  
  /**
   * Définit si le parsing a été un succès ou non
   * @param state
   */
  private void setState(boolean state) {
    this.stateSuccess = stateSuccess;
  }
      
  /**
   * Renvoie si le parsing a été un succès ou non
   * @return 
   */
  public boolean getStateSuccess() {
    return this.stateSuccess;
  }
}  

// =========== Définition de la structure des RSF et RSS ================

rss116
@init{collecteur.startElement("rss116");}
@after{collecteur.endElement();}:
rss116_header;
//rss116_content*;

rss116_header
@init{collecteur.startElement("rss116_header");}
@after{collecteur.endElement();}:
nm9["finess"] nm3["numlot"] st2["statutetablissement"] nm8["dbtperiode"] nm8["finperiode"] nm6["nbenregistrements"] nm6["nbrSS"] nm7["premierrss"] nm7["dernierrss"] st1["dernierenvoitrimestre"] newline;

st1[String name]
@init{collecteur.startElement($name);}
@after{collecteur.endElement();}:
  st st
  {collecteur.content($text)};

st2[String name]
@init{collecteur.startElement($name);}
@after{collecteur.endElement();}:
  st st
  {collecteur.content($text)};
  
nm3[String name]
@init{collecteur.startElement($name);}
@after{collecteur.endElement();}:
  nm nm nm
  {collecteur.content($text)};

nm6[String name]
@init{collecteur.startElement($name);}
@after{collecteur.endElement();}:
  nm nm nm nm nm nm
  {collecteur.content($text)};

nm7[String name]
@init{collecteur.startElement($name);}
@after{collecteur.endElement();}:
  nm nm nm nm nm nm nm
  {collecteur.content($text)};

nm8[String name]
@init{collecteur.startElement($name);}
@after{collecteur.endElement();}:
  nm nm nm nm nm nm nm nm
  {collecteur.content($text)};

nm9[String name]
@init{collecteur.startElement($name);}
@after{collecteur.endElement();}:
  nm nm nm nm nm nm nm nm nm
  {collecteur.content($text)};

st: NM | CHAR;

nm: NM;

newline: CR? NL;

CHAR: ~(NM | CR | NL);

NM: '0'..'9';

CR: '\r';

NL: '\n';
