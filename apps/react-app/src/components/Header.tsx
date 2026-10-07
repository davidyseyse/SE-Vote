import { AppBar, Toolbar, Typography } from '@mui/material';
import { useTheme } from '@mui/material/styles';
import React, {useEffect, useState} from "react";
import Box from "@mui/material/Box";
import {useAuth} from "../context/AuthContext.tsx";

export default function Header() {
    const theme = useTheme();
    const { username } = useAuth();

    return (
        <AppBar
            position="fixed"
            elevation={0}
            sx={{
                color: theme.palette.mode === 'dark' ? 'white' : 'hsl(220, 30%, 6%)',
                backgroundColor: theme.palette.mode === 'dark'
                    ? 'black'
                    : 'hsla(0, 0%, 100%, 0.9)',
                '&.MuiAppBar-root': {
                    [theme.breakpoints.up('sm')]: {
                        backgroundColor: 'transparent',
                    },
                },
            }}
        >
            <Toolbar sx={{ justifyContent: 'space-between' }}>
                <Box display="flex" alignItems="center">
                    <Typography variant="h6" sx={{ flexGrow: 1 }}>
                        ISSLAB
                    </Typography>
                    {username && (
                        <Typography variant="body1" sx={{ ml: 2 }}>
                            Hi, {username}
                        </Typography>
                    )}
                </Box>
            </Toolbar>
        </AppBar>
    );
}