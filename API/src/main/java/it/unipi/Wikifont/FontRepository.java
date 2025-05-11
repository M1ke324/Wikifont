package it.unipi.Wikifont;

import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Michele Castrucci 636159
 */
public interface FontRepository extends CrudRepository<Font, String> {
    Font findByFamily(String n);
}
