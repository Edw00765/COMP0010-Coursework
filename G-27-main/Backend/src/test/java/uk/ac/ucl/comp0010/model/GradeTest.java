package uk.ac.ucl.comp0010.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Objects;

/**
 * The GradeTest class provides a set of unit tests for the Grade class
 * @version 0.5
 */
public class GradeTest {

  private Module testModule;
  private int dummyScore;
  private Grade testGrade;
  private Student testStudent;

  @BeforeEach
  public void setup(){
    String dummyCode = "EXMPL00010";
    String dummyName = "Example Course Name";
    Boolean dummyMnc = true;
    testModule = new Module(dummyCode, dummyName, dummyMnc);
    dummyScore = 74;
    
    long testStudentId = 1234;
    String testFirstName = "John";
    String testLastName = "Doe";
    String testUsername = "JohnDoe";
    String testEmail = "johnDoe@gmail.com";
    testStudent = new Student(testStudentId, testFirstName, testLastName, testUsername, testEmail);
    testGrade = new Grade(testModule, dummyScore);
    testStudent.addGrade(testGrade);
  }

  /**
   * Tests that the Grade constructor properly assigns the input values to the score and module fields
   */
  @Test
  public void testGradeConstruction(){
    assertEquals(testGrade.getScore(), dummyScore);
    assertEquals(testGrade.getModule(), testModule);
    assertEquals(testGrade.getStudent(), testStudent);
  }

  /**
   * Tests that the getScore method properly returns the grade most recently input to it.
   * Currently, it can only be set at initialisation, and so that's all we'll test
   */
  @Test
  public void testGetScore(){
    assertEquals(testGrade.getScore(), dummyScore);
  }

  /**
   * Tests the getModule method properly returns the module associated with the grade at initialisation.
   */
  @Test
  public void testGetModule(){
    assertEquals(testGrade.getModule(), testModule);
  }
  
  /**
   * Tests that the getStudent method properly returns the student associated with the grade at initialization.
   */
  @Test
  public void testGetStudent(){
    assertEquals(testGrade.getStudent(), testStudent);
  }

  /**
   * Tests that the setScore method correctly sets the value of the Grade's score to the
   * newScore
   */
  @Test
  public void testSetScore(){
    int newScore = 80;
    testGrade.setScore(newScore);
    assertEquals(testGrade.getScore(), newScore);
  }

  /**
   * Tests that the setModule method correctly sets the value of the Grade's module to the
   * newModule
   */
  @Test
  public void testSetModule(){
    String newDummyCode = "newEXMPL00010";
    String newDummyName = "New Example Course Name";
    Boolean newDummyMnc = false;
    Module newModule = new Module(newDummyCode, newDummyName, newDummyMnc);
    testGrade.setModule(newModule);
    assertEquals(testGrade.getModule(), newModule);
  }

  /**
   * Tests that the setStudent method correctly sets the value of the Grade's student to the
   * newStudent
   */
  @Test
  public void testSetStudent(){
    long newTestStudentId = 2345;
    String newTestFirstName = "newJohn";
    String newTestLastName = "newDoe";
    String newTestUsername = "newJohnnewDoe";
    String newTestEmail = "newjohnnewDoe@gmail.com";
    Student newTestStudent = new Student(newTestStudentId, newTestFirstName, newTestLastName, newTestUsername, newTestEmail);
    testGrade.setStudent(newTestStudent);
    assertEquals(testGrade.getStudent(), newTestStudent);
  }
}
