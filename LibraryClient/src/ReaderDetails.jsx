import { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import api from "./api.js";

const API_URL = 'http://localhost:8080';

const ReaderDetails = () => {
    const { id } = useParams();
    const [reader, setReader] = useState(null);
    const [loans, setLoans] = useState([]);

    useEffect(() => {
        api.get(`/readers/${id}`)
            .then(res => setReader(res.data));

        api.get(`/loans/reader_id/${id}`)
            .then(res => {
                const data = res.data._embedded ?
                    (res.data._embedded.loans || res.data._embedded.loanDTOList) :
                    res.data;
                setLoans(Array.isArray(data) ? data : []);
            })
            .catch(() => setLoans([]));
    }, [id]);

    if (!reader) return <p>Ładowanie danych...</p>;

    return (
        <div>
            <h2>Szczegóły Czytelnika: {reader.login}</h2>
            <div>
                <p><strong>ID:</strong> {reader.id}</p>
                <p><strong>Email:</strong> {reader.email}</p>
                <p><strong>Wiek:</strong> {reader.age}</p>
                <p><strong>Status:</strong> {reader.active ? 'Aktywny' : 'Zablokowany'}</p>
                <p><strong>Aktualne wypożyczenia:</strong> {reader.currentLoansCount}</p>
                <Link to="/readers"><button>Powrót do listy</button></Link>
            </div>

            <h3>Lista Alokacji (Wypożyczenia)</h3>
            <table border="1" cellPadding="5">
                <thead>
                <tr>
                    <th>ID Książki (BookSet)</th>
                    <th>Data Wypożyczenia</th>
                    <th>Data Zwrotu</th>
                    <th>Czy aktywne?</th>
                </tr>
                </thead>
                <tbody>
                {loans.length === 0 && <tr><td colSpan="4">Brak wypożyczeń w historii.</td></tr>}
                {loans.map(loan => (
                    <tr key={loan.id}>
                        <td>{loan.bookSetId}</td>
                        <td>{new Date(loan.startTime).toLocaleString()}</td>
                        <td>{loan.returnTime ? new Date(loan.returnTime).toLocaleString() : '-'}</td>
                        <td>{loan.active ? 'TAK' : 'NIE'}</td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
};

export default ReaderDetails;