import React, { useState } from 'react';
import { LightBulbIcon, ExclamationTriangleIcon } from '@heroicons/react/24/outline';

const ValidationErrorDisplay = ({ errors }) => {
    if (!errors || errors.length === 0) return null;
    
    return (
        <div className="mt-2 p-2 bg-red-50 border border-red-200 rounded">
            <div className="flex items-start">
                <ExclamationTriangleIcon className="w-4 h-4 text-red-500 mt-0.5 mr-2 flex-shrink-0" />
                <div className="text-xs text-red-700">
                    {errors.map((error, index) => (
                        <div key={index}>{error}</div>
                    ))}
                </div>
            </div>
        </div>
    );
};

const validateField = (field, value, validation) => {
    const errors = [];
    
    if (!validation) return errors;
    
    // Required validation
    if (validation.required?.includes(field)) {
        if (!value || (typeof value === 'string' && value.trim() === '')) {
            errors.push(`${field} is required`);
            return errors; // Return early if required field is empty
        }
    }
    
    // Skip other validations if field is empty but not required
    if (!value || (typeof value === 'string' && value.trim() === '')) {
        return errors;
    }
    
    // Type validation
    if (validation.type?.[field]) {
        const expectedType = validation.type[field];
        if (expectedType === 'number' && isNaN(Number(value))) {
            errors.push(`${field} must be a number`);
        }
    }
    
    // Min length validation
    if (validation.minLength?.[field] && value.length < validation.minLength[field]) {
        errors.push(`${field} must be at least ${validation.minLength[field]} characters`);
    }
    
    // Max length validation
    if (validation.maxLength?.[field] && value.length > validation.maxLength[field]) {
        errors.push(`${field} must be no more than ${validation.maxLength[field]} characters`);
    }
    
    // Enum validation
    if (validation.enum?.[field] && !validation.enum[field].includes(value)) {
        errors.push(`${field} must be one of: ${validation.enum[field].join(', ')}`);
    }
    
    // Pattern validation
    if (validation.pattern?.[field] && !validation.pattern[field].test(value)) {
        errors.push(`${field} format is invalid`);
    }
    
    return errors;
};

const FormField = ({ 
    field, 
    exampleValue, 
    requestData, 
    currentEndpoint, 
    updateRequestField, 
    updateNestedField,
    onFileChange,
    fieldErrors = []
}) => {
    const validation = currentEndpoint.validation;
    const isRequired = validation?.required?.includes(field);
    const isFile = field === 'image' && currentEndpoint.contentType === 'multipart/form-data';
    
    if (typeof exampleValue === 'object' && !Array.isArray(exampleValue)) {
        // Handle nested objects
        return (
            <div key={field} className="space-y-2">
                <label className="block text-sm font-medium text-gray-700 capitalize">
                    {field.replace(/([A-Z])/g, ' $1').trim()}
                    {isRequired && <span className="text-red-500 ml-1">*</span>}
                </label>
                <div className="ml-4 space-y-2">
                    {Object.entries(exampleValue).map(([nestedField, nestedValue]) => (
                        <div key={nestedField}>
                            <label className="block text-xs text-gray-600 capitalize">
                                {nestedField.replace(/([A-Z])/g, ' $1').trim()}
                            </label>
                            <input
                                type={typeof nestedValue === 'number' ? 'number' : 'text'}
                                value={requestData[field]?.[nestedField] || ''}
                                onChange={(e) => updateNestedField(field, nestedField, typeof nestedValue === 'number' ? Number(e.target.value) : e.target.value)}
                                placeholder={`Enter ${nestedField}`}
                                className="w-full px-3 py-2 border border-gray-300 rounded text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                            />
                        </div>
                    ))}
                </div>
                <ValidationErrorDisplay errors={fieldErrors} />
            </div>
        );
    } else if (Array.isArray(exampleValue)) {
        // Handle arrays
        return (
            <div key={field}>
                <label className="block text-sm font-medium text-gray-700 capitalize mb-1">
                    {field.replace(/([A-Z])/g, ' $1').trim()} (comma-separated)
                    {isRequired && <span className="text-red-500 ml-1">*</span>}
                </label>
                <input
                    type="text"
                    value={Array.isArray(requestData[field]) ? requestData[field].join(', ') : ''}
                    onChange={(e) => updateRequestField(field, e.target.value.split(',').map(item => item.trim()).filter(item => item))}
                    placeholder={`Enter ${field} separated by commas`}
                    className="w-full px-3 py-2 border border-gray-300 rounded text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                />
                <p className="text-xs text-gray-500 mt-1">Example: {exampleValue.join(', ')}</p>
                <ValidationErrorDisplay errors={fieldErrors} />
            </div>
        );
    } else if (isFile) {
        // Handle file upload
        return (
            <div key={field}>
                <label className="block text-sm font-medium text-gray-700 capitalize mb-1">
                    {field.replace(/([A-Z])/g, ' $1').trim()}
                    {isRequired && <span className="text-red-500 ml-1">*</span>}
                </label>
                <input
                    type="file"
                    accept={validation?.fileTypes?.[field] ? validation.fileTypes[field].map(ext => `.${ext}`).join(',') : 'image/*'}
                    onChange={(e) => onFileChange(field, e.target.files[0])}
                    className="w-full px-3 py-2 border border-gray-300 rounded text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent file:mr-4 file:py-2 file:px-4 file:rounded file:border-0 file:text-sm file:font-semibold file:bg-blue-50 file:text-blue-700 hover:file:bg-blue-100"
                />
                <p className="text-xs text-gray-500 mt-1">
                    {validation?.fileTypes?.[field] ? 
                        `Accepted formats: ${validation.fileTypes[field].join(', ')}` : 
                        'Accepted formats: jpg, jpeg, png, gif, webp'}
                </p>
                <ValidationErrorDisplay errors={fieldErrors} />
            </div>
        );
    } else {
        // Handle simple fields with enhanced validation display
        const showEnumOptions = validation?.enum?.[field];
        
        return (
            <div key={field}>
                <label className="block text-sm font-medium text-gray-700 capitalize mb-1">
                    {field.replace(/([A-Z])/g, ' $1').trim()}
                    {isRequired && <span className="text-red-500 ml-1">*</span>}
                    {field === 'id' && currentEndpoint.method !== 'POST' && <span className="text-red-500 ml-1">*</span>}
                </label>
                
                {showEnumOptions ? (
                    <select
                        value={requestData[field] || ''}
                        onChange={(e) => updateRequestField(field, e.target.value)}
                        className="w-full px-3 py-2 border border-gray-300 rounded text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                    >
                        <option value="">Select {field}</option>
                        {validation.enum[field].map(option => (
                            <option key={option} value={option}>{option}</option>
                        ))}
                    </select>
                ) : (
                    <input
                        type={typeof exampleValue === 'number' ? 'number' : 
                              typeof exampleValue === 'boolean' ? 'checkbox' : 
                              field === 'publishedOn' ? 'date' : 'text'}
                        value={typeof exampleValue === 'boolean' ? requestData[field] || false : requestData[field] || ''}
                        checked={typeof exampleValue === 'boolean' ? requestData[field] || false : undefined}
                        onChange={(e) => {
                            const value = typeof exampleValue === 'number' ? Number(e.target.value) :
                                         typeof exampleValue === 'boolean' ? e.target.checked : e.target.value;
                            updateRequestField(field, value);
                        }}
                        placeholder={typeof exampleValue === 'boolean' ? undefined : `Enter ${field}`}
                        className={`w-full px-3 py-2 border border-gray-300 rounded text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent ${
                            typeof exampleValue === 'boolean' ? 'w-4 h-4' : ''
                        }`}
                    />
                )}
                
                {showEnumOptions ? (
                    <p className="text-xs text-gray-500 mt-1">Options: {validation.enum[field].join(', ')}</p>
                ) : (
                    <p className="text-xs text-gray-500 mt-1">Example: {String(exampleValue)}</p>
                )}
                
                <ValidationErrorDisplay errors={fieldErrors} />
            </div>
        );
    }
};

const DynamicRequestForm = ({ 
    currentEndpoint, 
    requestData, 
    updateRequestField, 
    updateNestedField 
}) => {
    const [_files, setFiles] = useState({});
    const [validationErrors, setValidationErrors] = useState({});
    
    if (!currentEndpoint?.requestBody || !['POST', 'PUT', 'DELETE'].includes(currentEndpoint.method)) {
        return null;
    }

    const handleFileChange = (field, file) => {
        setFiles(prev => ({ ...prev, [field]: file }));
        // Update request data with file name for display
        updateRequestField(field, file ? file.name : '');
        
        // Validate file
        if (file && currentEndpoint.validation?.fileTypes?.[field]) {
            const fileExtension = file.name.split('.').pop()?.toLowerCase();
            const allowedTypes = currentEndpoint.validation.fileTypes[field];
            if (!allowedTypes.includes(fileExtension)) {
                setValidationErrors(prev => ({
                    ...prev,
                    [field]: [`File type must be one of: ${allowedTypes.join(', ')}`]
                }));
            } else {
                setValidationErrors(prev => {
                    const newErrors = { ...prev };
                    delete newErrors[field];
                    return newErrors;
                });
            }
        }
    };

    const handleFieldChange = (field, value) => {
        updateRequestField(field, value);
        
        // Real-time validation
        if (currentEndpoint.validation) {
            const errors = validateField(field, value, currentEndpoint.validation);
            setValidationErrors(prev => ({
                ...prev,
                [field]: errors.length > 0 ? errors : undefined
            }));
        }
    };

    const getMethodTip = (method) => {
        const tips = {
            'POST': ' do not include an ID as it will be generated by the server.',
            'PUT': ' make sure the ID exists in your database.',
            'DELETE': ' make sure the ID exists and can be deleted.'
        };
        return tips[method] || '';
    };

    const getContentTypeTip = () => {
        if (currentEndpoint.contentType === 'multipart/form-data') {
            return ' This endpoint uses form-data for file uploads. Files will be sent as binary data.';
        }
        return ' This endpoint accepts JSON data.';
    };

    // Enhanced updateRequestField that includes validation
    const enhancedUpdateRequestField = (field, value) => {
        handleFieldChange(field, value);
    };

    return (
        <div className="mb-6 p-4 bg-gray-50 border border-gray-200 rounded-lg">
            <h4 className="text-sm font-semibold text-gray-800 mb-3">
                Request Body
                {currentEndpoint.contentType === 'multipart/form-data' && (
                    <span className="ml-2 px-2 py-1 bg-orange-100 text-orange-800 rounded text-xs font-medium">
                        Form Data
                    </span>
                )}
            </h4>
            <div className="space-y-3">
                {Object.entries(currentEndpoint.requestBody).map(([field, exampleValue]) => (
                    <FormField
                        key={field}
                        field={field}
                        exampleValue={exampleValue}
                        requestData={requestData}
                        currentEndpoint={currentEndpoint}
                        updateRequestField={enhancedUpdateRequestField}
                        updateNestedField={updateNestedField}
                        onFileChange={handleFileChange}
                        fieldErrors={validationErrors[field]}
                    />
                ))}
            </div>
            
            {/* Validation Summary */}
            {Object.keys(validationErrors).length > 0 && (
                <div className="mt-4 p-3 bg-red-50 border border-red-200 rounded">
                    <h5 className="text-sm font-medium text-red-800 mb-2">Validation Errors:</h5>
                    <ul className="text-xs text-red-700 space-y-1">
                        {Object.entries(validationErrors).map(([field, errors]) => 
                            errors?.map((error, index) => (
                                <li key={`${field}-${index}`}>• {error}</li>
                            ))
                        )}
                    </ul>
                </div>
            )}
            
            <div className="mt-4 p-3 bg-blue-50 border border-blue-200 rounded">
                <p className="text-xs text-blue-700">
                    <LightBulbIcon className="w-3 h-3 inline mr-1" />
                    Fill in the fields above with your actual data. For {currentEndpoint.method} requests,
                    {getMethodTip(currentEndpoint.method)}
                    {getContentTypeTip()}
                </p>
            </div>
        </div>
    );
};

export default DynamicRequestForm;
