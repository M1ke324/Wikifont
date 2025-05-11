/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.unipi.Wikifont;

import java.io.Serializable;

/**
 *
 * @author Michele Castrucci 636159
 */
public class Variants implements Serializable{
    public String family;
    public String variant;
    public String link;

    //Costruttori
    public Variants(String family, String variant, String link) {
        this.variant = variant;
        this.family = family;
        this.link = link;
    }

    public Variants() {
    }

    //Metodi getter e setter
    public String getFamily() {
        return family;
    }

    public String getVariant() {
        return variant;
    }

    public String getLink() {
        return link;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public void setVariant(String variant) {
        this.variant = variant;
    }

    public void setLink(String link) {
        this.link = link;
    }
    
    
}
