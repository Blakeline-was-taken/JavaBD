package modele;

import java.util.TreeSet;

public class Client {
    public static void main(String[] args) {
        testSelect();
        testRemove();
        testJoin();
    }

    public static void testSelect(){
        DataTable employees = generateEmployees();
        employees.printTable();
        System.out.println();

        // Test 1 : Sélection de toutes les colonnes sans condition
        DataTable result1 = employees.select(new TreeSet<>());
        System.out.println("Test 1: Sélection de toutes les colonnes sans condition");
        result1.printTable();
        System.out.println();

        // Test 2 : Sélection des colonnes "ID", "First Name" et "Email" sans condition
        TreeSet<String> fields2 = new TreeSet<>();
        fields2.add("ID");
        fields2.add("First Name");
        fields2.add("Email");
        DataTable result2 = employees.select(fields2);
        System.out.println("Test 2: Sélection des colonnes \"ID\", \"First Name\" et \"Email\" sans condition");
        result2.printTable();
        System.out.println();

        // Test 3 : Sélection des colonnes "ID" et "Last Name" avec la condition "Gender = 'Female'"
        TreeSet<String> fields3 = new TreeSet<>();
        fields3.add("ID");
        fields3.add("Last Name");
        Condition condition3 = new ValueCondition("Gender", "=", "Female");
        DataTable result3 = employees.select(fields3, condition3);
        System.out.println("Test 3: Sélection des colonnes \"ID\" et \"Last Name\" avec la condition \"Gender = 'Female'\"");
        result3.printTable();
        System.out.println();

        // Test 4 : Sélection de toutes les colonnes avec la condition "Salary >= 5000.00"
        Condition condition4 = new ValueCondition("Salary", ">=", 5000.00);
        DataTable result4 = employees.select(new TreeSet<>(), condition4);
        System.out.println("Test 4: Sélection de toutes les colonnes avec la condition \"Salary >= 5000.00\"");
        result4.printTable();
        System.out.println();

        // Test 5 : Sélection des colonnes "First Name" et "Last Name" avec la condition "Age < 30" et "Has Raise = true"
        TreeSet<String> fields5 = new TreeSet<>();
        fields5.add("First Name");
        fields5.add("Last Name");
        Condition condition5a = new ValueCondition("Age", "<", 30);
        Condition condition5b = new ValueCondition("Has Raise", "=", true);
        DataTable result5 = employees.select(fields5, condition5a, condition5b);
        System.out.println("Test 5: Sélection des colonnes \"First Name\" et \"Last Name\" avec la condition \"Age < 30\" et \"Has Raise = true\"");
        result5.printTable();
        System.out.println();

        // Test 6 : Sélection de la colonne "*" avec la condition "Age = ID"
        TreeSet<String> fields6 = new TreeSet<>();
        fields6.add("*");
        Condition condition6 = new ColumnCondition("Age", "=", "ID");
        DataTable result6 = employees.select(fields6, condition6);
        System.out.println("Test 6: Sélection de la colonne \"*\" avec la condition \"Age = ID\"");
        result6.printTable();
    }

    public static void testJoin(){
        // Création de la première table
        DataTable table1 = new DataTable("Table1");
        table1.addColumn("ID", Integer.class);
        table1.addColumn("Nom", String.class);
        table1.addColumn("Ville", String.class);
        table1.addColumn("Âge", Integer.class);

        table1.insertRow(1, "Alice", "Paris", 25);
        table1.insertRow(2, "Bob", "Berlin", 30);
        table1.insertRow(3, "Charlie", "Madrid", 35);

        // Affichage de la première table
        table1.printTable();
        System.out.println();

        // Création de la deuxième table
        DataTable table2 = new DataTable("Table2");
        table2.addColumn("Ville", String.class);
        table2.addColumn("Habitants", Integer.class);
        table2.addColumn("Pays", String.class);

        table2.insertRow("Paris", 2200000, "France");
        table2.insertRow("Berlin", 3500000, "Allemagne");
        table2.insertRow("Londres", 8900000, "Royaume-Uni");

        // Affichage de la deuxième table
        table2.printTable();
        System.out.println();

        // Join des deux tables
        table1.join(table2, "Ville", "Ville").printTable();
    }

    public static void testRemove() {
        // Création de la table de données
        DataTable table = generateEmployees();

        // Affichage de la table avant les suppressions
        System.out.println("Table avant les suppressions :");
        table.printTable();
        System.out.println();

        // Suppression d'une ligne
        try {
            System.out.println("Suppression de la ligne d'index 1 :");
            table.removeRow(1);
            table.printTable();
        } catch (Exception e) {
            System.out.println("Erreur lors de la suppression de la ligne : " + e.getMessage());
        }
        System.out.println();

        // Suppression d'un nom de colonne
        try {
            System.out.println("Suppression de la colonne \"Age\" :");
            table.removeColumn("Age");
            table.printTable();
        } catch (Exception e) {
            System.out.println("Erreur lors de la suppression de la colonne : " + e.getMessage());
        }
        System.out.println();

        // Suppression d'un index de colonne
        try {
            System.out.println("Suppression de la colonne d'index 1 :");
            table.removeColumn(1);
            table.printTable();
        } catch (Exception e) {
            System.out.println("Erreur lors de la suppression de la colonne : " + e.getMessage());
        }
        System.out.println();

        // Suppression d'une cellule par index de colonne et index de ligne
        try {
            System.out.println("Suppression de la cellule à la colonne d'index 1 et à la ligne d'index 0 :");
            table.removeCell(1, 0);
            table.printTable();
        } catch (Exception e) {
            System.out.println("Erreur lors de la suppression de la cellule : " + e.getMessage());
        }
        System.out.println();

        // Suppression d'une cellule par nom de colonne et index de ligne
        try {
            System.out.println("Suppression de la cellule à la colonne \"Salary\" et à la ligne d'index 4 :");
            table.removeCell("Salary", 4);
            table.printTable();
        } catch (Exception e) {
            System.out.println("Erreur lors de la suppression de la cellule : " + e.getMessage());
        }
        System.out.println();

        // Suppression d'une ligne illégale
        try {
            System.out.println("Suppression de la ligne d'index 15 :");
            table.removeRow(15);
            table.printTable();
        } catch (Exception e) {
            System.out.println("Erreur lors de la suppression de la ligne : " + e.getMessage());
        }
        System.out.println();

        // Suppression d'une colonne illégale
        try {
            System.out.println("Suppression de la colonne d'index -1 :");
            table.removeColumn(-1);
            table.printTable();
        } catch (Exception e) {
            System.out.println("Erreur lors de la suppression de la colonne : " + e.getMessage());
        }
        System.out.println();



        // Affichage de la table après les suppressions
        System.out.println("Table après les suppressions :");
        table.printTable();
    }

    public static DataTable generateEmployees() {
        // Création de la table
        DataTable employees = new DataTable("Employees");

        // Ajout des colonnes à la table
        employees.addColumn("ID", Integer.class);
        employees.addColumn("First Name", String.class);
        employees.addColumn("Last Name", String.class);
        employees.addColumn("Age", Integer.class);
        employees.addColumn("Gender", String.class);
        employees.addColumn("Position", String.class);
        employees.addColumn("Department", String.class);
        employees.addColumn("Salary", Double.class);
        employees.addColumn("Hire Date", Date.class);
        employees.addColumn("Phone", String.class);
        employees.addColumn("Email", String.class);
        employees.addColumn("Address", String.class);
        employees.addColumn("City", String.class);
        employees.addColumn("Country", String.class);
        employees.addColumn("Has Raise", Boolean.class);

        // Insertion des données dans la table
        employees.insertRow(1, "John", "Doe", 30, "Male", "Manager", "Sales", 5000.00, new Date(10, 5, 1985), "+1 123-456-7890", "john.doe@example.com", "123 Main St", "New York", "USA", true);
        employees.insertRow(2, "Jane", "Smith", 28, "Female", "Supervisor", "Marketing", 4500.00, new Date(15, 9, 1990), "+1 987-654-3210", "jane.smith@example.com", "456 Elm St", "Los Angeles", "USA", false);
        employees.insertRow(3, "Michael", "Johnson", 35, "Male", "Manager", "Finance", 5500.00, new Date(25, 2, 1980), "+1 555-123-4567", "michael.johnson@example.com", "789 Oak St", "Chicago", "USA", true);
        employees.insertRow(4, "Emily", "Brown", 32, "Female", "Analyst", "Finance", 4000.00, new Date(5, 12, 1988), "+1 222-333-4444", "emily.brown@example.com", "321 Pine St", "Houston", "USA", false);
        employees.insertRow(5, "David", "Taylor", 29, "Male", "Engineer", "Engineering", 4800.00, new Date(20, 7, 1992), "+1 777-888-9999", "david.taylor@example.com", "654 Maple St", "San Francisco", "USA", true);
        employees.insertRow(6, "Sarah", "Davis", 27, "Female", "Supervisor", "Marketing", 4300.00, new Date(12, 4, 1994), "+1 444-555-6666", "sarah.davis@example.com", "987 Walnut St", "Boston", "USA", false);
        employees.insertRow(7, "Robert", "Anderson", 33, "Male", "Analyst", "Sales", 4100.00, new Date(8, 8, 1987), "+1 666-777-8888", "robert.anderson@example.com", "852 Birch St", "Seattle", "USA", true);
        employees.insertRow(8, "Olivia", "Wilson", 31, "Female", "Engineer", "Engineering", 5200.00, new Date(18, 6, 1989), "+1 999-000-1111", "olivia.wilson@example.com", "753 Oak St", "Miami", "USA", false);
        employees.insertRow(9, "James", "Martin", 36, "Male", "Manager", "Finance", 6000.00, new Date(28, 3, 1984), "+1 999-888-7777", "james.martin@example.com", "369 Cedar St", "Denver", "USA", true);
        employees.insertRow(10, "Sophia", "Clark", 30, "Female", "Supervisor", "Sales", 4700.00, new Date(7, 11, 1991), "+1 333-444-5555", "sophia.clark@example.com", "246 Oak St", "Austin", "USA", false);
        employees.insertRow(11, "Daniel", "Lewis", 34, "Male", "Analyst", "Finance", 4200.00, new Date(14, 1, 1986), "+1 777-222-3333", "daniel.lewis@example.com", "135 Elm St", "Phoenix", "USA", true);
        employees.insertRow(12, "Ava", "Harris", 26, "Female", "Engineer", "Engineering", 4900.00, new Date(2, 10, 1995), "+1 111-222-3333", "ava.harris@example.com", "468 Pine St", "San Diego", "USA", false);
        employees.insertRow(13, "Matthew", "Walker", 33, "Male", "Manager", "Sales", 5700.00, new Date(16, 4, 1987), "+1 555-999-1111", "matthew.walker@example.com", "987 Walnut St", "Orlando", "USA", true);
        employees.insertRow(14, "Isabella", "Turner", 29, "Female", "Supervisor", "Marketing", 4400.00, new Date(23, 8, 1992), "+1 333-888-9999", "isabella.turner@example.com", "258 Maple St", "Atlanta", "USA", false);
        employees.insertRow(15, "Andrew", "Adams", 15, "Male", "Analyst", "Finance", 4100.00, new Date(11, 6, 1980), "+1 888-111-2222", "andrew.adams@example.com", "753 Oak St", "Miami", "USA", true);

        return employees;
    }
}
