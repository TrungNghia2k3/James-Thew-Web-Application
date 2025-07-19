import React from 'react';
import { Prism as SyntaxHighlighter } from 'react-syntax-highlighter';
import { prism } from 'react-syntax-highlighter/dist/esm/styles/prism';
import { LockClosedIcon } from '@heroicons/react/24/outline';

const EndpointHeader = ({ currentEndpoint }) => {
    if (!currentEndpoint) return null;

    const getMethodColor = (method) => {
        switch (method) {
            case 'GET': return 'bg-green-100 text-green-800';
            case 'POST': return 'bg-blue-100 text-blue-800';
            case 'PUT': return 'bg-orange-100 text-orange-800';
            case 'DELETE': return 'bg-red-100 text-red-800';
            default: return 'bg-gray-100 text-gray-800';
        }
    };

    return (
        <div className="mb-8">
            <h1 className="text-3xl lg:text-4xl font-bold text-black mb-2 leading-tight">
                {currentEndpoint.title}
            </h1>
            <p className="text-base md:text-lg text-gray-600 mb-4 leading-relaxed">
                {currentEndpoint.description}
            </p>

            <div className="flex items-center space-x-4 mb-2">
                <span className={`px-3 py-1 rounded-full text-sm font-mono font-semibold ${getMethodColor(currentEndpoint.method)}`}>
                    {currentEndpoint.method}
                </span>
                <code className="bg-gray-100 text-gray-800 px-3 py-1 rounded text-sm font-mono">
                    {currentEndpoint.endpoint}
                </code>
            </div>

            {(currentEndpoint.requiresAuth || currentEndpoint.role || (currentEndpoint.permissions && currentEndpoint.permissions.length > 0)) && (
                <div className="flex items-center space-x-4 my-6">
                    {currentEndpoint.requiresAuth && (
                        <span className="px-3 py-1 bg-red-100 text-red-800 rounded-full text-sm font-semibold flex items-center">
                            <LockClosedIcon className="w-4 h-4 mr-1" />
                            Auth Required
                        </span>
                    )}
                    {currentEndpoint.role && (
                        <span className="px-3 py-1 bg-purple-100 text-purple-800 rounded-full text-sm font-semibold">
                            Role: {currentEndpoint.role}
                        </span>
                    )}
                    {currentEndpoint.permissions && currentEndpoint.permissions.length > 0 && (
                        <span className="px-3 py-1 bg-yellow-100 text-yellow-800 rounded-full text-sm font-semibold">
                            Permissions: {currentEndpoint.permissions.join(', ')}
                        </span>
                    )}
                </div>
            )}
        </div>
    );
};

const ParametersTable = ({ currentEndpoint }) => {
    if (!currentEndpoint?.parameters?.length) return null;

    return (
        <div className="mb-8">
            <h3 className="text-2xl font-semibold text-black mb-4">Parameters</h3>
            <div className="bg-white rounded-lg border border-gray-200 overflow-hidden shadow-sm">
                <table className="w-full">
                    <thead className="bg-gray-50">
                        <tr>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-700 uppercase tracking-wider">Name</th>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-700 uppercase tracking-wider">Type</th>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-700 uppercase tracking-wider">Required</th>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-700 uppercase tracking-wider">Description</th>
                        </tr>
                    </thead>
                    <tbody className="bg-white divide-y divide-gray-200">
                        {currentEndpoint.parameters.map((param, index) => (
                            <tr key={index}>
                                <td className="px-6 py-4 whitespace-nowrap text-sm font-mono font-medium text-gray-900">
                                    {param.name}
                                </td>
                                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-600">
                                    <code className="bg-gray-100 px-2 py-1 rounded font-mono text-gray-800">
                                        {param.type}
                                    </code>
                                </td>
                                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-600">
                                    {param.required ? (
                                        <span className="text-red-600 font-semibold">Yes</span>
                                    ) : (
                                        <span className="text-gray-400">No</span>
                                    )}
                                </td>
                                <td className="px-6 py-4 text-sm text-gray-600">{param.description}</td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
};

const RequestBodySection = ({ currentEndpoint }) => {
    if (!currentEndpoint?.requestBody) return null;

    return (
        <div className="mb-8">
            <h3 className="text-2xl font-semibold text-black mb-4">Request Body</h3>
            <div className="bg-gray-50 rounded-lg overflow-hidden border border-gray-200">
                <SyntaxHighlighter 
                    language="json" 
                    style={prism} 
                    customStyle={{ margin: 0, background: '#f9fafb' }}
                >
                    {JSON.stringify(currentEndpoint.requestBody, null, 2)}
                </SyntaxHighlighter>
            </div>
        </div>
    );
};

const SampleRequestSection = ({ currentEndpoint }) => {
    if (!currentEndpoint?.sampleRequest) return null;

    return (
        <div className="mb-8">
            <h3 className="text-2xl font-semibold text-black mb-4">Sample Request</h3>
            <div className="bg-gray-50 rounded-lg overflow-hidden border border-gray-200">
                <SyntaxHighlighter 
                    language="javascript" 
                    style={prism} 
                    customStyle={{ margin: 0, background: '#f9fafb' }}
                >
                    {currentEndpoint.sampleRequest}
                </SyntaxHighlighter>
            </div>
        </div>
    );
};

const SampleResponseSection = ({ currentEndpoint }) => {
    if (!currentEndpoint?.sampleResponse) return null;

    return (
        <div className="mb-8">
            <h3 className="text-2xl font-semibold text-black mb-4">Sample Response</h3>
            <div className="bg-gray-50 rounded-lg overflow-hidden border border-gray-200">
                <SyntaxHighlighter 
                    language="json" 
                    style={prism} 
                    customStyle={{ margin: 0, background: '#f9fafb' }}
                >
                    {currentEndpoint.sampleResponse}
                </SyntaxHighlighter>
            </div>
        </div>
    );
};

const EmptyState = () => {
    return (
        <div className="text-center py-16">
            <h2 className="text-2xl font-semibold text-gray-600 mb-4">Select an Endpoint</h2>
            <p className="text-gray-500">Choose a category and endpoint from the sidebar to view its documentation.</p>
        </div>
    );
};

const MainContent = ({ currentEndpoint }) => {
    return (
        <div className="flex-1 overflow-y-auto">
            <div className="max-w-5xl mx-auto px-4 py-8">
                {currentEndpoint ? (
                    <>
                        <EndpointHeader currentEndpoint={currentEndpoint} />
                        <ParametersTable currentEndpoint={currentEndpoint} />
                        <RequestBodySection currentEndpoint={currentEndpoint} />
                        <SampleRequestSection currentEndpoint={currentEndpoint} />
                        <SampleResponseSection currentEndpoint={currentEndpoint} />
                    </>
                ) : (
                    <EmptyState />
                )}
            </div>
        </div>
    );
};

export default MainContent;
