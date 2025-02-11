package uk.ac.ucl.comp0010.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Objects;


/**
 * The ModuleTest class provides a set of unit tests for the Module class
 * @version 0.5
 */
public class ModuleTest {

  private Module testModule;
  private String dummyCode;
  private String dummyName;
  private Boolean dummyMnc;

  /**
   * Creates the set of conditions necessary for standardised testing of the module class
   */
  @BeforeEach
  public void setup(){
    dummyCode = "EXMPL00010";
    dummyName = "Example Course Name";
    dummyMnc = true;
    testModule = new Module(dummyCode, dummyName, dummyMnc);
  }


  /**
   * Tests that the module constructor properly assigns the input values to the code, name and mnc fields
   */
  @Test
  @DisplayName("Tests Object Initialisation works as intended")
  public void testModuleConstruction(){
    assertEquals(testModule.getCode(), dummyCode);
    assertEquals(testModule.getName(), dummyName);
    assertEquals(testModule.getMnc(), dummyMnc);
  }
  /**
   * Tests that the getCode method properly returns the code input to the object at construction
   */
  @Test
  @DisplayName("Tests the getCode getter method works")
  public void testGetCode(){
    assertEquals(testModule.getCode(), dummyCode);
  }
  /**
   * Tests that the getName method properly returns the name input to the object at construction
   */
  @Test
  public void testGetName(){
    assertEquals(testModule.getName(), dummyName);
  }
  /**
   * Tests that the getMcn method properly returns the name mcn boolean to the object at construction
   */
  @Test
  public void testGetMnc(){
    assertEquals(testModule.getMnc(), dummyMnc);
  }

  /**
   * Tests that the setCode method correctly sets the value of the Module's code to the
   * newCode
   */
  @Test
  public void testSetCode(){
    String newCode = "newEXMPL00010";
    testModule.setCode(newCode);
    assertEquals(testModule.getCode(), newCode);
  }

  /**
   * Tests that the setName method correctly sets the value of the Module's name to the
   * newName
   */
  @Test
  public void testSetName(){
    String newName = "New Example Course Name";
    testModule.setName(newName);
    assertEquals(testModule.getName(), newName);
  }

  /**
   * Tests that the setMnc method correctly sets the value of the Module's mnc to the
   * newMnc
   */
  @Test
  public void testSetMnc(){
    Boolean newMnc = false;
    testModule.setMnc(newMnc);
    assertEquals(testModule.getMnc(), newMnc);
  }

}