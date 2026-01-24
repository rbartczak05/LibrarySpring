import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { Link } from 'react-router-dom';

const LoginForm = () => {
    const [formData, setFormData] = useState({
        login: '',
        password: ''
    });
    const { login } = useAuth();
    const navigate = useNavigate();

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await login(formData);
            navigate('/');
        } catch (err) {
            alert("Nie udało się zalogować. Sprawdź login i hasło.");
        }
    };

    return (
        <div>
            <h3>Logowanie</h3>
            <form onSubmit={handleSubmit}>
                <label>
                    Login:
                    <input
                        type="text"
                        name="login"
                        value={formData.login}
                        onChange={handleChange}
                        required
                    />
                </label>
                <label>
                    Hasło:
                    <input
                        type="password"
                        name="password"
                        value={formData.password}
                        onChange={handleChange}
                        required
                    />
                </label>
                <div>
                    <button type="submit">Zaloguj</button>
                    <button type="button" onClick={() => navigate('/')}>Anuluj</button>
                    <label>Nie masz konta? <Link to="/register">Zarejestruj się</Link></label>
                </div>
            </form>
        </div>
    );
};

export default LoginForm;