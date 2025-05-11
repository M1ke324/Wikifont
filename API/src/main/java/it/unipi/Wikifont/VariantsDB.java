/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.unipi.Wikifont;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import java.io.Serializable;

/**
 *
 * @author Michele Castrucci 636159
 */
@Entity
@Table(name="variants", schema="636159")
public class VariantsDB implements Serializable{
    //Chiave primaria composta da family e variant
    @EmbeddedId
    private PrimaryKey variant;
    
    
    //Creazione del vincolo di chiave esterna su una delle due chiavi primarie: family
    @ManyToOne
    @MapsId("family")
    @JoinColumn(name="family")
    private Font family;
            
    @Column(name="link", length=350)
    private String link;
    
    //Costruttori
    public VariantsDB(PrimaryKey variant, Font family, String link) {
        this.variant = variant;
        this.family = family;
        this.link = link;
    }

    public VariantsDB() {
    }
    
    //Metodi getter e setter
    public PrimaryKey getVariant() {
        return variant;
    }

    public Font getFamily() {
        return family;
    }

    public String getLink() {
        return link;
    }

    public void setVariant(PrimaryKey variant) {
        this.variant = variant;
    }

    public void setFamily(Font family) {
        this.family = family;
    }

    public void setLink(String link) {
        this.link = link;
    }
    
    
    //Creazione chiave primaria composta
    @Embeddable
    public static class PrimaryKey implements Serializable{
        
        @Column(name="family")
        private String family;
        
        @Column(name="variant")
        private String variant;

        //Costruttori
        public PrimaryKey() {
        }

        public PrimaryKey(String family, String variant) {
            this.family = family;
            this.variant = variant;
        }
        
        //Metodi getter e setter
        public void setFamily(String family) {
            this.family = family;
        }

        public void setVariant(String variant) {
            this.variant = variant;
        }

        
        public String getFamily() {
            return family;
        }

        public String getVariant() {
            return variant;
        }
        
    }
}
