package biblio;

import java.sql.Connection; 
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.ArrayList;

/**
 * Composant logiciel assurant la gestion des abonnés.
 */
public class ComposantBDAbonne {

  /**
   * Récupération de la liste complète des abonnés.
   * 
   * @return un <code>ArrayList<String[]></code>. Chaque tableau de chaînes
   * de caractères contenu correspond à un abonné.<br/>
   * Il doit contenir 5 éléments (dans cet ordre) :
   * <ul>
   *   <li>0 : id</li>
   *   <li>1 : nom</li>
   *   <li>2 : prénom</li>
   *   <li>3 : statut</li>
   *   <li>4 : adresse email</li>
   * </ul>
   * @throws SQLException en cas d'erreur de connexion à la base.
   */
  public static ArrayList<String[]> listeTousLesAbonnes() throws SQLException {
    // L'ArrayList qui sera renvoyé : structure de données de type tableau non limitée en taille
    ArrayList<String[]> abonnes = new ArrayList<String[]>();
    Statement stmt = Connexion.getConnection().createStatement();
    String query = "select * from usager";
    ResultSet rset = stmt.executeQuery(query);

    while (rset.next()) {
      String[] abonne = new String[5];
      abonne[0] = rset.getString("id");
      abonne[1] = rset.getString("nom");
      abonne[2] = rset.getString("prenom");
      abonne[3] = rset.getString("statut");
      abonne[4] = rset.getString("email");
      abonnes.add(abonne);
    }
    
    rset.close();
    stmt.close();
    
    return abonnes;
  }

  /**
   * Retourne le nombre d'abonnés référencés dans la base.
   * 
   * @return le nombre d'abonnés.
   * @throws SQLException en cas d'erreur de connexion à la base.
   */
  public static int nbAbonnes() throws SQLException {
	  
	  Statement stmt = Connexion.getConnection().createStatement(); // creation d'une instanxce de la connection
	  String query = "select distinct count(id) as nbre_usager from usager"; // creation de la requette
	  ResultSet rset = stmt.executeQuery(query);
	  rset.next();
	  int nbre_abonne = rset.getInt("nbre_usager");
	  rset.close();
	  stmt.close();
		
	  return nbre_abonne;
  }

  /**
   * Récupération des informations sur un abonné à partir de son identifiant.
   * 
   * @param idAbonne : id de l'abonné à rechercher
   * @return un tableau de chaînes de caractères (<code>String[]</code>). Chaque
   * tableau doit contenir 5 éléments (dans cet ordre) :
   * <ul>
   *   <li>0 : id</li>
   *   <li>1 : nom</li>
   *   <li>2 : prénom</li>
   *   <li>3 : statut</li>
   *   <li>4 : adresse email</li>
   * </ul>
   * @throws SQLException en cas d'erreur de connexion à la base.
   */
  public static String[] getAbonne(int idAbonne) throws SQLException {
	  
    String[] abonne = new String[5];
    Statement stmt = Connexion.getConnection().createStatement(); // creation d'une instanxce de la connection
    String query = "select * from usager where id=" + idAbonne; // creation de la requette
    ResultSet rset = stmt.executeQuery(query); // execution de la requette
    
    while (rset.next()) {					// chargement des informations
    	abonne[0] = rset.getString("id");
        abonne[1] = rset.getString("nom");
        abonne[2] = rset.getString("prenom");
        abonne[3] = rset.getString("statut");
        abonne[4] = rset.getString("email");

      }
    
    rset.close(); // fermeture de la connection
    stmt.close();
    return abonne;
  }

  /**
   * Référencement d'un nouvel abonné dans la base de données.
   * 
   * @param nom
   * @param prenom
   * @param statut (deux valeurs possibles <i>Etudiant</i> et <i>Enseignant</i>)
   * @param email
   * @return l'identifiant de l'abonné référencé.
   * @throws SQLException en cas d'erreur de connexion à la base.
   */
  public static int insererNouvelAbonne(String nom, String prenom, String statut, String email) throws SQLException {
	  
	  int testeur_insertion = 0; // variable pour verifier l'insertion 	  
	  String query1 = "insert into usager values(nextval('usager_id_seq'), ?, ?,?,?)"; // preparation de la requette
	  PreparedStatement ps = Connexion.getConnection().prepareStatement(query1);
	  ps.setString(1,nom);
	  ps.setString(2,prenom);
	  ps.setString(3,statut);
	  ps.setString(4,email);	  
	  testeur_insertion = ps.executeUpdate(); 
	  ps.close();
	  
	  if(testeur_insertion==0)
	  {
		  return -1; // la requette n'a pas été éxécuté
		  
	  }else
	  {
		  Statement stmt = Connexion.getConnection().createStatement();
		  String query2 = "select currval('usager_id_seq') as valeur_courante_id_usager";
		  ResultSet rset = stmt.executeQuery(query2);
		  rset.next();
		  int id = rset.getInt("valeur_courante_id_usager");
		  rset.close();
		  stmt.close();
		  
		  return id ;
	  }
  }

  /**
   * Modification des informations d'un abonné donné connu à partir de son
   * identifiant : les nouvelles valeurs (nom, prenom, etc.) écrasent les anciennes.
   * 
   * @param idAbonne : identifiant de l'abonné dont on veut modifier les informations.
   * @param nom
   * @param prenom
   * @param statut (deux valeurs possibles <i>Etudiant</i> et <i>Enseignant</i>)
   * @param email
   * @throws SQLException en cas d'erreur de connexion à la base.
   */
  public static void modifierAbonne(int idAbonne, String nom, String prenom, String statut, String email) throws SQLException {
	 String query= "update usager set nom= ?, prenom= ?, statut=?, email = ? where id= ? ";	
		  
	 PreparedStatement updateAbonne = Connexion.getConnection().prepareStatement(query);
		  
	 updateAbonne.setString(1,nom);
	 updateAbonne.setString(2,prenom);
	 updateAbonne.setString(3,statut);
	 updateAbonne.setString(4,email);	 
	 updateAbonne.setInt(5,idAbonne);
	 updateAbonne.executeUpdate(); 
		  
	 updateAbonne.close();
  }

  /**
   * Suppression d'un abonné connu à partir de son identifiant.
   * 
   * @param idAbonne : identifiant de l'utilisateur
   * @throws SQLException en cas d'erreur de connexion à la base.
   */
  public static void supprimerAbonne(int idAbonne) throws SQLException {
	
	  		// verification s'il y a un emprunt en cours car si oui suppression impossible
		 if(ComposantBDEmprunt.listeEmpruntsEnCours(idAbonne).isEmpty())
		 {
			 ComposantBDEmprunt.supprimerDansEmprunt(idAbonne, 1);		 
			 Statement stmt = Connexion.getConnection().createStatement();// creation d'une instanxce de la connection
			 String query = "delete from usager where id=" + idAbonne;

			 stmt.executeUpdate(query); // execution de la requette
			 stmt.close();
		 }
		 else throw new SQLException("Suppression abonné impossible") ;
  }
}
