import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

const API_URL = 'http://localhost:8080';

const LibrarianForm = () => {
    const navigate = useNavigate();
    const [formData, setFormData] = useState({
        login: '',
        email: '',
        age: '',
        active: true,
        type: 'librarian'
    });

    const handleChange = (e) => setFormData({ ...formData, [e.target.name]: e.target.value });

    const getErrorMessage = (err) => {
        if (err.response && err.response.data) {
            const data = err.response.data;
            if (data.errors && Array.isArray(data.errors)) {
                return data.errors.map(e => `${e.field}: ${e.defaultMessage}`).join('\n');
            }
            return data.message || data.error || "Błąd serwera";
        }
        return err.message;
    };

    const handleSubmit = (e) => {
        e.preventDefault();

        if (parseInt(formData.age) <= 0) {
            alert("Wiek musi być liczbą dodatnią.");
            return;
        }

        if (!window.confirm("Czy na pewno chcesz dodać tego bibliotekarza?")) return;

        const payload = {
            login: formData.login,
            email: formData.email,
            age: parseInt(formData.age),
            active: true,
            type: 'librarian'
        };

        axios.post(`${API_URL}/librarians`, payload)
            .then(() => {
                alert("Bibliotekarz dodany!");
                navigate('/librarians');
            })
            .catch(err => {
                alert("Nie udało się dodać bibliotekarza:\n" + getErrorMessage(err));
            });
    };

    return (
        <div>
            <h3>Nowy Bibliotekarz</h3>
            <form onSubmit={handleSubmit}>
                <label>
                    Login:
                    <input name="login" value={formData.login} onChange={handleChange} required />
                </label>
                <label>
                    Email:
                    <input type="email" name="email" value={formData.email} onChange={handleChange} required />
                </label>
                <label>
                    Wiek:
                    <input type="number" name="age" value={formData.age} onChange={handleChange} required />
                </label>

                <div>
                    <button type="submit">Dodaj</button>
                    <button type="button" onClick={() => navigate('/librarians')}>Anuluj</button>
                </div>
            </form>
        </div>
    );
};

export default LibrarianForm;