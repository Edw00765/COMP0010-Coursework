package uk.ac.ucl.comp0010.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.ucl.comp0010.model.Grade;
import uk.ac.ucl.comp0010.model.Module;
import uk.ac.ucl.comp0010.model.Registration;
import uk.ac.ucl.comp0010.model.Student;
import uk.ac.ucl.comp0010.repository.ModuleRepository;
import uk.ac.ucl.comp0010.repository.RegistrationRepository;

/**
 * Controller class responsible for managing operations related to Modules.
 * This class provides endpoints for adding and managing modules (for professors)
 */
@RestController
public class ModuleController {
  private final ModuleRepository moduleRepository;

  private final RegistrationRepository registrationRepository;
  
  /**
   * Constructs a new ModuleController, with the required repository classes as endpoints.
   *
   * @param moduleRepository the repository for accessing Module data
   */
  public ModuleController(ModuleRepository moduleRepository,
      RegistrationRepository registrationRepository) {
    this.moduleRepository = moduleRepository;
    this.registrationRepository = registrationRepository;
  }
  
  /**
   * A method to delete a particular Module from the backend of the database.
   *
   * @param id The unique string identifier that represents a module
   * @return A responseEntity describing the success of the operation
   */
  @DeleteMapping(value = "/modules/{id}")
  public ResponseEntity<Module> deleteModule(@PathVariable String id) {
    Module retrievedModule;
    try {
      retrievedModule = moduleRepository.findById(id).get();
    } catch (NoSuchElementException exception) {
      return ResponseEntity.notFound().build();
    }
    moduleRepository.delete(retrievedModule);
    return ResponseEntity.ok().build();
  }

  /**
   * Handles the HTTP GET request to retrieve details of a specific module, including the module
   * information, students registered in the module, grade distribution, total number of grades,
   * and the average grade.
   *
   * @param moduleCode The code of the module for which details are requested.
   * @return A ResponseEntity containing a map of module details.
   */
  @GetMapping(value = "/modules/moduleDetails/{moduleCode}")
  public ResponseEntity<Map<String, Object>> sendModuleDetails(@PathVariable String moduleCode) {
    Module module = moduleRepository.findById(moduleCode).orElseThrow();

    List<Student> students = new ArrayList<>();
    double totalGrade = 0;
    int gradeCount = 0;
    Map<String, Integer> pieCount = new HashMap<>();
    pieCount.put("0-40", 0);
    pieCount.put("40-50", 0);
    pieCount.put("50-60", 0);
    pieCount.put("60-70", 0);
    pieCount.put("70-100", 0);

    for (Registration registration : registrationRepository.findAll()) {
      if (registration.getModule().equals(module)) {
        Student student = registration.getStudent();
        students.add(student);
        try {
          Grade grade = student.getGrade(module);
          int score = grade.getScore();
          totalGrade = totalGrade + score;
          gradeCount = gradeCount + 1;
          if (score < 40) {
            pieCount.replace("0-40", pieCount.get("0-40") + 1);
          } else if (score < 50) {
            pieCount.replace("40-50", pieCount.get("40-50") + 1);
          } else if (score < 60) {
            pieCount.replace("50-60", pieCount.get("50-60") + 1);
          } else if (score < 70) {
            pieCount.replace("60-70", pieCount.get("60-70") + 1);
          } else {
            pieCount.replace("70-100", pieCount.get("70-100") + 1);
          }
        } catch (Exception ignored) {
          continue;
        }
      }

    }

    Map<String, Object> response = new HashMap<>();
    response.put("students", students);
    response.put("totalGrades", gradeCount);
    response.put("average", totalGrade / gradeCount);
    response.put("pieChart", pieCount);
    response.put("module", module);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

}
