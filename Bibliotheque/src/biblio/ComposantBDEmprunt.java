package biblio;



import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
//import java.sql.Date ;
import java.sql.Timestamp ;
import java.text.SimpleDateFormat;
import java.util.Date ;


/**
 * Composant logiciel assurant la gestion des emprunts d'exemplaires
 * de livre par les abonnés.
 */
public class ComposantBDEmprunt {

  /**
   * Retourne le nombre total d'emprunts en cours référencés dans la base.
   * 
   * @return le nombre d'emprunts.
   * @throws SQLException en cas d'erreur de connexion à la base.
   */
	
	private static SimpleDateFormat formatageDate = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	
  public static int nbEmpruntsEnCours() throws SQLException {
    //
    // A COMPLETER
    //
    Statement stmt = Connexion.getConnection().createStatement(); 
    String query="select count(*) as nbre_emprunt from emprunt where date_retour is null";
    ResultSet rset=stmt.executeQuery(query);
    rset.next();
    int nbreEmprunt=rset.getInt("nbre_emprunt") ;
    rset.close();
    stmt.close();
    return nbreEmprunt;
    
  }

  /**
   * Retourne le nombre d'emprunts en cours pour un abonné donné connu
   * par son identifiant.
   * 
   * @return le nombre d'emprunts.
   * @throws SQLException en cas d'erreur de connexion à la base.
   */
  public static int nbEmpruntsEnCours(int idAbonne) throws SQLException {
    //
    // A COMPLETER
    //
    Statement stmt = Connexion.getConnection().createStatement() ;
    String query = "select count(*) as nbre_emprunt from emprunt where abonne_id=" + idAbonne ;
    ResultSet rset = stmt.executeQuery(query);
    rset.next() ;
    int nbreEmprunt=rset.getInt("nbre_emprunt");
    rset.close();
    stmt.close();
    return nbreEmprunt ;
    
  }

  
  /**
   * Récupération de la liste complète des emprunts en cours.
   * 
   * @return un <code>ArrayList<String[]></code>. Chaque tableau de chaînes
   * de caractères contenu correspond à un emprunt en cours.<br/>
   * Il doit contenir 8 éléments (dans cet ordre) :
   * <ul>
   *   <li>0 : id de l'exemplaire</li>
   *   <li>1 : id du livre correspondant</li>
   *   <li>2 : titre du livre</li>
   *   <li>3 : son auteur</li>
   *   <li>4 : id de l'abonné</li>
   *   <li>5 : nom de l'abonné</li>
   *   <li>6 : son prénom</li>
   *   <li>7 : la date de l'emprunt</li>
   * </ul>
   * @throws SQLException en cas d'erreur de connexion à la base.
   */
  public static ArrayList<String[]> listeEmpruntsEnCours() throws SQLException {
    ArrayList<String[]> emprunts = new ArrayList<String[]>();
    //
    // A COMPLETER
    //
    Statement stmt = Connexion.getConnection().createStatement();
    String query = "select exemplaire.id as id_exemplaire,livre.id as id_livre, livre.titre, livre.auteur, usager.id as id_abonne, usager.nom, usager.prenom, emprunt.date_emprunt" 
    				+ " from emprunt"
    				+ " inner join exemplaire on exemplaire.id = emprunt.exemplaire_id"
    				+ " inner join livre on livre.id = exemplaire.livre_id"
    				+ " inner join usager on usager.id=emprunt.abonne_id"
    				+ " where emprunt.date_retour is null";	    
    
    ResultSet rset = stmt.executeQuery(query);

    while (rset.next()) {
      String[] emprunt = new String[8];
      emprunt[0] = rset.getString("id_exemplaire");
      emprunt[1] = rset.getString("id_livre");
      emprunt[2] = rset.getString("titre");
      emprunt[3] = rset.getString("auteur");
      emprunt[4] = rset.getString("id_abonne");
      emprunt[5] = rset.getString("nom");
      emprunt[6] = rset.getString("prenom");
      //formatage de la Date      
      emprunt[7] = formatageDate.format(rset.getTimestamp("date_emprunt"));

      emprunts.add(emprunt);
    }
    
    rset.close();
    stmt.close();
    	
    return emprunts;
  }

  /**
   * Récupération de la liste des emprunts en cours pour un abonné donné.
   * 
   * @return un <code>ArrayList<String[]></code>. Chaque tableau de chaînes
   * de caractères contenu correspond à un emprunt en cours pour l'abonné.<br/>
   * Il doit contenir 5 éléments (dans cet ordre) :
   * <ul>
   *   <li>0 : id de l'exemplaire</li>
   *   <li>1 : id du livre correspondant</li>
   *   <li>2 : titre du livre</li>
   *   <li>3 : son auteur</li>
   *   <li>4 : la date de l'emprunt</li>
   * </ul>
   * @throws SQLException en cas d'erreur de connexion à la base.
   */
  public static ArrayList<String[]> listeEmpruntsEnCours(int idAbonne) throws SQLException {
    ArrayList<String[]> emprunts = new ArrayList<String[]>();
    //
    // A COMPLETER
    //
    Statement stmt = Connexion.getConnection().createStatement();
    String query = "select exemplaire.id as id_exemplaire,livre.id as id_livre, livre.titre, livre.auteur, emprunt.date_emprunt" 
    				+ " from emprunt"
    				+ " inner join exemplaire on exemplaire.id = emprunt.exemplaire_id"
    				+ " inner join livre on livre.id = exemplaire.livre_id"
    				+ " inner join usager on usager.id=emprunt.abonne_id"
    				+ " where emprunt.date_retour is null and usager.id="+ idAbonne;	
    
    ResultSet rset = stmt.executeQuery(query);

    while (rset.next()) {
      String[] emprunt = new String[5];
      emprunt[0] = rset.getString("id_exemplaire");
      emprunt[1] = rset.getString("id_livre");
      emprunt[2] = rset.getString("titre");
      emprunt[3] = rset.getString("auteur");
      // formatage de la Date      
      emprunt[4] = formatageDate.format(rset.getTimestamp("date_emprunt"));
      emprunts.add(emprunt);
    }
    
    rset.close();
    stmt.close();
    	
    return emprunts;
  }

  /**
   * Récupération de la liste complète des emprunts passés.
   * 
   * @return un <code>ArrayList<String[]></code>. Chaque tableau de chaînes
   * de caractères contenu correspond à un emprunt passé.<br/>
   * Il doit contenir 9 éléments (dans cet ordre) :
   * <ul>
   *   <li>0 : id de l'exemplaire</li>
   *   <li>1 : id du livre correspondant</li>
   *   <li>2 : titre du livre</li>
   *   <li>3 : son auteur</li>
   *   <li>4 : id de l'abonné</li>
   *   <li>5 : nom de l'abonné</li>
   *   <li>6 : son prénom</li>
   *   <li>7 : la date de l'emprunt</li>
   *   <li>8 : la date de retour</li>
   * </ul>
   * @return un <code>ArrayList</code> contenant autant de tableaux de String (5 chaînes de caractères) que d'emprunts dans la base.
   * @throws SQLException en cas d'erreur de connexion à la base.
   */
  public static ArrayList<String[]> listeEmpruntsHistorique() throws SQLException {
    ArrayList<String[]> emprunts = new ArrayList<String[]>();
    //
    // A COMPLETER
    //
    Statement stmt = Connexion.getConnection().createStatement();
    String query = "select exemplaire.id as id_exemplaire,livre.id as id_livre, livre.titre, livre.auteur, usager.id as id_abonne, usager.nom, usager.prenom, emprunt.date_emprunt, emprunt.date_retour" 
    				+ " from emprunt"
    				+ " inner join exemplaire on exemplaire.id = emprunt.exemplaire_id"
    				+ " inner join livre on livre.id = exemplaire.livre_id"
    				+ " inner join usager on usager.id=emprunt.abonne_id"
    				+ " where emprunt.date_retour is not null";	    
    
    ResultSet rset = stmt.executeQuery(query);

    while (rset.next()) {
      String[] emprunt = new String[9];
      emprunt[0] = rset.getString("id_exemplaire");
      emprunt[1] = rset.getString("id_livre");
      emprunt[2] = rset.getString("titre");
      emprunt[3] = rset.getString("auteur");
      emprunt[4] = rset.getString("id_abonne");
      emprunt[5] = rset.getString("nom");
      emprunt[6] = rset.getString("prenom");
   // formatage de la Date      
      emprunt[7] = formatageDate.format(rset.getTimestamp("date_emprunt"));
      emprunt[8] = formatageDate.format(rset.getTimestamp("date_retour"));

      emprunts.add(emprunt);
    }
    
    rset.close();
    stmt.close();
    return emprunts;
  }

  /**
   * Emprunter un exemplaire à partir de l'identifiant de l'abonné et de
   * l'identifiant de l'exemplaire.
   * 
   * @param idAbonne : id de l'abonné emprunteur.
   * @param idExemplaire id de l'exemplaire emprunté.
   * @throws SQLException en cas d'erreur de connexion à la base.
   */
  public static void emprunter(int idAbonne, int idExemplaire) throws SQLException {
    //
    // A COMPLETER
    //
	  if (!estEmprunte(idExemplaire)){
		  String query = "insert into emprunt values(?,?,?,NULL)";
	      PreparedStatement ps = Connexion.getConnection().prepareStatement(query);
	      Timestamp date_emprunt = new Timestamp(new Date().getTime()) ;
	      ps.setInt(1,idAbonne);
	      ps.setInt(2,idExemplaire);
	      ps.setTimestamp(3,date_emprunt) ;
	      ps.executeUpdate(); 
	      ps.close();
	  }
	  else throw new SQLException("Exemplaire dejà emmprunté");
      
	  
  }

  /**
   * Retourner un exemplaire à partir de son identifiant.
   * 
   * @param idExemplaire id de l'exemplaire à rendre.
   * @throws SQLException en cas d'erreur de connexion à la base.
   */
  public static void rendre(int idExemplaire) throws SQLException {
    //
    // A COMPLETER
    //
	  if (estEmprunte(idExemplaire)){
		  java.sql.Timestamp date_retour = new java.sql.Timestamp(new Date().getTime()) ;
		  
		  String query= "update emprunt set  date_retour = ? where exemplaire_id = ? and date_retour is null";
		  
		  PreparedStatement retourEmprunt = Connexion.getConnection().prepareStatement(query);
		  
		  retourEmprunt.setTimestamp(1,date_retour);
		  retourEmprunt.setInt(2,idExemplaire);
		  
		  retourEmprunt.executeUpdate(); 
	  }
	  else {
		  throw new SQLException("Exemplaire non emprunté, retour impossible");
	  }
	  
  }
  
  /**
   * Détermine si un exemplaire sonné connu par son identifiant est
   * actuellement emprunté.
   * 
   * @param idExemplaire
   * @return <code>true</code> si l'exemplaire est emprunté, <code>false</code> sinon
   * @throws SQLException en cas d'erreur de connexion à la base.
   */
  public static boolean estEmprunte(int idExemplaire) throws SQLException {
    //
    // A COMPLETER
    //
    Statement stmt = Connexion.getConnection().createStatement(); // creation d'une instanxce de la connection
    String query = "select * from emprunt where date_retour is null and exemplaire_id=" + idExemplaire; // creation de la requette
    ResultSet rset = stmt.executeQuery(query); // execution de la requette
    
    if(rset.next()){
    	rset.close();
    	stmt.close() ;
    	return true;
    }
    else{
    	return false ;
    }
   
  }

  /**
   * Récupération des statistiques sur les emprunts (nombre d'emprunts et de
   * retours par jour).
   * 
   * @return un <code>HashMap<String, int[]></code>. Chaque enregistrement de la
   * structure de données est identifiée par la date (la clé) exprimée sous la forme
   * d'une chaîne de caractères. La valeur est un tableau de 2 entiers qui représentent :
   * <ul>
   *   <li>0 : le nombre d'emprunts</li>
   *   <li>1 : le nombre de retours</li>
   * </ul>
   * Exemple :
   * <pre>
   * +-------------------------+
   * | "2017-04-01" --> [3, 1] |
   * | "2017-04-02" --> [0, 1] |
   * | "2017-04-07" --> [5, 9] |
   * +-------------------------+
   * </pre>
   *   
   * @throws SQLException
   */
  public static HashMap<String, int[]> statsEmprunts() throws SQLException
  {
    HashMap<String, int[]> stats = new HashMap<String, int[]>();
    //
    // A COMPLETER
    //
    Statement stmt = Connexion.getConnection().createStatement();
    Statement stmtRetour = Connexion.getConnection().createStatement();            
    Statement stmtEmprunt = Connexion.getConnection().createStatement();
    String jour = " ";

    // selection des dates de la table emprunt

    String queryJour = "select date_jour from ( "
				+ " select date(date_retour) as date_jour from emprunt where date_retour is not null "
				+ " union"
				+ " select date(date_emprunt) as date_jour from emprunt ) as jours order by date_jour asc ";

	ResultSet rsetJour = stmt.executeQuery(queryJour);
	
	while (rsetJour.next()) {
		int[] nbreEmpRet = new int[2];
		jour = rsetJour.getString("date_jour");
		String queryNbreRetour = "select count(date_retour) as nbre_retour from emprunt"
								+ " where date(date_retour) = " + "'" + jour + "'";

		String queryNbreEmprunt = "select count(date_emprunt) as nbre_emprunt from emprunt "
								+ " where date(date_emprunt) =" + "'" + jour + "'";

		ResultSet rsetNbreEmprunt = stmtEmprunt.executeQuery(queryNbreEmprunt);
		
		if (rsetNbreEmprunt.next()){
			nbreEmpRet[0] = rsetNbreEmprunt.getInt("nbre_emprunt");
		}

		ResultSet rsetNbreRetour = stmtRetour.executeQuery(queryNbreRetour);

		if (rsetNbreRetour.next()){
			nbreEmpRet[1] = rsetNbreRetour.getInt("nbre_retour");
		}

		rsetNbreRetour.close();
		rsetNbreEmprunt.close();
		stats.put(jour, nbreEmpRet);

	}

	stmtRetour.close();
	stmtEmprunt.close();
	rsetJour.close();
	stmt.close();	

    return stats;
  }
  
  
  
  /**
   * Supprime une ligne dans un emprunt qui n'est pas en cours
   * 
   * @return un boolean true pour delete et false pour not delete
   * il recoit en parametre 2 valeur, 
   * id : l'id de l'element a supprimer exemple id_Abonne ou id_exemplaire
   * si element est egal a 1 alors utiliser l'id_Abonne pour supprimer l'emprunt
   * Sinon utiliser l'id_Exemplaire pour supprimer l'emprunt
   *   <li>0 : le nombre d'emprunts</li>
   *   <li>1 : le nombre de retours</li>
   * </ul>
   *   
   * @throws SQLException
   */
  
  public static void supprimerDansEmprunt(int id, int elemnt) throws SQLException
  {
	  
	  Statement stmt = Connexion.getConnection().createStatement();// creation d'une instanxce de la connection	  
	  
	  String query = "delete from emprunt where date_retour is not null and exemplaire_id=" + id; // suppression avec id_exemplaire
	  
	  if(elemnt==1) // suppression avec id_abonne
	  {
		  query = "delete from emprunt where date_retour is not null and abonne_id=" + id;
	  }
	    
	  stmt.executeUpdate(query); // execution de la requette
	  stmt.close();
	   
  }
}
