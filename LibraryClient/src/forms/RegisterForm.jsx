import {useState} from 'react';
import {useNavigate} from 'react-router-dom';
import api from '../api';

const RegisterForm = () => {
    const [formData, setFormData] = useState({login: '', password: '', email: '', age: ''});
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await api.post('/auth/register', formData);
            alert("Zarejestrowano pomyślnie! Twoje konto oczekuje na aktywację przez bibliotekarza.");
            navigate('/login');
        } catch (err) {
            alert("Błąd rejestracji: " + (err.response?.data?.message || "Spróbuj ponownie"));
        }
    };

    return (
        <div>
            <h3>Rejestracja Czytelnika</h3>
            <form onSubmit={handleSubmit}>
                <input type="text" placeholder="Login" onChange={e => setFormData({...formData, login: e.target.value})}
                       required/>
                <input type="password" placeholder="Hasło"
                       onChange={e => setFormData({...formData, password: e.target.value})} required/>
                <input type="email" placeholder="Email"
                       onChange={e => setFormData({...formData, email: e.target.value})} required/>
                <input type="number" placeholder="Wiek" onChange={e => setFormData({...formData, age: e.target.value})}
                       required/>
                <div>
                    <button type="submit">Zarejestruj się</button>
                    <button type="button" onClick={() => navigate('/login')}>Powrót</button>
                </div>
            </form>
        </div>
    );
};

export default RegisterForm;