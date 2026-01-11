import { useState, useEffect } from 'react';
import axios from 'axios';

const API_URL = 'http://localhost:8080';

const BookManager = () => {
    const [books, setBooks] = useState([]);
    const [form, setForm] = useState({ title: '', author: '', releaseYear: '', quantity: '' });

    useEffect(() => {
        loadBooks();
    }, []);

    const loadBooks = () => {
        axios.get(`${API_URL}/book_set`)
            .then(res => setBooks(res.data))
            .catch(err => {
                const msg = err.response?.data?.message || err.message;
                alert(`Błąd pobierania danych: ${msg}`);
            });
    };

    const handleChange = (e) => setForm({ ...form, [e.target.name]: e.target.value });

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

        axios.post(`${API_URL}/book_set`, payload)
            .then(() => {
                alert("Książka dodana!");
                setForm({ title: '', author: '', releaseYear: '', quantity: '' });
                loadBooks();
            })
            .catch(err => {
                alert("Nie udało się dodać książki:\n" + getErrorMessage(err));
            });
    };

    const handleDelete = (id) => {
        if (!window.confirm("Czy na pewno chcesz usunąć tę książkę?")) return;

        axios.delete(`${API_URL}/book_set/${id}`)
            .then(() => loadBooks())
            .catch(err => {
                alert("Nie udało się usunąć książki:\n" + getErrorMessage(err));
            });
    }

    return (
        <div>
            <h2>Książki</h2>

            <div>
                <h4>Dodaj nową pozycję</h4>
                <form onSubmit={handleSubmit}>
                    <input name="title" placeholder="Tytuł" value={form.title} onChange={handleChange} required />
                    <input name="author" placeholder="Autor" value={form.author} onChange={handleChange} required />
                    <input name="releaseYear" type="number" placeholder="Rok wydania" value={form.releaseYear} onChange={handleChange} required />
                    <input name="quantity" type="number" placeholder="Ilość sztuk" value={form.quantity} onChange={handleChange} required min="0" />
                    <button type="submit">Dodaj</button>
                </form>
            </div>

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
                            <button onClick={() => handleDelete(b.id)}>Usuń</button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
};

export default BookManager;