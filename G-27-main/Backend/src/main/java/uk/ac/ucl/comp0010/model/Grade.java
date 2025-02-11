package uk.ac.ucl.comp0010.model;

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
 * Represents the Grade of a student.
 * The Grade class contains information about the score, and the corresponding module.
 */

@Entity
@Table(name = "Grade")
public class Grade {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "Id", nullable = false, unique = true)
  private long id;
  
  @ManyToOne
  @JoinColumn(name = "Module_Code", nullable = false)
  private Module module;
  
  @ManyToOne(optional = false)
  @JoinColumn(name = "Student_Id", nullable = false)
  @JsonIgnoreProperties({"gradeList"})
  private Student student;
  
  @Column(name = "Score", nullable = false)
  private int score;

  /**
   * Default constructor required by JPA.
   * This should not be used by POJO backend objects.
   */
  public Grade() {}
  
  /**
   * Creates a new Grade object with the specified details.
   *
   * @param module which is the module that will have this score
   * @param score which is the score
   */
  public Grade(Module module, int score) {
    this.module = module;
    this.score = score;
  }

  /**
   * Returns the score of this module.
   *
   * @return the score (which is an integer)
   */
  public int getScore() {
    return this.score;
  }

  /**
   * Returns the module of this grade.
   *
   * @return the module as a Module object reference
   */
  public Module getModule() {
    return this.module;
  }
  
  /**
   * Returns the unique ID for this registration.
   *
   * @return The registration ID, which is a unique Long
   */
  public long getId() {
    return this.id;
  }
  
  /**
   * Returns the student which this registration was assigned to.
   *
   * @return The student.
   */
  public Student getStudent() {
    return this.student;
  }

  /**
   * A setter method for the grade's score.
   *
   * @param score which is the new score of the grade
   */
  public void setScore(int score) {
    this.score = score;
  }

  /**
   * A setter method for the grade's module.
   *
   * @param module which is the new module of the grade
   */
  public void setModule(Module module) {
    this.module = module;
  }

  /**
   * A setter method for the grade's student.
   *
   * @param student which is the new student of the grade
   */
  public void setStudent(Student student) {
    this.student = student;
  }

}