import React, { useEffect, useRef } from "react";
import { useParams } from "react-router-dom";
import { useNavigate, useLocation } from "react-router-dom";
import axios from "axios";
import { Breadcrumbs, Link, Typography, Alert, Grid, Button, TextField, Paper, MenuItem, Select} from "@mui/material";
import App from "../App";
import { API_ENDPOINT } from "../config";


function ModuleDetail() {
    const { id } = useParams();
    const navigate = useNavigate();
    const location = useLocation();
    const [students, setStudents] = React.useState([]);
    const [error, setError] = React.useState();
    const [totalGrades, setTotalGrades] = React.useState();
    const [average, setAverage] = React.useState();
    const [pieChart, setPieChart] = React.useState({});
    const [module, setModule] = React.useState();
    const [idFilter, setIdFilter] = React.useState();
    const [nameFilter, setNameFilter] = React.useState();
    const [usernameFilter, setUsernameFilter] = React.useState();
    const [emailFilter, setEmailFilter] = React.useState();
    const [gradeStatusFilter, setGradeStatusFilter] = React.useState("");
    const canvasRef = React.useRef(null);

    const handleNavigateToStudentDetail = (studentId) => {
        // Store breadcrumb details for Modules and Module Detail
        sessionStorage.setItem("previousPage", JSON.stringify({
          parent: { href: "/modules", label: "Modules" },
          current: { href: location.pathname, label: "Module Detail" },
        }));
        navigate(`/students/studentDetail/${studentId}`);
      };

    React.useEffect(() => {
      updateStudents();
    }, []);

    useEffect(() => {
        if (Object.keys(pieChart).length > 0) {
            drawPieChart();
        }
    }, [pieChart]);


    function updateStudents() {
    setError(null);
    axios
        .get(`${API_ENDPOINT}/modules/moduleDetails/${id}`)
        .then((response) => {
        setStudents(response.data.students);
        setTotalGrades(response.data.totalGrades);
        setAverage(response.data.average);
        setPieChart(response.data.pieChart);
        setModule(response.data.module);
        })
        .catch((response) => {
        setError(response.message);
        });
    }

    function drawPieChart() {
        const canvas = canvasRef.current;
        if (!canvas) return;
        const ctx = canvas.getContext("2d");
        if (!ctx) return;
        const data = Object.values(pieChart);
        const labels = Object.values(pieChart);
        const key = Object.keys(pieChart);
        const colors = ["#FF6384", "#36A2EB", "#FFCE56", "#4BC0C0", "#9966FF"];
        const total = data.reduce((sum, value) => sum + value, 0);
        let startAngle = 0;

        data.forEach((value, index) => {
            const sliceAngle = (value / total) * 2 * Math.PI;
            ctx.beginPath();
            ctx.moveTo(150, 75); // Center of the pie chart
            ctx.arc(150, 75, 75, startAngle, startAngle + sliceAngle);
            ctx.closePath();
            ctx.fillStyle = colors[index];
            ctx.fill();
            startAngle += sliceAngle;
        });

        // Draw labels
        startAngle = 0;
        data.forEach((value, index) => {
            if (value > 0) {
                const sliceAngle = (value / total) * 2 * Math.PI;
                const labelX = 150 + (75 / 2) * Math.cos(startAngle + sliceAngle / 2);
                const labelY = 75 + (75 / 2) * Math.sin(startAngle + sliceAngle / 2);
                ctx.fillStyle = "black";
                ctx.font = "12px Arial";
                ctx.fillText(labels[index], labelX, labelY);
                startAngle += sliceAngle;
            }
        });

        // Draw legend
        const legend = document.getElementById("legend");
        legend.innerHTML = "";
        key.forEach((label, index) => {
            const legendItem = document.createElement("div");
            legendItem.style.marginLeft = "8px";
            legendItem.style.marginTop = "8px";
            legendItem.innerHTML = `<span style="display:inline-block;width:20px;background-color:${colors[index % colors.length]};">&nbsp;</span> ${label}`;
            legend.appendChild(legendItem);
        });
    }

    function unregister(moduleCode, studentId) {
        axios
          .delete(`${API_ENDPOINT}/students/studentDetail/registrations/${studentId}/${moduleCode}`)
          .then((response) => {
            console.log("Successfully unregistered.");
            updateStudents();
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
      
    return (
    <App>
        <Breadcrumbs sx={{ marginBottom: "30px" }}>
        <Link underline="hover" color="inherit" href="/">
            Home
        </Link>
        <Link underline="hover" color="inherit" href="/modules">
            Modules
        </Link>
        <Typography sx={{ color: "text.primary" }}>Module Detail</Typography>
        </Breadcrumbs>
        {error && <Alert color="error">{error}</Alert>}
        {!error && students.length < 1 && (
        <Alert color="warning">No students registered to the Module</Alert>
        )}
         <Paper elevation={3} sx={{ padding: "20px", marginBottom: "30px" }} justifyContent="center">
          <Typography variant="h5">
            Module Details
          </Typography>
          <Grid container style={{ padding: "10px 0" }} justifyContent="center">
            <Grid item xs={2}>
                Module Code : {module?.code}
            </Grid>
            <Grid item xs={2}>
                Module Name : {module?.name}
            </Grid>
            <Grid item xs={2}>
                MNC : {module?.mnc ? "Yes" : "No"}
            </Grid>
          </Grid>
          <Grid container style={{ padding: "10px 0" }} justifyContent="center">
            <Grid item xs={2}>
                No. of Students : {students.length}
            </Grid>
            <Grid item xs={2}>
                No. of Students Graded : {totalGrades}
            </Grid>
            <Grid item xs={2}>
                Grade Average : {average}
            </Grid>
          </Grid>
         </Paper>
         <div>
            {totalGrades > 0 && (
            <Paper elevation={3} sx={{ padding: "20px", marginBottom: "30px" }} justifyContent="center">
                <Typography variant="h6">Grade Distribution</Typography>
                <canvas ref={canvasRef} width="300" height="150"></canvas>
                <div id="legend" style={{ display: "flex", justifyContent: "center" }}></div>
            </Paper>
            )}
         </div>
         {students.length > 0 && (
        <>
        <div style={{ display: 'flex', alignItems: 'flex-end', gap: '10px', marginBottom: '20px'}}>
        <div style={{ display: 'flex', gap: '10px' }}>
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
        <Select
        value={gradeStatusFilter ?? ""}
        onChange={(e) => setGradeStatusFilter(e.target.value)}
        sx={{ display: 'flex' }}
        displayEmpty
        >
        <MenuItem value="">Grade Status Filter / None</MenuItem>
        <MenuItem value="true">Graded</MenuItem>
        <MenuItem value="false">Not Graded</MenuItem>
        </Select>
        </div>
            <Grid container style={{ padding: "10px 0" }} justifyContent="center">
            <Grid item xs={1.5}>
                <Typography variant="subtitle1" style={{ fontWeight: 'bold' }}>
                Student Id
                </Typography>
            </Grid>
            <Grid item xs={1.5}>
                <Typography variant="subtitle1" style={{ fontWeight: 'bold' }}>
                First Name
                </Typography>
            </Grid>
            <Grid item xs={1.5}>
                <Typography variant="subtitle1" style={{ fontWeight: 'bold' }}>
                Last Name
                </Typography>
            </Grid>
            <Grid item xs={1.5}>
                <Typography variant="subtitle1" style={{ fontWeight: 'bold' }}>
                UserName
                </Typography>
            </Grid>
            <Grid item xs={2}>
                <Typography variant="subtitle1" style={{ fontWeight: 'bold' }}>
                Email
                </Typography>
            </Grid>
            <Grid item xs={1.5}>
                <Typography variant="subtitle1" style={{ fontWeight: 'bold' }}>
                Grade
                </Typography>
            </Grid>
            <Grid item xs={2}>
                <Typography variant="subtitle1" style={{ fontWeight: 'bold' }}>
                Actions
                </Typography>
            </Grid>
            <Grid item xs={1.5}>

            </Grid>
            </Grid>
            {students.map((s) => {
            const hasGradeWithModuleCode = s.gradeList.some(grade => grade.module.code === module?.code);
            const matchesFilter = //This is a bool which checks if it passes the checks
                (nameFilter
                ? (
                    `${s?.firstName} ${s?.lastName}`.toLowerCase().includes(nameFilter.toLowerCase())
                    )
                : true) &&
                (idFilter
                ? (
                    String(s.id).includes(idFilter)
                    )
                : true)&&
                (emailFilter
                ? (
                    s.email.toLowerCase().includes(nameFilter.toLowerCase())
                    )
                : true) &&
                (usernameFilter
                ? (
                    s.username.toLowerCase().includes(usernameFilter.toLowerCase())
                    )
                : true) &&
                (gradeStatusFilter
                    ? (
                        String(hasGradeWithModuleCode).toLowerCase().includes(gradeStatusFilter)
                      )
                    : true);
            if (!matchesFilter) return null;
            const grade = s.gradeList.find(grade => grade.module.code === module?.code);
            const gradeValue = grade ? grade.score : 'Not Graded';
            return (
                <Grid container key={s.id} style={{ padding: "10px 0" }} justifyContent="center">
                <Grid item xs={1.5}>
                    {s.id}
                </Grid>
                <Grid item xs={1.5}>
                    {s.firstName}
                </Grid>
                <Grid item xs={1.5}>
                    {s.lastName}
                </Grid>
                <Grid item xs={1.5}>
                    {s.username}
                </Grid>
                <Grid item xs={2}>
                    {s.email}
                </Grid>
                <Grid item xs={1.5}>
                    {gradeValue}
                </Grid>
                <Grid item xs={1}>
                    <Button onClick={() => handleNavigateToStudentDetail(s.id)}>View Details</Button>
                </Grid>
                <Grid item xs={1}>
                    <Button onClick={() => unregister(module.code, s?.id)}>
                      Unregister Module
                    </Button>
                </Grid>
                </Grid>
            );
            })}
        </>
        )}
    </App>
    );
}

export default ModuleDetail;
