import { createContext, useContext, useState, useEffect, ReactNode } from 'react';

interface AuthContextType {
    username: string | null;
    login: (name: string, token: string) => void;
    logout: () => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider = ({ children }: { children: ReactNode }) => {
    const [username, setUsername] = useState<string | null>(null);

    useEffect(() => {
        const token = localStorage.getItem('token');
        const name = localStorage.getItem('name');
        if (token && name) {
            setUsername(name);
        }
    }, []);

    const setToken = (token: string) => {
        localStorage.setItem('token', token);
    };

    const setName = (name: string) => {
        localStorage.setItem('name', name);
        setUsername(name);
    };

    const logout = () => {
        localStorage.removeItem('token');
        localStorage.removeItem('name');
        setUsername(null);
    };

    return (
        <AuthContext.Provider value={{ username, setToken, setName, logout }}>
    {children}
    </AuthContext.Provider>
);
};

export const useAuth = () => {
    const context = useContext(AuthContext);
    if (!context) throw new Error('useAuth must be used within an AuthProvider');
    return context;
};
