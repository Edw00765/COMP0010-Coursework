package uk.ac.ucl.comp0010.repository;

import org.springframework.data.repository.CrudRepository;
import uk.ac.ucl.comp0010.model.Module;

/**
 * The repository class for interfacing with the Module POJO.
 */
public interface ModuleRepository extends CrudRepository<Module, String> {

}
