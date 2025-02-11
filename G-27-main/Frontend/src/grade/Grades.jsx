import React from "react";
import axios from "axios";
import { Breadcrumbs, Link, Typography, Alert, Grid, TextField, Button, Select, MenuItem, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, TablePagination, Paper} from "@mui/material";
import App from "../App";
import { API_ENDPOINT } from "../config";
import AddGrade from "./AddGrade";

function Grades() {
  const [grades, setGrades] = React.useState([]);
  const [error, setError] = React.useState();
  const [studentFilter, setStudentFilter] = React.useState("");
  const [moduleFilter, setModuleFilter] = React.useState("");
  const [page, setPage] = React.useState(0);
  const [rowsPerPage, setRowsPerPage] = React.useState(5);
  
  React.useEffect(() => {
    setError(null);
    updateGrades();
  }, []);

  function updateGrades() {
    axios
      .get(`${API_ENDPOINT}/grades`)
      .then((response) => {
        setGrades(response.data._embedded.grades);
      })
      .catch((response) => {
        setError(response.message);
      });
  }
  
  function deleteGrade(id) {
    axios
      .delete(`${API_ENDPOINT}/grades/${id}`)
      .then((response) => {
        console.log("Grade successfully deleted.");
        updateGrades();
        setError("Grade successfully deleted.");
      })
      .catch((error) => {
        if(error.status == 404) {
          setError("Grade was not found.")
        } else {
          setError("Failed to delete grade.")
        }
      })
  }
  
  const filteredGrades = grades.filter((grade) => {
    const matchesStudentFilter = (student) =>
      student &&
      (String(student.id).includes(studentFilter) ||
        `${student.firstName} ${student.lastName}`
          .toLowerCase()
          .includes(studentFilter.toLowerCase()));

    const matchesModuleFilter = (module) =>
      module &&
      (String(module.code).toLowerCase().includes(moduleFilter.toLowerCase()) ||
        module.name.toLowerCase().includes(moduleFilter.toLowerCase()));

    const studentRequest = axios.get(grade._links.student.href).then((res) => res.data);
    const moduleRequest = axios.get(grade._links.module.href).then((res) => res.data);

    return Promise.all([studentRequest, moduleRequest]).then(([student, module]) =>
      matchesStudentFilter(student) && matchesModuleFilter(module)
    );
  });
  
  function GradeRow(props) {
    const { grade, studentFilter, moduleFilter } = props;
    const [student, setStudent] = React.useState();
    const [module, setModule] = React.useState();

    React.useEffect(() => {
      axios
        .get(grade._links.module.href)
        .then((response) => setModule(response.data));

      axios
        .get(grade._links.student.href)
        .then((response) => setStudent(response.data));
    }, [grade]);
    
    const matchesFilter = //This is a bool which checks if it passes the checks
      (studentFilter
        ? (
            String(student.id).includes(studentFilter) || 
            `${student?.firstName} ${student?.lastName}`.toLowerCase().includes(studentFilter.toLowerCase())
          )
        : true) &&
      (moduleFilter
        ? (
            String(module.code).toLowerCase().includes(moduleFilter.toLowerCase()) || 
            module.name.toLowerCase().includes(moduleFilter.toLowerCase())
          )
        : true);
    if (!matchesFilter) return null;
      
    return (
      <TableRow key={grade.id}>
        <TableCell>
          {student ? `${student.firstName} ${student.lastName} (${student.id})` : "Loading..."}
        </TableCell>
        <TableCell>
          {module ? `${module.code} ${module.name}` : "Loading..."}
        </TableCell>
        <TableCell>{grade.score}</TableCell>
        <TableCell>
          <Button onClick={() => deleteGrade(grade.id)}>
            Delete Grade
          </Button>
        </TableCell>
      </TableRow>
    );
  }

  return (
    <App>
      <Breadcrumbs sx={{ marginBottom: "30px" }}>
        <Link underline="hover" color="inherit" href="/">
          Home
        </Link>
        <Typography sx={{ color: "text.primary" }}>Grades</Typography>
      </Breadcrumbs>
      {error && <Alert color="error">{error}</Alert>}
      {!error && grades.length < 1 && <Alert color="warning">No grades</Alert>}
      <div style={{ display: 'flex', alignItems: 'center', marginBottom: '20px'}}>
        <TextField
          label="Student Filter"
          placeholder="Student Filter"
          value={studentFilter || ""}
          onChange={(e) => setStudentFilter(e.target.value)}
          sx={{ height: '80px', marginLeft: '0', marginRight: '10px' }}
        />
        <TextField
          label="Module Filter"
          placeholder="Module Filter"
          value={moduleFilter || ""}
          onChange={(e) => setModuleFilter(e.target.value)}
          sx={{ height: '80px', marginLeft: '0', marginRight: '10px' }}
        />
      </div>
      {grades.length > 0 && (
        <TableContainer component={Paper}>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell style={{ fontWeight: "bold" }}>Student</TableCell>
                <TableCell style={{ fontWeight: "bold" }}>Module</TableCell>
                <TableCell style={{ fontWeight: "bold" }}>Grade</TableCell>
                <TableCell style={{ fontWeight: "bold" }}>Actions</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {grades.map((grade) => (
                <GradeRow key={grade.id} grade={grade} />
              ))}
            </TableBody>
          </Table>
        </TableContainer>
      )}
      <br />
      <br />
      <AddGrade update={updateGrades} />
    </App>
  );
}

export default Grades;
