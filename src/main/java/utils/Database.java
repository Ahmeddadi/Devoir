package utils;

import modeles.*;

import java.util.*;

public class Database {
    public static Map<Integer, Employe> employes = new HashMap<>();
    public static Map<Integer, Departement> departements = new HashMap<>();
    public static List<Evaluation> evaluations = new ArrayList<>();
    private static int employeIdCounter = 1;
    private static int departementIdCounter = 1;

    static {
        System.out.println("[DATABASE] Initialisation des données...");
        
        // Création du département "Administration"
        Departement adminDep = new Departement(departementIdCounter++, "Administration");
        departements.put(adminDep.getId(), adminDep);

        // Création de l'administrateur par défaut
        Employe admin = new Employe(
            employeIdCounter++, "Admin", "Admin", "0000", "Adresse", "0000000000",
            "admin@example.com", "admin", adminDep, Role.ADMIN
        );
        employes.put(admin.getId(), admin);

        System.out.println("[DATABASE] Administrateur ajouté : " + admin.getEmail() + " (ID: " + admin.getId() + ")");

        // Création du département "Développement"
        Departement devDep = new Departement(departementIdCounter++, "Développement");
        departements.put(devDep.getId(), devDep);

        // Ajout des employés
        Employe employe1 = new Employe(
            employeIdCounter++, "Dupont", "Jean", "123456", "10 rue Paris", "0606060606",
            "jean@example.com", "password123", devDep, Role.EMPLOYE
        );

        Employe employe2 = new Employe(
            employeIdCounter++, "Martin", "Sophie", "789012", "5 rue Lyon", "0707070707",
            "sophie@example.com", "password456", devDep, Role.RESPONSABLE
        );

        employes.put(employe1.getId(), employe1);
        employes.put(employe2.getId(), employe2);

        System.out.println("[DATABASE] Employé ajouté : " + employe1.getEmail() + " (ID: " + employe1.getId() + ")");
        System.out.println("[DATABASE] Employé ajouté : " + employe2.getEmail() + " (ID: " + employe2.getId() + ")");

        // Vérification des données initialisées
        System.out.println("[DATABASE] Nombre total d'employés : " + employes.size());
        System.out.println("[DATABASE] Nombre total de départements : " + departements.size());
    }

    public static int generateEmployeId() { return employeIdCounter++; }
    public static int generateDepartementId() { return departementIdCounter++; }
}
