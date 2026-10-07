import * as React from 'react';
import {useNavigate, Link as RouterLink} from 'react-router-dom';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import FormLabel from '@mui/material/FormLabel';
import FormControl from '@mui/material/FormControl';
import Link from '@mui/material/Link';
import TextField from '@mui/material/TextField';
import Typography from '@mui/material/Typography';

import api from '../../lib/axios';
import {ROUTES} from '../../constants/routes';

import ForgotPassword from './ForgotPassword.tsx';
import CardWrapper from "../CardWrapper.tsx";
import OutlinedInput from "@mui/material/OutlinedInput";
import {FormHelperText} from "@mui/material";
import InputAdornment from "@mui/material/InputAdornment";
import IconButton from "@mui/material/IconButton";
import VisibilityOff from "@mui/icons-material/VisibilityOff";
import Visibility from "@mui/icons-material/Visibility";
import Stack from "@mui/material/Stack";
import SidePageWrapper from "../SidePageWrapper.tsx";
import {useAuth} from "../../context/AuthContext.tsx";
import {useEffect} from "react";

export default function SignInCard() {
    const navigate = useNavigate();
    const [usernameError, setUsernameError] = React.useState(false);
    const [usernameErrorMessage, setUsernameErrorMessage] = React.useState('');
    const [passwordError, setPasswordError] = React.useState(false);
    const [passwordErrorMessage, setPasswordErrorMessage] = React.useState('');
    const [open, setOpen] = React.useState(false);

    useEffect(() => {
        getTAEmail();
    }, [])

    const handleClickOpen = () => {
        setOpen(true);
    };

    const handleClose = () => {
        setOpen(false);
    };

    const validateInputs = () => {
        const username = document.getElementById('username') as HTMLInputElement;
        const password = document.getElementById('password') as HTMLInputElement;

        let isValid = true;

        if (!/^\d{9,}$/.test(username.value)) {
            setUsernameError(true);
            setUsernameErrorMessage('學號格式錯誤');
            isValid = false;
        } else {
            setUsernameError(false);
            setUsernameErrorMessage('');
        }

        if (!password.value || password.value.length < 6) {
            setPasswordError(true);
            setPasswordErrorMessage('密碼至少 6 碼');
            isValid = false;
        } else {
            setPasswordError(false);
            setPasswordErrorMessage('');
        }

        return isValid;
    };

    const [showPassword, setShowPassword] = React.useState(false);

    const handleClickShowPassword = () => setShowPassword((show) => !show);
    const handleMouseDownPassword = (event: React.MouseEvent<HTMLButtonElement>) => {
        event.preventDefault();
    };
    const handleMouseUpPassword = (event: React.MouseEvent<HTMLButtonElement>) => {
        event.preventDefault();
    };

    const { setToken, setName } = useAuth();

    const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();

        if (!validateInputs()) return;

        const data = new FormData(event.currentTarget);
        const username = data.get('username') as string;
        const password = data.get('password') as string;

        try {
            const res = await api.post('/api/auth/login', {username, password});
            // localStorage.setItem('token', res.data.token);
            setToken(res.data.token);
            const me = await api.get('/api/auth/me');
            // localStorage.setItem('name', me.data.name);
            setName(me.data.name);

            navigate(ROUTES.FORM);
        } catch (err) {
            // 錯誤處理可補上
        }
    };

    const [email, setTAEmail] = React.useState('');

    const getTAEmail = async () => {
        try {
            const res = await api.get('/api/info/email/ta');
            setTAEmail(res.data.email);
        } catch (err) {
            console.error(err)
        }
    };

    return (
        <SidePageWrapper variant="outlined">
            <Stack direction="column">
                <Typography
                    component="h1"
                    variant="h4"
                    sx={{width: '100%', fontSize: 'clamp(2rem, 10vw, 2.15rem)'}}
                >
                    Welcome!
                </Typography>
                <Typography component="p" variant="body2" sx={{mb: '0.5rem'}}>
                    Log in to review this weeks's presenters.
                </Typography>
            </Stack>
            <Box
                component="form"
                onSubmit={handleSubmit}
                noValidate
                sx={{display: 'flex', flexDirection: 'column', width: '100%', gap: 5}}
            >
                <Stack direction="column" spacing={2.5}>
                    <FormControl error={usernameError}>
                        <FormLabel htmlFor="username">學號</FormLabel>
                        <OutlinedInput
                            id="username"
                            name="username"
                            placeholder="請輸入學號"
                            autoComplete="username"
                            autoFocus
                            required
                            fullWidth
                            color={usernameError ? 'error' : 'primary'}
                        />
                        {usernameError && (
                            <FormHelperText>{usernameErrorMessage}</FormHelperText>
                        )}
                    </FormControl>
                    <FormControl error={passwordError}>
                        <Box sx={{display: 'flex', justifyContent: 'space-between'}}>
                            <FormLabel htmlFor="password">密碼</FormLabel>
                            <Link
                                component="button"
                                type="button"
                                onClick={handleClickOpen}
                                variant="body2"
                                sx={{alignSelf: 'baseline'}}
                            >
                                忘記密碼？
                            </Link>
                        </Box>
                        <OutlinedInput
                            name="password"
                            placeholder="••••••"
                            type={showPassword ? 'text' : 'password'}
                            id="password"
                            autoComplete="current-password"
                            required
                            fullWidth
                            color={passwordError ? 'error' : 'primary'}
                            endAdornment={
                                <InputAdornment position="end">
                                    <IconButton
                                        aria-label={showPassword ? 'hide the password' : 'display the password'}
                                        onClick={handleClickShowPassword}
                                        onMouseDown={handleMouseDownPassword}
                                        onMouseUp={handleMouseUpPassword}
                                        edge="end"
                                    >
                                        {showPassword ? <VisibilityOff/> : <Visibility/>}
                                    </IconButton>
                                </InputAdornment>
                            }
                        />
                        {passwordError && (
                            <FormHelperText>{passwordErrorMessage}</FormHelperText>
                        )}
                    </FormControl>
                </Stack>
                <ForgotPassword open={open} handleClose={handleClose} email={email}/>
                <Button type="submit" fullWidth variant="contained" sx={{mt: '0.5rem'}}>
                    Sign in
                </Button>
            </Box>
        </SidePageWrapper>
    );
}
