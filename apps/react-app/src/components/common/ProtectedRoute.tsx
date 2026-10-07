import React, {useEffect, useState} from 'react';
import {Navigate} from 'react-router-dom';
import api from '../../lib/axios';

import AppTheme from '../../shared-theme/AppTheme';
import CssBaseline from '@mui/material/CssBaseline';
import Box from '@mui/material/Box';
import CircularProgress from '@mui/material/CircularProgress';
import Typography from '@mui/material/Typography';
import Grid from '@mui/material/Grid';

const ProtectedRoute = ({children}: { children: JSX.Element }) => {
    const [authChecked, setAuthChecked] = useState(false);
    const [isAuthorized, setIsAuthorized] = useState(false);

    useEffect(() => {
        const checkAuth = async () => {
            try {
                await api.get('/api/auth/me');
                setIsAuthorized(true);
            } catch (e) {
                setIsAuthorized(false);
            } finally {
                setAuthChecked(true);
            }
        };
        checkAuth();
    }, []);

    if (!authChecked) {
        return (
            // <AppTheme>
            //   <CssBaseline enableColorScheme />
            <>
                <Grid
                    container
                    justifyContent="center"
                    alignItems="center"
                    sx={{height: '100vh', backgroundColor: 'background.default'}}
                >
                    <Box textAlign="center">
                        <CircularProgress/>
                        <Typography variant="h6" sx={{mt: 2}}>
                            驗證身份中，請稍候...
                        </Typography>
                    </Box>
                </Grid>
            </>
            // </AppTheme>
        );
    }

    return isAuthorized ? children : <Navigate to="/" replace/>;
};

export default ProtectedRoute;
