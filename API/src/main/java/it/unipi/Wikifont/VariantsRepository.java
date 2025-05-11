/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.unipi.Wikifont;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Michele Castrucci 636159
 */
public interface VariantsRepository extends CrudRepository<VariantsDB, VariantsDB.PrimaryKey> {
    List<VariantsDB> findAllByVariantFamily(String n);
}