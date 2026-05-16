import axios from 'axios';

const api = axios.create({
    headers: {
        'Content-Type': 'application/json',
    },
});

api.interceptors.request.use(
    (config) => {
        const url = config.url || '';
        if (url.startsWith('/auth') || url.startsWith('/readers') || url.startsWith('/admins') || url.startsWith('/librarians')) {
            config.baseURL = 'http://localhost:8081';
        } else {
            config.baseURL = 'http://localhost:8080';
        }

        const token = sessionStorage.getItem('token');
        if (token) {
            config.headers['Authorization'] = `Bearer ${token}`;
        }
        return config;
    },
    (error) => Promise.reject(error)
);

api.interceptors.response.use(
    (response) => response,
    async (error) => {
        const originalRequest = error.config;

        if (error.response && (error.response.status === 401 || error.response.status === 403) && !originalRequest._retry) {
            originalRequest._retry = true;
            const refreshToken = sessionStorage.getItem('refreshToken');

            if (refreshToken) {
                try {
                    const response = await axios.post('http://localhost:8081/auth/refresh', {
                        refreshToken: refreshToken
                    });

                    const {accessToken} = response.data;

                    sessionStorage.setItem('token', accessToken);
                    api.defaults.headers.common['Authorization'] = `Bearer ${accessToken}`;
                    originalRequest.headers['Authorization'] = `Bearer ${accessToken}`;

                    return api(originalRequest);
                    // eslint-disable-next-line no-unused-vars
                } catch (refreshError) {
                    sessionStorage.removeItem('token');
                    sessionStorage.removeItem('refreshToken');
                    window.location.href = '/login';
                }
            } else {
                sessionStorage.removeItem('token');
                window.location.href = '/login';
            }
        }
        return Promise.reject(error);
    }
);

export default api;