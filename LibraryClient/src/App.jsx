import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
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
    const { user, logout } = useAuth();
    const role = user?.role;
    const isAdmin = role === 'ROLE_ADMIN';
    const isLibrarian = role === 'ROLE_LIBRARIAN';

    return (
        <div>
            <Link to="/">Strona Główna</Link>
            {user ? (
                <>
                    <span>Zalogowany jako: {user.login} ({getRoleName(user.role)})</span>
                    <button onClick={logout}>Wyloguj</button>
                    <Link to="/change-password">Zmień hasło</Link>
                </>
            ) : (
                <Link to="/login">Zaloguj</Link>
            )}

            {/* Admin widzi wszystko */}
            {isAdmin && <Link to="/admins">Administratorzy</Link>}
            
            {/* Admin widzi bibliotekarzy */}
            {isAdmin && <Link to="/librarians">Bibliotekarze</Link>}

            {/* Admin i Bibliotekarz widzą czytelników i wypożyczenia */}
            {(isAdmin || isLibrarian) && <Link to="/readers">Czytelnicy</Link>}
            {(isAdmin || isLibrarian) && <Link to="/loans">Wypożyczenia</Link>}

            {/* Wszyscy zalogowani widzą książki */}
            {user && <Link to="/books">Książki</Link>}
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
                    </Routes>
                </div>
            </Router>
        </AuthProvider>
    );
}

export default App;