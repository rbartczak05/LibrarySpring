import { useState, useEffect } from 'react';
import api from '../api';

const LoanManager = ({ myLoansOnly = false }) => {
    const [loans, setLoans] = useState([]);
    const [newLoan, setNewLoan] = useState({ readerId: '', bookSetId: '' });

    useEffect(() => {
        loadLoans();
    }, [myLoansOnly]);

    const loadLoans = () => {
        const endpoint = myLoansOnly ? '/loans/me' : '/loans';
        api.get(endpoint)
            .then(res => {
                const data = res.data._embedded.loans;
                setLoans(Array.isArray(data) ? data : []);
            })
            .catch(() => alert("Błąd pobierania wypożyczeń"));
    };

    const handleCreate = (e) => {
        e.preventDefault();

        if (!window.confirm("Czy na pewno chcesz utworzyć to wypożyczenie?")) return;

        api.post('/loans', null, {
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

    const handleEndLoan = (url) => {
        if (!window.confirm("Czy na pewno chcesz zakończyć to wypożyczenie?")) return;

        api.post(url)
            .then(() => {
                alert("Zwrot zaakceptowany.");
                loadLoans();
            })
            .catch(err => alert("Błąd: " + (err.response?.data?.message || err.message)));
    };

    const handleDeleteLoan = (url) => {
        if (!window.confirm("Czy na pewno chcesz USUNĄĆ tę alokację?")) return;
        try {
            api.delete(url)
                .then(() => {
                    alert("Alokacja usunięta.");
                    loadLoans();
                })
                .catch(err => alert("Błąd: " + (err.response?.data?.message || err.message)));
        } catch (e) {
            alert("Wystąpił błąd podczas usuwania alokacji: " + e.message);
        }
    };

    return (
        <div>
            <h2>{myLoansOnly ? 'Moje Wypożyczenia' : 'Wszystkie Wypożyczenia'}</h2>

            {!myLoansOnly && (
                <div>
                    <label>Nowa Alokacja</label>
                    <form onSubmit={handleCreate}>
                        <input
                            type="text"
                            placeholder="Podaj ID Czytelnika"
                            value={newLoan.readerId}
                            onChange={e => setNewLoan({...newLoan, readerId: e.target.value})}
                            required
                            minLength="1"
                        />
                        <input
                            type="text"
                            placeholder="Podaj ID Książki"
                            value={newLoan.bookSetId}
                            onChange={e => setNewLoan({...newLoan, bookSetId: e.target.value})}
                            required
                            minLength="1"
                        />
                        <button type="submit">WYPOŻYCZ</button>
                    </form>
                </div>
            )}

            <h3>{myLoansOnly ? 'Historia twoich wypożyczeń' : 'Aktywne i zakończone alokacje'}</h3>
            <table border="1" cellPadding="8">
                <thead>
                <tr>
                    <th>ID Wypożyczenia</th>
                    {!myLoansOnly && <th>Czytelnik ID</th>}
                    <th>Książka ID</th>
                    <th>Data Wypożyczenia</th>
                    <th>Data Zwrotu</th>
                    {!myLoansOnly && <th>Akcja</th>}
                </tr>
                </thead>
                <tbody>
                {loans.map(loan => (
                    <tr key={loan.id}>
                        <td><small>{loan.id}</small></td>
                        {!myLoansOnly && <td><small>{loan.readerId}</small></td>}
                        <td><small>{loan.bookSetId}</small></td>
                        <td>{new Date(loan.startTime).toLocaleString()}</td>
                        <td>{loan.returnTime ? new Date(loan.returnTime).toLocaleString() : '-'}</td>
                        {!myLoansOnly && (
                            <td>
                                {loan.active && (
                                    <button onClick={() => handleEndLoan(loan._links.end.href)}>
                                        Zakończ (Zwrot)
                                    </button>
                                )}

                                {loan._links?.delete && (
                                    <button
                                        onClick={() => handleDeleteLoan(loan._links.delete.href)}
                                        style={{ backgroundColor: '#ef4444' }}>Usuń</button>
                                )}
                            </td>
                        )}
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
};

export default LoanManager;