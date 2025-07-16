import React from 'react';
import { apiDocs } from '../../data/apiDocs';
import { CategoryButton, SubcategoryButton, EndpointButton } from './NavigationButtons';

const Sidebar = ({
    selectedCategory,
    selectedSubcategory,
    selectedEndpoint,
    onCategorySelect,
    onSubcategorySelect,
    onEndpointSelect
}) => {
    return (
        <div className="w-80 xl:w-96 bg-gray-50 border-r border-gray-200 overflow-y-auto flex-shrink-0">
            <div className="p-6 border-b border-gray-200">
                <h2 className="text-xl font-bold text-black">API Documentation</h2>
            </div>

            <nav className="p-4">
                {Object.entries(apiDocs).map(([key, category]) => (
                    <div key={key} className="mb-4">
                        <CategoryButton
                            categoryKey={key}
                            category={category}
                            selectedCategory={selectedCategory}
                            onCategorySelect={onCategorySelect}
                        />

                        {selectedCategory === key && (
                            <div className="mt-2 ml-4 space-y-1">
                                {/* Handle subcategories if they exist */}
                                {category.subcategories ? (
                                    Object.entries(category.subcategories).map(([subKey, subcategory]) => (
                                        <div key={subKey}>
                                            <SubcategoryButton
                                                subKey={subKey}
                                                subcategory={subcategory}
                                                selectedSubcategory={selectedSubcategory}
                                                onSubcategorySelect={onSubcategorySelect}
                                            />
                                            
                                            {selectedSubcategory === subKey && (
                                                <div className="ml-4 space-y-1">
                                                    {subcategory.endpoints.map((endpoint, index) => (
                                                        <EndpointButton
                                                            key={index}
                                                            endpoint={endpoint}
                                                            index={index}
                                                            selectedEndpoint={selectedEndpoint}
                                                            onEndpointSelect={onEndpointSelect}
                                                        />
                                                    ))}
                                                </div>
                                            )}
                                        </div>
                                    ))
                                ) : (
                                    /* Handle direct endpoints for categories without subcategories */
                                    category.endpoints && category.endpoints.map((endpoint, index) => (
                                        <EndpointButton
                                            key={index}
                                            endpoint={endpoint}
                                            index={index}
                                            selectedEndpoint={selectedEndpoint}
                                            onEndpointSelect={onEndpointSelect}
                                        />
                                    ))
                                )}
                            </div>
                        )}
                    </div>
                ))}
            </nav>
        </div>
    );
};

export default Sidebar;
