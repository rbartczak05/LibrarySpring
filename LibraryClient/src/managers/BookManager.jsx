import {useEffect, useState} from 'react';
import api from '../api';
import {useAuth} from '../context/AuthContext';

const BookManager = () => {
    const {user} = useAuth();
    const [books, setBooks] = useState([]);
    const [form, setForm] = useState({title: '', author: '', releaseYear: '', quantity: ''});

    const isReader = user?.role === 'ROLE_READER';

    useEffect(() => {
        loadBooks();
    }, []);

    const loadBooks = () => {
        api.get('/book_set')
            .then(res => setBooks(res.data))
            .catch(err => {
                const msg = err.response?.data?.message || err.message;
                alert(`Błąd pobierania danych: ${msg}`);
            });
    };

    const handleChange = (e) => setForm({...form, [e.target.name]: e.target.value});

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

        if (!form.title || !form.author || form.releaseYear === '' || form.quantity === '') {
            alert("Wypełnij wszystkie pola.");
            return;
        }

        if (parseInt(form.quantity) < 0 || parseInt(form.releaseYear) < 0) {
            alert("Ilość i rok muszą być liczbami nieujemnymi.");
            return;
        }

        if (!window.confirm("Czy na pewno chcesz dodać tę książkę?")) return;

        const payload = {
            title: form.title,
            author: form.author,
            releaseYear: parseInt(form.releaseYear),
            quantity: parseInt(form.quantity)
        };

        api.post('/book_set', payload)
            .then(() => {
                alert("Książka dodana!");
                setForm({title: '', author: '', releaseYear: '', quantity: ''});
                loadBooks();
            })
            .catch(err => {
                alert("Nie udało się dodać książki:\n" + getErrorMessage(err));
            });
    };

    const handleDelete = (id) => {
        if (!window.confirm("Czy na pewno chcesz usunąć tę książkę?")) return;

        api.delete(`/book_set/${id}`)
            .then(() => loadBooks())
            .catch(err => {
                alert("Nie udało się usunąć książki:\n" + getErrorMessage(err));
            });
    };

    const handleRent = (bookId) => {
        if (!window.confirm("Czy na pewno chcesz wypożyczyć tę książkę?")) return;

        api.post('/loans/me', null, {
            params: {bookSetId: bookId}
        })
            .then(() => {
                alert("Książka została wypożyczona pomyślnie!");
                loadBooks();
            })
            .catch(err => {
                const msg = err.response?.data?.message || "Nie możesz wypożyczyć książki. (sprawdź swój limit wypożyczeń lub poproś o aktywację konta)";
                alert("Błąd: " + msg);
            });
    };

    return (
        <div>
            <h2>Książki</h2>
            {!isReader && (
                <div>
                    <label>Dodaj nową pozycję</label>
                    <form onSubmit={handleSubmit}>
                        <input
                            type="text"
                            name="title"
                            placeholder="Tytuł"
                            value={form.title}
                            onChange={handleChange}
                            required
                            minLength="1"
                        />
                        <input
                            type="text"
                            name="author"
                            placeholder="Autor"
                            value={form.author}
                            onChange={handleChange}
                            required
                            minLength="1"
                        />
                        <input
                            name="releaseYear"
                            type="number"
                            placeholder="Rok wydania"
                            value={form.releaseYear}
                            onChange={handleChange}
                            required
                            min="0"
                        />
                        <input
                            name="quantity"
                            type="number"
                            placeholder="Ilość sztuk"
                            value={form.quantity}
                            onChange={handleChange}
                            required
                            min="0"
                        />
                        <button type="submit">Dodaj</button>
                    </form>
                </div>
            )}

            <table border="1" cellPadding="10">
                <thead>
                <tr>
                    <th>Tytuł</th>
                    <th>Autor</th>
                    <th>Rok</th>
                    <th>Ilość</th>
                    <th>ID (UUID)</th>
                    <th>Akcje</th>
                </tr>
                </thead>
                <tbody>
                {books.map(b => (
                    <tr key={b.id}>
                        <td>{b.title}</td>
                        <td>{b.author}</td>
                        <td>{b.releaseYear}</td>
                        <td>{b.quantity}</td>
                        <td>{b.id}</td>
                        <td>
                            {isReader ? (
                                <button onClick={() => handleRent(b.id)} disabled={b.quantity <= 0}>
                                    {b.quantity > 0 ? "Wypożycz" : "Niedostępna"}
                                </button>
                            ) : (
                                <button onClick={() => handleDelete(b.id)}>Usuń</button>
                            )}
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
};

export default BookManager;