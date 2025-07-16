import React from 'react'
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom'
import Header from './components/Header'
import Footer from './components/Footer'
import Home from './pages/Home'
import Docs from './pages/Docs'
import './App.css'

function App() {
    return (
        <Router>
            <div className="min-h-screen bg-white text-black flex flex-col">
                <Header />
                <main className="flex-grow">
                    <Routes>
                        <Route path="/" element={<Home />} />
                        <Route path="/docs" element={<Docs />} />
                    </Routes>
                </main>
                <Footer />
            </div>
        </Router>
    )
}

export default App
