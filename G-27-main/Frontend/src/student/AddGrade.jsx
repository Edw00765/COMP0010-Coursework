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

function AddGrade({update, registeredModules, studentId}) {
  const [grade, setGrade] = React.useState({});
  const [error, setError] = React.useState();

  function request() {
    setError(null);
    const updatedGrade = { ...grade, student_id: studentId };
    axios
      .post(`${API_ENDPOINT}/students/studentDetail/addGrade`, updatedGrade)
      .then(() => {
        setGrade({
            "module_code": null,
            "student_id": studentId,
            "score": null
        })
        update();
      })
      .catch((response) => {
        if (response.status === 500) {
          setError("Please choose the module and enter the grade.");
        } else {
          setError(response.message);
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
        value={grade.module_code ?? ""}
        onChange={(e) => setGrade({ ...grade, module_code: e.target.value })}
        label="Module"
        displayEmpty
      >
      <MenuItem value="" disabled>
        Modules
      </MenuItem>
        {registeredModules &&
          registeredModules.map((m) => {
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
        value={grade.score ?? ""}
        onChange={(e) => setGrade({ ...grade, score: Number(e.target.value) || null})}
      />
      <br />
      <br />
      <Button onClick={request}>Add/Update</Button>
      <br />
      <br />
      {error && <Alert color="error">{error}</Alert>}
    </Paper>
  );
}

export default AddGrade;
