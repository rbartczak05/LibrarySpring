import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import api from '../api';

const LibrarianList = () => {
    const [librarians, setLibrarians] = useState([]);
    const [filterId, setFilterId] = useState('');

    useEffect(() => {
        api.get('/librarians')
            .then(res => setLibrarians(res.data))
            .catch(() => alert("Błąd pobierania listy bibliotekarzy"));
    }, []);

    const filteredLibrarians = librarians.filter(l =>
        l.id.toLowerCase().includes(filterId.toLowerCase())
    );

    return (
        <div>
            <h2>Bibliotekarze</h2>
            <div>
                <label>Filtruj po ID: </label>
                <input
                    type="text"
                    value={filterId}
                    onChange={e => setFilterId(e.target.value)}
                />

                <Link to="/librarians/add">
                    <button>+ Dodaj Bibliotekarza</button>
                </Link>
            </div>

            <table border="1" cellPadding="8">
                <thead>
                <tr>
                    <th>Login</th>
                    <th>Email</th>
                    <th>Wiek</th>
                    <th>Status</th>
                    <th>ID (UUID)</th>
                </tr>
                </thead>
                <tbody>
                {filteredLibrarians.map(l => (
                    <tr key={l.id}>
                        <td>{l.login}</td>
                        <td>{l.email}</td>
                        <td>{l.age}</td>
                        <td>
                            {l.active ? 'AKTYWNY' : 'NIEAKTYWNY'}
                        </td>
                        <td>{l.id}</td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
};

export default LibrarianList;