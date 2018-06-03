package biblio;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.sql.PreparedStatement;
/**
 * Composant logiciel assurant la gestion des livres et des exemplaires
 * de livre.
 */
public class ComposantBDLivre {

  /**
   * Récupération de la liste complète des livres.
   * 
   * @return un <code>ArrayList<String[]></code>. Chaque tableau de chaînes
   * de caractères contenu correspond à un livre.<br/>
   * Il doit contenir 5 éléments (dans cet ordre) :
   * <ul>
   *   <li>0 : id</li>
   *   <li>1 : isbn10</li>
   *   <li>2 : isbn13</li>
   *   <li>3 : titre</li>
   *   <li>4 : auteur</li>
   * </ul>
   * @throws SQLException en cas d'erreur de connexion à la base.
   */
  public static ArrayList<String[]> listeTousLesLivres() throws SQLException {

    ArrayList<String[]> livres = new ArrayList<String[]>();

    Statement stmt = Connexion.getConnection().createStatement();
    String sql = "select * from livre";
    ResultSet rset = stmt.executeQuery(sql);

    while (rset.next()) {
      String[] livre = new String[5];
      livre[0] = rset.getString("id");
      livre[1] = rset.getString("isbn10");
      livre[2] = rset.getString("isbn13");
      livre[3] = rset.getString("titre");
      livre[4] = rset.getString("auteur");

      livres.add(livre);
    }
    rset.close();
    stmt.close();

    return livres;
  }

  /**
   * Retourne le nombre de livres référencés dans la base.
   * 
   * @return le nombre de livres.
   * @throws SQLException en cas d'erreur de connexion à la base.
   */
  public static int nbLivres() throws SQLException {
    //
    // A COMPLETER
    //
	Statement stmt = Connexion.getConnection().createStatement();
	String query = "select distinct count(id) as nbre_livre from livre";
	ResultSet rset = stmt.executeQuery(query);
	rset.next();
	int nbre_livre = rset.getInt("nbre_livre");
	rset.close();
	stmt.close();
	return nbre_livre;
  }

  /**
   * Récupération des informations sur un livre connu à partir de son identifiant.
   * 
   * @param idLivre : id du livre à rechercher
   * @return un tableau de chaînes de caractères (<code>String[]</code>). Chaque
   * tableau doit contenir 5 éléments (dans cet ordre) :
   * <ul>
   *   <li>0 : id</li>
   *   <li>1 : isbn10</li>
   *   <li>2 : isbn13</li>
   *   <li>3 : titre</li>
   *   <li>4 : auteur</li>
   * </ul>
   * @throws SQLException en cas d'erreur de connexion à la base.
   */
   public static String[] getLivre(int idLivre) throws SQLException {
     String[] livre = new String[5];
     Statement stmt = Connexion.getConnection().createStatement(); 
     String query = "select * from livre where id=" + idLivre; 
     ResultSet rset = stmt.executeQuery(query);  
     
     while (rset.next()) {	
         livre[0] = rset.getString("id");
         livre[1] = rset.getString("isbn10");
         livre[2] = rset.getString("isbn13");
         livre[3] = rset.getString("titre");
         livre[4] = rset.getString("auteur");

       }
     
     rset.close(); 
     stmt.close();
     return livre;
   }
  
 /**
  * Récupération des informations sur un livre connu à partir de l'identifiant
  * de l'un de ses exemplaires.
  * 
  * @param idExemplaire : id de l'exemplaire
  * @return un tableau de chaînes de caractères (<code>String[]</code>). Chaque
  * tableau doit contenir 6 éléments (dans cet ordre) :
  * <ul>
  *   <li>0 : id de l'exemplaire</li>
  *   <li>1 : id du livre</li>
  *   <li>2 : isbn10</li>
  *   <li>3 : isbn13</li>
  *   <li>4 : titre</li>
  *   <li>5 : auteur</li>
  * </ul>
  * @throws SQLException en cas d'erreur de connexion à la base.
  */
  public static String[] getLivreParIdExemplaire(int idExemplaire) throws SQLException {
    String[] livre = new String[6];
    
    Statement stmt = Connexion.getConnection().createStatement(); 
    String query = "select exemplaire.id as id_exemplaire, livre.id as id_livre, livre.isbn10, livre.isbn13, livre.titre, livre.auteur "
    				+ "from livre inner join exemplaire on livre.id = exemplaire.livre_id "
    				+ "where exemplaire.id="+ idExemplaire; 
    ResultSet rset = stmt.executeQuery(query); 
    
    while (rset.next()) {		
        livre[0] = rset.getString("id_exemplaire");
        livre[1] = rset.getString("id_livre");
        livre[2] = rset.getString("isbn10");
        livre[3] = rset.getString("isbn13");
        livre[4] = rset.getString("titre");
        livre[5] = rset.getString("auteur");

      }
    
    rset.close();
    stmt.close();
    
    return livre;
  }

  /**
   * Référencement d'un nouveau livre dans la base de données.
   * 
   * @param isbn10
   * @param isbn13
   * @param titre
   * @param auteur
   * @return l'identifiant (id) du livre créé.
   * @throws SQLException en cas d'erreur de connexion à la base.
   */
  public static int insererNouveauLivre(String isbn10, String isbn13, String titre, String auteur) throws SQLException {
		    
		  
		  //Verification de l'isbn du Livre a inséré
		  String query = "select * from livre where isbn10 = ? 	or  isbn13= ?";

		  
		  PreparedStatement ps = Connexion.getConnection().prepareStatement(query);
		  ps.setString(1, isbn10);
		  ps.setString(2, isbn13);
		  ResultSet rsetvef = ps.executeQuery();
		  if(rsetvef.next())
		  {
			  return -1;
		  }
		  
		  int insertion_test = 0; // insertion réussie ou pas
		  // preparation de la requette
		  String query1 = "insert into livre values(nextval('livre_id_seq'), ?, ?,?,?)";
		  PreparedStatement ps1 = Connexion.getConnection().prepareStatement(query1);
		  ps1.setString(1,isbn10);
		  ps1.setString(2,isbn13);
		  ps1.setString(3,titre);
		  ps1.setString(4,auteur);
		  insertion_test = ps1.executeUpdate(); 
		  ps1.close();
		  if(insertion_test==0)
		  {		 
			  return -1; // la requette n'a pas été éxécuté
			  
		  }else
		  {
			  Statement stmt = Connexion.getConnection().createStatement();
			  String query2 = "select currval('livre_id_seq') as id_livre";
			  ResultSet rset = stmt.executeQuery(query2);
			  rset.next();
			  int id = rset.getInt("id_livre");
			  rset.close();
			  stmt.close();
			  return id;
		  }

  }
  
/**
   * Modification des informations d'un livre donné connu à partir de son
   * identifiant : les nouvelles valeurs (isbn10, isbn13, etc.) écrasent les
   * anciennes.
   * 
   * @param idLivre : id du livre à modifier.
   * @param isbn10 : nouvelle valeur d'isbn10.
   * @param isbn13 : nouvelle valeur d'isbn13.
   * @param titre : nouvelle valeur du titre.
   * @param auteur : nouvel auteur.
   * @throws SQLException en cas d'erreur de connexion à la base.
   */
  public static void modifierLivre(int idLivre, String isbn10, String isbn13, String titre, String auteur) throws SQLException {
	  
	  String query= "update livre set isbn10= ?, isbn13= ?, titre=?, auteur = ? where id= ? ";
	  
	  PreparedStatement ps = Connexion.getConnection().prepareStatement(query);
	  
	  ps.setString(1,isbn10);
	  ps.setString(2,isbn13);
	  ps.setString(3,titre);
	  ps.setString(4,auteur);
	  ps.setInt(5,idLivre);
	  
	  ps.executeUpdate(); 
	  
	  ps.close();

  }

  /**
   * Suppression d'un abonné connu à partir de son identifiant.
   * 
   * @param idLivre : id du livre à supprimer.
   * @throws SQLException en cas d'erreur de connexion à la base.
   */
   public static void supprimerLivre(int idLivre) throws SQLException {
	   ArrayList<Integer> listeExemplaire= listeExemplaires(idLivre);
	   
	   
	   //1 et 2
	   for (Integer idexempl : listeExemplaire) 
	   {
		   if(!ComposantBDEmprunt.estEmprunte(idexempl)) continue;
		   {		   
			   //return false; 
		   }
		   
	   }
	   
	   //3
	   for (Integer idexempl : listeExemplaire) 
	   {
		  supprimerExemplaire(idexempl);
		   
	   }
	   //4 on supprime maintenant le Livre
	   Statement stmt = Connexion.getConnection().createStatement();// creation d'une instanxce de la connection
	   String query = "delete from livre where id=" + idLivre;
	   stmt.executeUpdate(query); // execution de la requette
	   stmt.close();
	   
	   //return true;   

   }

   /**
    * Retourne le nombre d'exemplaire d'un livre donné connu à partir
    * de son identifiant.
    * 
    * @param idLivre : id du livre dont on veut connaître le nombre d'exemplaires.
    * @return le nombre d'exemplaires
    * @throws SQLException en cas d'erreur de connexion à la base.
    */
   public static int nbExemplaires(int idLivre) throws SQLException {
	   
	   Statement stmt = Connexion.getConnection().createStatement(); 
	   String query = "select distinct count(id) as nbre_exemplaire from exemplaire where livre_id=" + idLivre ; 
	   ResultSet rset = stmt.executeQuery(query);
	   rset.next();
	   int nbre_exemplaires = rset.getInt("nbre_exemplaire");
	   rset.close();
	   stmt.close();
	 
     return nbre_exemplaires;
   }

  /**
   * Récupération de la liste des identifiants d'exemplaires d'un livre donné
   * connu à partir de son identifiant.
   * 
   * @param idLivre : identifiant du livre dont on veut la liste des exemplaires.
   * @return un <code>ArrayList<Integer></code> contenant les identifiants des exemplaires
   * @throws SQLException en cas d'erreur de connexion à la base.
   */
  public static ArrayList<Integer> listeExemplaires(int idLivre) throws SQLException {
	  
    ArrayList<Integer> exemplaires = new ArrayList<Integer>();
    Statement stmt = Connexion.getConnection().createStatement();
    String query = "select * from exemplaire where livre_id=" + idLivre;
    ResultSet rset = stmt.executeQuery(query);

    while (rset.next()) {
    	Integer exemplaire = 0; 
    	exemplaire = rset.getInt("id");
    	
      exemplaires.add(exemplaire);
    }
    rset.close();
    stmt.close();
    
    return exemplaires;
  }

  /**
   * Ajout d'un exemplaire à un livre donné connu par son identifiant.
   * 
   * @param id identifiant du livre dont on veut ajouter un exemplaire.
   * @throws SQLException en cas d'erreur de connexion à la base.
   */
   public static void ajouterExemplaire(int idLivre) throws SQLException {
	   Statement stmt = Connexion.getConnection().createStatement();
	   String query = "insert into exemplaire values(nextval('exemplaire_id_seq')," + idLivre + ")";
	   stmt.executeUpdate(query);
	   stmt.close();
   }

    /**
     * Suppression d'un exemplaire donné connu par son identifiant.
     * 
     * @param idExemplaire : identifiant du livre dont on veut supprimer un exemplaire.
     * @throws SQLException en cas d'erreur de connexion à la base.
     */
   public static void supprimerExemplaire(int idExemplaire) throws SQLException {
	   if(!ComposantBDEmprunt.estEmprunte(idExemplaire)){
		   ComposantBDEmprunt.supprimerDansEmprunt(idExemplaire, 2);
		   Statement stmt = Connexion.getConnection().createStatement();// creation d'une instanxce de la connection
		   String query = "delete from exemplaire where id=" + idExemplaire;
		   stmt.executeUpdate(query); // execution de la requette
		   stmt.close();
		 
	   }
	   else throw new SQLException("suprimer invalid!");

   }
   
   
}
