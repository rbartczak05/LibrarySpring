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

function App() {
    return (
        <Router>
            <div>
                <div>
                    <Link to="/">Strona Główna  </Link>
                    <Link to="/admins">Administratorzy</Link>
                    <Link to="/librarians">Bibliotekarze</Link>
                    <Link to="/readers">Czytelnicy</Link>
                    <Link to="/books">Książki</Link>
                    <Link to="/loans">Wypożyczenia</Link>
                </div>

                <Routes>
                    <Route path="/" element={<Home />} />

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
    );
}

export default App;