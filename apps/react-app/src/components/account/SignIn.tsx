// import * as React from 'react';
import Stack from '@mui/material/Stack';
import ColorModeSelect from '../../shared-theme/ColorModeSelect.tsx';
import SignInCard from './SignInCard.tsx';
import SignInPageContent from './SignInPageContent.tsx';
import {Typography} from "@mui/material";
import BackgroundWrapper from "../BackgroundWrpper.tsx";

export default function SignIn() {
    return (
            <BackgroundWrapper>
                <ColorModeSelect sx={{position: 'fixed', top: '1rem', right: '1rem', zIndex: 4000}}/>
                <Stack
                    component="main"
                    sx={(theme) => ({
                        minHeight: '100vh',
                        justifyContent: 'center',
                        alignItems: 'flex-start',
                        [theme.breakpoints.down('md')]: {
                            paddingTop: '80px',
                            mx: '2rem',
                        },
                    })}

                >
                    <Stack
                        direction={{xs: 'column-reverse', md: 'row'}}
                        spacing={{xs: 4, sm: 8}}
                        alignItems="center"
                        justifyContent="center"
                        sx={(theme) => ({
                            [theme.breakpoints.down('md')]: {
                                marginBottom: '2rem',
                            },
                        })}
                    >
                        <SignInCard/>

                        <Stack spacing={{xs: 4, sm: 10}} alignItems="center">
                            <Stack alignItems="start">
                                <Typography variant="h2" textAlign="start">
                                    NCU 2025
                                </Typography>
                                <Typography variant="h1" textAlign="start">
                                    Software Engineer
                                </Typography>
                            </Stack>
                            <SignInPageContent/>
                        </Stack>
                    </Stack>
                </Stack>
            </BackgroundWrapper>
    );

}