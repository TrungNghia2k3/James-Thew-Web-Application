import React from 'react';
import {
    GlobeAltIcon,
    LockClosedIcon,
    FolderIcon
} from '@heroicons/react/24/outline';

const CategoryButton = ({ 
    categoryKey, 
    category, 
    selectedCategory, 
    onCategorySelect 
}) => {
    const isSelected = selectedCategory === categoryKey;
    
    return (
        <button
            onClick={() => onCategorySelect(categoryKey, category)}
            className={`w-full text-left px-4 py-3 rounded-lg font-semibold transition-colors ${
                isSelected
                    ? 'bg-blue-100 text-blue-800 border border-blue-200'
                    : 'text-gray-700 hover:bg-gray-100'
            }`}
        >
            <div className="flex items-center justify-between">
                <span>{category.title}</span>
                <div className="flex items-center space-x-2">
                    {category.accessLevel === 'public' ? (
                        <span className="px-2 py-1 bg-green-100 text-green-800 rounded-full text-xs font-semibold flex items-center">
                            <GlobeAltIcon className="w-3 h-3 mr-1" />
                            Public
                        </span>
                    ) : (
                        <span className="px-2 py-1 bg-red-100 text-red-800 rounded-full text-xs font-semibold flex items-center">
                            <LockClosedIcon className="w-3 h-3 mr-1" />
                            Private
                        </span>
                    )}
                    {category.role && (
                        <span className="px-2 py-1 bg-purple-100 text-purple-800 rounded-full text-xs font-semibold">
                            {category.role}
                        </span>
                    )}
                </div>
            </div>
        </button>
    );
};

const SubcategoryButton = ({ 
    subKey, 
    subcategory, 
    selectedSubcategory, 
    onSubcategorySelect 
}) => {
    const isSelected = selectedSubcategory === subKey;
    
    return (
        <button
            onClick={() => onSubcategorySelect(subKey)}
            className={`w-full text-left px-3 py-2 text-sm rounded transition-colors mb-1 ${
                isSelected
                    ? 'bg-blue-50 text-blue-700 font-medium'
                    : 'text-gray-600 hover:bg-gray-100'
            }`}
        >
            <div className="flex items-center">
                <FolderIcon className="w-4 h-4 mr-2" />
                {subcategory.title}
            </div>
        </button>
    );
};

const EndpointButton = ({ 
    endpoint, 
    index, 
    selectedEndpoint, 
    onEndpointSelect 
}) => {
    const isSelected = selectedEndpoint === index;
    
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
        <button
            onClick={() => onEndpointSelect(index)}
            className={`w-full text-left px-3 py-2 text-sm rounded transition-colors ${
                isSelected
                    ? 'bg-blue-50 text-blue-700'
                    : 'text-gray-600 hover:bg-gray-100'
            }`}
        >
            <span className={`inline-block px-2 py-1 text-xs rounded mr-2 font-mono ${getMethodColor(endpoint.method)}`}>
                {endpoint.method}
            </span>
            {endpoint.title}
        </button>
    );
};

export { CategoryButton, SubcategoryButton, EndpointButton };
