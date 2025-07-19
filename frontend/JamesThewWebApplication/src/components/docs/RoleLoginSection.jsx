import React from 'react';
import {
    RocketLaunchIcon,
    CheckCircleIcon,
    LightBulbIcon
} from '@heroicons/react/24/outline';

const RoleLoginCard = ({ 
    role, 
    credentials, 
    roleTokens, 
    loading, 
    onLogin, 
    onClear 
}) => {
    const getRoleColor = (role) => {
        switch (role) {
            case 'ADMIN': return 'bg-red-100 text-red-800';
            case 'WRITER': return 'bg-orange-100 text-orange-800';
            case 'SUBSCRIBER': return 'bg-yellow-100 text-yellow-800';
            default: 
                return role.startsWith('STAFF') ? 'bg-purple-100 text-purple-800' : 'bg-gray-100 text-gray-800';
        }
    };

    return (
        <div className="flex items-center justify-between p-2 bg-white rounded border">
            <div className="flex-1">
                <div className="flex items-center space-x-2">
                    <span className={`px-2 py-1 rounded-full text-xs font-semibold ${getRoleColor(role)}`}>
                        {role === 'STAFF_LIMITED' ? 'STAFF (Limited)' : role}
                    </span>
                    {roleTokens[role] && (
                        <span className="text-green-600 text-xs flex items-center">
                            <CheckCircleIcon className="w-3 h-3 mr-1" />
                            Logged in
                        </span>
                    )}
                </div>
                <div className="text-xs text-gray-600 mt-1">
                    {credentials.username}
                    {role === 'STAFF' && (
                        <span className="block text-xs text-purple-600">
                            Permissions: MANAGE_CONTESTS, ANSWER_QUESTIONS
                        </span>
                    )}
                    {role === 'STAFF_LIMITED' && (
                        <span className="block text-xs text-purple-600">
                            Permissions: ANSWER_QUESTIONS only
                        </span>
                    )}
                </div>
            </div>
            <div className="flex space-x-1">
                <button
                    onClick={() => onLogin(role, credentials)}
                    disabled={loading}
                    className="px-2 py-1 bg-blue-600 text-white rounded text-xs hover:bg-blue-700 disabled:bg-gray-400"
                >
                    {roleTokens[role] ? 'Refresh' : 'Login'}
                </button>
                {roleTokens[role] && (
                    <button
                        onClick={() => onClear(role)}
                        className="px-2 py-1 bg-red-600 text-white rounded text-xs hover:bg-red-700"
                    >
                        Clear
                    </button>
                )}
            </div>
        </div>
    );
};

const RoleLoginSection = ({ 
    roleCredentials, 
    roleTokens, 
    loading, 
    onLogin, 
    onClear, 
    onClearAll 
}) => {
    return (
        <div className="mb-6 p-4 bg-blue-50 border border-blue-200 rounded-lg">
            <h4 className="text-sm font-semibold text-blue-800 mb-3 flex items-center">
                <RocketLaunchIcon className="w-4 h-4 mr-1" />
                Role-Based Quick Login
            </h4>
            <div className="space-y-3">
                {Object.entries(roleCredentials).map(([role, credentials]) => (
                    <RoleLoginCard
                        key={role}
                        role={role}
                        credentials={credentials}
                        roleTokens={roleTokens}
                        loading={loading}
                        onLogin={onLogin}
                        onClear={onClear}
                    />
                ))}
            </div>
            <div className="mt-3 flex justify-between">
                <p className="text-xs text-blue-600 flex items-center">
                    <LightBulbIcon className="w-3 h-3 mr-1" />
                    Login with different roles to test various endpoints
                </p>
                {Object.keys(roleTokens).length > 0 && (
                    <button
                        onClick={onClearAll}
                        className="text-xs text-red-600 hover:text-red-800 underline"
                    >
                        Clear All Tokens
                    </button>
                )}
            </div>
        </div>
    );
};

export default RoleLoginSection;
