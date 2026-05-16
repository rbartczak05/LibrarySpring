import {createContext, useContext, useEffect, useState} from 'react';
import api from '../api';
import {jwtDecode} from "jwt-decode";

const AuthContext = createContext(null);

export const AuthProvider = ({children}) => {
    const [user, setUser] = useState(null);
    const [token, setToken] = useState(sessionStorage.getItem('token'));
    const [refreshToken, setRefreshToken] = useState(sessionStorage.getItem('refreshToken'));

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
                // eslint-disable-next-line react-hooks/set-state-in-effect
                setUser({login: decoded.sub, role: decoded.role});
                api.defaults.headers.common['Authorization'] = `Bearer ${token}`;
            } catch {
                // eslint-disable-next-line react-hooks/immutability
                logout();
            }
        } else {
            setUser(null);
            delete api.defaults.headers.common['Authorization'];
        }
    }, [token]);

    const login = async (loginData) => {
        const res = await api.post('/auth/login', loginData);

        const newToken = res.data.accessToken;
        const newRefreshToken = res.data.refreshToken;

        sessionStorage.setItem('token', newToken);
        sessionStorage.setItem('refreshToken', newRefreshToken);

        setToken(newToken);
        setRefreshToken(newRefreshToken);
    };

    const logout = () => {
        sessionStorage.removeItem('token');
        sessionStorage.removeItem('refreshToken');
        setToken(null);
        setRefreshToken(null);
        setUser(null);
        delete api.defaults.headers.common['Authorization'];
    };

    return (
        <AuthContext.Provider value={{user, login, logout, can}}>
            {children}
        </AuthContext.Provider>
    );
};

// eslint-disable-next-line react-refresh/only-export-components
export const useAuth = () => useContext(AuthContext);