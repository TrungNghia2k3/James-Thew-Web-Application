import React from 'react';

const StatItem = ({ icon, value, label, description }) => {
    return (
        <div className="text-center">
            <div className="text-4xl mb-2">{icon}</div>
            <div className="text-3xl font-bold text-white mb-2">{value}</div>
            <div className="text-lg font-medium text-slate-100 mb-1">{label}</div>
            <div className="text-sm text-slate-200 opacity-90">{description}</div>
        </div>
    );
};

const StatsSection = () => {
    const stats = [
        { 
            icon: "📚", 
            value: "1500+", 
            label: "Recipes Available", 
            description: "From quick snacks to gourmet meals" 
        },
        { 
            icon: "⚡", 
            value: "<50ms", 
            label: "API Response", 
            description: "Lightning-fast recipe data" 
        },
        { 
            icon: "🌍", 
            value: "25+", 
            label: "Global Cuisines", 
            description: "Flavors from around the world" 
        },
        { 
            icon: "🆓", 
            label: "Free to Start", 
            value: "Always", 
            description: "No credit card required" 
        }
    ];

    return (
        <div className="bg-gradient-to-r from-slate-600 to-slate-700 text-white p-8 rounded-2xl shadow-lg mb-16">
            <div className="text-center mb-8">
                <h2 className="text-3xl md:text-4xl font-bold mb-3">🔥 Why Choose James Thew Chef?</h2>
                <p className="text-slate-100 text-lg max-w-2xl mx-auto">
                    Join thousands of developers building amazing culinary applications with our comprehensive API
                </p>
            </div>
            <div className="grid md:grid-cols-2 lg:grid-cols-4 gap-8">
                {stats.map((stat, index) => (
                    <StatItem
                        key={index}
                        icon={stat.icon}
                        value={stat.value}
                        label={stat.label}
                        description={stat.description}
                    />
                ))}
            </div>
            
            {/* Call to Action */}
            <div className="text-center mt-8 pt-8 border-t border-slate-500">
                <p className="text-slate-100 mb-4">Ready to start cooking up something amazing?</p>
                <div className="flex flex-col sm:flex-row gap-4 justify-center items-center">
                    <div className="flex items-center text-sm text-slate-200">
                        <span className="w-2 h-2 bg-green-400 rounded-full mr-2"></span>
                        All endpoints documented
                    </div>
                    <div className="flex items-center text-sm text-slate-200">
                        <span className="w-2 h-2 bg-green-400 rounded-full mr-2"></span>
                        Live examples included
                    </div>
                    <div className="flex items-center text-sm text-slate-200">
                        <span className="w-2 h-2 bg-green-400 rounded-full mr-2"></span>
                        Multiple auth levels
                    </div>
                </div>
            </div>
        </div>
    );
};

export default StatsSection;
