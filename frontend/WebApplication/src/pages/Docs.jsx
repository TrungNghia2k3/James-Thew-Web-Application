import React, { useEffect } from 'react';
import { apiDocs } from '../data/apiDocs';
import Sidebar from '../components/docs/Sidebar';
import MainContent from '../components/docs/MainContent';
import RightPanel from '../components/docs/RightPanel';
import { 
    useDocsState, 
    useTokenManagement, 
    useRequestData, 
    useEndpointHelpers 
} from '../hooks/useDocsHooks';

const Docs = () => {
    const {
        selectedCategory,
        setSelectedCategory,
        selectedSubcategory,
        setSelectedSubcategory,
        selectedEndpoint,
        setSelectedEndpoint,
        response,
        setResponse,
        loading,
        setLoading,
        roleTokens,
        setRoleTokens,
        requestData,
        setRequestData,
        serverUrl,
        roleCredentials
    } = useDocsState();

    const { saveTokenForRole, clearTokenForRole, clearAllTokens } = useTokenManagement(setRoleTokens);
    const { initializeRequestData, updateRequestField, updateNestedField } = useRequestData(setRequestData);
    const { getCurrentEndpoint } = useEndpointHelpers(selectedCategory, selectedSubcategory, selectedEndpoint);

    const currentEndpoint = getCurrentEndpoint();

    // Initialize request data when endpoint changes
    useEffect(() => {
        const endpoint = getCurrentEndpoint();
        initializeRequestData(endpoint);
    }, [selectedCategory, selectedSubcategory, selectedEndpoint, getCurrentEndpoint, initializeRequestData]);

    // Navigation handlers
    const handleCategorySelect = (categoryKey, category) => {
        setSelectedCategory(categoryKey);
        setSelectedSubcategory('');
        setSelectedEndpoint(0);
        setResponse('');
        
        // If category has subcategories, select the first one
        if (category.subcategories) {
            const firstSubcategory = Object.keys(category.subcategories)[0];
            setSelectedSubcategory(firstSubcategory);
        }
    };

    const handleSubcategorySelect = (subKey) => {
        setSelectedSubcategory(subKey);
        setSelectedEndpoint(0);
        setResponse('');
    };

    const handleEndpointSelect = (index) => {
        setSelectedEndpoint(index);
        setResponse('');
    };

    // Role login handler
    const handleRoleLogin = async (role, credentials) => {
        setLoading(true);
        try {
            const res = await fetch('http://localhost:8080/JamesThewWebApplication/api/login', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(credentials)
            });
            const data = await res.json();
            if (data.success && data.data) {
                saveTokenForRole(data.data, role);
                setResponse(`${role} login successful!\n${JSON.stringify(data, null, 2)}`);
            } else {
                setResponse(`${role} login failed:\n${JSON.stringify(data, null, 2)}`);
            }
        } catch (error) {
            setResponse(`${role} login error: ${error.message}`);
        } finally {
            setLoading(false);
        }
    };

    // Try It handler
    const handleTryIt = async () => {
        if (!currentEndpoint) return;
        
        setLoading(true);
        setResponse('');

        try {
            let fetchOptions = {
                method: currentEndpoint.method,
                headers: {}
            };

            // Add authorization header if required - use role-specific token
            if (currentEndpoint.requiresAuth) {
                const endpointRole = currentEndpoint.role || apiDocs[selectedCategory].role;
                const tokenToUse = endpointRole ? roleTokens[endpointRole] : '';
                
                if (tokenToUse) {
                    fetchOptions.headers['Authorization'] = `Bearer ${tokenToUse}`;
                }
            }

            // Add request body for POST, PUT, DELETE methods using dynamic data
            if ((currentEndpoint.method === 'POST' || currentEndpoint.method === 'PUT' || currentEndpoint.method === 'DELETE') && requestData && Object.keys(requestData).length > 0) {
                if (currentEndpoint.contentType === 'multipart/form-data') {
                    // Handle form-data requests (for file uploads)
                    const formData = new FormData();
                    
                    Object.entries(requestData).forEach(([key, value]) => {
                        if (value !== undefined && value !== null && value !== '') {
                            // Handle file inputs specifically
                            if (key === 'image' && typeof value === 'string') {
                                // For demo purposes, we'll create a mock file
                                // In real implementation, this would be handled by the form
                                const blob = new Blob(['mock file content'], { type: 'image/jpeg' });
                                const file = new File([blob], value, { type: 'image/jpeg' });
                                formData.append(key, file);
                            } else {
                                formData.append(key, value.toString());
                            }
                        }
                    });
                    
                    fetchOptions.body = formData;
                    // Don't set Content-Type for FormData - browser will set it with boundary
                } else {
                    // Handle JSON requests
                    fetchOptions.headers['Content-Type'] = 'application/json';
                    fetchOptions.body = JSON.stringify(requestData);
                }
            } else if (!['POST', 'PUT', 'DELETE'].includes(currentEndpoint.method)) {
                // For GET requests, set Content-Type to JSON
                fetchOptions.headers['Content-Type'] = 'application/json';
            }

            // Build the full URL using serverUrl and endpoint path
            // Use url property if available, otherwise extract path from endpoint
            let urlPath = currentEndpoint.url;
            if (!urlPath && currentEndpoint.endpoint) {
                // Extract path from full endpoint URL
                try {
                    const endpointUrl = new URL(currentEndpoint.endpoint);
                    urlPath = endpointUrl.pathname;
                } catch {
                    // Fallback: use the endpoint as-is
                    urlPath = currentEndpoint.endpoint;
                }
            }
            
            const fullUrl = urlPath.startsWith('http') ? urlPath : `${serverUrl}${urlPath}`;
            const res = await fetch(fullUrl, fetchOptions);
            const data = await res.json();
            setResponse(JSON.stringify(data, null, 2));
        } catch (error) {
            setResponse(`Error: ${error.message}`);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="flex h-screen bg-white">
            <Sidebar
                selectedCategory={selectedCategory}
                selectedSubcategory={selectedSubcategory}
                selectedEndpoint={selectedEndpoint}
                onCategorySelect={handleCategorySelect}
                onSubcategorySelect={handleSubcategorySelect}
                onEndpointSelect={handleEndpointSelect}
            />

            <MainContent currentEndpoint={currentEndpoint} />

            <RightPanel
                // State
                roleTokens={roleTokens}
                requestData={requestData}
                loading={loading}
                response={response}
                currentEndpoint={currentEndpoint}
                selectedCategory={selectedCategory}
                
                // Data
                roleCredentials={roleCredentials}
                apiDocs={apiDocs}
                
                // Handlers
                updateRequestField={updateRequestField}
                updateNestedField={updateNestedField}
                handleRoleLogin={handleRoleLogin}
                clearTokenForRole={clearTokenForRole}
                clearAllTokens={clearAllTokens}
                handleTryIt={handleTryIt}
            />
        </div>
    );
};

export default Docs;
