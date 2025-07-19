import React from 'react';
import { KeyIcon } from '@heroicons/react/24/outline';
import RoleLoginSection from './RoleLoginSection';
import DynamicRequestForm from './DynamicRequestForm';
import TokenStatus from './TokenStatus';
import { TryItButton, ResponseDisplay } from './TryItButton';

const RightPanel = ({
    // State
    roleTokens,
    requestData,
    loading,
    response,
    currentEndpoint,
    selectedCategory,
    
    // Data
    roleCredentials,
    apiDocs,
    
    // Handlers
    updateRequestField,
    updateNestedField,
    handleRoleLogin,
    clearTokenForRole,
    clearAllTokens,
    handleTryIt
}) => {
    return (
        <div className="w-96 xl:w-[28rem] bg-gray-50 border-l border-gray-200 overflow-y-auto flex-shrink-0">
            {/* Header */}
            <div className="p-6 border-b border-gray-200">
                <div className="flex items-center justify-between mb-2">
                    <h3 className="text-lg font-semibold text-black">Try It Out</h3>
                    {Object.keys(roleTokens).length > 0 && (
                        <span className="px-2 py-1 bg-green-100 text-green-800 rounded-full text-xs font-semibold flex items-center">
                            <KeyIcon className="w-3 h-3 mr-1" />
                            {Object.keys(roleTokens).length} Token(s) Ready
                        </span>
                    )}
                </div>
                <p className="text-sm text-gray-600">Test the API endpoint with role-based authentication</p>
            </div>

            {/* Content */}
            <div className="p-6">
                <RoleLoginSection
                    roleCredentials={roleCredentials}
                    roleTokens={roleTokens}
                    loading={loading}
                    onLogin={handleRoleLogin}
                    onClear={clearTokenForRole}
                    onClearAll={clearAllTokens}
                />

                <DynamicRequestForm
                    currentEndpoint={currentEndpoint}
                    requestData={requestData}
                    updateRequestField={updateRequestField}
                    updateNestedField={updateNestedField}
                />

                <TokenStatus
                    currentEndpoint={currentEndpoint}
                    roleTokens={roleTokens}
                    selectedCategory={selectedCategory}
                    apiDocs={apiDocs}
                />

                <TryItButton
                    currentEndpoint={currentEndpoint}
                    loading={loading}
                    roleTokens={roleTokens}
                    selectedCategory={selectedCategory}
                    apiDocs={apiDocs}
                    onTryIt={handleTryIt}
                />

                <ResponseDisplay response={response} />
            </div>
        </div>
    );
};

export default RightPanel;
