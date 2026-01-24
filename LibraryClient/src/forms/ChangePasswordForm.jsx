import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../api';

const ChangePasswordForm = () => {
    const navigate = useNavigate();
    const [formData, setFormData] = useState({
        oldPassword: '',
        newPassword: '',
        confirmNewPassword: ''
    });

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = (e) => {
        e.preventDefault();

        if (formData.newPassword !== formData.confirmNewPassword) {
            alert("Nowe hasła muszą być identyczne.");
            return;
        }

        if (formData.newPassword.length < 5) {
            alert("Hasło musi mieć co najmniej 5 znaków.");
            return;
        }

        const payload = {
            oldPassword: formData.oldPassword,
            newPassword: formData.newPassword
        };

        api.post('/auth/change-password', payload)
            .then(() => {
                alert("Hasło zostało zmienione pomyślnie.");
                navigate('/');
            })
            .catch(err => {
                const msg = err.response?.data?.message || err.response?.data?.error || "Wystąpił błąd podczas zmiany hasła.";
                alert("Błąd: " + msg);
            });
    };

    return (
        <div>
            <h3>Zmiana hasła</h3>
            <form onSubmit={handleSubmit}>
                <label>
                    Stare hasło:
                    <input
                        type="password"
                        name="oldPassword"
                        value={formData.oldPassword}
                        onChange={handleChange}
                        required
                    />
                </label>
                <label>
                    Nowe hasło:
                    <input
                        type="password"
                        name="newPassword"
                        value={formData.newPassword}
                        onChange={handleChange}
                        required
                        minLength="5"
                    />
                </label>
                <label>
                    Powtórz nowe hasło:
                    <input
                        type="password"
                        name="confirmNewPassword"
                        value={formData.confirmNewPassword}
                        onChange={handleChange}
                        required
                        minLength="5"
                    />
                </label>

                <div>
                    <button type="submit">Zmień hasło</button>
                    <button type="button" onClick={() => navigate('/')}>Anuluj</button>
                </div>
            </form>
        </div>
    );
};

export default ChangePasswordForm;