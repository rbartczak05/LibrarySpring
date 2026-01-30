import { BrowserRouter as Router, Routes, Route, Link, useNavigate, Navigate } from 'react-router-dom';
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

const ProtectedRoute = ({ children, permission }) => {
    const { user, can } = useAuth();

    if (!user) {
        return <Navigate to="/login" replace />;
    }

    if (permission && !can(permission)) {
        return <Navigate to="/" replace />;
    }

    return children;
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

                        <Route path="/change-password" element={
                            <ProtectedRoute><ChangePasswordForm /></ProtectedRoute>
                        } />

                        <Route path="/admins" element={
                            <ProtectedRoute permission="manage_admins"><AdministratorList /></ProtectedRoute>
                        } />
                        <Route path="/admins/add" element={
                            <ProtectedRoute permission="manage_admins"><AdministratorForm /></ProtectedRoute>
                        } />

                        <Route path="/librarians" element={
                            <ProtectedRoute permission="manage_librarians"><LibrarianList /></ProtectedRoute>
                        } />
                        <Route path="/librarians/add" element={
                            <ProtectedRoute permission="manage_librarians"><LibrarianForm /></ProtectedRoute>
                        } />

                        <Route path="/readers" element={
                            <ProtectedRoute permission="manage_readers"><ReaderList /></ProtectedRoute>
                        } />
                        <Route path="/readers/add" element={
                            <ProtectedRoute permission="manage_readers"><ReaderForm /></ProtectedRoute>
                        } />
                        <Route path="/readers/edit/:id" element={
                            <ProtectedRoute permission="manage_readers"><ReaderForm /></ProtectedRoute>
                        } />
                        <Route path="/readers/:id" element={
                            <ProtectedRoute permission="manage_readers"><ReaderDetails /></ProtectedRoute>
                        } />

                        <Route path="/books" element={
                            <ProtectedRoute permission="view_books"><BookManager /></ProtectedRoute>
                        } />

                        <Route path="/loans" element={
                            <ProtectedRoute permission="manage_loans"><LoanManager /></ProtectedRoute>
                        } />

                        <Route path="/my-loans" element={
                            <ProtectedRoute permission="view_my_loans"><LoanManager myLoansOnly={true} /></ProtectedRoute>
                        } />
                    </Routes>
                </div>
            </Router>
        </AuthProvider>
    );
}

export default App;