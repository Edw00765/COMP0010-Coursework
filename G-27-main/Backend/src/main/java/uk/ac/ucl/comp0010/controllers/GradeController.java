package uk.ac.ucl.comp0010.controllers;

import static java.lang.Integer.parseInt;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.ucl.comp0010.model.Grade;
import uk.ac.ucl.comp0010.model.Module;
import uk.ac.ucl.comp0010.model.Student;
import uk.ac.ucl.comp0010.repository.GradeRepository;
import uk.ac.ucl.comp0010.repository.ModuleRepository;
import uk.ac.ucl.comp0010.repository.StudentRepository;

/**
 * Controller class responsible for managing operations related to grades.
 * This class provides endpoints for adding and managing grades for students
 * within the system.
 */
@RestController
public class GradeController {
  private final StudentRepository studentRepository;
  private final ModuleRepository moduleRepository;
  private final GradeRepository gradeRepository;
  
  /**
   * Constructs a new GradeController with the specified repositories.
   *
   * @param studentRepository the repository for accessing student data
   * @param moduleRepository the repository for accessing module data
   * @param gradeRepository the repository for accessing grade data
   */
  public GradeController(StudentRepository studentRepository, ModuleRepository moduleRepository,
      GradeRepository gradeRepository) {
    this.studentRepository = studentRepository;
    this.moduleRepository = moduleRepository;
    this.gradeRepository = gradeRepository;
  }
  
  /**
   * Adds a grade to the specified student for a given module.
   * This endpoint allows adding a new grade for a student by providing the
   * student ID, module code, and grade score in the request body.
   * The student must be registered for the module associated with the
   * input module code.
   *
   * @param params a map containing the parameters:
   *               - "student_id": the ID of the student
   *               - "module_code": the code of the module
   *               - "score": the grade score
   * @return a ResponseEntity containing the created Grade object
   */
  @RequestMapping(path = {"/grades/addGrade", "/students/studentDetail/addGrade"},
      method = RequestMethod.POST)
  public ResponseEntity<Grade> addGrade(@RequestBody Map<String, String> params) {
    Student student;
    Module module;
    try {
      student = studentRepository.findById(Long.valueOf(params.get("student_id")))
        .orElseThrow();
      module = moduleRepository.findById(params.get("module_code")).orElseThrow();
      if (student.getStudentRegistration().stream().noneMatch(
          registration -> registration.getModule().equals(module))) {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
      }
    } catch (NoSuchElementException exception) {
      // If the provided student id or module code don't have entities existing in the database
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    
    Grade grade = new Grade();
    grade.setModule(module);
    grade.setScore(parseInt((params.get("score"))));
    
    // Allows for updates without allowing duplicate grades for a particular module, student pair
    Optional<Grade> alreadyExists = gradeRepository.findByModuleAndStudent(module, student);
    alreadyExists.ifPresent(gradeRepository::delete);
    
    student.addGrade(grade);
    grade = gradeRepository.save(grade);
    return ResponseEntity.ok(grade);
  }
  
  /**
   * A method to delete a particular Grade from the backend of the database.
   *
   * @param id The unique long identifier that represents a grade
   * @return A responseEntity describing the success of the operation
   */
  @DeleteMapping(value = "/grades/{id}")
  public ResponseEntity<Grade> deleteGrade(@PathVariable long id) {
    Grade retrievedGrade;
    try {
      retrievedGrade = gradeRepository.findById(id).get();
    } catch (NoSuchElementException exception) {
      return ResponseEntity.notFound().build();
    }
    gradeRepository.delete(retrievedGrade);
    return ResponseEntity.ok().build();
  }
}