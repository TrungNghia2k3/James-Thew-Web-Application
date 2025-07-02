import React from 'react';

const Header = () => {
    return (
        <header className="bg-gray-800 p-5 text-white text-center">
            <h1 className="m-0 text-2xl">Welcome to James Thew's Web Application</h1>
            <nav className="mt-2">
                <a href="/" className="mx-2 text-white no-underline text-base hover:underline">Home</a>
                <a href="/about" className="mx-2 text-white no-underline text-base hover:underline">About</a>
                <a href="/contact" className="mx-2 text-white no-underline text-base hover:underline">Contact</a>
                <a href="/services" className="mx-2 text-white no-underline text-base hover:underline">Services</a>
                <a href="/portfolio" className="mx-2 text-white no-underline text-base hover:underline">Portfolio</a>
                <a href="/blog" className="mx-2 text-white no-underline text-base hover:underline">Blog</a>
                <a href="/login" className="mx-2 text-white no-underline text-base hover:underline">Login</a>
            </nav>
        </header>
    );
};

export default Header;
