import { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import axios from 'axios';

const API_URL = 'http://localhost:8080';

const ReaderForm = () => {
    const { id } = useParams();
    const navigate = useNavigate();

    const [formData, setFormData] = useState({
        login: '',
        email: '',
        age: '',
        active: false,
        currentLoansCount: 0,
        type: 'reader'
    });

    const [etag, setEtag] = useState('');

    useEffect(() => {
        if (id) {
            axios.get(`${API_URL}/readers/${id}`)
                .then(res => {
                    setFormData(res.data);
                    setEtag(res.headers['if-match'] || res.headers['If-Match']); // Pobranie ETaga
                })
                .catch(err => alert("Błąd pobierania danych: " + (err.response?.data?.message || err.message)));
        }
    }, [id]);

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

        const msg = id ? "Czy na pewno chcesz zapisać zmiany?" : "Czy na pewno chcesz utworzyć użytkownika?";
        if (!window.confirm(msg)) return;

        const payload = {
            login: formData.login,
            email: formData.email,
            age: parseInt(formData.age),
            active: formData.active,
            currentLoansCount: parseInt(formData.currentLoansCount),
            type: 'reader'
        };

        if (id) {
            payload.id = id;
        }

        const request = id
            ? axios.post(`${API_URL}/readers/${id}`, payload, {
                headers: { 'If-Match': etag }
              })
            : axios.post(`${API_URL}/readers`, payload);

        request
            .then(() => {
                alert("Operacja zakończona sukcesem.");
                navigate('/readers');
            })
            .catch(err => {
                alert("Nie udało się zapisać danych:\n" + getErrorMessage(err));
            });
    };

    return (
        <div>
            <h3>{id ? 'Edycja Czytelnika' : 'Nowy Czytelnik'}</h3>

            <form onSubmit={handleSubmit}>
                <label>
                    Login:
                    <input
                        name="login"
                        value={formData.login}
                        onChange={handleChange}
                        required
                        minLength="3"
                        maxLength="20"
                        title={id
                            ? "Aby zmienić login, skontaktuj się z administracją biblioteki."
                            : "Login musi mieć od 3 do 20 znaków."
                        }
                        disabled={!!id}
                        className={id ? 'input-disabled' : ''}
                    />
                </label>
                <label>
                    Email:
                    <input
                        type="email"
                        name="email"
                        value={formData.email}
                        onChange={handleChange}
                        required
                        maxLength="100"
                        pattern="^.+@.+$"
                        title="Podaj poprawny adres email."
                    />
                </label>
                <label>
                    Wiek:
                    <input
                        type="number"
                        name="age"
                        value={formData.age}
                        onChange={handleChange}
                        required
                        min="1"
                        title="Wiek musi być liczbą dodatnią."
                    />
                </label>

                <div>
                    <button type="submit">Zapisz</button>
                    <button type="button" onClick={() => navigate('/readers')}>Anuluj</button>
                </div>
            </form>
        </div>
    );
};

export default ReaderForm;