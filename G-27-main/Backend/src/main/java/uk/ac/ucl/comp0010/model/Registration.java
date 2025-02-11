package uk.ac.ucl.comp0010.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Represents the Registration that links the student and module classes.
 * The Registration class contains the module for which a student is registered.
 */

@Entity
@Table(name = "Registration")
public class Registration {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "Id", nullable = false, unique = true)
  private long id;
  
  @ManyToOne(optional = false)
  @JoinColumn(name = "Student_Id", nullable = false)
  @JsonIgnore
  private Student student;
  
  @ManyToOne
  @JoinColumn(name = "Registered_Module_Code", nullable = false)
  private Module module;
  
  /**
   * Default constructor required by JPA.
   */
  public Registration() {}
  
  /**
   * The public constructor the module class, in which the module for which the student is
   * registered is assigned.
   *
   * @param module The module for which a student is registered, and the student it is being
   *              registered to
   */
  public Registration(Module module) {
    this.module = module;
  }

  /**
   * A getter method for the module field.
   *
   * @return module: a Module object representing the module that a student is registered for
   */
  public Module getModule() {
    return this.module;
  }
  
  /**
   * Returns the unique ID for this registration.
   *
   * @return The registration ID.
   */
  public long getId() {
    return this.id;
  }
  
  /**
   * Tests that the getStudent method properly returns the student associated with the registration
   * at initialization.
   */
  public Student getStudent() {
    return this.student;
  }

  /**
   * A setter method for the Registration's module.
   *
   * @param module which is the new module of the Registration
   */
  public void setModule(Module module) {
    this.module = module;
  }

  /**
   * A setter method for the Registration's Student.
   *
   * @param student which is the new student of the Registration
   */
  public void setStudent(Student student) {
    this.student = student;
  }

}
