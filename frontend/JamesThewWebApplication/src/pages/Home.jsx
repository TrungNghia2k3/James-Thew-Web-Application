import React from 'react';
import HeroSection from '../components/home/HeroSection';
import FeaturesGrid from '../components/home/FeaturesGrid';
import StatsSection from '../components/home/StatsSection';

// API Access Levels Component
const ApiAccessLevels = () => {
    const accessLevels = [
        {
            level: "Public",
            icon: "🌐",
            color: "bg-slate-50 border-slate-200 text-slate-800",
            features: ["Free recipe browsing", "User registration", "Basic search", "Public recipe data"],
            endpoints: "12+"
        },
        {
            level: "General User",
            icon: "👤",
            color: "bg-blue-50 border-blue-200 text-blue-800",
            features: ["Profile management", "Comment on recipes", "Save favorites", "Recipe history"],
            endpoints: "8+"
        },
        {
            level: "Subscriber",
            icon: "⭐",
            color: "bg-indigo-50 border-indigo-200 text-indigo-800",
            features: ["Premium recipes", "Contest participation", "Advanced features", "Priority support"],
            endpoints: "15+"
        },
        {
            level: "Staff",
            icon: "🛠️",
            color: "bg-cyan-50 border-cyan-200 text-cyan-800",
            features: ["Content moderation", "Recipe management", "Contest management", "User support"],
            endpoints: "25+"
        },
        {
            level: "Admin",
            icon: "👑",
            color: "bg-sky-50 border-sky-200 text-sky-800",
            features: ["Full system access", "User management", "Role assignment", "System configuration"],
            endpoints: "30+"
        }
    ];

    return (
        <div className="mb-16">
            <div className="text-center mb-8">
                <h2 className="text-3xl font-bold text-black mb-4">🔐 API Access Levels</h2>
                <p className="text-gray-600 max-w-3xl mx-auto">
                    Our API supports multiple user roles with different access levels. Choose the right level for your application needs.
                </p>
            </div>
            <div className="grid md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-5 gap-4">
                {accessLevels.map((level, index) => (
                    <div key={index} className={`${level.color} border-2 p-4 rounded-lg`}>
                        <div className="text-center mb-3">
                            <div className="text-2xl mb-2">{level.icon}</div>
                            <h3 className="font-semibold text-sm">{level.level}</h3>
                            <div className="text-xs opacity-70">{level.endpoints} endpoints</div>
                        </div>
                        <ul className="text-xs space-y-1">
                            {level.features.map((feature, idx) => (
                                <li key={idx} className="flex items-start">
                                    <span className="mr-1">•</span>
                                    <span>{feature}</span>
                                </li>
                            ))}
                        </ul>
                    </div>
                ))}
            </div>
        </div>
    );
};

// Popular Endpoints Showcase
const PopularEndpoints = () => {
    const endpoints = [
        {
            method: "GET",
            path: "/api/recipes",
            description: "Browse free recipes with pagination",
            popular: true
        },
        {
            method: "POST",
            path: "/api/login",
            description: "User authentication",
            popular: true
        },
        {
            method: "GET",
            path: "/api/protected/subscriber/contest-entry",
            description: "Contest entry management",
            popular: false
        },
        {
            method: "POST",
            path: "/api/protected/general/comments",
            description: "Add recipe comments",
            popular: true
        }
    ];

    const getMethodColor = (method) => {
        const colors = {
            GET: "bg-slate-100 text-slate-800",
            POST: "bg-blue-100 text-blue-800",
            PUT: "bg-indigo-100 text-indigo-800",
            DELETE: "bg-cyan-100 text-cyan-800"
        };
        return colors[method] || "bg-gray-100 text-gray-800";
    };

    return (
        <div className="mb-16">
            <div className="text-center mb-8">
                <h2 className="text-3xl font-bold text-black mb-4">🚀 Popular API Endpoints</h2>
                <p className="text-gray-600 max-w-3xl mx-auto">
                    Get started with these frequently used endpoints. Each comes with complete documentation and examples.
                </p>
            </div>
            <div className="bg-gray-50 rounded-lg p-6">
                <div className="space-y-3">
                    {endpoints.map((endpoint, index) => (
                        <div key={index} className="flex items-center justify-between bg-white p-4 rounded-lg border">
                            <div className="flex items-center space-x-3">
                                <span className={`px-2 py-1 rounded text-xs font-medium ${getMethodColor(endpoint.method)}`}>
                                    {endpoint.method}
                                </span>
                                <code className="text-sm font-mono text-gray-700">{endpoint.path}</code>
                                {endpoint.popular && (
                                    <span className="bg-slate-100 text-slate-800 px-2 py-1 rounded text-xs">
                                        🔥 Popular
                                    </span>
                                )}
                            </div>
                            <span className="text-sm text-gray-600">{endpoint.description}</span>
                        </div>
                    ))}
                </div>
                <div className="text-center mt-6">
                    <p className="text-sm text-gray-600 mb-4">
                        Want to see all endpoints? Check out our comprehensive documentation.
                    </p>
                </div>
            </div>
        </div>
    );
};

const Home = () => {
    return (
        <div className="max-w-7xl mx-auto px-4 py-16">
            <div className="text-center">
                <HeroSection />
                <FeaturesGrid />
                <ApiAccessLevels />
                <PopularEndpoints />
                <StatsSection />
            </div>
        </div>
    );
};

export default Home;
