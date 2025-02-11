package uk.ac.ucl.comp0010.repository;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import uk.ac.ucl.comp0010.model.Module;
import uk.ac.ucl.comp0010.model.Registration;
import uk.ac.ucl.comp0010.model.Student;

/**
 * The repository class for interfacing with the Registration POJO.
 */
public interface RegistrationRepository extends CrudRepository<Registration, Long> {
  /**
   * Searches through the database and finds a registration that has a unique
   * combination of module and student.
   *
   * @param module The module for which the registration has been achieved
   * @param student The student to whom the registration belongs
   */
  Optional<Registration> findByModuleAndStudent(Module module, Student student);
  
}