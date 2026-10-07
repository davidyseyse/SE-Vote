import React, {useEffect, useState} from "react";
import AppTheme from "../../shared-theme/AppTheme.tsx";
import CssBaseline from "@mui/material/CssBaseline";
import CardWrapper from "../CardWrapper.tsx";
import Typography from "@mui/material/Typography";
import FormControl from "@mui/material/FormControl";
import FormLabel from "@mui/material/FormLabel";
import Box from "@mui/material/Box";
import Stack from "@mui/material/Stack";
import {backgroundGradientStyle} from "../../styles/backgroundStyles.tsx";
import Button from "@mui/material/Button";
import SendIcon from '@mui/icons-material/Send';
import api from "../../lib/axios.ts";
import OutlinedInput from '@mui/material/OutlinedInput';
import InputAdornment from "@mui/material/InputAdornment";
import IconButton from "@mui/material/IconButton";
import Visibility from '@mui/icons-material/Visibility';
import VisibilityOff from "@mui/icons-material/VisibilityOff";
import {FormHelperText} from "@mui/material";
import axios from "axios";
import {ROUTES} from "../../constants/routes.ts";
import {useNavigate} from "react-router-dom";
import BackgroundWrapper from "../BackgroundWrpper.tsx";
import ColorModeSelect from "../../shared-theme/ColorModeSelect.tsx";
import {useAuth} from "../../context/AuthContext.tsx";


export default function ChangePassword(props: { disableCustomTheme?: boolean }) {
    const [matchError, setMatchError] = useState<string | null>(null);
    const [showPassword, setShowPassword] = React.useState(false);

    const handleClickShowPassword = () => setShowPassword((show) => !show);
    const handleMouseDownPassword = (event: React.MouseEvent<HTMLButtonElement>) => {
        event.preventDefault();
    };
    const handleMouseUpPassword = (event: React.MouseEvent<HTMLButtonElement>) => {
        event.preventDefault();
    };

    const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();

        const oldPassword = (document.getElementById('password') as HTMLInputElement).value;
        const newPassword = (document.getElementById('newPassword') as HTMLInputElement).value;
        const newPasswordCheck = (document.getElementById('newPasswordCheck') as HTMLInputElement).value;

        if (newPassword !== newPasswordCheck) {
            setMatchError('兩次輸入密碼不相同');
            return;
        }

        if (!newPassword || newPassword.length < 6) {
            alert('密碼無效，請輸入至少 6 碼');
            return;
        }

        try {
            const res = await api.post(
                '/api/password/change',
                { oldPassword, newPassword },
            );
            alert('密碼修改成功');
        } catch (err) {
            console.error('密碼修改失敗', err);
            alert('密碼修改失敗');
        }
    };

    const navigate = useNavigate();
    const STEP_STORAGE_KEY = 'presenter_step';
    const TOKEN_STORAGE_KEY = 'token';

    const { logout } = useAuth();

    return (
        <>
        <BackgroundWrapper>
        <Stack
                sx={{
                    position: 'fixed',
                    top: 16,
                    right: 16,
                    zIndex: 2000, // 確保蓋在其他元素上
                }}
                spacing={1}
                direction="row"
            >
                <Button
                    variant="text"
                    color="primary"
                    onClick={() => navigate(ROUTES.FORM)}
                >
                    表單
                </Button>
                <Button
                    variant="text"
                    color="primary"
                    onClick={() => {
                        logout();
                        navigate(ROUTES.SIGN_IN);
                        localStorage.removeItem(TOKEN_STORAGE_KEY);
                        sessionStorage.removeItem(STEP_STORAGE_KEY);
                    }}
                >
                    登出
                </Button>
                <ColorModeSelect/>
            </Stack>
            <Stack
                component="main"
                sx={(theme) => ({
                    minHeight: '100vh',
                    justifyContent: 'center',
                    alignItems: 'center',
                    p: 2,
                    ...backgroundGradientStyle(theme),
                })}
            >

                <Stack
                    direction={{ xs: 'column-reverse', md: 'row' }}
                    spacing={{ xs: 4, sm: 8}}
                    alignItems="center"
                    justifyContent="center"
                >
                    <CardWrapper>
                        <Typography
                            component="h1"
                            variant="h4"
                            sx={{ width: '100%', fontSize: 'clamp(2rem, 10vw, 2.15rem)' }}
                        >
                            Change password
                        </Typography>
                        <Box
                            component="form"
                            onSubmit={handleSubmit}
                            noValidate
                            sx={{ display: 'flex', flexDirection: 'column', width: '100%', gap: 2 }}
                        >
                            <Stack spacing={2}>
                                {/* 舊密碼欄位 */}
                                <FormControl required fullWidth variant="outlined">
                                    <FormLabel htmlFor="password">Current password</FormLabel>
                                    <OutlinedInput
                                        id="password"
                                        type={showPassword ? 'text' : 'password'}
                                        endAdornment={
                                            <InputAdornment position="end">
                                                <IconButton
                                                    aria-label={showPassword ? 'hide the password' : 'display the password'}
                                                    onClick={handleClickShowPassword}
                                                    onMouseDown={handleMouseDownPassword}
                                                    onMouseUp={handleMouseUpPassword}
                                                    edge="end"
                                                >
                                                    {showPassword ? <VisibilityOff /> : <Visibility />}
                                                </IconButton>
                                            </InputAdornment>
                                        }
                                        label="password"
                                    />
                                </FormControl>

                                {/* 新密碼欄位 */}
                                <FormControl required fullWidth variant="outlined">
                                    <FormLabel htmlFor="newPassword">New password</FormLabel>
                                    <OutlinedInput
                                        id="newPassword"
                                        type={showPassword ? 'text' : 'password'}
                                        endAdornment={
                                            <InputAdornment position="end">
                                                <IconButton
                                                    aria-label={showPassword ? 'hide the password' : 'display the password'}
                                                    onClick={handleClickShowPassword}
                                                    onMouseDown={handleMouseDownPassword}
                                                    onMouseUp={handleMouseUpPassword}
                                                    edge="end"
                                                >
                                                    {showPassword ? <VisibilityOff /> : <Visibility />}
                                                </IconButton>
                                            </InputAdornment>
                                        }
                                        label="New password"
                                    />
                                </FormControl>

                                {/* 確認密碼欄位 */}
                                <FormControl required fullWidth variant="outlined" error={!!matchError}>
                                    <FormLabel htmlFor="newPasswordCheck">Enter password again</FormLabel>
                                    <OutlinedInput
                                        id="newPasswordCheck"
                                        type={showPassword ? 'text' : 'password'}
                                        endAdornment={
                                            <InputAdornment position="end">
                                                <IconButton
                                                    aria-label={showPassword ? 'hide the password' : 'display the password'}
                                                    onClick={handleClickShowPassword}
                                                    onMouseDown={handleMouseDownPassword}
                                                    onMouseUp={handleMouseUpPassword}
                                                    edge="end"
                                                >
                                                    {showPassword ? <VisibilityOff /> : <Visibility />}
                                                </IconButton>
                                            </InputAdornment>
                                        }
                                        label="Enter password again"
                                    />
                                    {!!matchError && <FormHelperText>{matchError}</FormHelperText>}
                                </FormControl>
                            </Stack>

                            <Button type="submit" fullWidth variant="contained" endIcon={<SendIcon />}>
                                Submit
                            </Button>
                        </Box>
                    </CardWrapper>
                </Stack>
            </Stack>
        </BackgroundWrapper>
        </>
    )
}
