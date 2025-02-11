import React from "react";
import axios from "axios";
import {
  Paper,
  Button,
  Typography,
  Select,
  MenuItem,
  Alert,
} from "@mui/material";
import { API_ENDPOINT } from "../config";

function Register({ studentId, onRegister }) {
  const [module, setModule] = React.useState({});
  const [modules, setModules] = React.useState([]);
  const [error, setError] = React.useState();

  React.useEffect(() => {
    setError(null);
    axios
      .get(`${API_ENDPOINT}/modules`)
      .then((response) => setModules(response.data._embedded.modules))
      .catch((response) => setError(response.message));
  }, []);

  function request() {
    setError(null);
    axios
      .post(`${API_ENDPOINT}/students/studentDetail/register`, { ...module, student_id: studentId })
      .then(() => {
        if (onRegister) {
          onRegister();
          setModule({
           "module_code": null
          })
        }
        
      })
      .catch((response) => {
        if (response.status == 500) {
          setError("Please choose which module to register.")
        } else if (response.status == 409) {
          setError("Please choose a module which has not been registered.")
        } else {
          setError(response.message);
        }
        console.log(response);
      });
  }

  return (
    <Paper sx={{ padding: "30px" }}>
      <Typography variant="h5">Register Student to Module</Typography>
      <br />
      <br />
      <Select
        sx={{ minWidth: "300px" }}
        value={module.module_code ?? ""}
        onChange={(e) => setModule({ ...module, module_code: e.target.value || null })}
        label="Module"
        displayEmpty
      >
      <MenuItem value="" disabled>
        Modules
      </MenuItem>
        {modules &&
          modules.map((m) => {
            return (
              <MenuItem key={m.code} value={m.code}>
                {`${m.code} ${m.name}`}
              </MenuItem>
            );
          })}
      </Select>
      <br />
      <br />
      <Button onClick={request}>Register</Button>
      <br />
      <br />
      {error && <Alert color="error">{error}</Alert>}
    </Paper>
  );
}

export default Register;
