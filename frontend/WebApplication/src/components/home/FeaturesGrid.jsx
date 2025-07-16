import React from 'react';
import {
    BookOpenIcon,
    TrophyIcon,
    UserGroupIcon,
    SparklesIcon,
    ChatBubbleLeftRightIcon,
    HeartIcon
} from '@heroicons/react/24/outline';

// eslint-disable-next-line no-unused-vars
const FeatureCard = ({ icon: IconComponent, title, description, endpoints, color = "slate" }) => {
    const colorClasses = {
        slate: { bg: "border-slate-200 bg-slate-50", icon: "text-slate-600" },
        blue: { bg: "border-blue-200 bg-blue-50", icon: "text-blue-600" },
        indigo: { bg: "border-indigo-200 bg-indigo-50", icon: "text-indigo-600" },
        cyan: { bg: "border-cyan-200 bg-cyan-50", icon: "text-cyan-600" },
        teal: { bg: "border-teal-200 bg-teal-50", icon: "text-teal-600" },
        sky: { bg: "border-sky-200 bg-sky-50", icon: "text-sky-600" }
    };

    return (
        <div className={`${colorClasses[color].bg} border-2 p-6 rounded-xl shadow-sm hover:shadow-md transition-shadow`}>
            <div className="mb-4">
                <IconComponent className={`w-10 h-10 mx-auto ${colorClasses[color].icon}`} />
            </div>
            <h3 className="text-lg font-semibold text-black mb-3">{title}</h3>
            <p className="text-sm text-gray-600 leading-relaxed mb-4">
                {description}
            </p>
            <div className="text-xs text-gray-500">
                <span className="font-medium">{endpoints}</span> API endpoints
            </div>
        </div>
    );
};

const FeaturesGrid = () => {
    const features = [
        {
            icon: BookOpenIcon,
            title: "Recipe Library",
            description: "Access thousands of recipes from free family favorites to premium chef exclusives. Search by cuisine, category, ingredients, and cooking time.",
            endpoints: "15+",
            color: "slate"
        },
        {
            icon: TrophyIcon,
            title: "Cooking Contests",
            description: "Join exciting cooking competitions! Submit your creations, get scored by expert judges, and compete with fellow food enthusiasts.",
            endpoints: "12+",
            color: "blue"
        },
        {
            icon: UserGroupIcon,
            title: "Community Features",
            description: "Connect with other food lovers! Rate recipes, leave comments, save favorites, and share your culinary journey.",
            endpoints: "8+",
            color: "indigo"
        },
        {
            icon: SparklesIcon,
            title: "Premium Content",
            description: "Unlock exclusive recipes, advanced techniques, meal planning, and chef secrets with premium subscription access.",
            endpoints: "6+",
            color: "cyan"
        },
        {
            icon: ChatBubbleLeftRightIcon,
            title: "Support System",
            description: "Get help when you need it! Submit cooking questions and receive answers from our expert culinary team.",
            endpoints: "4+",
            color: "teal"
        },
        {
            icon: HeartIcon,
            title: "User Management",
            description: "Manage your profile, preferences, dietary restrictions, and personalize your cooking experience.",
            endpoints: "10+",
            color: "sky"
        }
    ];

    return (
        <div className="mb-16">
            <h2 className="text-3xl font-bold text-black mb-4">🌟 Platform Features</h2>
            <p className="text-gray-600 mb-8 max-w-3xl mx-auto">
                Discover what makes James Thew Chef the ultimate culinary platform for developers and food enthusiasts
            </p>
            <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
                {features.map((feature, index) => (
                    <FeatureCard
                        key={index}
                        icon={feature.icon}
                        title={feature.title}
                        description={feature.description}
                        endpoints={feature.endpoints}
                        color={feature.color}
                    />
                ))}
            </div>
        </div>
    );
};

export default FeaturesGrid;
