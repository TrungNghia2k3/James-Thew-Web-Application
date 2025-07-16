import React from 'react';
import { Link } from 'react-router-dom';
import { ChevronRightIcon } from '@heroicons/react/24/outline';

const HeroSection = () => {
    return (
        <div className="mb-16">
            <div className="mb-6">
                <span className="text-6xl mb-4 block">🍳</span>
                <h1 className="text-4xl lg:text-6xl font-bold text-black mb-6 leading-tight">
                    Welcome to <span className="text-slate-600">James Thew Chef</span>
                </h1>
            </div>
            <p className="text-lg md:text-xl text-gray-600 mb-8 leading-relaxed max-w-4xl mx-auto">
                Your ultimate culinary companion! Discover thousands of recipes, join cooking contests, 
                share your culinary creations, and connect with food lovers worldwide. From free family favorites 
                to premium chef secrets - your cooking journey starts here.
            </p>

            <div className="flex flex-col sm:flex-row gap-4 justify-center items-center mb-8">
                <Link
                    to="/docs"
                    className="inline-flex items-center bg-slate-600 text-white px-8 py-4 rounded-lg font-semibold text-lg hover:bg-slate-700 transition-colors shadow-lg"
                >
                    <span>🚀 Explore Our API</span>
                    <ChevronRightIcon className="ml-2 w-5 h-5" />
                </Link>
                <div className="text-sm text-gray-500 flex items-center">
                    <span className="w-2 h-2 bg-green-500 rounded-full mr-2"></span>
                    Live API • Ready to integrate
                </div>
            </div>

            {/* Quick Stats */}
            <div className="flex flex-wrap justify-center gap-8 text-sm text-gray-600 mb-8">
                <div className="flex items-center">
                    <span className="text-slate-500 mr-2">📚</span>
                    <span>1500+ Free Recipes</span>
                </div>
                <div className="flex items-center">
                    <span className="text-slate-500 mr-2">🏆</span>
                    <span>Cooking Contests</span>
                </div>
                <div className="flex items-center">
                    <span className="text-slate-500 mr-2">👥</span>
                    <span>Community Driven</span>
                </div>
                <div className="flex items-center">
                    <span className="text-slate-500 mr-2">⭐</span>
                    <span>Premium Content</span>
                </div>
            </div>
        </div>
    );
};

export default HeroSection;
