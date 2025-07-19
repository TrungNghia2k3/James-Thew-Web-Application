export const generalEndpoints = {
  title: 'General User Endpoints',
  description: 'Standard user functions - requires GENERAL role',
  accessLevel: 'private',
  role: 'GENERAL',
  subcategories: {
    profile: {
      title: 'Profile Management',
      description: 'Manage your user profile and settings',
      endpoints: [
        {
          title: 'Get My Profile',
          method: 'GET',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/general/profile',
          description: 'Retrieve your user profile information',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'GENERAL',
          permissions: [],
          parameters: [],
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/protected/general/profile', {
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
  "profile": {
    "id": 123,
    "username": "cookingfan2024",
    "email": "user@example.com",
    "firstName": "John",
    "lastName": "Doe",
    "role": "GENERAL",
    "joinedAt": "2024-01-15T10:30:00Z",
    "preferences": {
      "dietaryRestrictions": ["vegetarian"],
      "favoriteCuisines": ["Italian", "Mexican"],
      "skillLevel": "intermediate"
    },
    "stats": {
      "recipesViewed": 45,
      "favoritesCount": 12,
      "commentsCount": 8
    }
  }
}`
        },
        {
          title: 'Update Profile',
          method: 'PUT',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/general/profile',
          description: 'Update your profile information',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'GENERAL',
          permissions: [],
          requestBody: {
            firstName: "John",
            lastName: "Smith",
            preferences: {
              dietaryRestrictions: ["vegetarian", "gluten-free"],
              favoriteCuisines: ["Italian", "Mexican", "Thai"],
              skillLevel: "advanced"
            }
          },
          parameters: [],
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/protected/general/profile', {
  method: 'PUT',
  headers: {
    'Authorization': 'Bearer <token>',
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    firstName: "John",
    lastName: "Smith",
    preferences: {
      dietaryRestrictions: ["vegetarian", "gluten-free"],
      favoriteCuisines: ["Italian", "Mexican", "Thai"],
      skillLevel: "advanced"
    }
  })
})
.then(res => res.json())
.then(console.log);`,
          sampleResponse: `{
  "success": true,
  "message": "Profile updated successfully",
  "profile": {
    "id": 123,
    "firstName": "John",
    "lastName": "Smith",
    "preferences": {
      "dietaryRestrictions": ["vegetarian", "gluten-free"],
      "favoriteCuisines": ["Italian", "Mexican", "Thai"],
      "skillLevel": "advanced"
    },
    "updatedAt": "2024-03-15T10:30:00Z"
  }
}`
        },
        {
          title: 'Change Password',
          method: 'PUT',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/general/profile/password',
          description: 'Change your account password',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'GENERAL',
          permissions: [],
          requestBody: {
            currentPassword: "oldPassword123",
            newPassword: "newSecurePassword456"
          },
          parameters: [],
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/protected/general/profile/password', {
  method: 'PUT',
  headers: {
    'Authorization': 'Bearer <token>',
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    currentPassword: "oldPassword123",
    newPassword: "newSecurePassword456"
  })
})
.then(res => res.json())
.then(console.log);`,
          sampleResponse: `{
  "success": true,
  "message": "Password changed successfully",
  "timestamp": "2024-03-15T10:30:00Z"
}`
        }
      ]
    },
    basicRecipes: {
      title: 'Basic Recipe Access',
      description: 'Access and interact with free recipes',
      endpoints: [
        {
          title: 'Get My Recipe History',
          method: 'GET',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/general/recipes/history',
          description: 'Get your recently viewed recipes',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'GENERAL',
          permissions: [],
          parameters: [
            { name: 'limit', type: 'number', description: 'Number of recipes to return', required: false }
          ],
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/protected/general/recipes/history?limit=10', {
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
  "history": [
    {
      "recipeId": 18,
      "recipeName": "Tyler Smith's Air-Fryer Buffalo Wings",
      "category": "Miscellaneous",
      "viewedAt": "2024-03-15T09:30:00Z",
      "accessType": "FREE"
    },
    {
      "recipeId": 2,
      "recipeName": "Apple & Blackberry Crumble",
      "category": "Dessert",
      "viewedAt": "2024-03-14T15:20:00Z",
      "accessType": "FREE"
    }
  ]
}`
        },
        {
          title: 'Add Recipe Comment',
          method: 'POST',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/general/recipes/comment',
          description: 'Add a comment to a recipe',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'GENERAL',
          permissions: [],
          requestBody: {
            recipeId: 18,
            content: "This recipe is amazing! Made it for dinner last night.",
            rating: 5
          },
          parameters: [],
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/protected/general/recipes/comment', {
  method: 'POST',
  headers: {
    'Authorization': 'Bearer <token>',
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    recipeId: 18,
    content: "This recipe is amazing! Made it for dinner last night.",
    rating: 5
  })
})
.then(res => res.json())
.then(console.log);`,
          sampleResponse: `{
  "success": true,
  "message": "Comment added successfully",
  "comment": {
    "id": 456,
    "recipeId": 18,
    "content": "This recipe is amazing! Made it for dinner last night.",
    "rating": 5,
    "postedAt": "2024-03-15T10:30:00Z"
  }
}`
        },
        {
          title: 'Save Recipe to Personal Collection',
          method: 'POST',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/general/recipes/save',
          description: 'Save a free recipe to your personal collection',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'GENERAL',
          permissions: [],
          requestBody: {
            recipeId: 18,
            notes: "Great for game day parties"
          },
          parameters: [],
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/protected/general/recipes/save', {
  method: 'POST',
  headers: {
    'Authorization': 'Bearer <token>',
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    recipeId: 18,
    notes: "Great for game day parties"
  })
})
.then(res => res.json())
.then(console.log);`,
          sampleResponse: `{
  "success": true,
  "message": "Recipe saved to your collection",
  "savedRecipe": {
    "id": 789,
    "recipeId": 18,
    "notes": "Great for game day parties",
    "savedAt": "2024-03-15T10:30:00Z"
  }
}`
        },
        {
          title: 'Get My Saved Recipes',
          method: 'GET',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/general/recipes/saved',
          description: 'Retrieve your saved recipe collection',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'GENERAL',
          permissions: [],
          parameters: [
            { name: 'page', type: 'number', description: 'Page number for pagination', required: false },
            { name: 'category', type: 'string', description: 'Filter by recipe category', required: false }
          ],
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/protected/general/recipes/saved?page=1', {
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
  "savedRecipes": [
    {
      "id": 789,
      "recipe": {
        "id": 18,
        "name": "Tyler Smith's Air-Fryer Buffalo Wings",
        "category": "Miscellaneous",
        "accessType": "FREE"
      },
      "notes": "Great for game day parties",
      "savedAt": "2024-03-15T10:30:00Z"
    }
  ],
  "totalCount": 8
}`
        }
      ]
    },
    comments: {
      title: 'Comments Management',
      description: 'Manage recipe comments and ratings',
      endpoints: [
        {
          title: 'Get Comments by ID',
          method: 'GET',
          url: '/protected/general/comments',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/general/comments',
          description: 'Retrieve comments by ID, userId, or recipeId',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'GENERAL',
          permissions: [],
          parameters: [
            { name: 'id', type: 'number', description: 'Comment ID', required: false },
            { name: 'userId', type: 'number', description: 'User ID to get their comments', required: false },
            { name: 'recipeId', type: 'number', description: 'Recipe ID to get its comments', required: false }
          ],
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/protected/general/comments?recipeId=18', {
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
  "message": "Comments retrieved successfully",
  "data": [
    {
      "id": 25,
      "userId": 123,
      "recipeId": 18,
      "content": "Rich and decadent!",
      "rating": 5,
      "date": "2025-05-06",
      "isBanned": false
    }
  ]
}`
        },
        {
          title: 'Create Comment',
          method: 'POST',
          url: '/protected/general/comments',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/general/comments',
          description: 'Create a new comment for a recipe',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'GENERAL',
          permissions: [],
          requestBody: {
            userId: 123,
            recipeId: 18,
            content: "This recipe is amazing! Made it for dinner last night.",
            rating: 5
          },
          validation: {
            required: ['userId', 'recipeId', 'content', 'rating'],
            type: {
              userId: 'number',
              recipeId: 'number',
              rating: 'number'
            },
            minLength: {
              content: 5
            },
            maxLength: {
              content: 1000
            },
            range: {
              rating: { min: 1, max: 5 }
            }
          },
          parameters: [],
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/protected/general/comments', {
  method: 'POST',
  headers: {
    'Authorization': 'Bearer <token>',
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    userId: 123,
    recipeId: 18,
    content: "This recipe is amazing! Made it for dinner last night.",
    rating: 5
  })
})
.then(res => res.json())
.then(console.log);`,
          sampleResponse: `{
  "success": true,
  "status": 201,
  "message": "Comment created successfully",
  "data": {
    "id": 456,
    "userId": 123,
    "recipeId": 18,
    "content": "This recipe is amazing! Made it for dinner last night.",
    "rating": 5,
    "date": "2025-07-16",
    "isBanned": false
  }
}`
        },
        {
          title: 'Update Comment',
          method: 'PUT',
          url: '/protected/general/comments',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/general/comments',
          description: 'Update an existing comment',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'GENERAL',
          permissions: [],
          requestBody: {
            id: 456,
            userId: 123,
            recipeId: 18,
            content: "This recipe is absolutely amazing! Made it for dinner last night and everyone loved it.",
            rating: 5
          },
          validation: {
            required: ['id', 'userId', 'recipeId', 'content', 'rating'],
            type: {
              id: 'number',
              userId: 'number',
              recipeId: 'number',
              rating: 'number'
            },
            minLength: {
              content: 5
            },
            maxLength: {
              content: 1000
            },
            range: {
              rating: { min: 1, max: 5 }
            }
          },
          parameters: [],
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/protected/general/comments', {
  method: 'PUT',
  headers: {
    'Authorization': 'Bearer <token>',
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    id: 456,
    userId: 123,
    recipeId: 18,
    content: "This recipe is absolutely amazing! Made it for dinner last night and everyone loved it.",
    rating: 5
  })
})
.then(res => res.json())
.then(console.log);`,
          sampleResponse: `{
  "success": true,
  "status": 200,
  "message": "Comment updated successfully",
  "data": {
    "id": 456,
    "userId": 123,
    "recipeId": 18,
    "content": "This recipe is absolutely amazing! Made it for dinner last night and everyone loved it.",
    "rating": 5,
    "date": "2025-07-16",
    "isBanned": false
  }
}`
        },
        {
          title: 'Delete Comment',
          method: 'DELETE',
          url: '/protected/general/comments',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/general/comments',
          description: 'Delete a comment by ID',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'GENERAL',
          permissions: [],
          parameters: [
            { name: 'id', type: 'number', description: 'Comment ID to delete', required: true }
          ],
          validation: {
            required: ['id'],
            type: {
              id: 'number'
            }
          },
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/protected/general/comments?id=456', {
  method: 'DELETE',
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
  "message": "Comment deleted successfully"
}`
        }
      ]
    },
    contestEntry: {
      title: 'Contest Entry Management',
      description: 'Manage contest entries and submissions',
      endpoints: [
        {
          title: 'Get Contest Entries',
          method: 'GET',
          url: '/protected/general/contest-entry',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/general/contest-entry',
          description: 'Retrieve contest entries by ID, contestId+userId, or userId',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'GENERAL',
          permissions: [],
          parameters: [
            { name: 'id', type: 'number', description: 'Contest Entry ID', required: false },
            { name: 'contestId', type: 'number', description: 'Contest ID', required: false },
            { name: 'userId', type: 'number', description: 'User ID', required: false }
          ],
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/protected/general/contest-entry?userId=123', {
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
  "message": "Contest entries retrieved successfully",
  "data": [
    {
      "id": 4,
      "contestId": 1,
      "userId": 123,
      "name": "Mi Quang",
      "ingredients": "pork, basil, mint, and cilantro",
      "image": "mi-quang-1752554674772.jpg",
      "prepareTime": "30",
      "cookingTime": "30",
      "yield": "30",
      "category": "Breakfast",
      "area": "Vietnamese",
      "shortDescription": "Mì Quảng, a popular Vietnamese noodle dish",
      "contestEntryInstructions": "Traditional cooking method with special broth",
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
          url: '/protected/general/contest-entry',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/general/contest-entry',
          description: 'Create a new contest entry with image upload',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'GENERAL',
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
            contestEntryInstructions: "Traditional cooking method with special broth",
            image: "mi-quang.jpg"
          },
          validation: {
            required: ['contestId', 'userId', 'name', 'ingredients', 'prepareTime', 'cookingTime', 'yield', 'category', 'area', 'shortDescription', 'contestEntryInstructions'],
            type: {
              contestId: 'number',
              userId: 'number'
            },
            minLength: {
              name: 3,
              ingredients: 10,
              shortDescription: 20,
              contestEntryInstructions: 20
            },
            maxLength: {
              name: 100,
              ingredients: 1000,
              shortDescription: 500,
              contestEntryInstructions: 2000
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
formData.append('contestEntryInstructions', 'Traditional cooking method with special broth');
formData.append('image', fileInput.files[0]); // File object

fetch('http://localhost:8080/JamesThewWebApplication/api/protected/general/contest-entry', {
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
  "success": true,
  "status": 201,
  "message": "Contest entry created successfully",
  "data": {
    "id": 5,
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
    "contestEntryInstructions": "Traditional cooking method with special broth",
    "dateCreated": "2025-07-16",
    "status": "PENDING"
  }
}`
        },
        {
          title: 'Update Contest Entry',
          method: 'PUT',
          url: '/protected/general/contest-entry',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/general/contest-entry',
          description: 'Update an existing contest entry with optional image upload',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'GENERAL',
          permissions: [],
          contentType: 'multipart/form-data',
          requestBody: {
            id: "5",
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
            contestEntryInstructions: "Traditional cooking method with special broth and premium toppings"
          },
          validation: {
            required: ['id', 'contestId', 'userId', 'name', 'ingredients', 'prepareTime', 'cookingTime', 'yield', 'category', 'area', 'shortDescription', 'contestEntryInstructions'],
            optional: ['image'],
            type: {
              id: 'number',
              contestId: 'number',
              userId: 'number'
            },
            minLength: {
              name: 3,
              ingredients: 10,
              shortDescription: 20,
              contestEntryInstructions: 20
            },
            maxLength: {
              name: 100,
              ingredients: 1000,
              shortDescription: 500,
              contestEntryInstructions: 2000
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
formData.append('id', '5');
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
formData.append('contestEntryInstructions', 'Traditional cooking method with special broth and premium toppings');
// formData.append('image', fileInput.files[0]); // Optional: new image file

fetch('http://localhost:8080/JamesThewWebApplication/api/protected/general/contest-entry', {
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
  "success": true,
  "status": 200,
  "message": "Contest entry updated successfully",
  "data": {
    "id": 5,
    "contestId": 1,
    "userId": 123,
    "name": "Mi Quang Deluxe",
    "ingredients": "pork, shrimp, basil, mint, cilantro, and quail eggs",
    "prepareTime": "35",
    "cookingTime": "35",
    "yield": "4",
    "category": "Breakfast",
    "area": "Vietnamese",
    "shortDescription": "Enhanced Mì Quảng with premium ingredients",
    "contestEntryInstructions": "Traditional cooking method with special broth and premium toppings",
    "dateModified": "2025-07-16",
    "status": "PENDING"
  }
}`
        },
        {
          title: 'Delete Contest Entry',
          method: 'DELETE',
          url: '/protected/general/contest-entry',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/general/contest-entry',
          description: 'Delete a contest entry',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'GENERAL',
          permissions: [],
          requestBody: {
            id: 5,
            contestId: 1,
            userId: 123,
            name: "Mi Quang Deluxe"
          },
          validation: {
            required: ['id', 'contestId', 'userId', 'name'],
            type: {
              id: 'number',
              contestId: 'number',
              userId: 'number'
            },
            minLength: {
              name: 3
            },
            maxLength: {
              name: 100
            }
          },
          parameters: [],
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/protected/general/contest-entry', {
  method: 'DELETE',
  headers: {
    'Authorization': 'Bearer <token>',
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    id: 5,
    contestId: 1,
    userId: 123,
    name: "Mi Quang Deluxe"
  })
})
.then(res => res.json())
.then(console.log);`,
          sampleResponse: `{
  "success": true,
  "status": 200,
  "message": "Contest entry deleted successfully"
}`
        }
      ]
    },
    support: {
      title: 'User Support',
      description: 'Get help and submit questions',
      endpoints: [
        {
          title: 'Submit Question',
          method: 'POST',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/general/support/question',
          description: 'Submit a question or support request',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'GENERAL',
          permissions: [],
          requestBody: {
            category: "recipe-help",
            subject: "Question about cooking times",
            question: "How do I adjust cooking times for high altitude cooking?"
          },
          parameters: [],
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/protected/general/support/question', {
  method: 'POST',
  headers: {
    'Authorization': 'Bearer <token>',
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    category: "recipe-help",
    subject: "Question about cooking times",
    question: "How do I adjust cooking times for high altitude cooking?"
  })
})
.then(res => res.json())
.then(console.log);`,
          sampleResponse: `{
  "success": true,
  "message": "Question submitted successfully",
  "question": {
    "id": 456,
    "category": "recipe-help",
    "subject": "Question about cooking times",
    "status": "pending",
    "submittedAt": "2024-03-15T10:30:00Z",
    "ticketNumber": "HELP-2024-456"
  }
}`
        },
        {
          title: 'Get My Questions',
          method: 'GET',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/general/support/questions',
          description: 'Retrieve your submitted questions and their status',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'GENERAL',
          permissions: [],
          parameters: [
            { name: 'status', type: 'string', description: 'Filter by question status', required: false }
          ],
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/protected/general/support/questions?status=pending', {
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
  "questions": [
    {
      "id": 456,
      "category": "recipe-help",
      "subject": "Question about cooking times",
      "question": "How do I adjust cooking times for high altitude cooking?",
      "status": "answered",
      "submittedAt": "2024-03-15T10:30:00Z",
      "answeredAt": "2024-03-15T14:20:00Z",
      "answer": "For high altitude cooking, you typically need to...",
      "ticketNumber": "HELP-2024-456"
    }
  ]
}`
        }
      ]
    }
  }
};
