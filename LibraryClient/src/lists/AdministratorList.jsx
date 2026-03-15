import {useEffect, useState} from 'react';
import {Link} from 'react-router-dom';
import api from '../api';

const AdministratorList = () => {
    const [admins, setAdmins] = useState([]);
    const [filterId, setFilterId] = useState('');

    useEffect(() => {
        api.get('/admins')
            .then(res => setAdmins(res.data))
            .catch(() => alert("Błąd pobierania listy administratorów"));
    }, []);

    const filteredAdmins = admins.filter(a =>
        a.id.toLowerCase().includes(filterId.toLowerCase())
    );

    return (
        <div>
            <h2>Administratorzy</h2>
            <div>
                <label>Filtruj po ID: </label>
                <input
                    type="text"
                    value={filterId}
                    onChange={e => setFilterId(e.target.value)}
                />

                <Link to="/admins/add">
                    <button>+ Dodaj Administratora</button>
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
                {filteredAdmins.map(a => (
                    <tr key={a.id}>
                        <td>{a.login}</td>
                        <td>{a.email}</td>
                        <td>{a.age}</td>
                        <td>
                            {a.active ? 'AKTYWNY' : 'NIEAKTYWNY'}
                        </td>
                        <td>{a.id}</td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
};

export default AdministratorList;