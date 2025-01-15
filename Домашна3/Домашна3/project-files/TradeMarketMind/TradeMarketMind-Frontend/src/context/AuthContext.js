import React, { createContext, useContext, useState } from 'react';

// Создавање на контекст за автентикација
const AuthContext = createContext();

// Обезбедување на контекстот на автентикација за апликацијата
export const AuthProvider = ({ children }) => {
    // Состојба за токен и корисничко име
    const [token, setToken] = useState(localStorage.getItem('token') || null);
    const [user, setUser] = useState(localStorage.getItem('user') || null);

    // Функција за најавување - зачувување токен и корисничко име
    const signIn = (authToken, username) => {
        setToken(authToken);
        setUser(username);
        localStorage.setItem('token', authToken);
        localStorage.setItem('user', username);
    };

    // Функција за одјавување - бришење на зачуваните податоци
    const signOut = () => {
        setToken(null);
        setUser(null);
        localStorage.removeItem('token');
        localStorage.removeItem('user');
    };

    // Проверка дали корисникот е најавен
    const isLoggedIn = () => !!token;

    return (
        <AuthContext.Provider value={{ token, user, signIn, signOut, isLoggedIn }}>
            {children}
        </AuthContext.Provider>
    );
};

// Прилагоден hook за користење на AuthContext
export const useAuth = () => useContext(AuthContext);
