import React from "react";
import axios from "axios";
import {
  Breadcrumbs,
  Link,
  Typography,
  Alert,
  TextField,
  Select,
  MenuItem,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  TablePagination,
  Button,
} from "@mui/material";
import App from "../App.jsx";
import { API_ENDPOINT } from "../config";
import AddModule from "./AddModule";

function Modules() {
  const [modules, setModules] = React.useState([]);
  const [error, setError] = React.useState();
  const [codeFilter, setCodeFilter] = React.useState("");
  const [nameFilter, setNameFilter] = React.useState("");
  const [mncFilter, setMncFilter] = React.useState("");
  const [page, setPage] = React.useState(0);
  const [rowsPerPage, setRowsPerPage] = React.useState(5);
  
  React.useEffect(() => {
    updateModules();
  }, []);

  function updateModules() {
    axios
      .get(`${API_ENDPOINT}/modules`)
      .then((response) => {
        setModules(response.data._embedded.modules);
      })
      .catch((response) => {
        setError(response.message);
      });
  }
  
  const handleChangePage = (event, newPage) => {
    setPage(newPage);
  };
  
  const handleChangeRowsPerPage = (event) => {
    setRowsPerPage(parseInt(event.target.value, 10));
    setPage(0);
  };

  const filteredModules = modules.filter((m) => {
    return (
      (codeFilter
        ? m.code.toLowerCase().includes(codeFilter.toLowerCase())
        : true) &&
      (nameFilter
        ? m.name.toLowerCase().includes(nameFilter.toLowerCase())
        : true) &&
      (mncFilter ? String(m.mnc).toLowerCase().includes(mncFilter) : true)
    );
  });

  return (
    <App>
      <Breadcrumbs sx={{ marginBottom: "30px" }}>
        <Link underline="hover" color="inherit" href="/">
          Home
        </Link>
        <Typography sx={{ color: "text.primary" }}>Modules</Typography>
      </Breadcrumbs>
      {error && <Alert color="error">{error}</Alert>}
      {!error && modules.length < 1 && (
        <Alert color="warning">No modules</Alert>
      )}
      <div style={{ display: 'flex', alignItems: 'flex-end', gap: '10px', marginBottom: '20px'}}>
        <div style={{ display: 'flex', gap: '10px' }}>
          <TextField
            label="Code Filter"
            placeholder="Code Filter"
            value={codeFilter || ""}
            onChange={(e) => setCodeFilter(e.target.value)}
            sx={{ display: 'flex' }}
          />
          <TextField
            label="Name Filter"
            placeholder="Name Filter"
            value={nameFilter || ""}
            onChange={(e) => setNameFilter(e.target.value)}
            sx={{ display: 'flex' }}
          />
        </div>
        <Select
          value={mncFilter ?? ""}
          onChange={(e) => setMncFilter(e.target.value)}
          sx={{ display: 'flex' }}
          displayEmpty
        >
          <MenuItem value="">MNC Filter / None</MenuItem>
          <MenuItem value="true">Yes</MenuItem>
          <MenuItem value="false">No</MenuItem>
        </Select>
      </div>
      {filteredModules.length > 0 && (
        <TableContainer>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>Module Code</TableCell>
                <TableCell>Module Name</TableCell>
                <TableCell>Is MNC</TableCell>
                <TableCell>Actions</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {filteredModules
                .slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage)
                .map((m) => (
                  <TableRow key={m.code}>
                    <TableCell>{m.code}</TableCell>
                    <TableCell>{m.name}</TableCell>
                    <TableCell>{m.mnc ? "Yes" : "No"}</TableCell>
                    <TableCell>
                      <Button href={`/modules/moduleDetails/${m.code}`}>View Details</Button>
                    </TableCell>
                  </TableRow>
                ))}
            </TableBody>
          </Table>
          <TablePagination
            rowsPerPageOptions={[5, 10, 25]}
            component="div"
            count={filteredModules.length}
            rowsPerPage={rowsPerPage}
            page={page}
            onPageChange={handleChangePage}
            onRowsPerPageChange={handleChangeRowsPerPage}
          />
        </TableContainer>
      )}
      <br />
      <br />
      <AddModule update={updateModules} />
    </App>
  );
}

export default Modules;
