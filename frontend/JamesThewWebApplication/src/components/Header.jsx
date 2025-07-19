import React from 'react';
import { Link, useLocation } from 'react-router-dom';

const Header = () => {
    const location = useLocation();

    return (
        <header className="bg-white border-b border-gray-200 shadow-sm">
            <div className="max-w-7xl mx-auto px-4">
                <div className="flex items-center justify-between h-16">
                    <Link
                        to="/"
                        className="text-black text-2xl font-bold hover:opacity-80 transition-opacity"
                    >
                        🍳 James Thew Chef
                    </Link>

                    <div className="flex items-center space-x-6">
                        <nav className="flex space-x-6">
                            <Link
                                to="/"
                                className={`text-black hover:opacity-70 transition-opacity px-3 py-2 rounded-md font-medium ${location.pathname === '/' ? 'bg-gray-100' : ''
                                    }`}
                            >
                                Home
                            </Link>
                            <Link
                                to="/docs"
                                className={`text-black hover:opacity-70 transition-opacity px-3 py-2 rounded-md font-medium ${location.pathname === '/docs' ? 'bg-gray-100' : ''
                                    }`}
                            >
                                Docs
                            </Link>
                        </nav>
                    </div>
                </div>
            </div>
        </header>
    );
};

export default Header;
