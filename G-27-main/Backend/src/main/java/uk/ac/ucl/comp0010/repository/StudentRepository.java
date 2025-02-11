package uk.ac.ucl.comp0010.repository;

import org.springframework.data.repository.CrudRepository;
import uk.ac.ucl.comp0010.model.Student;

/**
 * The Repository class for interfacing with the Student POJOs.
 */
public interface StudentRepository extends CrudRepository<Student, Long> {

}