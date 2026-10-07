import React from 'react'
import SignIn from "./components/account/SignIn.tsx";
import {Route, Routes } from 'react-router-dom';
import ChangePassword from "./components/account/ChangePassword.tsx";
import FormPage from "./components/form/FormPage.tsx";
import ProtectedRoute from './components/common/ProtectedRoute.tsx';
import {ROUTES} from "./constants/routes.ts";
import ResetPassword from "./components/account/ResetPassword.tsx";
import ProtectedLayout from "./components/ProtectedLayout.tsx";
import RootLayout from "./components/RootLayout.tsx";
import AppTheme from "./shared-theme/AppTheme.tsx";
import CssBaseline from "@mui/material/CssBaseline";
import {AuthProvider} from "./context/AuthContext.tsx";

const App: React.FC = () => {
    return (
    <AppTheme>
        <CssBaseline enableColorScheme />
        <AuthProvider>
            <Routes>
                <Route element={<RootLayout/>}>
                    <Route path={ROUTES.SIGN_IN} element={<SignIn/>}/>
                    <Route path={ROUTES.RESET_PASSWORD} element={<ResetPassword/>}/>

                    <Route element={<ProtectedLayout />}>
                        <Route path={ROUTES.CHANGE_PASSWORD} element={<ChangePassword/>}/>
                        <Route path={ROUTES.FORM}
                            element={
                            <ProtectedRoute>
                                <FormPage />
                            </ProtectedRoute>
                            }/>
                    </Route>
                </Route>
            </Routes>
        </AuthProvider>
    </AppTheme>
    );
}
export default App
