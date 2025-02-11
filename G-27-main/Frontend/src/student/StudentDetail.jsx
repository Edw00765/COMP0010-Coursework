import React from "react";
import { useParams } from "react-router-dom";
import { useLocation, useNavigate } from "react-router-dom";
import axios from "axios";
import { Breadcrumbs, Link, Typography, Alert, Grid, Paper, Button } from "@mui/material";
import App from "../App";
import { API_ENDPOINT } from "../config";
import Register from "./Register";
import AddGrade from "./AddGrade";
import ComputeAverage from "./ComputeAverage";

function StudentDetail() {
  const { id } = useParams();
  const location = useLocation();
  const navigate = useNavigate();
  const [registeredModules, setRegisteredModules] = React.useState([]);
  const [grades, setGrades] = React.useState([]);
  const [student, setStudent] = React.useState();
  const [error, setError] = React.useState();

  const breadcrumbData = JSON.parse(sessionStorage.getItem("previousPage"));

  const fetchStudentDetails = async () => {
    setError(null);
    try {
      const { data } = await axios.get(`${API_ENDPOINT}/students/studentDetail/${id}`);
      setStudent(data.student);
      setRegisteredModules(data.registeredModules);
      setGrades(data.grades);
    } catch (err) {
      if (err.response && err.response.status === 404) {
        setError("Student not found.");
      } else {
        setError("An error occurred while fetching student details.");
      }
    }
  };
  
  function GradeRow(props) {
    const { grade } = props;
    
    return (
      <Grid key={grade.id} container style={{ padding: "10px 0" }} justifyContent="center">
        <Grid item xs={2}>
          {/* Since module is already directly on grade, access it directly */}
          {grade.module ? `${grade.module.code} ${grade.module.name}` : "Loading..."}
        </Grid>
        <Grid item xs={2}>
          {grade.score}
        </Grid>
        <Grid item xs={3}>
          <Button onClick={() => deleteGrade(grade.id)}>
            Delete Grade
          </Button>
        </Grid>
      </Grid>
    );
  }
  
  function deleteGrade(id) {
    axios
      .delete(`${API_ENDPOINT}/grades/${id}`)
      .then((response) => {
        console.log("Grade successfully deleted.");
        fetchStudentDetails();
      })
      .catch((error) => {
        console.log(error);
        if(error.status == 404) {
          setError("Grade was not found.")
        } else {
          setError("Failed to delete the grade.")
        }
      })
  }

  function unregister(moduleCode, studentId) {
    axios
      .delete(`${API_ENDPOINT}/students/studentDetail/registrations/${studentId}/${moduleCode}`)
      .then((response) => {
        console.log("Successfully unregistered.");
        fetchStudentDetails();
      })
      .catch((error) => {
        console.log(error);
        if (error.response && error.response.status === 404) {
          setError("Registration was not found.");
        } else {
          setError("Failed to unregister.");
        }
      });
  }

  
  React.useEffect(() => {
    fetchStudentDetails();
  }, [id]);

  return (
    <App>
      <Breadcrumbs sx={{ marginBottom: "30px" }}>
        <Link underline="hover" color="inherit" href="/">
          Home
        </Link>
        {breadcrumbData?.parent && (
          <Link underline="hover" color="inherit" href={breadcrumbData.parent.href}>
            {breadcrumbData.parent.label}
          </Link>
        )}
        {breadcrumbData?.current && (
          <Link underline="hover" color="inherit" href={breadcrumbData.current.href}>
            {breadcrumbData.current.label}
          </Link>
        )}
        <Typography sx={{ color: "text.primary" }}>Student Detail</Typography>
      </Breadcrumbs>
      {error && <Alert color="error">{error}</Alert>}
      <Paper elevation={3} sx={{ padding: "20px", marginBottom: "30px" }} justifyContent="center">
        <Typography variant="h5" gutterBottom>
          Student Details
        </Typography>
          <Grid container style={{ padding: "10px 0" }} justifyContent="center">
            <Grid item xs={2}>
              Student ID : {student?.id}
            </Grid>
            <Grid item xs={2}>
              First Name : {student?.firstName}
            </Grid>
            <Grid item xs={2}>
              Last Name : {student?.lastName}
            </Grid>
            <Grid item xs={2}>
              Username : {student?.username}
            </Grid>
            <Grid item xs={3}>
              Email : {student?.email}
            </Grid>
          </Grid>
      </Paper>
      <Paper elevation={3} sx={{ padding: "20px", marginBottom: "30px" }}>
        <Typography variant="h5" gutterBottom>
          Registered Modules
        </Typography>
        {!error && registeredModules.length < 1 && (
          <Alert color="warning">No Registered Modules</Alert>
        )}
        {registeredModules.length > 0 && (
          <>
            <Grid container style={{ padding: "10px 0" }} justifyContent="center">
              <Grid item xs={2}>Module Code</Grid>
              <Grid item xs={2}>Module Name</Grid>
              <Grid item xs={2}>MNC</Grid>
              <Grid item xs={2}>
              </Grid>
            </Grid>
            {registeredModules.map((rm) => (
              console.log(rm),
              student && (
                <Grid container key={rm.code} style={{ padding: "10px 0" }} justifyContent="center">
                  <Grid item xs={2}>{rm.code}</Grid>
                  <Grid item xs={2}>{rm.name}</Grid>
                  <Grid item xs={2}>{rm.mnc ? "Yes" : "No"}</Grid>
                  <Grid item xs={2}>
                    <Button onClick={() => unregister(rm.code, student?.id)}>
                      Unregister Module
                    </Button>
                  </Grid>
                </Grid>
              )
            ))}
          </>
        )}
        <>
          <Register studentId={id} onRegister={fetchStudentDetails} />
        </>
      </Paper>

      <Paper elevation={3} sx={{ padding: "20px" }}>
        <Typography variant="h5" gutterBottom>
          Grades
        </Typography>
        {!error && grades.length < 1 && (
          <Alert color="warning">No Grades</Alert>
        )}
        {grades.length > 0 && (
          <>
            <Grid container style={{ padding: "10px 0" }} justifyContent="center">
              <Grid item xs={2}>Module Code</Grid>
              <Grid item xs={2}>Score</Grid>
              <Grid item xs={3}></Grid>
            </Grid>
            {grades.map((g) => {
              return <GradeRow key={g.id} grade={g} />;
            })}
          </>
        )}
        <br />
        <br />
        <ComputeAverage studentId={student?.id}/>
        <br />
        <br />
        <AddGrade update={fetchStudentDetails} registeredModules={registeredModules} studentId={student?.id}/>
      </Paper>
    </App>
  );
}

export default StudentDetail;
