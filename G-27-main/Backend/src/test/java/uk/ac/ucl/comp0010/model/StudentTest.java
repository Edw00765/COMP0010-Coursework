package uk.ac.ucl.comp0010.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.ac.ucl.comp0010.exceptions.NoGradeAvailableException;
import uk.ac.ucl.comp0010.exceptions.NoRegistrationException;

/**
 * The StudentTest class provides a set of unit tests for the Student class
 * @version 0.5
 */
public class StudentTest {
  private Long dummyId;
  private String dummyFirstName;
  private String dummyLastName;
  private String dummyUserName;
  private String dummyEmail;
  private Student dummyStudent;
  private Module testModule;
  private Grade testGrade;
  
  @BeforeEach
  public void setup(){
    dummyId = 0L;
    dummyFirstName = "first";
    dummyLastName = "second";
    dummyUserName = "username";
    dummyEmail = "first.second@ucl.ac.uk";
    dummyStudent = new Student(dummyId, dummyFirstName, dummyLastName, dummyUserName, dummyEmail);

    String dummyCode = "EXMPL00010";
    String dummyName = "Example Course Name";
    Boolean dummyMnc = true;
    testModule = new Module(dummyCode, dummyName, dummyMnc);

    int dummyScore = 74;
    testGrade = new Grade(testModule, dummyScore);
  }
  
  /**
   * Tests that the Student constructor properly initialises the values of the id, firstName, lastName, userName,
   * and email fields
   */
  @Test
  public void testConstruction(){
    assertEquals(dummyStudent.getId(), dummyId);
    assertEquals(dummyStudent.getFirstName(), dummyFirstName);
    assertEquals(dummyStudent.getLastName(), dummyLastName);
    assertEquals(dummyStudent.getUsername(), dummyUserName);
    assertEquals(dummyStudent.getEmail(), dummyEmail);
    assertEquals(0, dummyStudent.getStudentRegistration().size());
    assertEquals(0, dummyStudent.getGradeList().size());
  }

  /**
   * Tests that the setFirstName method correctly sets the value of the Student's firstName to the
   * newFirstName
   */
  @Test
  public void testSetFirstName(){
    String newFirstName = "newFirst";
    dummyStudent.setFirstName(newFirstName);
    assertEquals(dummyStudent.getFirstName(), newFirstName);
  }

  /**
   * Tests that the setLastName method correctly sets the value of the Student's lastName to the
   * newLastName
   */
  @Test
  public void testSetLastName(){
    String newLastName = "newSecond";
    dummyStudent.setLastName(newLastName);
    assertEquals(dummyStudent.getLastName(), newLastName);
  }

  /**
   * Tests that the setUsername method correctly sets the value of the Student's username to the
   * newUsername
   */
  @Test
  public void testSetUsername(){
    String newUsername = "newUsername";
    dummyStudent.setUsername(newUsername);
    assertEquals(dummyStudent.getUsername(), newUsername);
  }

  /**
   * Tests that the setEmail method correctly sets the value of the Student's email to the
   * newEmail
   */
  @Test
  public void testSetEmail(){
    String newEmail = "newFirst.newSecond@ucl.ac.uk";
    dummyStudent.setEmail(newEmail);
    assertEquals(dummyStudent.getEmail(), newEmail);
  }

  /**
   * Tests that the setId method correctly sets the value of the Student's id to the
   * newId
   */
  @Test
  public void testSetId(){
    long newId = 1L;
    dummyStudent.setId(newId);
    assertEquals(dummyStudent.getId(), newId);
  }

  /**
   * Tests that the setGradeList method correctly sets the value of the Student's gradeList to the
   * newGradeList
   */
  @Test
  public void testSetGradeList(){
    int dummyScore1 = 74;
    int dummyScore2 = 75;
    int dummyScore3 = 76;

    Grade testGrade1 = new Grade(testModule, dummyScore1);
    Grade testGrade2 = new Grade(testModule, dummyScore2);
    Grade testGrade3 = new Grade(testModule, dummyScore3);

    List<Grade> newGradeList = new ArrayList<>();
    newGradeList.add(testGrade1);
    newGradeList.add(testGrade2);
    newGradeList.add(testGrade3);

    dummyStudent.setGradeList(newGradeList);
    assertEquals(dummyStudent.getGradeList(), newGradeList);
    
    dummyStudent.setGradeList(null);
    assertEquals(dummyStudent.getGradeList().size(), 0);
  }

  /**
   * Tests that the setStudentRegistration method correctly sets the value of the Student's studentRegistration to the
   * newStudentRegistration
   */
  @Test
  public void testSetStudentRegistration(){
    Module testModule1 = new Module("COMP0010", "Software Engineering", true);
    Module testModule2 = new Module("COMP0011", "System Engineering", true);
    Module testModule3 = new Module("COMP0012", "Logic", true);

    Registration registration1 = new Registration(testModule1);
    Registration registration2 = new Registration(testModule2);
    Registration registration3 = new Registration(testModule3);

    List<Registration> newStudentRegistration = new ArrayList<>();
    newStudentRegistration.add(registration1);
    newStudentRegistration.add(registration2);
    newStudentRegistration.add(registration3);

    dummyStudent.setStudentRegistration(newStudentRegistration);
    assertEquals(dummyStudent.getStudentRegistration(), newStudentRegistration);
    
    dummyStudent.setStudentRegistration(null);
    assertEquals(dummyStudent.getStudentRegistration().size(), 0);
  }

  /**
   * Tests that the AddGrade method correctly adds a new Grade to the grades field
   */
  @Test
  public void testAddGrade(){
    int previousGradesHad = dummyStudent.getGradeList().size();
    dummyStudent.addGrade(testGrade);

    assertEquals(dummyStudent.getGradeList().size(), previousGradesHad + 1);
    assertEquals(dummyStudent.getGradeList().get(dummyStudent.getGradeList().size() - 1), testGrade);
  }

  /**
   * Tests that the ComputeAverage method correctly computes and returns the average of the student's grades
   */
  @Test
  public void testComputeAverage(){
    int dummyScore1 = 74;
    int dummyScore2 = 75;
    int dummyScore3 = 76;

    Grade testGrade1 = new Grade(testModule, dummyScore1);
    Grade testGrade2 = new Grade(testModule, dummyScore2);
    Grade testGrade3 = new Grade(testModule, dummyScore3);

    dummyStudent.addGrade(testGrade1);
    dummyStudent.addGrade(testGrade2);
    dummyStudent.addGrade(testGrade3);

    float actualAverage =  (dummyScore1 + dummyScore2 + dummyScore3) / 3.0f;
    assertEquals(dummyStudent.computeAverage(), actualAverage, 0.0);
  }

  /**
   * Tests that the ComputeAverage method correctly returns 0 if the student's gradeList is empty
   */
  @Test
  public void testComputerAverageEmptyGradeListCase(){
    assertEquals(dummyStudent.computeAverage(), 0.0f, 0.0);
  }

  /**
   * Tests that the getGrade method correctly returns the grade associated with a particular module,
   * provided a valid module (that already has a grade associated with it)
   */
  @Test
  public void testValidGetGrade() throws NoRegistrationException, NoGradeAvailableException {
    Module nonRegisteredModule = new Module("EXMP", "EXMP Module", false);
    Grade dummyGrade = new Grade(nonRegisteredModule, 60);
    dummyStudent.registerModule(testModule);
    dummyStudent.addGrade(testGrade);
    dummyStudent.addGrade(dummyGrade);
    assertEquals(dummyStudent.getGrade(testModule), testGrade);
  }
  
  /**
   * Tests that the getGrade method will return a NoGradeAvailableException when a module that does not have an
   * associated grade for the student is input as a parameter.
   */
  @Test
  public void testNoGradeAvailableGetGrade(){
    dummyStudent.registerModule(testModule);
    assertThrows(NoGradeAvailableException.class, () -> {dummyStudent.getGrade(testModule);});
  }
  
  /**
   * Tests that the getGrade method will return a NoRegistrationException when it tries to access a grade for
   * a class that the student has not registered for
   */
  @Test
  public void testNoRegistrationGetGrade(){
    assertThrows(NoRegistrationException.class, () -> {dummyStudent.getGrade(testModule);});
  }
  
  /**
   * Tests that the registerModule method creates a new registration for the input module, and stores it in the
   * registrations field.
   */
  @Test
  public void testRegisterModule(){
    dummyStudent.registerModule(testModule);
    assertEquals(
            dummyStudent.getStudentRegistration().get(dummyStudent.getStudentRegistration().size() - 1).getModule(), testModule);
  }
}