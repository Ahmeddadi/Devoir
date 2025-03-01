package modeles;

public class Employe {
    private int id;
    private String nom;
    private String prenom;
    private String numSecuriteSociale;
    private String adresse;
    private String telephone;
    private String email;
    private String motDePasse;
    private Departement departement;
    private Role role;

    public Employe(int id, String nom, String prenom, String numSecuriteSociale, String adresse, String telephone, String email, String motDePasse, Departement departement, Role role) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.numSecuriteSociale = numSecuriteSociale;
        this.adresse = adresse;
        this.telephone = telephone;
        this.email = email;
        this.motDePasse = motDePasse;
        this.departement = departement;
        this.role = role;
    }

    public Employe(int id2, String nom2, String prenom2, String numSecu, String adresse2, String telephone2,
			String email2, String motDePasse2, Departement departement2, javax.management.relation.Role role2) {
		// TODO Auto-generated constructor stub
	}

	public int getId() { return id; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getNumSecuriteSociale() { return numSecuriteSociale; }
    public String getAdresse() { return adresse; }
    public String getTelephone() { return telephone; }
    public String getEmail() { return email; }
    public String getMotDePasse() { return motDePasse; }
    public Departement getDepartement() { return departement; }
    public Role getRole() { return role; }

    public void setAdresse(String adresse) { this.adresse = adresse; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }
}
