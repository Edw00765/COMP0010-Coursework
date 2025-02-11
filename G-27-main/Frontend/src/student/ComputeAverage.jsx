import React from "react";
import axios from "axios";
import { Typography, Grid, Button } from "@mui/material";
import App from "../App";
import { API_ENDPOINT } from "../config";

function ComputeAverage({ studentId }) {
    const [average, setAverage] = React.useState(null);

    const fetchAverage = async () => {
        try {
            const { data } = await axios.get(`${API_ENDPOINT}/students/studentDetail/computeAverage/${studentId}`);
            setAverage(data.average);
        } catch (error) {
            console.error("Error fetching the average:", error);
        }
    };

    return (
        <App>
            <Grid>
                <Button onClick={fetchAverage}>Calculate Average</Button>
                {average !== null && 
                  <>
                    <br />
                    <br />
                    <Typography variant="h5" gutterBottom>
                        Average: {average}
                    </Typography>
                  </>
                }
            </Grid>
        </App>
    );
}

export default ComputeAverage;