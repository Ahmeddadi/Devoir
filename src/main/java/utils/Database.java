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

        // 🔹 Création du département "Administration"
        Departement adminDep = new Departement(departementIdCounter++, "Administration");
        departements.put(adminDep.getId(), adminDep);

        // 🔹 Création de l'Administrateur
        Employe admin = new Employe(
            generateEmployeId(), "Admin", "Admin", "0000", "Adresse Admin", "0000000000",
            "admin@example.com", "admin", adminDep, Role.ADMIN
        );
        employes.put(admin.getId(), admin);
        System.out.println("[DATABASE] Administrateur ajouté : " + admin.getEmail() + " (ID: " + admin.getId() + ")");

        // 🔹 Création des départements
        Departement dep1 = new Departement(generateDepartementId(), "Département 1");
        Departement dep2 = new Departement(generateDepartementId(), "Département 2");
        departements.put(dep1.getId(), dep1);
        departements.put(dep2.getId(), dep2);

        // 🔹 Création des Responsables
        Employe responsable1 = new Employe(
            generateEmployeId(), "Moustapha", "Mouhamed", "1111", "10 rue Paris", "0606060606",
            "paul@example.com", "password1", dep1, Role.RESPONSABLE
        );
        Employe responsable2 = new Employe(
            generateEmployeId(), "Ahmed", "Elhaj Sidi", "2222", "20 rue Lyon", "0707070707",
            "claire@example.com", "password2", dep2, Role.RESPONSABLE
        );
        employes.put(responsable1.getId(), responsable1);
        employes.put(responsable2.getId(), responsable2);
        System.out.println("[DATABASE] Responsable ajouté : " + responsable1.getEmail() + " (ID: " + responsable1.getId() + ")");
        System.out.println("[DATABASE] Responsable ajouté : " + responsable2.getEmail() + " (ID: " + responsable2.getId() + ")");

        // 🔹 Création des employés sous chaque Responsable
        for (int i = 1; i <= 3; i++) {
            Employe empDep1 = new Employe(
                generateEmployeId(), "Employe" + i, "Dep1", "300" + i, "Adresse " + i, "061111111" + i,
                "emp" + i + "@dep1.com", "pass" + i, dep1, Role.EMPLOYE
            );
            employes.put(empDep1.getId(), empDep1);
            System.out.println("[DATABASE] Employé ajouté : " + empDep1.getEmail() + " (ID: " + empDep1.getId() + ") - Dép. 1");

            Employe empDep2 = new Employe(
                generateEmployeId(), "Employe" + i, "Dep2", "400" + i, "Adresse " + i, "071111111" + i,
                "emp" + i + "@dep2.com", "pass" + i, dep2, Role.EMPLOYE
            );
            employes.put(empDep2.getId(), empDep2);
            System.out.println("[DATABASE] Employé ajouté : " + empDep2.getEmail() + " (ID: " + empDep2.getId() + ") - Dép. 2");
        }

        // 🔹 Vérification finale
        System.out.println("[DATABASE] Nombre total d'employés : " + employes.size());
        System.out.println("[DATABASE] Nombre total de départements : " + departements.size());
    }

    // 🔹 Génère un ID unique pour un employé
    public static int generateEmployeId() {
        return employeIdCounter++;
    }

    // 🔹 Génère un ID unique pour un département
    public static int generateDepartementId() {
        return departementIdCounter++;
    }
}
