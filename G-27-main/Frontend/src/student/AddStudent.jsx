import React from "react";
import axios from "axios";
import { Paper, TextField, Button, Typography, Alert } from "@mui/material";
import { API_ENDPOINT } from "../config";

function AddStudent(props) {
  const [student, setStudent] = React.useState({});
  const [error, setError] = React.useState();

  function request() {
    setError(null);
    axios
      .post(`${API_ENDPOINT}/students`, student)
      .then(() => {
        setStudent({
          id: null,
          username: null,
          email: null,
          firstName: null,
          lastName: null,
        });
        props.update();
      })
      .catch((response) => {
        console.log(response);
        const constructorData = response.config.data
        let constructorError = false
        const studentConstructorError = {
          "id": "Please fill in the student id.",
          "username": "Please fill in the username.",
          "email": "Please fill in the email.",
          "firstName": "Please fill in the first name.",
          "lastName": "Please fill in the last name."
        };
        for (const error in studentConstructorError) {
          if (!constructorData.includes(error) || constructorData.includes(`"${error}":null`)) {
            setError(studentConstructorError[error]);
            constructorError = true;
            break;
          }
        }
        
        if(!constructorError) { //If it isnt a constructor error, it is probably a unique violation error
          let errorMessage = null;
          try {
            errorMessage = JSON.parse(response.request.response);
          } catch (error) {
            setError("Failed to parse the error response.");
            return;
          }
          errorMessage = errorMessage?.cause?.cause?.message || 'Unknown error';
          const errorIdentifiers = {
            "INDEX_BA": "This username has been used, please use another.",
            "INDEX_B": "This email has been used, please use another."
          };

          for (const identifier in errorIdentifiers) {
            if (errorMessage.includes(identifier)) {
              errorMessage = errorIdentifiers[identifier];
              break;
            }
          }
          setError(errorMessage);
        }
      });
  }

  return (
    <Paper sx={{ padding: "30px" }}>
      <Typography variant="h5">Add/Update Student</Typography>
      <br />
      <TextField
        label="Student ID"
        value={student.id || ""}
        onChange={(e) => {
          setStudent({ ...student, id: Number(e.target.value) || null });
        }}
      />
      <TextField
        label="Username"
        value={student.username || ""}
        onChange={(e) => {
          setStudent({ ...student, username: e.target.value || null });
        }}
      />
      <TextField
        label="email"
        value={student.email || ""}
        onChange={(e) => {
          setStudent({ ...student, email: e.target.value || null });
        }}
      />
      <br />
      <br />
      <TextField
        label="First Name"
        value={student.firstName || ""}
        onChange={(e) => {
          setStudent({ ...student, firstName: e.target.value || null });
        }}
      />
      <TextField
        label="Last Name"
        value={student.lastName || ""}
        onChange={(e) => {
          setStudent({ ...student, lastName: e.target.value || null });
        }}
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

export default AddStudent;