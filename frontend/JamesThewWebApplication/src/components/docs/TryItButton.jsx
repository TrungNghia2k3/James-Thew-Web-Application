import React from 'react';
import { Prism as SyntaxHighlighter } from 'react-syntax-highlighter';
import { prism } from 'react-syntax-highlighter/dist/esm/styles/prism';

const TryItButton = ({ 
    currentEndpoint, 
    loading, 
    roleTokens, 
    selectedCategory, 
    apiDocs, 
    onTryIt 
}) => {
    const isDisabled = !currentEndpoint || loading || (currentEndpoint && currentEndpoint.requiresAuth && (() => {
        const endpointRole = currentEndpoint.role || apiDocs[selectedCategory].role;
        return endpointRole && !roleTokens[endpointRole];
    })());

    const getButtonText = () => {
        if (!currentEndpoint) return 'Select an Endpoint';
        if (loading) return null; // Will show spinner
        if (currentEndpoint.requiresAuth && (() => {
            const endpointRole = currentEndpoint.role || apiDocs[selectedCategory].role;
            return endpointRole && !roleTokens[endpointRole];
        })()) {
            return `${currentEndpoint.role || apiDocs[selectedCategory].role} Token Required`;
        }
        return 'Try It!';
    };

    return (
        <button
            onClick={onTryIt}
            disabled={isDisabled}
            className={`w-full px-4 py-3 rounded-lg font-semibold text-white transition-colors ${
                isDisabled
                    ? 'bg-gray-400 cursor-not-allowed'
                    : 'bg-blue-600 hover:bg-blue-700'
            }`}
        >
            {loading ? (
                <div className="flex items-center justify-center">
                    <svg className="animate-spin -ml-1 mr-3 h-5 w-5 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                        <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4"></circle>
                        <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                    </svg>
                    Sending Request...
                </div>
            ) : (
                getButtonText()
            )}
        </button>
    );
};

const ResponseDisplay = ({ response }) => {
    if (!response) return null;

    return (
        <div className="mt-6">
            <h4 className="text-sm font-semibold text-black mb-3">Response:</h4>
            <div className="bg-gray-50 rounded-lg overflow-hidden border border-gray-200">
                <SyntaxHighlighter
                    language="json"
                    style={prism}
                    customStyle={{ margin: 0, fontSize: '12px', background: '#f9fafb' }}
                    showLineNumbers={true}
                >
                    {response}
                </SyntaxHighlighter>
            </div>
        </div>
    );
};

export { TryItButton, ResponseDisplay };
