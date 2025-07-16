import React from 'react';
import {
    KeyIcon,
    ExclamationTriangleIcon
} from '@heroicons/react/24/outline';

const TokenStatus = ({ 
    currentEndpoint, 
    roleTokens, 
    selectedCategory, 
    apiDocs 
}) => {
    if (!currentEndpoint?.requiresAuth) return null;

    const endpointRole = currentEndpoint.role || apiDocs[selectedCategory].role;
    const hasRequiredToken = endpointRole && roleTokens[endpointRole];
    
    return (
        <div className="mb-4">
            <div className={`p-3 border rounded-lg ${
                hasRequiredToken 
                    ? 'bg-green-50 border-green-200' 
                    : 'bg-yellow-50 border-yellow-200'
            }`}>
                <div className="flex items-center justify-between">
                    <div>
                        <div className="text-sm font-semibold flex items-center">
                            {hasRequiredToken ? (
                                <>
                                    <KeyIcon className="w-4 h-4 mr-1" />
                                    Ready to Test
                                </>
                            ) : (
                                <>
                                    <ExclamationTriangleIcon className="w-4 h-4 mr-1" />
                                    Token Required
                                </>
                            )}
                        </div>
                        <div className="text-xs mt-1">
                            {endpointRole ? (
                                hasRequiredToken 
                                    ? `Using ${endpointRole} token`
                                    : `Need ${endpointRole} token - login above`
                            ) : 'No specific role required'}
                        </div>
                    </div>
                    {hasRequiredToken && (
                        <span className="px-2 py-1 bg-green-100 text-green-800 rounded-full text-xs font-semibold">
                            {endpointRole}
                        </span>
                    )}
                </div>
            </div>
        </div>
    );
};

export default TokenStatus;
