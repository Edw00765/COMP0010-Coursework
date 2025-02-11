package uk.ac.ucl.comp0010.exceptions;

/**
 * This exception is thrown when a grade is requested for a module 
 * that has no recorded grade.
 * The NoGradeAvailableException indicates that the system was 
 * unable to find a grade for a given module, which may occur if the grade is unavailable. 
 * This checked exception ensures that such cases are explicitly handled by the calling code.
 *
 * @see uk.ac.ucl.comp0010.model.Student #getGrade(Module)
 */
public class NoGradeAvailableException extends Exception {
  public NoGradeAvailableException(String message) {
    super(message);
  }
}