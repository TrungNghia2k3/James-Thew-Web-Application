import React from 'react';
import { Link } from 'react-router-dom';
import { 
    BookOpenIcon, 
    HeartIcon, 
    EnvelopeIcon,
    PhoneIcon,
    MapPinIcon,
    CodeBracketIcon,
    TrophyIcon,
    UserGroupIcon
} from '@heroicons/react/24/outline';

const Footer = () => {
    const currentYear = new Date().getFullYear();

    const footerSections = [
        {
            title: "Platform",
            links: [
                { name: "Recipe Library", href: "/docs", icon: BookOpenIcon },
                { name: "API Documentation", href: "/docs", icon: CodeBracketIcon },
                { name: "Cooking Contests", href: "/docs", icon: TrophyIcon },
                { name: "Community", href: "/docs", icon: UserGroupIcon }
            ]
        },
        {
            title: "For Developers",
            links: [
                { name: "Getting Started", href: "/docs" },
                { name: "Authentication", href: "/docs" },
                { name: "Rate Limits", href: "/docs" },
                { name: "Status Page", href: "/docs" }
            ]
        },
        {
            title: "Resources",
            links: [
                { name: "Recipe Categories", href: "/docs" },
                { name: "Cuisine Types", href: "/docs" },
                { name: "Cooking Tips", href: "/docs" },
                { name: "Chef Secrets", href: "/docs" }
            ]
        }
    ];

    return (
        <footer className="bg-slate-800 text-gray-300 border-t border-slate-700">
            <div className="max-w-7xl mx-auto px-4 py-12">
                {/* Main Footer Content */}
                <div className="grid lg:grid-cols-4 md:grid-cols-2 gap-8 mb-8">
                    {/* Brand Section */}
                    <div className="lg:col-span-1">
                        <div className="flex items-center mb-4">
                            <span className="text-3xl mr-2">🍳</span>
                            <h3 className="text-xl font-bold text-white">James Thew Chef</h3>
                        </div>
                        <p className="text-gray-400 mb-6 leading-relaxed">
                            Your ultimate culinary API platform. Discover recipes, join contests, 
                            and build amazing food applications with our comprehensive endpoints.
                        </p>
                        
                        {/* Contact Info */}
                        <div className="space-y-3">
                            <div className="flex items-center text-sm">
                                <EnvelopeIcon className="w-4 h-4 mr-2 text-slate-400" />
                                <span>api@jamesthewchef.com</span>
                            </div>
                            <div className="flex items-center text-sm">
                                <PhoneIcon className="w-4 h-4 mr-2 text-slate-400" />
                                <span>+1 (555) 123-CHEF</span>
                            </div>
                            <div className="flex items-center text-sm">
                                <MapPinIcon className="w-4 h-4 mr-2 text-slate-400" />
                                <span>Culinary District, Food City</span>
                            </div>
                        </div>
                    </div>

                    {/* Footer Links */}
                    {footerSections.map((section, index) => (
                        <div key={index}>
                            <h4 className="text-white font-semibold mb-4">{section.title}</h4>
                            <ul className="space-y-3">
                                {section.links.map((link, linkIndex) => (
                                    <li key={linkIndex}>
                                        <Link 
                                            to={link.href} 
                                            className="text-gray-400 hover:text-white transition-colors flex items-center text-sm"
                                        >
                                            {link.icon && (
                                                <link.icon className="w-4 h-4 mr-2" />
                                            )}
                                            {link.name}
                                        </Link>
                                    </li>
                                ))}
                            </ul>
                        </div>
                    ))}
                </div>

                {/* API Stats Banner */}
                <div className="bg-slate-700 rounded-lg p-6 mb-8">
                    <div className="text-center">
                        <h4 className="text-white font-semibold mb-2 flex items-center justify-center">
                            <span className="text-green-400 mr-2">●</span>
                            API Status: All Systems Operational
                        </h4>
                        <div className="grid grid-cols-2 md:grid-cols-4 gap-4 text-sm">
                            <div className="text-center">
                                <div className="text-white font-bold">99.9%</div>
                                <div className="text-gray-400">Uptime</div>
                            </div>
                            <div className="text-center">
                                <div className="text-white font-bold">&lt;45ms</div>
                                <div className="text-gray-400">Response Time</div>
                            </div>
                            <div className="text-center">
                                <div className="text-white font-bold">1.5M+</div>
                                <div className="text-gray-400">API Calls/Month</div>
                            </div>
                            <div className="text-center">
                                <div className="text-white font-bold">24/7</div>
                                <div className="text-gray-400">Support</div>
                            </div>
                        </div>
                    </div>
                </div>

                {/* Bottom Section */}
                <div className="border-t border-slate-700 pt-8">
                    <div className="flex flex-col md:flex-row justify-between items-center">
                        <div className="text-sm text-gray-400 mb-4 md:mb-0">
                            <p>
                                © {currentYear} James Thew Chef API. Made with{' '}
                                <HeartIcon className="w-4 h-4 inline text-red-500" />{' '}
                                for food lovers and developers.
                            </p>
                        </div>
                        
                        <div className="flex items-center space-x-6 text-sm">
                            <Link to="/docs" className="text-gray-400 hover:text-white transition-colors">
                                Privacy Policy
                            </Link>
                            <Link to="/docs" className="text-gray-400 hover:text-white transition-colors">
                                Terms of Service
                            </Link>
                            <Link to="/docs" className="text-gray-400 hover:text-white transition-colors">
                                API License
                            </Link>
                        </div>
                    </div>
                    
                    {/* Developer Credit */}
                    <div className="text-center mt-4 pt-4 border-t border-slate-700">
                        <p className="text-xs text-gray-500">
                            🚀 Powered by React + Vite • Styled with Tailwind CSS • Icons by Heroicons
                        </p>
                    </div>
                </div>
            </div>
        </footer>
    );
};

export default Footer;
