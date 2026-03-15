import {useEffect, useState} from 'react';
import {Link} from 'react-router-dom';
import api from '../api';

const ReaderList = () => {
    const [readers, setReaders] = useState([]);
    const [filterId, setFilterId] = useState('');

    useEffect(() => {
        loadReaders();
    }, []);

    const loadReaders = () => {
        api.get('/readers')
            .then(res => setReaders(res.data))
            .catch(() => alert("Błąd pobierania listy czytelników"));
    };

    const handleToggleActive = (id, isActive) => {
        const action = isActive ? 'deactivate' : 'activate';
        const msg = isActive ? 'dezaktywować' : 'aktywować';

        if (!window.confirm(`Czy na pewno chcesz ${msg} tego użytkownika?`)) return;

        api.post(`/readers/${id}/${action}`)
            .then(() => loadReaders())
            .catch(err => alert("Błąd: " + (err.response?.data?.message || err.message)));
    };

    const filteredReaders = readers.filter(r => r.id.toLowerCase().includes(filterId.toLowerCase()));

    return (
        <div>
            <h2>Czytelnicy</h2>
            <div>
                <label>Filtruj po ID: </label>
                <input
                    type="text"
                    value={filterId}
                    onChange={e => setFilterId(e.target.value)}
                />
                <Link to="/readers/add">
                    <button>+ Dodaj Czytelnika</button>
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
                    <th>Akcje</th>
                </tr>
                </thead>
                <tbody>
                {filteredReaders.map(r => (
                    <tr key={r.id}>
                        <td>{r.login}</td>
                        <td>{r.email}</td>
                        <td>{r.age}</td>
                        <td>
                            {r.active ? 'AKTYWNY' : 'NIEAKTYWNY'}
                        </td>
                        <td>{r.id}</td>
                        <td>
                            <Link to={`/readers/${r.id}`}>
                                <button>Szczegóły</button>
                            </Link>
                            <Link to={`/readers/edit/${r.id}`}>
                                <button>Edytuj</button>
                            </Link>
                            <button onClick={() => handleToggleActive(r.id, r.active)}>
                                {r.active ? 'Dezaktywuj' : 'Aktywuj'}
                            </button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
};

export default ReaderList;