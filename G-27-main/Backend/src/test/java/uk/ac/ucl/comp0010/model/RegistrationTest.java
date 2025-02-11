package uk.ac.ucl.comp0010.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class RegistrationTest {
  private Registration testRegistration;
  private Module testModule;
  private Student testStudent;
  /**
   * Creates the set of conditions necessary for standardised testing of the Registration class
   */
  @BeforeEach
  public void setup(){
    testModule = new Module("COMP0010", "Software Engineering", true);
    testStudent = new Student(1234, "John", "Doe", "JohnDoe", "johnDoe@gmail.com");
    testStudent.registerModule(testModule);
    testRegistration = testStudent.getStudentRegistration().get(0);
  }

  /**
   * The unit test for the getModule getter method
   */
  @Test
  @DisplayName("Tests getter method returns module")
  public void testGetModule(){
    Assertions.assertEquals(this.testRegistration.getModule(), testModule);
  }

  /**
   * The unit test for the Registration class's public constructor
   */
  @Test
  @DisplayName("Tests the registration module public constructor")
  public void testRegistrationConstructor(){
    // Since the registration class is pretty simple initially, this ends up being the same as the test
    // for the getModule unit test, but has been added just in case there is an update to the function of this class
    Assertions.assertEquals(this.testRegistration.getModule(), testModule);
  }
  
  /**
   * The unit test for the getStudent getter method
   */
  @Test
  @DisplayName("Tests getter method returns student")
  public void testGetStudent() {
    // Test if the student in the registration is correctly returned
    Assertions.assertEquals(testRegistration.getStudent(), testStudent);
  }

  /**
   * Tests that the setModule method correctly sets the value of the Registration's module to the
   * newModule
   */
  @Test
  public void testSetModule(){
    Module newTestModule = new Module("newCOMP0010", "New Software Engineering", false);
    testRegistration.setModule(newTestModule);
    assertEquals(testRegistration.getModule(), newTestModule);
  }

  /**
   * Tests that the setStudent method correctly sets the value of the Registration's Student to the
   * newStudent
   */
  @Test
  public void testSetStudent(){
    Student newTestStudent = new Student(2345, "newJohn", "newDoe", "newJohnnewDoe", "newjohnnewDoe@gmail.com");
    testRegistration.setStudent(newTestStudent);
    assertEquals(testRegistration.getStudent(), newTestStudent);
  }
}
