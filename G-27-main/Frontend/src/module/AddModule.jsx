import React from "react";
import axios from "axios";
import {
  Paper,
  TextField,
  Switch,
  FormControlLabel,
  Button,
  Typography,
  Alert,
} from "@mui/material";
import { API_ENDPOINT } from "../config";

function AddModule(props) {
  const [module, setModule] = React.useState({mnc:false});
  const [error, setError] = React.useState();

  function request() {
    setError(null);
    console.log(module);
    axios
      .post(`${API_ENDPOINT}/modules`, module)
      .then(() => {
        setModule({
          code: null,
          name: null,
          mnc: false
        });
        props.update();
      })
      .catch((response) => {
        console.log(response)
        let errorMessage = response.config.data
        const errorIdentifiers = {
          "code": "Please fill in the code field.",
          "name": "Please fill in the name field."
        };
        let finalMessage = "Unknown Error"
        for (const identifier in errorIdentifiers) {
          if (!errorMessage.includes(identifier) || errorMessage.includes(`"${identifier}":null`)) {
            finalMessage = errorIdentifiers[identifier];
            break;
          }
        }
        setError(finalMessage);
      });
  }

  return (
    <Paper sx={{ padding: "30px" }}>
      <Typography variant="h5">Add/Update Module</Typography>
      <br />
      <TextField
        label="Module Code"
        value={module.code || ""}
        onChange={(e) => {
          setModule({ ...module, code: e.target.value.toUpperCase() || null });
        }}
      />
      <TextField
        label="Module Name"
        value={module.name || ""}
        onChange={(e) => {
          setModule({ ...module, name: e.target.value || null });
        }}
      />
      <br />
      <FormControlLabel
        control={
          <Switch
            checked={module.mnc ?? false}
            id="is_mnc"
            onChange={(e) => {
              setModule({ ...module, mnc: e.target.checked });
            }}
          />
        }
        label="MNC?"
      />
      <br />
      <Button onClick={request}>Add/Update</Button>
      <br />
      <br />
      {error && <Alert color="error">{error}</Alert>}
    </Paper>
  );
}

export default AddModule;
