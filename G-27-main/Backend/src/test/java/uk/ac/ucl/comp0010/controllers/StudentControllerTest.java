package uk.ac.ucl.comp0010.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.List;
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

@SpringBootTest(webEnvironment =  WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class StudentControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private ModuleRepository testModuleRepository;

  @Autowired
  private  RegistrationRepository testRegistrationRepository;

  @Autowired
  private StudentRepository testStudentRepository;

  @Autowired
  private GradeRepository testGradeRepository;

  Student testStudent;
  Module testModule;
  Grade testGrade;

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
    testStudent = testStudentRepository.save(testStudent);

    int dummyScore = 59;
    testGrade = new Grade(testModule, dummyScore);

    testStudent.addGrade(testGrade);
    testStudent.registerModule(testModule);
    testStudentRepository.save(testStudent);
  }

  /**
   * tearDown runs after each function in the test, it deletes everything in the database so that it does not
   * conflict with the tests happening after a test
   */
  @AfterEach
  void tearDown() {
    testStudentRepository.deleteAll();
    testModuleRepository.deleteAll();
    testRegistrationRepository.deleteAll();
    testGradeRepository.deleteAll();
  }

  /**
   * Tests that the controller class's sendStudentDetails method (mapped to /students/studentDetail) correctly sends the student's details
   * @throws Exception thrown in the case of an invalid MockMvc request - should never be thrown in this particular case
   */
  @Test
  void testSendStudentDetail() throws Exception {

    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/students/studentDetail/" + testStudent.getId()).accept(
        MediaType.APPLICATION_JSON)).andReturn();
    assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());

    Map<String, Object> response = objectMapper.readValue(result.getResponse().getContentAsString(), Map.class);

    Map<String, Object> studentData = (Map<String, Object>) response.get("student");
    assertNotNull(studentData);
    assertEquals(testStudent.getId(), ((Number) studentData.get("id")).longValue());
    assertEquals(testStudent.getFirstName(), studentData.get("firstName"));
    assertEquals(testStudent.getLastName(), studentData.get("lastName"));
    assertEquals(testStudent.getUsername(), studentData.get("username"));
    assertEquals(testStudent.getEmail(), studentData.get("email"));

    assertNotNull(response.get("registeredModules"));
    assertEquals(1, ((List<?>) response.get("registeredModules")).size());
    Map<String, Object> moduleData = (Map<String, Object>) ((List<?>) response.get("registeredModules")).get(0);
    assertEquals(testModule.getCode(), moduleData.get("code"));
    assertEquals(testModule.getName(), moduleData.get("name"));
    assertEquals(testModule.getMnc(), moduleData.get("mnc"));

    assertNotNull(response.get("grades"));
    System.out.println(response.get("grades"));
    assertEquals(1, ((List<?>) response.get("grades")).size());
  }

  /**
   * Tests that the controller class's registerStudent method (mapped to /students/studentDetail/register) correctly registers a student
   * to a module
   * @throws Exception thrown in the case of an invalid MockMvc request - should never be thrown in this particular case
   */
  @Test
  void testRegisterStudent() throws Exception {
    Module testModuleTwo = new Module("EXMP0010Two", "Example Course Name Two", false);
    testModuleTwo = testModuleRepository.save(testModuleTwo);
    Map<String, String> params = new HashMap<String, String>();
    params.put("student_id", String.valueOf(testStudent.getId()));
    params.put("module_code", testModuleTwo.getCode());

    MvcResult action = mockMvc.perform(MockMvcRequestBuilders.post("/students/studentDetail/register").contentType(
        MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(params))).andReturn();
    assertEquals(HttpStatus.OK.value(), action.getResponse().getStatus());

    Registration testRegistration = objectMapper.readValue(action.getResponse().getContentAsString(), Registration.class);
    assertEquals(testModuleTwo.getCode(), testRegistration.getModule().getCode());
    assertEquals(testModuleTwo.getName(), testRegistration.getModule().getName());
    assertEquals(testModuleTwo.getMnc(), testRegistration.getModule().getMnc());
    
    MvcResult failedAction = mockMvc.perform(MockMvcRequestBuilders.post("/students/studentDetail/register").contentType(
      MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(params))).andReturn();
    assertEquals(HttpStatus.CONFLICT.value(), failedAction.getResponse().getStatus());
    
  }

  /**
   * Tests that the controller class's computeAverage method (mapped to /students/studentDetail/computeAverage/{id})
   * correctly calculates and returns the average score of a student's grades.
   *
   * @throws Exception if the MockMvc request fails or the response is invalid.
   */
  @Test
  void testSendAverage() throws Exception {
    Module testModuleTwo = new Module("EXMP0010Two", "Example Course Name Two", false);
    testModuleTwo = testModuleRepository.save(testModuleTwo);
    testStudent.registerModule(testModuleTwo);
    int dummyScoreTwo = 90;
    Grade testGradeTwo = new Grade(testModuleTwo, dummyScoreTwo);
    testStudent.addGrade(testGradeTwo);
    testStudentRepository.save(testStudent);
    testGradeRepository.save(testGradeTwo);

    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/students/studentDetail/computeAverage/" + testStudent.getId()).accept(
        MediaType.APPLICATION_JSON)).andReturn();
    assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());

    Map<String, Double> response = objectMapper.readValue(result.getResponse().getContentAsString(), Map.class);

    Double average = (double) ((testGrade.getScore()) + (testGradeTwo.getScore())) / 2;
    assertEquals(average, response.get("average"));
  }
  
  /**
   * Tests that the deleteStudent method in the Student Controller class will delete a given student
   * from the database if provided with a valid ID, and else will return a notFound response
   *
   * @throws Exception mockMvc can throw an Exception when there is not a valid Request built
   */
  @Test
  public void testDeleteStudent() throws  Exception{
    MvcResult action = mockMvc.perform(MockMvcRequestBuilders.delete("/students/{id}", testStudent.getId())).andReturn();
    Assertions.assertEquals(action.getResponse().getStatus(), HttpStatus.OK.value());
    
    MvcResult failedAction = mockMvc.perform(MockMvcRequestBuilders.delete("/students/{id}", testStudent.getId())).andReturn();
    Assertions.assertEquals(failedAction.getResponse().getStatus(), HttpStatus.NOT_FOUND.value());
  }
  
  /**
   * Tests that the deleteRegistration method will delete a given registration, if provided a valid id.
   *
   * @throws Exception mockMvc can throw an Exception when there is not a valid request provided to it
   */
  @Test
  public void testDeleteRegistration() throws Exception {
    Registration testRegistration = testStudent.getStudentRegistration().getFirst();
    
    MvcResult action = mockMvc.perform(MockMvcRequestBuilders.delete("/students/studentDetail/registrations/{studentId}/{moduleCode}", testRegistration.getStudent().getId(), testRegistration.getModule().getCode())).andReturn();
    Assertions.assertEquals(action.getResponse().getStatus(), HttpStatus.OK.value());
    
    MvcResult failedAction = mockMvc.perform(MockMvcRequestBuilders.delete("/students/studentDetail/registrations/{studentId}/{moduleCode}", testRegistration.getStudent().getId(), testRegistration.getModule().getCode())).andReturn();
    Assertions.assertEquals(failedAction.getResponse().getStatus(), HttpStatus.NOT_FOUND.value());
  }

}
