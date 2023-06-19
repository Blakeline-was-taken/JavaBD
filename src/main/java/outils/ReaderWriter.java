package outils;

import java.io.*;

/**
 * La classe ReaderWriter contient des méthodes pour lire et écrire des objets dans un fichier.
 */
public class ReaderWriter {
    /**
     * Lit un objet à partir d'un fichier.
     *
     * @param file Le fichier à lire
     * @return L'objet lu à partir du fichier
     */
    public static Object read(File file) {
        ObjectInputStream stream;
        Object readObject = null;

        // Ouverture du fichier en mode lecture
        try {
            stream = new ObjectInputStream(new FileInputStream(file));
            readObject = stream.readObject();
            stream.close();
        } catch (ClassNotFoundException parException) {
            System.err.println("Erreur de classe :\n" + parException);
            System.exit(1);
        } catch (IOException parException) {
            System.err.println("Erreur lecture du fichier :\n" + parException);
            System.exit(1);
        }
        return readObject;
    }

    /**
     * Écrit un objet dans un fichier.
     *
     * @param file   Le fichier dans lequel écrire
     * @param object L'objet à écrire dans le fichier
     */
    public static void write(File file, Object object) {
        ObjectOutputStream stream;

        // Ouverture du fichier en mode écriture
        try {
            stream = new ObjectOutputStream(new FileOutputStream(file));
            stream.writeObject(object);
            stream.flush();
            stream.close();
        } catch (IOException parException) {
            System.err.println("Problème à l'écriture :\n" + parException);
            System.exit(1);
        }
    }
}