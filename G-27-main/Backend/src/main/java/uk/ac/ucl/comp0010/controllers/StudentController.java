package uk.ac.ucl.comp0010.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.ucl.comp0010.model.Grade;
import uk.ac.ucl.comp0010.model.Module;
import uk.ac.ucl.comp0010.model.Registration;
import uk.ac.ucl.comp0010.model.Student;
import uk.ac.ucl.comp0010.repository.ModuleRepository;
import uk.ac.ucl.comp0010.repository.RegistrationRepository;
import uk.ac.ucl.comp0010.repository.StudentRepository;

/**
 * Controller class responsible for managing operations related to students.
 * This class provides endpoints for managing students within the system.
 */
@RestController
public class StudentController {

  private final StudentRepository studentRepository;
  private final ModuleRepository moduleRepository;
  private final RegistrationRepository registrationRepository;

  /**
   * Constructs a new StudentController with the specified repositories.
   *
   * @param studentRepository the repository for accessing student data
   * @param moduleRepository  the repository for accessing module data
   */
  public StudentController(StudentRepository studentRepository, ModuleRepository moduleRepository,
      RegistrationRepository registrationRepository) {
    this.studentRepository = studentRepository;
    this.moduleRepository = moduleRepository;
    this.registrationRepository = registrationRepository;
  }

  /**
   * Handles HTTP GET requests to retrieve detailed information about a specific student.
   * This method fetches a student's information, their registered modules, and their grades
   * based on the provided student ID.
   *
   * @param id The unique identifier of the student whose details are to be retrieved.
   * @return A {@link ResponseEntity} containing a map with the following keys:
   *         <ul>
   *             <li><b>"student"</b>: The {@link Student} object representing the student's
   *             details.</li>
   *             <li><b>"registeredModules"</b>: A {@link List} of {@link Module} objects
   *             representing the
   *             modules the student is registered for.</li>
   *             <li><b>"grades"</b>: A list of grades associated with the student.</li>
   *         </ul>
   *         The response is wrapped in an HTTP status of {@code 200 OK}.
   */
  @GetMapping(value = "/students/studentDetail/{id}")
  public ResponseEntity<Map<String, Object>> sendStudentDetails(@PathVariable long id) {
    Student student = studentRepository.findById(id).orElseThrow();

    List<Module> registeredModules = new ArrayList<Module>();
    for (Registration registration : student.getStudentRegistration()) {
      registeredModules.add(registration.getModule());
    }

    Map<String, Object> response = new HashMap<>();
    response.put("student", student);
    response.put("registeredModules", registeredModules);
    response.put("grades", student.getGradeList());

    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  /**
   * Handles HTTP POST requests to register a student for a module.
   * This method processes a registration request by accepting a student's ID and
   * a module code.
   *
   * @param params A map containing the student's ID and the module code for registration.
   *               The map should include the following keys:
   *               <ul>
   *                 <li><b>student_id</b>: The unique ID of the student to register.</li>
   *                 <li><b>module_code</b>: The code of the module the student is to be registered
   *                 for.</li>
   *               </ul>
   * @return A {@link ResponseEntity} containing the {@link Registration} object with HTTP status
   *         200 (OK) if the registration is successful, or HTTP status 409 (CONFLICT) if the
   *         student is already registered for the module.
   */
  @PostMapping(value = "/students/studentDetail/register")
  public ResponseEntity<Registration> registerStudent(@RequestBody Map<String, String> params) {
    Student student = studentRepository.findById(Long.valueOf(params.get("student_id")))
        .orElseThrow();
    Module module = moduleRepository.findById(params.get("module_code")).orElseThrow();
    for (Registration registration : student.getStudentRegistration()) {
      if (registration.getModule().equals(module)) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .header("Error-Message", "Student is already registered for this module")
            .build();
      }
    }
    student.registerModule(module);
    studentRepository.save(student);
    Registration registration = student.getStudentRegistration()
        .get(student.getStudentRegistration().size() - 1);
    return ResponseEntity.ok(registration);
  }

  /**
   * Endpoint to compute and return the average grade of a student.
   *
   * @param id the unique identifier of the student whose average grade is to be computed.
   * @return a {@link ResponseEntity} containing a map with the key "average" and the computed
   *     average grade as the value.
   */
  @GetMapping(value = "/students/studentDetail/computeAverage/{id}")
  public ResponseEntity<Map<String, Double>> sendAverage(@PathVariable long id) {
    Student student = studentRepository.findById(id).orElseThrow();

    Map<String, Double> response = new HashMap<>();
    response.put("average", (double) student.computeAverage());

    return new ResponseEntity<>(response, HttpStatus.OK);
  }
  
  /**
   * A method to delete a particular Student from the backend of the database.
   *
   * @param id The unique long identifier that represents a student
   * @return A responseEntity describing the success of the operation
   */
  @DeleteMapping(value = "/students/{id}")
  public ResponseEntity<Student> deleteStudent(@PathVariable long id) {
    Student retrievedStudent;
    try {
      retrievedStudent = studentRepository.findById(id).get();
    } catch (NoSuchElementException exception) {
      return  ResponseEntity.notFound().build();
    }
    studentRepository.delete(retrievedStudent);
    return ResponseEntity.ok().build();
  }
  
  /**
   * A method to delete a particular registration from the backend of the database.
   *
   * @param studentId A unique long identifier that represents a student
   * @param moduleCode a unique string that represents a module
   * @return A response entity that describes the success of the operation
   */
  @DeleteMapping(value = "/students/studentDetail/registrations/{studentId}/{moduleCode}")
  public ResponseEntity<Registration> deleteRegistration(@PathVariable long studentId,
                                                         @PathVariable String moduleCode) {
    Student student = studentRepository.findById(studentId).orElseThrow();
    Module module = moduleRepository.findById(moduleCode).orElseThrow();

    Optional<Registration> registrationOpt =
        registrationRepository.findByModuleAndStudent(module, student);
    Registration registration;
    try {
      if (registrationOpt.isPresent()) {
        registration = registrationOpt.get();
      } else {
        throw new NoSuchElementException();
      }
    } catch (NoSuchElementException exception) {
      return ResponseEntity.notFound().build();
    }
    registrationRepository.delete(registration);
    int index = 0;
    for (Grade grade : student.getGradeList()) {
      if (grade.getModule().getCode().equals(module.getCode())) {
        index = student.getGradeList().indexOf(grade);
      }
    }
    student.getGradeList().remove(index);
    studentRepository.save(student);
    return ResponseEntity.ok().build();
  }
}