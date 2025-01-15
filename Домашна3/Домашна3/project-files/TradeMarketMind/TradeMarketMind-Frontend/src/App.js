import './App.css';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Home from './pages/home/Home';
import Predict from './pages/predict/Predict';
import Register from './pages/signin/SignIn';
import Login from './pages/login/Login';
import About from './pages/about/About';
import RootLayout from './layouts/RootLayout';
import StockDetails from './pages/stock-details/StockDetails';
import ProtectedRoute from './components/ProtectedRoute';
import { AuthProvider } from './context/AuthContext';
import PublicRoute from './components/PublicRoute';

function App() {
    return (
        <AuthProvider>
            <Router>
                <div className="App">
                    <RootLayout>
                        <Routes>
                            {/* Main Routes */}
                            <Route path="/" element={<Home />} />
                            <Route path="/about" element={<About />} />

                            {/* Public Routes */}
                            <Route path="/login" element={
                                <PublicRoute>
                                    <Login />
                                </PublicRoute>
                            } />
                            <Route path="/signin" element={
                                <PublicRoute>
                                    <Register />
                                </PublicRoute>
                            } />

                            {/* Protected Routes */}
                            <Route path="/predict" element={
                                <ProtectedRoute>
                                    <Predict />
                                </ProtectedRoute>
                            } />
                            <Route path="/predict/stock-details/:id" element={
                                <ProtectedRoute>
                                    <StockDetails />
                                </ProtectedRoute>
                            } />
                        </Routes>
                    </RootLayout>
                </div>
            </Router>
        </AuthProvider>
    );
}

export default App;
