export const writerEndpoints = {
  title: 'Writer Endpoints',
  description: 'Content creation functions - requires WRITER role',
  accessLevel: 'private',
  role: 'WRITER',
  subcategories: {
    articles: {
      title: 'Article Management',
      description: 'Create and manage articles',
      endpoints: [
        {
          title: 'Get Articles',
          method: 'GET',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/writer/articles',
          description: 'Retrieve your submitted articles',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'WRITER',
          permissions: [],
          parameters: [
            { name: 'status', type: 'string', description: 'Filter by article status', required: false },
            { name: 'limit', type: 'number', description: 'Number of articles to return', required: false }
          ],
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/protected/writer/articles?status=draft', {
  method: 'GET',
  headers: {
    'Authorization': 'Bearer <token>',
    'Content-Type': 'application/json'
  }
})
.then(res => res.json())
.then(console.log);`,
          sampleResponse: `{
  "success": true,
  "articles": [
    {
      "id": 567,
      "title": "How to Build Amazing Web Applications",
      "category": "Technology",
      "tags": ["web development", "react", "tutorial"],
      "status": "draft",
      "wordCount": 2500,
      "submittedAt": "2024-03-15T16:20:00Z"
    }
  ]
}`
        },
        {
          title: 'Submit Article',
          method: 'POST',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/writer/articles',
          description: 'Submit a new article for review and publication',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'WRITER',
          permissions: [],
          requestBody: {
            title: "How to Build Amazing Web Applications",
            content: "Article content goes here...",
            category: "Technology",
            tags: ["web development", "react", "tutorial"],
            status: "draft"
          },
          parameters: [],
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/protected/writer/articles', {
  method: 'POST',
  headers: {
    'Authorization': 'Bearer <token>',
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    title: "How to Build Amazing Web Applications",
    content: "Article content goes here...",
    category: "Technology",
    tags: ["web development", "react", "tutorial"],
    status: "draft"
  })
})
.then(res => res.json())
.then(console.log);`,
          sampleResponse: `{
  "success": true,
  "message": "Article submitted successfully",
  "articleId": 567,
  "status": "pending_review",
  "submittedAt": "2024-03-15T16:20:00Z"
}`
        },
        {
          title: 'Update Article',
          method: 'PUT',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/writer/articles',
          description: 'Update an existing article',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'WRITER',
          permissions: [],
          requestBody: {
            id: 567,
            title: "How to Build Amazing Web Applications - Updated",
            content: "Updated article content with more examples...",
            category: "Technology",
            tags: ["web development", "react", "tutorial", "javascript"],
            status: "draft"
          },
          parameters: [],
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/protected/writer/articles', {
  method: 'PUT',
  headers: {
    'Authorization': 'Bearer <token>',
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    id: 567,
    title: "How to Build Amazing Web Applications - Updated",
    content: "Updated article content with more examples...",
    category: "Technology",
    tags: ["web development", "react", "tutorial", "javascript"],
    status: "draft"
  })
})
.then(res => res.json())
.then(console.log);`,
          sampleResponse: `{
  "success": true,
  "message": "Article updated successfully",
  "article": {
    "id": 567,
    "title": "How to Build Amazing Web Applications - Updated",
    "status": "draft",
    "updated": "2024-03-15T17:00:00Z"
  }
}`
        },
        {
          title: 'Delete Article',
          method: 'DELETE',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/writer/articles',
          description: 'Delete an article (only drafts can be deleted)',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'WRITER',
          permissions: [],
          requestBody: {
            id: 567
          },
          parameters: [],
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/protected/writer/articles', {
  method: 'DELETE',
  headers: {
    'Authorization': 'Bearer <token>',
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    id: 567
  })
})
.then(res => res.json())
.then(console.log);`,
          sampleResponse: `{
  "success": true,
  "message": "Article deleted successfully",
  "deletedArticleId": 567
}`
        }
      ]
    },
    recipes: {
      title: 'Recipe Management',
      description: 'Create and manage recipe content',
      endpoints: [
        {
          title: 'Get My Recipes',
          method: 'GET',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/writer/recipes',
          description: 'Retrieve your submitted recipes',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'WRITER',
          permissions: [],
          parameters: [
            { name: 'status', type: 'string', description: 'Filter by recipe status', required: false },
            { name: 'page', type: 'number', description: 'Page number for pagination', required: false }
          ],
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/protected/writer/recipes?status=draft', {
  method: 'GET',
  headers: {
    'Authorization': 'Bearer <token>',
    'Content-Type': 'application/json'
  }
})
.then(res => res.json())
.then(console.log);`,
          sampleResponse: `{
  "success": true,
  "recipes": [
    {
      "id": 234,
      "name": "Ultimate Chocolate Cake",
      "category": "Dessert",
      "area": "American",
      "status": "draft",
      "accessType": "PREMIUM",
      "submittedAt": "2024-03-15T16:20:00Z"
    }
  ]
}`
        },
        {
          title: 'Submit Recipe',
          method: 'POST',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/writer/recipes',
          description: 'Submit a new recipe for review',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'WRITER',
          permissions: [],
          requestBody: {
            name: "Ultimate Chocolate Cake",
            category: "Dessert",
            area: "American",
            instructions: "Detailed cooking instructions...",
            ingredients: "List of ingredients...",
            prepareTime: "30 minutes",
            cookingTime: "45 minutes",
            yield: "8 servings",
            accessType: "PREMIUM"
          },
          parameters: [],
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/protected/writer/recipes', {
  method: 'POST',
  headers: {
    'Authorization': 'Bearer <token>',
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    name: "Ultimate Chocolate Cake",
    category: "Dessert",
    area: "American",
    instructions: "Detailed cooking instructions...",
    ingredients: "List of ingredients...",
    prepareTime: "30 minutes",
    cookingTime: "45 minutes",
    yield: "8 servings",
    accessType: "PREMIUM"
  })
})
.then(res => res.json())
.then(console.log);`,
          sampleResponse: `{
  "success": true,
  "message": "Recipe submitted successfully",
  "recipeId": 234,
  "status": "pending_review",
  "submittedAt": "2024-03-15T16:20:00Z"
}`
        },
        {
          title: 'Update Recipe',
          method: 'PUT',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/writer/recipes',
          description: 'Update an existing recipe',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'WRITER',
          permissions: [],
          requestBody: {
            id: 234,
            name: "Ultimate Chocolate Cake - Improved",
            instructions: "Updated cooking instructions with better techniques...",
            ingredients: "Updated ingredients list...",
            prepareTime: "25 minutes"
          },
          parameters: [],
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/protected/writer/recipes', {
  method: 'PUT',
  headers: {
    'Authorization': 'Bearer <token>',
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    id: 234,
    name: "Ultimate Chocolate Cake - Improved",
    instructions: "Updated cooking instructions with better techniques...",
    ingredients: "Updated ingredients list...",
    prepareTime: "25 minutes"
  })
})
.then(res => res.json())
.then(console.log);`,
          sampleResponse: `{
  "success": true,
  "message": "Recipe updated successfully",
  "recipe": {
    "id": 234,
    "name": "Ultimate Chocolate Cake - Improved",
    "status": "draft",
    "updated": "2024-03-15T17:00:00Z"
  }
}`
        }
      ]
    }
  }
};
