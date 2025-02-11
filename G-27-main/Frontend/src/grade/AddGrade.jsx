import React from "react";
import axios from "axios";
import {
  Paper,
  Button,
  Typography,
  Select,
  MenuItem,
  TextField,
  Alert,
} from "@mui/material";
import { API_ENDPOINT } from "../config";

function AddGrade(props) {
  const [grade, setGrade] = React.useState({});
  const [students, setStudents] = React.useState([]);
  const [modules, setModules] = React.useState();
  const [error, setError] = React.useState();

  React.useEffect(() => {
    setError(null);
    axios
      .get(`${API_ENDPOINT}/students`)
      .then((response) => {
        setStudents(response.data._embedded.students);
      })
      .catch((error) => {
        setError(error.message);
      });

    axios
      .get(`${API_ENDPOINT}/modules`)
      .then((response) => {
        setModules(response.data._embedded.modules);
      })
      .catch((error) => {
        setError(error.message);
      });
  }, []);

  function request() {
    setError(null);
    axios
      .post(`${API_ENDPOINT}/grades/addGrade`, grade)
      .then(() => {
        setGrade({
          score : null,
          module : null,
          student : null
        })
        props.update();
      })
      .catch((response) => {
        console.log(response);
        if (response.status === 500) {
          setError("Please select the student, module, and score.");
        } else if (response.status === 406) {
          setError("Please select a module which is registered to this student");
        } else {
          setError(response.message)
        }
      });
  }

  return (
    <Paper sx={{ padding: "30px" }}>
      <Typography variant="h5">Add Grade</Typography>
      <br />
      <br />
      <Select
        sx={{ minWidth: "300px" }}
        value={grade.student_id ?? ""}
        onChange={(e) => setGrade({ ...grade, student_id: e.target.value || null })}
        displayEmpty
      >
        <MenuItem value="" disabled>
          Student
        </MenuItem>
        {students &&
          students.map((s) => {
            return (
              <MenuItem
                key={s.id}
                value={s.id}
              >{`${s.firstName} ${s.lastName} (${s.id})`}</MenuItem>
            );
          })}
      </Select>
      <Select
        sx={{ minWidth: "300px" }}
        value={grade.module_code ?? ""}
        onChange={(e) => setGrade({ ...grade, module_code: e.target.value || null })}
        displayEmpty
      >
      <MenuItem value="" disabled>
        Module
      </MenuItem>
        {modules &&
          modules.map((m) => {
            return (
              <MenuItem
                key={m.code}
                value={m.code}
              >{`${m.code} ${m.name}`}</MenuItem>
            );
          })}
      </Select>
      <TextField
        label="Score"
        placeholder="Score"
        value={grade.score ?? ""}
        onChange={(e) => setGrade({ ...grade, score: e.target.value === "" ? "" : Number(e.target.value) })}
      />
      <br />
      <br />
      <Button onClick={request}>Add</Button>
      <br />
      <br />
      {error && <Alert color="error">{error}</Alert>}
    </Paper>
  );
}

export default AddGrade;
