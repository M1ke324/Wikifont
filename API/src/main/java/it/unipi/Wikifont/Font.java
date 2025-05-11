package it.unipi.Wikifont;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.sql.Date;

/**
 *
 * @author Michele Castrucci 636159
 */

@Entity
@Table(name="font", schema="636159")
public class Font implements Serializable{

    @Id
    private String family;
    
    @Column(name="version")
    private String version;
    
    @Column(name="lastmodified")
    private Date lastModified;
    
    @Column(name="category")
    private String category;
    
    @Column(name="kind")
    private String kind;
    
    @Column(name="menu", length=350)
    private String menu;
    
    
    //Costruttori
    public Font(String family, String version, Date lastModified, String category, String kind, String menu) {
        this.family = family;
        this.version = version;
        this.lastModified = lastModified;
        this.category = category;
        this.kind = kind;
        this.menu = menu;
    }

    public Font() {
    }
    
    //Metodi getter e setter
    public String getFamily() {
        return family;
    }

    public String getVersion() {
        return version;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public String getCategory() {
        return category;
    }

    public String getKind() {
        return kind;
    }

    public String getMenu() {
        return menu;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }
    
    
}
