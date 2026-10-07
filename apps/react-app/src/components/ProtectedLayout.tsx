// components/ProtectedLayout.tsx
import { Outlet, Navigate } from 'react-router-dom';
import {ROUTES} from "../constants/routes.ts";

export default function ProtectedLayout() {
    const token = localStorage.getItem('token');
    const loginPage = ROUTES.SIGN_IN;
    return token ? <Outlet /> : <Navigate to={loginPage} />;
}
