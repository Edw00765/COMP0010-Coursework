package uk.ac.ucl.comp0010.repository;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import uk.ac.ucl.comp0010.model.Grade;
import uk.ac.ucl.comp0010.model.Module;
import uk.ac.ucl.comp0010.model.Student;

/**
 * The Repository class for interfacing with the Grade POJOs.
 */
public interface GradeRepository  extends CrudRepository<Grade, Long> {
  /**
   * Searches through the database and finds a grade that has a unique combination of
   * module and student.
   *
   * @param module The module for which the grade has been achieved
   * @param student The student to whom the grade belongs
   */
  Optional<Grade> findByModuleAndStudent(Module module, Student student);
}
