How to run backend, from base directory:
- cd Backend
- mvn spring-boot:run
  
How to run frontend, from base directory:
- cd Frontend
- npm ci (if this does not work then run 'npm install')
- npm run dev

Note: Run Backend first, then Frontend.

Port : http://localhost:5173/

Extensions Made:
When we connected the basic frontend files provided to us to our basic backend, we could only add/update students, add/update modules and display students, modules and grades. Using the lectures, we then added the ability to add/update grades to a student. The extensions we made to this version:
- Added Filters for Students, Modules and Grades.
- Added ability to delete Students, Modules and Grades.
- Added a page called ‘StudentDetail’ that can be accessed by clicking the ‘ViewDetails’ button in student data rows. This page provides the basic details of the student, the modules that the student is registered to and the student’s grades. This page also allows the user to register/unregister the student to/from a module and add a grade for the student.
- Added a page called ‘ModuleDetail’ that can be accessed by clicking the ‘View Details’ button in module data rows. This page provides basic details of the module, No. of Students, No. of Students that have been graded, average grade for the module, grade distribution pie chart and details of all the students that are registered to the module and their grades (if graded). Also provides the ability to redirect to view student’s details and unregister a student from the module.
