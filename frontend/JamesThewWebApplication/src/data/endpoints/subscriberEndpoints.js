export const subscriberEndpoints = {
  title: 'Subscriber Endpoints',
  description: 'Premium content access - requires SUBSCRIBER role',
  accessLevel: 'private',
  role: 'SUBSCRIBER',
  subcategories: {
    contestEntry: {
      title: 'Contest Entry Management',
      description: 'Manage your contest entries and submissions',
      endpoints: [
        {
          title: 'Get Contest Entries',
          method: 'GET',
          url: '/protected/subscriber/contest-entry',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/subscriber/contest-entry',
          description: 'Retrieve contest entries by ID, contestId+userId, or userId',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'SUBSCRIBER',
          permissions: [],
          parameters: [
            { name: 'id', type: 'number', description: 'Get specific contest entry by ID', required: false },
            { name: 'contestId', type: 'number', description: 'Contest ID (use with userId)', required: false },
            { name: 'userId', type: 'number', description: 'User ID to get their contest entries', required: false }
          ],
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/protected/subscriber/contest-entry?userId=123', {
  method: 'GET',
  headers: {
    'Authorization': 'Bearer <token>',
    'Content-Type': 'application/json'
  }
})
.then(res => res.json())
.then(console.log);`,
          sampleResponse: `{
  "status": 200,
  "message": "User's contest entries fetched successfully",
  "data": [
    {
      "id": 4,
      "contestId": 1,
      "userId": 123,
      "name": "Mi Quang",
      "ingredients": "pork, basil, mint, and cilantro",
      "image": "http://localhost:8080/JamesThewWebApplication/api/images/contest-entries/mi-quang-1752554674772.jpg",
      "prepareTime": "30",
      "cookingTime": "30",
      "yield": "30",
      "category": "Breakfast",
      "area": "Vietnamese",
      "shortDescription": "Mì Quảng, a popular Vietnamese noodle dish",
      "contestEntryInstructions": [
        {
          "step": 1,
          "instruction": "Prepare the broth with traditional spices"
        },
        {
          "step": 2,
          "instruction": "Cook noodles until perfect texture"
        }
      ],
      "dateCreated": "2025-07-15",
      "dateModified": "2025-07-15",
      "status": "PENDING"
    }
  ]
}`
        },
        {
          title: 'Create Contest Entry',
          method: 'POST',
          url: '/protected/subscriber/contest-entry',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/subscriber/contest-entry',
          description: 'Create a new contest entry with image upload',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'SUBSCRIBER',
          permissions: [],
          contentType: 'multipart/form-data',
          requestBody: {
            contestId: "1",
            userId: "123",
            name: "Mi Quang",
            ingredients: "pork, basil, mint, and cilantro",
            prepareTime: "30",
            cookingTime: "30",
            yield: "30",
            category: "Breakfast",
            area: "Vietnamese",
            shortDescription: "Mì Quảng, a popular Vietnamese noodle dish",
            contestEntryInstructions: '[{"step":1,"instruction":"Prepare the broth with traditional spices"},{"step":2,"instruction":"Cook noodles until perfect texture"}]',
            image: "mi-quang.jpg"
          },
          validation: {
            required: ['contestId', 'userId', 'name', 'ingredients', 'prepareTime', 'cookingTime', 'yield', 'category', 'area', 'shortDescription', 'contestEntryInstructions', 'image'],
            type: {
              contestId: 'number',
              userId: 'number',
              contestEntryInstructions: 'json'
            },
            minLength: {
              name: 3,
              ingredients: 10,
              shortDescription: 20
            },
            maxLength: {
              name: 100,
              ingredients: 1000,
              shortDescription: 500
            },
            enum: {
              category: ['Beef', 'Chicken', 'Dessert', 'Lamb', 'Miscellaneous', 'Pasta', 'Pork', 'Seafood', 'Side', 'Starter', 'Vegan', 'Vegetarian', 'Breakfast'],
              area: ['American', 'British', 'Canadian', 'Chinese', 'Croatian', 'Dutch', 'Egyptian', 'French', 'Greek', 'Indian', 'Irish', 'Italian', 'Jamaican', 'Japanese', 'Kenyan', 'Malaysian', 'Mexican', 'Moroccan', 'Polish', 'Portuguese', 'Russian', 'Spanish', 'Thai', 'Tunisian', 'Turkish', 'Unknown', 'Vietnamese']
            },
            pattern: {
              prepareTime: /^\d+$/,
              cookingTime: /^\d+$/,
              yield: /^\d+$/
            },
            fileTypes: {
              image: ['jpg', 'jpeg', 'png', 'gif', 'webp']
            }
          },
          parameters: [],
          sampleRequest: `const formData = new FormData();
formData.append('contestId', '1');
formData.append('userId', '123');
formData.append('name', 'Mi Quang');
formData.append('ingredients', 'pork, basil, mint, and cilantro');
formData.append('prepareTime', '30');
formData.append('cookingTime', '30');
formData.append('yield', '30');
formData.append('category', 'Breakfast');
formData.append('area', 'Vietnamese');
formData.append('shortDescription', 'Mì Quảng, a popular Vietnamese noodle dish');
formData.append('contestEntryInstructions', JSON.stringify([
  {"step":1,"instruction":"Prepare the broth with traditional spices"},
  {"step":2,"instruction":"Cook noodles until perfect texture"}
]));
formData.append('image', fileInput.files[0]); // Required file

fetch('http://localhost:8080/JamesThewWebApplication/api/protected/subscriber/contest-entry', {
  method: 'POST',
  headers: {
    'Authorization': 'Bearer <token>'
    // Note: Do not set Content-Type header for FormData
  },
  body: formData
})
.then(res => res.json())
.then(console.log);`,
          sampleResponse: `{
  "status": 200,
  "message": "Contest entry created successfully",
  "data": null
}`
        },
        {
          title: 'Update Contest Entry',
          method: 'PUT',
          url: '/protected/subscriber/contest-entry',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/subscriber/contest-entry',
          description: 'Update an existing contest entry with optional image upload',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'SUBSCRIBER',
          permissions: [],
          contentType: 'multipart/form-data',
          requestBody: {
            id: "4",
            contestId: "1",
            userId: "123",
            name: "Mi Quang Deluxe",
            ingredients: "pork, shrimp, basil, mint, cilantro, and quail eggs",
            prepareTime: "35",
            cookingTime: "35",
            yield: "4",
            category: "Breakfast",
            area: "Vietnamese",
            shortDescription: "Enhanced Mì Quảng with premium ingredients",
            contestEntryInstructions: '[{"step":1,"instruction":"Prepare the enhanced broth with premium spices"},{"step":2,"instruction":"Add premium toppings and garnish"}]'
          },
          validation: {
            required: ['id', 'contestId', 'userId', 'name', 'ingredients', 'prepareTime', 'cookingTime', 'yield', 'category', 'area', 'shortDescription', 'contestEntryInstructions'],
            optional: ['image'],
            type: {
              id: 'number',
              contestId: 'number',
              userId: 'number',
              contestEntryInstructions: 'json'
            },
            minLength: {
              name: 3,
              ingredients: 10,
              shortDescription: 20
            },
            maxLength: {
              name: 100,
              ingredients: 1000,
              shortDescription: 500
            },
            enum: {
              category: ['Beef', 'Chicken', 'Dessert', 'Lamb', 'Miscellaneous', 'Pasta', 'Pork', 'Seafood', 'Side', 'Starter', 'Vegan', 'Vegetarian', 'Breakfast'],
              area: ['American', 'British', 'Canadian', 'Chinese', 'Croatian', 'Dutch', 'Egyptian', 'French', 'Greek', 'Indian', 'Irish', 'Italian', 'Jamaican', 'Japanese', 'Kenyan', 'Malaysian', 'Mexican', 'Moroccan', 'Polish', 'Portuguese', 'Russian', 'Spanish', 'Thai', 'Tunisian', 'Turkish', 'Unknown', 'Vietnamese']
            },
            pattern: {
              prepareTime: /^\d+$/,
              cookingTime: /^\d+$/,
              yield: /^\d+$/
            },
            fileTypes: {
              image: ['jpg', 'jpeg', 'png', 'gif', 'webp']
            }
          },
          parameters: [],
          sampleRequest: `const formData = new FormData();
formData.append('id', '4');
formData.append('contestId', '1');
formData.append('userId', '123');
formData.append('name', 'Mi Quang Deluxe');
formData.append('ingredients', 'pork, shrimp, basil, mint, cilantro, and quail eggs');
formData.append('prepareTime', '35');
formData.append('cookingTime', '35');
formData.append('yield', '4');
formData.append('category', 'Breakfast');
formData.append('area', 'Vietnamese');
formData.append('shortDescription', 'Enhanced Mì Quảng with premium ingredients');
formData.append('contestEntryInstructions', JSON.stringify([
  {"step":1,"instruction":"Prepare the enhanced broth with premium spices"},
  {"step":2,"instruction":"Add premium toppings and garnish"}
]));
// formData.append('image', fileInput.files[0]); // Optional: new image file

fetch('http://localhost:8080/JamesThewWebApplication/api/protected/subscriber/contest-entry', {
  method: 'PUT',
  headers: {
    'Authorization': 'Bearer <token>'
    // Note: Do not set Content-Type header for FormData
  },
  body: formData
})
.then(res => res.json())
.then(console.log);`,
          sampleResponse: `{
  "status": 200,
  "message": "Contest entry updated successfully",
  "data": null
}`
        },
        {
          title: 'Delete Contest Entry',
          method: 'DELETE',
          url: '/protected/subscriber/contest-entry',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/subscriber/contest-entry',
          description: 'Delete a contest entry',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'SUBSCRIBER',
          permissions: [],
          requestBody: {
            id: 4,
            userId: 123,
            contestId: 1
          },
          validation: {
            required: ['id', 'userId', 'contestId'],
            type: {
              id: 'number',
              userId: 'number',
              contestId: 'number'
            }
          },
          parameters: [],
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/protected/subscriber/contest-entry', {
  method: 'DELETE',
  headers: {
    'Authorization': 'Bearer <token>',
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    id: 4,
    userId: 123,
    contestId: 1
  })
})
.then(res => res.json())
.then(console.log);`,
          sampleResponse: `{
  "status": 200,
  "message": "Contest entry deleted successfully"
}`
        }
      ]
    },
    recipes: {
      title: 'Recipe Access',
      description: 'Access and browse all recipes with subscriber privileges',
      endpoints: [
        {
          title: 'Get Recipe by ID',
          method: 'GET',
          url: '/protected/subscriber/recipes',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/subscriber/recipes',
          description: 'Retrieve a specific recipe by its ID',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'SUBSCRIBER',
          permissions: [],
          parameters: [
            { name: 'id', type: 'number', description: 'The ID of the recipe', required: true }
          ],
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/protected/subscriber/recipes?id=125', {
  method: 'GET',
  headers: {
    'Authorization': 'Bearer <token>',
    'Content-Type': 'application/json'
  }
})
.then(res => res.json())
.then(console.log);`,
          sampleResponse: `{
  "status": 200,
  "message": "Recipe found",
  "data": {
    "id": 125,
    "name": "Master Chef's Secret Beef Wellington",
    "category": "Beef",
    "area": "British",
    "instructions": "Exclusive premium recipe instructions with professional techniques...",
    "image": "http://localhost:8080/JamesThewWebApplication/api/images/recipes/beef-wellington.jpg",
    "ingredients": "2 lbs beef tenderloin, 1 sheet puff pastry, 8 oz pâté...",
    "publishedOn": "2024-01-15",
    "recipedBy": 32,
    "prepareTime": "60 minutes",
    "cookingTime": "45 minutes",
    "yield": "6 servings",
    "shortDescription": "Professional-grade beef wellington with master chef techniques",
    "accessType": "PREMIUM",
    "comments": [],
    "detailedInstructions": [
      {
        "step": 1,
        "title": "Prepare the beef",
        "description": "Season and sear the beef tenderloin...",
        "tips": "Professional chef tips for perfect searing"
      }
    ]
  }
}`
        },
        {
          title: 'Get Recipes with Pagination',
          method: 'GET',
          url: '/protected/subscriber/recipes',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/subscriber/recipes',
          description: 'Retrieve recipes with pagination and filtering options',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'SUBSCRIBER',
          permissions: [],
          parameters: [
            { name: 'page', type: 'number', description: 'Page number (default: 1, min: 1)', required: false },
            { name: 'size', type: 'number', description: 'Items per page (default: 10, min: 1)', required: false },
            { name: 'area', type: 'string', description: 'Filter by area/cuisine', required: false },
            { name: 'category', type: 'string', description: 'Filter by category', required: false },
            { name: 'userId', type: 'number', description: 'Filter by user ID', required: false }
          ],
          validation: {
            type: {
              page: 'number',
              size: 'number',
              userId: 'number'
            },
            range: {
              page: { min: 1 },
              size: { min: 1 }
            },
            enum: {
              category: ['Beef', 'Chicken', 'Dessert', 'Lamb', 'Miscellaneous', 'Pasta', 'Pork', 'Seafood', 'Side', 'Starter', 'Vegan', 'Vegetarian', 'Breakfast'],
              area: ['American', 'British', 'Canadian', 'Chinese', 'Croatian', 'Dutch', 'Egyptian', 'French', 'Greek', 'Indian', 'Irish', 'Italian', 'Jamaican', 'Japanese', 'Kenyan', 'Malaysian', 'Mexican', 'Moroccan', 'Polish', 'Portuguese', 'Russian', 'Spanish', 'Thai', 'Tunisian', 'Turkish', 'Unknown', 'Vietnamese']
            }
          },
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/protected/subscriber/recipes?page=1&size=10&category=Beef', {
  method: 'GET',
  headers: {
    'Authorization': 'Bearer <token>',
    'Content-Type': 'application/json'
  }
})
.then(res => res.json())
.then(console.log);`,
          sampleResponse: `{
  "status": 200,
  "message": "Recipes by category fetched successfully",
  "data": {
    "recipes": [
      {
        "id": 125,
        "name": "Master Chef's Secret Beef Wellington",
        "category": "Beef",
        "area": "British",
        "instructions": "Professional beef wellington preparation...",
        "image": "http://localhost:8080/JamesThewWebApplication/api/images/recipes/beef-wellington.jpg",
        "ingredients": "2 lbs beef tenderloin, 1 sheet puff pastry...",
        "publishedOn": "2024-01-15",
        "recipedBy": 32,
        "prepareTime": "60 minutes",
        "cookingTime": "45 minutes",
        "yield": "6 servings",
        "shortDescription": "Professional-grade beef wellington",
        "accessType": "PREMIUM"
      },
      {
        "id": 126,
        "name": "Classic Beef Bourguignon",
        "category": "Beef",
        "area": "French",
        "instructions": "Traditional French slow-cooked beef...",
        "image": "http://localhost:8080/JamesThewWebApplication/api/images/recipes/beef-bourguignon.jpg",
        "ingredients": "3 lbs beef chuck, red wine, carrots...",
        "publishedOn": "2024-01-20",
        "recipedBy": 45,
        "prepareTime": "30 minutes",
        "cookingTime": "180 minutes",
        "yield": "8 servings",
        "shortDescription": "Classic French beef stew",
        "accessType": "FREE"
      }
    ],
    "totalItems": 150,
    "currentPage": 1,
    "totalPages": 15
  }
}`
        }
      ]
    },
    premiumRecipes: {
      title: 'Premium Recipes',
      description: 'Access premium recipe content',
      endpoints: [
        {
          title: 'Get Premium Recipes',
          method: 'GET',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/subscriber/recipes/premium',
          description: 'Retrieve premium recipes available to subscribers',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'SUBSCRIBER',
          permissions: [],
          parameters: [
            { name: 'page', type: 'number', description: 'Page number for pagination', required: false },
            { name: 'size', type: 'number', description: 'Number of recipes per page', required: false },
            { name: 'category', type: 'string', description: 'Filter by recipe category', required: false }
          ],
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/protected/subscriber/recipes/premium?page=1&size=10', {
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
  "status": 200,
  "message": "Premium recipes fetched successfully",
  "data": {
    "recipes": [
      {
        "id": 125,
        "name": "Master Chef's Secret Beef Wellington",
        "category": "Beef",
        "area": "British",
        "instructions": "Exclusive premium recipe instructions...",
        "ingredients": "Premium ingredients list...",
        "prepareTime": "60 minutes",
        "cookingTime": "45 minutes",
        "yield": "6 servings",
        "accessType": "PREMIUM",
        "chefRating": 5,
        "difficultyLevel": "Advanced"
      }
    ],
    "currentPage": 1,
    "totalPages": 25,
    "totalItems": 248
  }
}`
        },
        {
          title: 'Get Premium Recipe by ID',
          method: 'GET',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/subscriber/recipes/premium',
          description: 'Retrieve a specific premium recipe with full details',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'SUBSCRIBER',
          permissions: [],
          parameters: [
            { name: 'id', type: 'number', description: 'The ID of the premium recipe', required: true }
          ],
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/protected/subscriber/recipes/premium?id=125', {
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
  "status": 200,
  "message": "Premium recipe found",
  "data": {
    "id": 125,
    "name": "Master Chef's Secret Beef Wellington",
    "category": "Beef",
    "area": "British",
    "instructions": "Exclusive premium recipe instructions with professional techniques...",
    "ingredients": "2 lbs beef tenderloin, 1 sheet puff pastry, 8 oz pâté...",
    "prepareTime": "60 minutes",
    "cookingTime": "45 minutes",
    "yield": "6 servings",
    "accessType": "PREMIUM",
    "chefRating": 5,
    "difficultyLevel": "Advanced",
    "nutrition": {
      "calories": "650 calories per serving",
      "protein": "45g",
      "carbs": "25g",
      "fat": "38g"
    },
    "detailedInstructions": [
      {
        "step": 1,
        "title": "Prepare the beef",
        "description": "Season and sear the beef tenderloin...",
        "image": "premium-wellington-step1.jpg",
        "tips": "Professional chef tips for perfect searing"
      }
    ],
    "videoTutorial": "http://localhost:8080/JamesThewWebApplication/api/videos/premium/wellington-tutorial.mp4"
  }
}`
        },
        {
          title: 'Save Recipe to Favorites',
          method: 'POST',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/subscriber/favorites',
          description: 'Save a recipe to your personal favorites collection',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'SUBSCRIBER',
          permissions: [],
          requestBody: {
            recipeId: 125,
            notes: "Perfect for special occasions"
          },
          parameters: [],
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/protected/subscriber/favorites', {
  method: 'POST',
  headers: {
    'Authorization': 'Bearer <token>',
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    recipeId: 125,
    notes: "Perfect for special occasions"
  })
})
.then(res => res.json())
.then(console.log);`,
          sampleResponse: `{
  "success": true,
  "message": "Recipe added to favorites",
  "favorite": {
    "id": 456,
    "recipeId": 125,
    "notes": "Perfect for special occasions",
    "addedAt": "2024-03-15T10:30:00Z"
  }
}`
        }
      ]
    },
    exclusiveContent: {
      title: 'Exclusive Content',
      description: 'Access subscriber-only content and features',
      endpoints: [
        {
          title: 'Get Exclusive Articles',
          method: 'GET',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/subscriber/articles/exclusive',
          description: 'Access subscriber-only articles and content',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'SUBSCRIBER',
          permissions: [],
          parameters: [
            { name: 'category', type: 'string', description: 'Filter by article category', required: false },
            { name: 'page', type: 'number', description: 'Page number for pagination', required: false }
          ],
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/protected/subscriber/articles/exclusive?category=cooking-tips', {
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
      "id": 789,
      "title": "Professional Kitchen Secrets: 10 Techniques Every Home Cook Should Know",
      "category": "cooking-tips",
      "author": "Chef Marcus Williams",
      "publishedAt": "2024-03-10T10:30:00Z",
      "readTime": "8 minutes",
      "premium": true,
      "excerpt": "Discover the professional techniques that will transform your cooking..."
    }
  ]
}`
        },
        {
          title: 'Get Meal Plans',
          method: 'GET',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/subscriber/meal-plans',
          description: 'Access personalized meal planning features',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'SUBSCRIBER',
          permissions: [],
          parameters: [
            { name: 'week', type: 'string', description: 'Week for meal plan (YYYY-MM-DD)', required: false },
            { name: 'dietary', type: 'string', description: 'Dietary restrictions filter', required: false }
          ],
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/protected/subscriber/meal-plans?week=2024-03-18', {
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
  "mealPlan": {
    "week": "2024-03-18",
    "days": [
      {
        "date": "2024-03-18",
        "breakfast": {
          "recipeId": 567,
          "recipeName": "Protein-Packed Pancakes",
          "calories": 450
        },
        "lunch": {
          "recipeId": 568,
          "recipeName": "Mediterranean Quinoa Bowl",
          "calories": 520
        },
        "dinner": {
          "recipeId": 569,
          "recipeName": "Herb-Crusted Salmon",
          "calories": 680
        }
      }
    ],
    "shoppingList": [
      "2 lbs salmon fillets",
      "1 cup quinoa",
      "Fresh herbs (rosemary, thyme)"
    ]
  }
}`
        }
      ]
    },
    personalizedFeatures: {
      title: 'Personalized Features',
      description: 'Personal recipe collections and preferences',
      endpoints: [
        {
          title: 'Get My Favorites',
          method: 'GET',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/subscriber/favorites',
          description: 'Retrieve your saved favorite recipes',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'SUBSCRIBER',
          permissions: [],
          parameters: [
            { name: 'page', type: 'number', description: 'Page number for pagination', required: false },
            { name: 'category', type: 'string', description: 'Filter by recipe category', required: false }
          ],
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/protected/subscriber/favorites?page=1', {
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
  "favorites": [
    {
      "id": 456,
      "recipe": {
        "id": 125,
        "name": "Master Chef's Secret Beef Wellington",
        "category": "Beef",
        "accessType": "PREMIUM"
      },
      "notes": "Perfect for special occasions",
      "addedAt": "2024-03-15T10:30:00Z"
    }
  ],
  "totalCount": 15
}`
        },
        {
          title: 'Update Recipe Rating',
          method: 'POST',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/subscriber/recipes/rate',
          description: 'Rate and review a recipe',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'SUBSCRIBER',
          permissions: [],
          requestBody: {
            recipeId: 125,
            rating: 5,
            review: "Absolutely fantastic! The best beef wellington I've ever made."
          },
          parameters: [],
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/protected/subscriber/recipes/rate', {
  method: 'POST',
  headers: {
    'Authorization': 'Bearer <token>',
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    recipeId: 125,
    rating: 5,
    review: "Absolutely fantastic! The best beef wellington I've ever made."
  })
})
.then(res => res.json())
.then(console.log);`,
          sampleResponse: `{
  "success": true,
  "message": "Rating submitted successfully",
  "rating": {
    "id": 789,
    "recipeId": 125,
    "rating": 5,
    "review": "Absolutely fantastic! The best beef wellington I've ever made.",
    "submittedAt": "2024-03-15T10:30:00Z"
  }
}`
        }
      ]
    }
  }
};
