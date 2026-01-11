import { useState, useEffect } from 'react';
import axios from 'axios';

const API_URL = 'http://localhost:8080';

const LoanManager = () => {
    const [loans, setLoans] = useState([]);
    const [newLoan, setNewLoan] = useState({ readerId: '', bookSetId: '' });

    useEffect(() => {
        loadLoans();
    }, []);

    const loadLoans = () => {
        axios.get(`${API_URL}/loans`)
            .then(res => setLoans(res.data))
            .catch(() => alert("Błąd pobierania wypożyczeń"));
    };

    const handleCreate = (e) => {
        e.preventDefault();

        if (!window.confirm("Czy na pewno chcesz utworzyć to wypożyczenie?")) return;

        axios.post(`${API_URL}/loans`, null, {
            params: {
                readerId: newLoan.readerId,
                bookSetId: newLoan.bookSetId
            }
        })
            .then(() => {
                alert("Wypożyczono pomyślnie!");
                setNewLoan({ readerId: '', bookSetId: '' });
                loadLoans();
            })
            .catch(err => {
                alert("Błąd: " + (err.response?.data?.message || "Sprawdź ID czytelnika/książki lub limity"));
            });
    };

    const handleEndLoan = (id) => {
        if (!window.confirm("Czy na pewno chcesz zakończyć to wypożyczenie?")) return;

        axios.post(`${API_URL}/loans/${id}/end`)
            .then(() => {
                alert("Zwrot zaakceptowany.");
                loadLoans();
            })
            .catch(err => alert("Błąd: " + (err.response?.data?.message || err.message)));
    };

    return (
        <div>
            <h2>Wypożyczenia</h2>

            <div>
                <label>Nowa Alokacja</label>
                <form onSubmit={handleCreate}>
                    <input
                        placeholder="Podaj ID Czytelnika"
                        value={newLoan.readerId}
                        onChange={e => setNewLoan({...newLoan, readerId: e.target.value})}
                        required
                    />
                    <input
                        placeholder="Podaj ID Książki"
                        value={newLoan.bookSetId}
                        onChange={e => setNewLoan({...newLoan, bookSetId: e.target.value})}
                        required
                    />
                    <button type="submit">WYPOŻYCZ</button>
                </form>
            </div>

            <h3>Aktywne i zakończone alokacje</h3>
            <table border="1" cellPadding="8">
                <thead>
                <tr>
                    <th>ID Wypożyczenia</th>
                    <th>Czytelnik ID</th>
                    <th>Książka ID</th>
                    <th>Data Wypożyczenia</th>
                    <th>Data Zwrotu</th>
                    <th>Akcja</th>
                </tr>
                </thead>
                <tbody>
                {loans.map(loan => (
                    <tr key={loan.id}>
                        <td><small>{loan.id}</small></td>
                        <td><small>{loan.readerId}</small></td>
                        <td><small>{loan.bookSetId}</small></td>
                        <td>{new Date(loan.startTime).toLocaleString()}</td>
                        <td>{loan.returnTime ? new Date(loan.returnTime).toLocaleString() : '-'}</td>
                        <td>
                            {loan.active && (
                                <button onClick={() => handleEndLoan(loan.id)}>
                                    Zakończ (Zwrot)
                                </button>
                            )}
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
};

export default LoanManager;