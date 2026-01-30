import axios from 'axios';

const api = axios.create({
    baseURL: 'http://localhost:8080',
    headers: {
        'Content-Type': 'application/json',
    },
});

api.interceptors.response.use(
    (response) => {
        return response;
    },
    async (error) => {
        const originalRequest = error.config;

        if (error.response && (error.response.status === 401 || error.response.status === 403) && !originalRequest._retry) {
            originalRequest._retry = true;

            const refreshToken = sessionStorage.getItem('refreshToken');

            if (refreshToken) {
                try {
                    const response = await axios.post('http://localhost:8080/auth/refresh', {
                        refreshToken: refreshToken
                    });

                    const { token } = response.data;

                    sessionStorage.setItem('token', token);

                    api.defaults.headers.common['Authorization'] = `Bearer ${token}`;
                    originalRequest.headers['Authorization'] = `Bearer ${token}`;

                    return api(originalRequest);
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