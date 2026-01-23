import { createContext, useState, useContext, useEffect } from 'react';
import api from '../api';
import { jwtDecode } from "jwt-decode";

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(null);
    const [token, setToken] = useState(localStorage.getItem('token'));

    useEffect(() => {
        if (token) {
            try {
                const decoded = jwtDecode(token);
                setUser({ login: decoded.sub, role: decoded.role });
                api.defaults.headers.common['Authorization'] = `Bearer ${token}`;
            } catch (e) {
                logout();
            }
        }
    }, [token]);

    const login = async (loginData) => {
        const res = await api.post('/auth/login', loginData);
        const newToken = res.data.token;
        localStorage.setItem('token', newToken);
        setToken(newToken);
    };

    const logout = () => {
        localStorage.removeItem('token');
        setToken(null);
        setUser(null);
        delete api.defaults.headers.common['Authorization'];
    };

    return (
        <AuthContext.Provider value={{ user, login, logout }}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => useContext(AuthContext);