import React from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import { Breadcrumbs, Link, Typography, Alert, Grid, Button, TextField, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, TablePagination } from "@mui/material";
import App from "../App";
import { API_ENDPOINT } from "../config";
import AddStudent from "./AddStudent";



function Students() {
  const navigate = useNavigate();
  const [students, setStudents] = React.useState([]);
  const [error, setError] = React.useState();
  const [idFilter, setIdFilter] = React.useState();
  const [nameFilter, setNameFilter] = React.useState();
  const [usernameFilter, setUsernameFilter] = React.useState();
  const [emailFilter, setEmailFilter] = React.useState();
  const [page, setPage] = React.useState(0);
  const [rowsPerPage, setRowsPerPage] = React.useState(5);

  const handleNavigateToDetail = (studentId) => {
    sessionStorage.setItem("previousPage", JSON.stringify({
      parent: null,
      current: { href: "/students", label: "Students" },
    }));
    navigate(`/students/studentDetail/${studentId}`);
  };
  
  React.useEffect(() => {
    updateStudents();
  }, []);

  function updateStudents() {
    setError(null);
    axios
      .get(`${API_ENDPOINT}/students`)
      .then((response) => {
        setStudents(response.data._embedded.students);
      })
      .catch((response) => {
        setError(response.message);
      });
  }
  
  function deleteStudent(id) {
    axios
      .delete(`${API_ENDPOINT}/students/${id}`)
      .then((response) => {
        console.log("Student successfully deleted.");
        updateStudents();
        setError("Student successfully deleted.");
      })
      .catch((error) => {
        if(error.status == 404) {
          setError("Student was not found.")
        } else {
          setError("Failed to delete student.")
        }
      })
  }
  
  const handleChangePage = (event, newPage) => {
    setPage(newPage);
  };
  
  const handleChangeRowsPerPage = (event) => {
    setRowsPerPage(parseInt(event.target.value, 10));
    setPage(0);
  };
  
  const filteredStudents = students.filter((s) => {
    const matchesFilter =
      (nameFilter
        ? `${s?.firstName} ${s?.lastName}`.toLowerCase().includes(nameFilter.toLowerCase())
        : true) &&
      (idFilter ? String(s.id).includes(idFilter) : true) &&
      (emailFilter ? s.email.toLowerCase().includes(emailFilter.toLowerCase()) : true) &&
      (usernameFilter ? s.username.toLowerCase().includes(usernameFilter.toLowerCase()) : true);

    return matchesFilter;
  });
  
  return (
    <App>
      <Breadcrumbs sx={{ marginBottom: "30px" }}>
        <Link underline="hover" color="inherit" href="/">
          Home
        </Link>
        <Typography sx={{ color: "text.primary" }}>Students</Typography>
      </Breadcrumbs>
      {error && <Alert color="error">{error}</Alert>}
      {!error && students.length < 1 && (
        <Alert color="warning">No students</Alert>
      )}
      <div style={{ display: 'flex', alignItems: 'center', marginBottom: '20px'}}>
        <TextField
          label="Id Filter"
          placeholder="Id Filter"
          value={idFilter || ""}
          onChange={(e) => setIdFilter(e.target.value)}
          sx={{ height: '80px', marginLeft: '0', marginRight: '10px' }}
        />
        <TextField
          label="Name Filter"
          placeholder="Name Filter"
          value={nameFilter || ""}
          onChange={(e) => setNameFilter(e.target.value)}
          sx={{ height: '80px', marginLeft: '0', marginRight: '10px' }}
        />
        <TextField
          label="Username Filter"
          placeholder="Username Filter"
          value={usernameFilter || ""}
          onChange={(e) => setUsernameFilter(e.target.value)}
          sx={{ height: '80px', marginLeft: '0', marginRight: '10px' }}
        />
        <TextField
          label="Email Filter"
          placeholder="Email Filter"
          value={emailFilter || ""}
          onChange={(e) => setEmailFilter(e.target.value)}
          sx={{ height: '80px', marginLeft: '0', marginRight: '10px' }}
        />
      </div>
      
      {filteredStudents.length > 0 && (
        <TableContainer>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>Student Id</TableCell>
                <TableCell>First Name</TableCell>
                <TableCell>Last Name</TableCell>
                <TableCell>Username</TableCell>
                <TableCell>Email</TableCell>
                <TableCell>Actions</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {filteredStudents
                .slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage)
                .map((s) => (
                  <TableRow key={s.id}>
                    <TableCell>{s.id}</TableCell>
                    <TableCell>{s.firstName}</TableCell>
                    <TableCell>{s.lastName}</TableCell>
                    <TableCell>{s.username}</TableCell>
                    <TableCell>{s.email}</TableCell>
                    <TableCell>
                      <Button onClick={() => handleNavigateToDetail(s.id)}>View Details</Button>
                      <Button onClick={() => deleteStudent(s.id)}>Delete Student</Button>
                    </TableCell>
                  </TableRow>
                ))}
            </TableBody>
          </Table>
          <TablePagination
            rowsPerPageOptions={[5, 10, 25]}
            component="div"
            count={filteredStudents.length}
            rowsPerPage={rowsPerPage}
            page={page}
            onPageChange={handleChangePage}
            onRowsPerPageChange={handleChangeRowsPerPage}
          />
        </TableContainer>
      )}
      <br />
      <br />
      <AddStudent update={updateStudents} />
    </App>
  );
}

export default Students;
