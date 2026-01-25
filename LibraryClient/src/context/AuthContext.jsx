import { createContext, useState, useContext, useEffect } from 'react';
import api from '../api';
import { jwtDecode } from "jwt-decode";

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(null);
    const [token, setToken] = useState(localStorage.getItem('token'));
    const [refreshToken, setRefreshToken] = useState(localStorage.getItem('refreshToken'));

    const PERMISSIONS = {
        'ROLE_ADMIN': ['manage_admins', 'manage_librarians', 'manage_readers', 'manage_loans', 'view_books'],
        'ROLE_LIBRARIAN': ['manage_readers', 'manage_loans', 'view_books'],
        'ROLE_READER': ['rent_books', 'view_my_loans', 'view_books']
    };

    const can = (permission) => {
        if (!user || !user.role) return false;
        return PERMISSIONS[user.role].includes(permission);
    }

    useEffect(() => {
        if (token) {
            try {
                const decoded = jwtDecode(token);
                const currentTime = Date.now() / 1000;
                if (decoded.exp < currentTime) {
                }

                setUser({ login: decoded.sub, role: decoded.role });
                api.defaults.headers.common['Authorization'] = `Bearer ${token}`;
            } catch (e) {
                logout();
            }
        } else {
            setUser(null);
            delete api.defaults.headers.common['Authorization'];
        }
    }, [token]);

    const login = async (loginData) => {
        const res = await api.post('/auth/login', loginData);

        const newToken = res.data.token;
        const newRefreshToken = res.data.refreshToken;

        localStorage.setItem('token', newToken);
        localStorage.setItem('refreshToken', newRefreshToken);

        setToken(newToken);
        setRefreshToken(newRefreshToken);
    };

    const logout = () => {
        localStorage.removeItem('token');
        localStorage.removeItem('refreshToken');
        setToken(null);
        setRefreshToken(null);
        setUser(null);
        delete api.defaults.headers.common['Authorization'];
    };

    return (
        <AuthContext.Provider value={{ user, login, logout, can }}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => useContext(AuthContext);