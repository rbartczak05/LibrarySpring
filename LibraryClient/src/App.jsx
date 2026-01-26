import { BrowserRouter as Router, Routes, Route, Link, useNavigate } from 'react-router-dom';
import Home from './Home';
import ReaderList from './lists/ReaderList.jsx';
import ReaderForm from './forms/ReaderForm.jsx';
import ReaderDetails from './ReaderDetails';
import AdministratorList from './lists/AdministratorList.jsx';
import AdministratorForm from './forms/AdministratorForm.jsx';
import LibrarianList from './lists/LibrarianList.jsx';
import LibrarianForm from './forms/LibrarianForm.jsx';
import BookManager from './managers/BookManager.jsx';
import LoanManager from './managers/LoanManager.jsx';
import LoginForm from './forms/LoginForm.jsx'
import RegisterForm from './forms/RegisterForm.jsx'
import ChangePasswordForm from './forms/ChangePasswordForm.jsx'
import { AuthProvider, useAuth } from "./context/AuthContext.jsx";

const getRoleName = (role) => {
    switch (role) {
        case 'ROLE_ADMIN': return 'Administrator';
        case 'ROLE_LIBRARIAN': return 'Bibliotekarz';
        case 'ROLE_READER': return 'Czytelnik';
        default: return role;
    }
};

const Navigation = () => {
    const { user, logout, can } = useAuth();
    const navigate = useNavigate();
    const handleLogout = () => {
        logout();
        navigate('/');
    };

    return (
        <div>
            <Link to="/">Strona Główna</Link>
            {user ? (
                <>
                    <span>Zalogowany jako: {user.login} ({getRoleName(user.role)})</span>
                    <button onClick={handleLogout}>Wyloguj</button>
                    <Link to="/change-password">Zmień hasło</Link>
                </>
            ) : (
                <Link to="/login">Zaloguj</Link>
            )}

            {can('manage_admins') && <Link to="/admins">Administratorzy</Link>}

            {can('manage_librarians') && <Link to="/librarians">Bibliotekarze</Link>}

            {can('manage_readers') && <Link to="/readers">Czytelnicy</Link>}
            {can('manage_loans') && <Link to="/loans">Wszystkie Wypożyczenia</Link>}

            {can('view_my_loans') && <Link to="/my-loans">Moje Wypożyczenia</Link>}

            {can('view_books') && <Link to="/books">Książki</Link>}
        </div>
    );
};

function App() {
    return (
        <AuthProvider>
            <Router>
                <div>
                    <Navigation />

                    <Routes>
                        <Route path="/" element={<Home />} />

                        <Route path="/login" element={<LoginForm />} />
                        <Route path="/register" element={<RegisterForm />} />
                        <Route path="/change-password" element={<ChangePasswordForm />} />

                        <Route path="/admins" element={<AdministratorList />} />
                        <Route path="/admins/add" element={<AdministratorForm />} />

                        <Route path="/librarians" element={<LibrarianList />} />
                        <Route path="/librarians/add" element={<LibrarianForm />} />

                        <Route path="/readers" element={<ReaderList />} />
                        <Route path="/readers/add" element={<ReaderForm />} />
                        <Route path="/readers/edit/:id" element={<ReaderForm />} />
                        <Route path="/readers/:id" element={<ReaderDetails />} />

                        <Route path="/books" element={<BookManager />} />
                        <Route path="/loans" element={<LoanManager />} />
                        <Route path="/my-loans" element={<LoanManager myLoansOnly={true} />} />
                    </Routes>
                </div>
            </Router>
        </AuthProvider>
    );
}

export default App;