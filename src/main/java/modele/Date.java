package modele;

import java.io.Serializable;

/**
 * La classe Date représente une date avec un jour, un mois et une année.
 */
public class Date implements Serializable {

    /**
     * Le jour de la date.
     */
    protected int chDay;

    /**
     * Le mois de la date.
     */
    protected int chMonth;

    /**
     * L'année de la date.
     */
    protected int chYear;

    /**
     * Constructeur de la classe Date pour une année spécifiée.
     *
     * @param year l'année
     */
    public Date(int year) {
        chDay = 1;
        chMonth = 1;
        chYear = year;
    }

    /**
     * Constructeur de la classe Date pour une date spécifiée.
     *
     * @param day   le jour
     * @param month le mois
     * @param year  l'année
     */
    public Date(int day, int month, int year) {
        chDay = day;
        chMonth = month;
        chYear = year;
    }

    /**
     * Renvoie une représentation sous forme de chaîne de caractères de la date.
     *
     * @return la représentation de la date sous forme de chaîne de caractères
     */
    public String toString() {
        return chDay + "/" + chMonth + "/" + chYear;
    }

    /**
     * Compare cette date à une autre date spécifiée.
     *
     * @param date la date à comparer
     * @return une valeur négative si cette date est antérieure à la date spécifiée,
     *         zéro si les dates sont égales, une valeur positive si cette date est ultérieure à la date spécifiée
     */
    public int compareTo(Date date) {
        int int1 = chYear * 10000 + chMonth * 100 + chDay;
        int int2 = date.chYear * 10000 + date.chMonth * 100 + date.chDay;
        return int1 - int2;
    }

    /**
     * Renvoie le dernier jour du mois pour le mois et l'année spécifiés.
     *
     * @param month le mois
     * @param year  l'année
     * @return le dernier jour du mois
     */
    public static int getLastDayOfMonth(int month, int year) {
        switch (month) {
            case 1, 3, 5, 7, 8, 10, 12 -> {
                return 31; // Mois avec 31 jours
            }
            case 2 -> {
                if (Date.isLeapYear(year)) {
                    return 29; // Février d'une année bissextile
                } else {
                    return 28; // Février d'une année non bissextile
                }
            }
            case 4, 6, 9, 11 -> {
                return 30; // Mois avec 30 jours
            }
            default -> {
                return -1; // Mois invalide
            }
        }
    }

    /**
     * Vérifie si l'année spécifiée est une année bissextile.
     *
     * @param year l'année
     * @return true si l'année est une année bissextile, false sinon
     */
    public static boolean isLeapYear(int year) {
        return year % 400 == 0 || (year % 4 == 0 && year % 100 != 0);
    }

    /**
     * Vérifie si la date est valide.
     *
     * @return true si la date est valide, false sinon
     */
    public boolean isValid() {
        return (chYear > 1582) && (0 < chMonth) && (chMonth < 13) &&
                (0 < chDay) && (chDay <= Date.getLastDayOfMonth(chMonth, chYear));
    }

    /**
     * Renvoie la date du jour suivant.
     *
     * @return la date du jour suivant
     */
    public Date getNextDay() {
        if (chDay == Date.getLastDayOfMonth(chMonth, chYear)) {
            if (chMonth == 12) {
                return new Date(1, 1, chYear + 1);
            } else {
                return new Date(1, chMonth + 1, chYear);
            }
        } else {
            return new Date(chDay + 1, chMonth, chYear);
        }
    }

    /**
     * Renvoie la date du jour précédent.
     *
     * @return la date du jour précédent
     */
    public Date getPreviousDay() {
        if (chDay == 1) {
            if (chMonth == 1) {
                return new Date(Date.getLastDayOfMonth(12, chYear - 1), 12, chYear - 1);
            } else {
                return new Date(Date.getLastDayOfMonth(chMonth - 1, chYear), chMonth - 1, chYear);
            }
        } else {
            return new Date(chDay - 1, chMonth, chYear);
        }
    }

    /**
     * Renvoie le jour de la date.
     *
     * @return le jour
     */
    public int getDay() {
        return chDay;
    }

    /**
     * Renvoie le mois de la date.
     *
     * @return le mois
     */
    public int getMonth() {
        return chMonth;
    }

    /**
     * Renvoie l'année de la date.
     *
     * @return l'année
     */
    public int getYear() {
        return chYear;
    }
}
