package uk.ac.ucl.comp0010.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import uk.ac.ucl.comp0010.model.Grade;
import uk.ac.ucl.comp0010.model.Module;
import uk.ac.ucl.comp0010.model.Registration;
import uk.ac.ucl.comp0010.model.Student;
import uk.ac.ucl.comp0010.repository.GradeRepository;
import uk.ac.ucl.comp0010.repository.ModuleRepository;
import uk.ac.ucl.comp0010.repository.RegistrationRepository;
import uk.ac.ucl.comp0010.repository.StudentRepository;
import static org.junit.jupiter.api.Assertions.*;

/**
 * The set of unit tests for the GradeController class, which handles the translation of frontend requests
 * to backend endpoints.
 */
@SpringBootTest(webEnvironment =  WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class GradeControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private StudentRepository testStudentRepository;

  @Autowired
  private ModuleRepository testModuleRepository;
  
  @Autowired
  private RegistrationRepository testRegistrationRepository;
  
  @Autowired
  private GradeRepository gradeRepository;

  Module testModule;
  Student testStudent;
  
  /**
   * Setup runs before each function in the test, it reduces the amount of redundant boilerplate code necessary
   * for testing
   */
  @BeforeEach
  void setup() {
    String dummyCode = "EXMP0010";
    String dummyName = "Example Course Name";
    Boolean dummyMnc = true;
    testModule = new Module(dummyCode, dummyName, dummyMnc);
    testModule = testModuleRepository.save(testModule);

    long testStudentId = 1L;
    String testFirstName = "John";
    String testLastName = "Doe";
    String testUsername = "JohnDoe";
    String testEmail = "johnDoe@gmail.com";
    testStudent = new Student(testStudentId, testFirstName, testLastName, testUsername, testEmail);
    testStudent.registerModule(testModule);
    testStudent = testStudentRepository.save(testStudent);
    Registration newRegistration = testStudent.getStudentRegistration().getLast();
    testRegistrationRepository.save(newRegistration);
  }
  
  /**
   * Returns the database to the pre-setup state (empty) to prevent tests from interfering with one another
   */
  @AfterEach
  public void tearDown() {
    testRegistrationRepository.deleteAll();
    testStudentRepository.deleteAll();
    gradeRepository.deleteAll();
    testModuleRepository.deleteAll();
  }
  
  /**
   * Tests that the controller class's addGrade method (mapped to /AddGrade) correctly adds a new grade to a student
   * @throws JsonProcessingException thrown in the case where the java params provided in the request are invalid
   * @throws Exception thrown in the case of an invalid MockMvc request - should never be thrown in this particular case
   */
  @Test
  void testAddGrade() throws JsonProcessingException, Exception {
    Map<String, String> params = new HashMap<String, String>();
    params.put("student_id", String.valueOf(testStudent.getId()));
    params.put("module_code", testModule.getCode());
    params.put("score", "50");

    MvcResult action = mockMvc.perform(MockMvcRequestBuilders.post("/grades/addGrade").contentType(
        MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(params))).andReturn();
    assertEquals(HttpStatus.OK.value(), action.getResponse().getStatus());

    Grade testGrade = objectMapper.readValue(action.getResponse().getContentAsString(), Grade.class);
    assertEquals(testStudent.getId(), testGrade.getStudent().getId());
    assertEquals(testStudent.getFirstName(), testGrade.getStudent().getFirstName());
    assertEquals(testStudent.getLastName(), testGrade.getStudent().getLastName());
    assertEquals(testStudent.getUsername(), testGrade.getStudent().getUsername());
    assertEquals(testStudent.getEmail(), testGrade.getStudent().getEmail());
    assertEquals(50, testGrade.getScore());
    assertEquals(testModule.getCode(), testGrade.getModule().getCode());
    assertEquals(testModule.getName(), testGrade.getModule().getName());
    assertEquals(testModule.getMnc(), testGrade.getModule().getMnc());
    
    Map<String, String> updateParams = new HashMap<>();
    updateParams.put("student_id", String.valueOf(testStudent.getId()));
    updateParams.put("module_code", testModule.getCode());
    updateParams.put("score", "80");
    
    MvcResult updateAction = mockMvc.perform(MockMvcRequestBuilders.post("/grades/addGrade").contentType(
      MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(updateParams))).andReturn();
    assertEquals(HttpStatus.OK.value(), action.getResponse().getStatus());
    
    testGrade = objectMapper.readValue(updateAction.getResponse().getContentAsString(), Grade.class);
    assertEquals(testStudent.getId(), testGrade.getStudent().getId());
    assertEquals(testStudent.getFirstName(), testGrade.getStudent().getFirstName());
    assertEquals(testStudent.getLastName(), testGrade.getStudent().getLastName());
    assertEquals(testStudent.getUsername(), testGrade.getStudent().getUsername());
    assertEquals(testStudent.getEmail(), testGrade.getStudent().getEmail());
    assertEquals(80, testGrade.getScore());
    assertEquals(testModule.getCode(), testGrade.getModule().getCode());
    assertEquals(testModule.getName(), testGrade.getModule().getName());
    assertEquals(testModule.getMnc(), testGrade.getModule().getMnc());
    
    Map<String, String> invalidModuleParams = new HashMap<>();
    Module newModule = new Module("REGS0101", "Registration Test Module", true);
    testModuleRepository.save(newModule);
    invalidModuleParams.put("student_id", String.valueOf(testStudent.getId()));
    invalidModuleParams.put("module_code", newModule.getCode());
    invalidModuleParams.put("score", "99");
    
    MvcResult failedAction = mockMvc.perform(MockMvcRequestBuilders.post("/grades/addGrade").contentType(
      MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(invalidModuleParams))).andReturn();
    assertEquals(HttpStatus.NOT_ACCEPTABLE.value(), failedAction.getResponse().getStatus());
    
    Map<String, String> nonExistentStudentParams = new HashMap<>();
    nonExistentStudentParams.put("student_id", "111");
    nonExistentStudentParams.put("module_code", testModule.getCode());
    nonExistentStudentParams.put("score", "99");
    
    MvcResult secondFailedAction = mockMvc.perform(MockMvcRequestBuilders.post("/grades/addGrade").contentType(
      MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(nonExistentStudentParams))).andReturn();
    assertEquals(HttpStatus.NOT_FOUND.value(), secondFailedAction.getResponse().getStatus());
  }
  
  /**
   * Tests that the deleteGrade method in the GradeController correctly deletes a grade from the database,
   * and that it correctly returns a "not found" status when the grade does not exist in the database.
   *
   * @throws Exception mockMvc can throw an Exception when there is not a valid Request built
   */
  @Test
  void testDeleteGrade() throws Exception {
    Grade newGrade = new Grade(testModule, 74);
    newGrade.setStudent(testStudent);
    gradeRepository.save(newGrade);
    MvcResult action = mockMvc.perform(MockMvcRequestBuilders.delete("/grades/{id}", newGrade.getId())).andReturn();
    Assertions.assertEquals(action.getResponse().getStatus(), HttpStatus.OK.value());
    
    MvcResult failedAction = mockMvc.perform(MockMvcRequestBuilders.delete("/grades/{id}", newGrade.getId())).andReturn();
    Assertions.assertEquals(failedAction.getResponse().getStatus(), HttpStatus.NOT_FOUND.value());
  }
}