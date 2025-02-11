package uk.ac.ucl.comp0010.model;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Null;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import uk.ac.ucl.comp0010.exceptions.NoGradeAvailableException;
import uk.ac.ucl.comp0010.exceptions.NoRegistrationException;


/**
 * Represents a student in the university system.
 * The Student class maintains information about a student's ID, name,
 * email, and registration details. It also allows managing grades
 * and computing the average grade.
 */
@Entity
@Table(name = "Student")
public class Student {
  @Id
  @Column(name = "Id", nullable = false, unique = true)
  private long id;
  
  @Column(name = "First_Name", nullable = false)
  private String firstName;
  @Column(name = "Last_Name", nullable = false)
  private String lastName;
  @Column(name = "Username", nullable = false, unique = true)
  private String username;
  @Column(name = "Email", nullable = false, unique = true)
  private String email;

  /**
   * The student's list of registered modules.
   */
  @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Registration> studentRegistration = new ArrayList<>();
  /**
   * The student's list of grades.
   */
  @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Grade> gradeList = new ArrayList<>();
  
  /**
   * Default constructor required by JPA.
   */
  public Student() {}
  
  /**
   * Creates a new Student with the specified details.
   *
   * @param id the unique identifier for the student
   * @param firstName the student's first name
   * @param lastName the student's last name
   * @param username the student's username
   * @param email the student's email address
   */
  public Student(long id, String firstName, String lastName, String username, String email) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.username = username;
    this.email = email;
  }

  /**
   * A getter method for the student's firstName.
   *
   * @return the first name of the student
   */
  public String getFirstName() {
    return this.firstName;
  }
  
  /**
   * A getter method for the student's lastName.
   *
   * @return the last name of the student
   */
  public String getLastName() {
    return this.lastName;
  }
  
  /**
   * A getter method for the student's username.
   *
   * @return the username of the student
   */
  public String getUsername() {
    return this.username;
  }
  
  /**
   * A getter method for the student's email.
   *
   * @return the email of the student
   */
  public String getEmail() {
    return this.email;
  }
  
  /**
   * A getter method for the student's studentId.
   *
   * @return the id of the student
   */
  public long getId() {
    return this.id;
  }
  
  /**
   * A getter method for the gradeList field.
   *
   * @return List of all grades the student has
   */
  public List<Grade> getGradeList() {
    return this.gradeList;
  }
  
  /**
   * A getter method for the studentRegistration field.
   *
   * @return A list of all registrations a student has
   */
  public List<Registration> getStudentRegistration() {
    return this.studentRegistration;
  }

  /**
   * A setter method for the student's firstName.
   *
   * @param firstName which is the new first name of the student
   */
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  /**
   * A setter method for the student's lastName.
   *
   * @param lastName which is the new last name of the student
   */
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  /**
   * A setter method for the student's username.
   *
   * @param username which is the new username of the student
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * A setter method for the student's email.
   *
   * @param email which is the new email of the student
   */
  public void setEmail(String email) {
    this.email = email;
  }

  /**
   * A setter method for the student's id.
   *
   * @param id which is the new id of the student
   */
  public void setId(long id) {
    this.id = id;
  }

  /**
   * A setter method for the student's gradeList.
   *
   * @param gradeList which is the new gradeList of the student
   */
  public void setGradeList(List<Grade> gradeList) {
    this.gradeList.clear();
    if (gradeList != null) {
      this.gradeList.addAll(gradeList);
    }
  }

  /**
   * A setter method for the student's studentRegistration.
   *
   * @param studentRegistration which is the new studentRegistration of the student
   */
  public void setStudentRegistration(List<Registration> studentRegistration) {
    this.studentRegistration.clear();
    if (studentRegistration != null) {
      this.studentRegistration.addAll(studentRegistration);
    }
  }

  /**
   * Adds a grade to the student's list of grades
   * This method appends the grade object from it's param to the grade list of this student,
   * allowing it to be considered in any calculations or retrievals of the student's grades.
   *
   * @param grade which is the grade object that wants to add to the grade list.
   */
  public void addGrade(Grade grade) {
    this.gradeList.add(grade);
    grade.setStudent(this);
  }
  
  /**
   * Calculates and returns the average score of the student's grades.
   *
   * @return the average score as a float, or 0 if the student has no grades.
   */
  public float computeAverage() {
    if (this.gradeList.isEmpty()) {
      return 0f;
    }

    int sum = 0;
    for (Grade grade : this.gradeList) {
      sum += grade.getScore();
    }
    return (float) sum / gradeList.size();
  }
  
  /**
   * Retrieves the Grade object from the grade list for a specified module
   * This method searched the student's grade list for a grade associated with the specified module.
   * If the module is found in the student registration, and if a matching grade is found, it will
   * be returned. Otherwise, if the module is found in the student registration but a matching
   * grade is not found then a NoGradeAvailableException is thrown.
   * Otherwise, if the module is not found in student registration, a NoRegistrationException is
   * thrown.
   *
   * @param module which is the module for the grade requested.
   * @return the grade of the module as a Grade object.
   * @throws NoGradeAvailableException if no grade is available for the specified module.
   * @throws NoRegistrationException if the module is not registered.
 */
  public Grade getGrade(Module module) throws NoGradeAvailableException, NoRegistrationException {
    ArrayList<Module> registeredModules = new ArrayList<>();
    for (Registration registration : this.studentRegistration) {
      registeredModules.add(registration.getModule());
    }

    if (registeredModules.contains(module)) {
      boolean gradePresent = false;
      Grade g = new Grade();
      for (Grade grade : this.gradeList) {
        if (grade.getModule().getCode().equals(module.getCode())) {
          g = grade;
          gradePresent = true;
        }
      }
      if (gradePresent) {
        return g;
      } else {
        throw new NoGradeAvailableException("There is no grade available for this module.");
      }
    } else {
      throw new NoRegistrationException("Student with Id " + this.id
              + " is not registered for module with code " + module.getCode());
    }
  }
  
  /**
   * Registers a new module for the student
   * Creates a new Registration object which will for the specified module. It will then add that
   * new registration object into the student's registration list.
   *
   * @param module which is the module that we want to register.
   */
  public void registerModule(Module module) {
    Registration newRegister = new Registration(module);
    newRegister.setStudent(this);
    this.studentRegistration.add(newRegister);
  }
}