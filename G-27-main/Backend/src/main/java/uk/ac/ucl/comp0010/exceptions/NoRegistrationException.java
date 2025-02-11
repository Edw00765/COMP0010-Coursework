package uk.ac.ucl.comp0010.exceptions;

/**
 * This exception is thrown when a grade is requested for a student which does not have that module.
 * The NoRegistrationException indicates that the system was unable to find
 * the module in a student's registered modules.
 * This may occur if the student requests for a grade for a module which was never
 * registered to them.
 * This checked exception ensure that such cases are handled by the calling code.
 *
 * @see uk.ac.ucl.comp0010.model.Student #getGrade(Module)
 */
public class NoRegistrationException extends Exception {
  public NoRegistrationException(String message) {
    super(message);
  }
}