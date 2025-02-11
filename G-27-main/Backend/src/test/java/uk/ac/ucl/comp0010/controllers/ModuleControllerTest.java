package uk.ac.ucl.comp0010.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import uk.ac.ucl.comp0010.model.Grade;
import uk.ac.ucl.comp0010.model.Student;
import uk.ac.ucl.comp0010.repository.ModuleRepository;
import uk.ac.ucl.comp0010.repository.RegistrationRepository;
import uk.ac.ucl.comp0010.repository.StudentRepository;
import uk.ac.ucl.comp0010.model.Module;

/**
 * The set of unit tests for the GradeController class, which handles the translation of frontend requests
 * to backend endpoints.
 */
@SpringBootTest(webEnvironment =  SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class ModuleControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private ModuleRepository testModuleRepository;

  @Autowired
  private RegistrationRepository testRegistrationRepository;

  @Autowired
  private StudentRepository testStudentRepository;
  private Module testModule;
  
  /**
   * Standardises the database before each function runs, to reduce code duplication and related potential bugs
   */
  @BeforeEach
  public void setup(){
    testModule = new Module("COMP0010", "Software Engineering", false);
    testModuleRepository.save(testModule);
  }
  
  /**
   * Resets the database to empty after each function to prevent any tests from interfering with one another
   * and producing uncertain results.
   */
  @AfterEach
  public void tearDown(){
    testRegistrationRepository.deleteAll();
    testStudentRepository.deleteAll();
    testModuleRepository.deleteAll();
  }
  
  /**
   * Tests that the deleteModule method in the ModuleController correctly deletes a module from the database,
   * and that it correctly returns a "not found" status when the module does not exist in the database.
   * @throws Exception mockMvc can throw an Exception when there is not a valid Request built
   */
  @Test
  void testDeleteModule() throws Exception {
    MvcResult action = mockMvc.perform(MockMvcRequestBuilders.delete("/modules/{id}", testModule.getCode())).andReturn();
    assertEquals(action.getResponse().getStatus(), HttpStatus.OK.value());
    
    MvcResult failedAction = mockMvc.perform(MockMvcRequestBuilders.delete("/modules/{id}", testModule.getCode())).andReturn();
    assertEquals(failedAction.getResponse().getStatus(), HttpStatus.NOT_FOUND.value());
  }

  /**
   * Tests that the sendModuleDetails method (mapped to /modules/moduleDetails/{moduleCode}) correctly retrieves module details.
   * @throws Exception thrown in the case of an invalid MockMvc request - should never happen in this case.
   */
  @Test
  void testSendModuleDetails() throws Exception {
    Module dummyModule = new Module("COMP0016", "System Engineering", false);
    testModuleRepository.save(dummyModule);
    Student testStudent1 = new Student(1L, "first1", "last1", "first1.last1", "first1.last1@example.com");
    testStudent1 = testStudentRepository.save(testStudent1);
    Student testStudent2 = new Student(2L, "first2", "last2", "first2.last2", "first2.last2@example.com");
    testStudent2 = testStudentRepository.save(testStudent2);
    Student testStudent3 = new Student(3L, "first3", "last3", "first2.last3", "first3.last3@example.com");
    testStudent3 = testStudentRepository.save(testStudent3);
    Student testStudent4 = new Student(4L, "first4", "last4", "first4.last4", "first4.last4@example.com");
    testStudent4 = testStudentRepository.save(testStudent4);
    Student testStudent5 = new Student(5L, "first5", "last5", "first5.last5", "first5.last5@example.com");
    testStudent5 = testStudentRepository.save(testStudent5);
    Student testStudent6 = new Student(6L, "first6", "last6", "first6.last6", "first6.last6@example.com");
    testStudent6 = testStudentRepository.save(testStudent6);
    Student testStudent7 = new Student(7L, "first7", "last7", "first7.last7", "first7.last7@example.com");
    testStudent7 = testStudentRepository.save(testStudent7);

    testStudent1.registerModule(testModule);
    testStudent2.registerModule(testModule);
    testStudent3.registerModule(testModule);
    testStudent4.registerModule(testModule);
    testStudent5.registerModule(testModule);
    testStudent6.registerModule(testModule);
    testStudent7.registerModule(dummyModule);

    Grade testGrade1 = new Grade(testModule, 35);
    testStudent1.addGrade(testGrade1);
    testStudent1 = testStudentRepository.save(testStudent1);

    Grade testGrade2 = new Grade(testModule, 45);
    testStudent2.addGrade(testGrade2);
    testStudent2 = testStudentRepository.save(testStudent2);

    Grade testGrade3 = new Grade(testModule, 55);
    testStudent3.addGrade(testGrade3);
    testStudent3 = testStudentRepository.save(testStudent3);

    Grade testGrade4 = new Grade(testModule, 65);
    testStudent4.addGrade(testGrade4);
    testStudent4 = testStudentRepository.save(testStudent4);

    Grade testGrade5 = new Grade(testModule, 75);
    testStudent5.addGrade(testGrade5);
    testStudent5 = testStudentRepository.save(testStudent5);

    testStudent6 = testStudentRepository.save(testStudent6);
    testStudent7 = testStudentRepository.save(testStudent7);

    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/modules/moduleDetails/" + testModule.getCode())
            .accept(MediaType.APPLICATION_JSON)).andReturn();
    assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());

    Map<String, Object> response = objectMapper.readValue(result.getResponse().getContentAsString(), Map.class);

    assertNotNull(response);
    assertEquals(testModule.getCode(), ((Map<String, Object>) response.get("module")).get("code"));
    assertEquals(testModule.getName(), ((Map<String, Object>) response.get("module")).get("name"));

    assertEquals(6, ((List<?>) response.get("students")).size());
    Map<String, Object> studentData1 = (Map<String, Object>) ((List<?>) response.get("students")).get(0);
    assertEquals(testStudent1.getId(), ((Number) studentData1.get("id")).longValue());
    assertEquals(testStudent1.getFirstName(), studentData1.get("firstName"));

    Map<String, Object> studentData2 = (Map<String, Object>) ((List<?>) response.get("students")).get(1);
    assertEquals(testStudent2.getId(), ((Number) studentData2.get("id")).longValue());
    assertEquals(testStudent2.getFirstName(), studentData2.get("firstName"));

    Map<String, Object> studentData3 = (Map<String, Object>) ((List<?>) response.get("students")).get(2);
    assertEquals(testStudent3.getId(), ((Number) studentData3.get("id")).longValue());
    assertEquals(testStudent3.getFirstName(), studentData3.get("firstName"));

    Map<String, Object> studentData4 = (Map<String, Object>) ((List<?>) response.get("students")).get(3);
    assertEquals(testStudent4.getId(), ((Number) studentData4.get("id")).longValue());
    assertEquals(testStudent4.getFirstName(), studentData4.get("firstName"));

    Map<String, Object> studentData5 = (Map<String, Object>) ((List<?>) response.get("students")).get(4);
    assertEquals(testStudent5.getId(), ((Number) studentData5.get("id")).longValue());
    assertEquals(testStudent5.getFirstName(), studentData5.get("firstName"));

    Map<String, Object> studentData6 = (Map<String, Object>) ((List<?>) response.get("students")).get(5);
    assertEquals(testStudent6.getId(), ((Number) studentData6.get("id")).longValue());
    assertEquals(testStudent6.getFirstName(), studentData6.get("firstName"));

    assertNotNull(response.get("pieChart"));
    Map<String, Integer> pieChart = (Map<String, Integer>) response.get("pieChart");
    assertEquals(1, pieChart.get("0-40").intValue());
    assertEquals(1, pieChart.get("40-50").intValue());
    assertEquals(1, pieChart.get("50-60").intValue());
    assertEquals(1, pieChart.get("60-70").intValue());
    assertEquals(1, pieChart.get("70-100").intValue());
    }
}