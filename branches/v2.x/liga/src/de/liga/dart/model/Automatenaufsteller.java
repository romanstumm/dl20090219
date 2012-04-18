package de.liga.dart.model;

/**
 * Automatenaufsteller 
 */
public class Automatenaufsteller  implements java.io.Serializable,
        LigaPersistence {


     private long aufstellerId;
     private Liga liga;
     private String aufstellerName;
     private String kontaktName;
     private String email;
     private String telefon;
     private String mobil;
     private String fax;
     private String plz;
     private String ort;
     private String strasse;
     private String externeId;
//     private Set<Spielort> spielorte = new HashSet<Spielort>(0);

    public Automatenaufsteller() {
    }
    
    public long getAufstellerId() {
        return this.aufstellerId;
    }
    
    public void setAufstellerId(long aufstellerId) {
        this.aufstellerId = aufstellerId;
    }
    public Liga getLiga() {
        return this.liga;
    }
    
    public void setLiga(Liga liga) {
        this.liga = liga;
    }
    public String getAufstellerName() {
        return this.aufstellerName;
    }
    
    public void setAufstellerName(String aufstellerName) {
        this.aufstellerName = aufstellerName;
    }
    public String getKontaktName() {
        return this.kontaktName;
    }
    
    public void setKontaktName(String kontaktName) {
        this.kontaktName = kontaktName;
    }
    public String getEmail() {
        return this.email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    public String getTelefon() {
        return this.telefon;
    }
    
    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }
    public String getMobil() {
        return this.mobil;
    }
    
    public void setMobil(String mobil) {
        this.mobil = mobil;
    }
    public String getFax() {
        return this.fax;
    }
    
    public void setFax(String fax) {
        this.fax = fax;
    }
    public String getPlz() {
        return this.plz;
    }
    
    public void setPlz(String plz) {
        this.plz = plz;
    }
    public String getOrt() {
        return this.ort;
    }
    
    public void setOrt(String ort) {
        this.ort = ort;
    }
    public String getStrasse() {
        return this.strasse;
    }
    
    public void setStrasse(String strasse) {
        this.strasse = strasse;
    }

    public String getExterneId() {
        return externeId;
    }

    public void setExterneId(String externeId) {
        this.externeId = externeId;
    }

/*    public Set<Spielort> getSpielorte() {
    return this.spielorte;
}

public void setSpielorte(Set<Spielort> spielorte) {
    this.spielorte = spielorte;
}*/

    public String toString() {
        return getAufstellerName();
    }

    public long getKeyValue() {
        return getAufstellerId();
    }

    public Class getModelClass() {
        return Automatenaufsteller.class;
    }

    public String toInfoString() {
        return "Aufsteller \"" + getAufstellerName() + "\"";
    }
}


