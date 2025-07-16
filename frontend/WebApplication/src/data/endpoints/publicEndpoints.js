export const publicEndpoints = {
  title: 'Public Endpoints',
  description: 'Accessible to all users without authentication',
  accessLevel: 'public',
  subcategories: {
    auth: {
      title: 'Authentication',
      description: 'User login and registration endpoints',
      endpoints: [
        {
          title: 'User Login',
          method: 'POST',
          url: '/login',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/login',
          description: 'Authenticate user and receive access token',
          requiresAuth: false,
          accessLevel: 'public',
          requestBody: {
            username: "admin_user",
            password: "admin123"
          },
          parameters: [],
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/login', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    username: 'admin_user',
    password: 'admin123'
  })
})
.then(res => res.json())
.then(console.log);`,
          sampleResponse: `{
  "success": true,
  "status": 200,
  "message": "Login successful",
  "data": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbl91c2VyIiwidXNlcl9pZCI6MSwicm9sZXMiOlsiQURNSU4iXSwicGVybWlzc2lvbnMiOltdLCJpYXQiOjE3NTIzMDYzNjMsImV4cCI6MTc1MjM5Mjc2M30.MxtuTSQt4_fg9b2YGo3vmgmYFN53VD4dT7efUx0Sot0"
}`
        },
        {
          title: 'User Registration',
          method: 'POST',
          url: '/register',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/register',
          description: 'Register a new user account',
          requiresAuth: false,
          accessLevel: 'public',
          requestBody: {
            email: "newuser@example.com",
            password: "password123",
            firstName: "John",
            lastName: "Doe"
          },
          parameters: [],
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/register', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    email: 'newuser@example.com',
    password: 'password123',
    firstName: 'John',
    lastName: 'Doe'
  })
})
.then(res => res.json())
.then(console.log);`,
          sampleResponse: `{
  "success": true,
  "message": "User registered successfully",
  "user": {
    "id": 2,
    "email": "newuser@example.com",
    "firstName": "John",
    "lastName": "Doe",
    "role": "GENERAL"
  }
}`
        }
      ]
    },
    recipes: {
      title: 'Free Recipes API',
      description: 'Access free recipes and recipe data',
      endpoints: [
        {
          title: 'Get Free Recipe by ID',
          method: 'GET',
          url: '/recipes',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/recipes',
          description: 'Retrieve a specific free recipe by its ID',
          requiresAuth: false,
          accessLevel: 'public',
          parameters: [
            { name: 'id', type: 'number', description: 'The ID of the recipe', required: true }
          ],
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/recipes?id=1')
  .then(res => res.json())
  .then(console.log);`,
          sampleResponse: `{
  "success": true,
  "status": 200,
  "message": "Recipe found",
  "data": {
    "id": 2,
    "name": "Apple & Blackberry Crumble",
    "category": "Dessert",
    "area": "British",
    "instructions": "Put the butter and sugar in a medium saucepan and melt together over a medium heat.",
    "image": "http://localhost:8080/JamesThewWebApplication/api/images/recipes/apple-blackberry-crumble.jpg",
    "ingredients": "120g Plain Flour. 60g Caster Sugar.",
    "publishedOn": "2018-02-09",
    "recipedBy": 32,
    "prepareTime": "20 minutes",
    "cookingTime": "25 minutes",
    "yield": "6 servings",
    "shortDescription": "Perfect for a cozy dinner party or a family gathering, serve warm with vanilla ice cream and enjoy the sweet and tangy flavors.",
    "accessType": "FREE",
    "comments": [],
    "detailedInstructions": []
  }
}`
        },
        {
          title: 'Get All Free Recipes',
          method: 'GET',
          url: '/recipes',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/recipes',
          description: 'Retrieve all free recipes with pagination',
          requiresAuth: false,
          accessLevel: 'public',
          parameters: [
            { name: 'page', type: 'number', description: 'The page number (default = 1)', required: false },
            { name: 'size', type: 'number', description: 'The number of records per page (default = 10)', required: false }
          ],
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/recipes?page=1&size=10')
  .then(res => res.json())
  .then(console.log);`,
          sampleResponse: `{
  "success": true,
  "status": 200,
  "message": "Free recipes fetched successfully",
  "data": {
    "recipes": [
      {
        "id": 18,
        "name": "Tyler Smith's Air-Fryer Buffalo Wings",
        "category": "Miscellaneous",
        "area": "American",
        "instructions": "Mix until evenly coated./nPlace the wings in the air fryer (make sure they are spaced without touching) and cook at 375° for 10-15 minutes.",
        "image": "http://localhost:8080/JamesThewWebApplication/api/images/recipes/tyler-smith-s-air-fryer-buffalo-wings.jpg",
        "ingredients": "18 chicken wings. 1/2 cup all-purpose flour. 1/4 cup cornstarch.",
        "publishedOn": "2019-04-11",
        "recipedBy": 14,
        "prepareTime": "20 minutes",
        "cookingTime": "15 minutes",
        "yield": "4 servings",
        "shortDescription": "Spicy and tangy buffalo-style air-fryer wings, perfect for a party or game day gathering.",
        "accessType": "FREE",
        "comments": [
          {
            "id": 25,
            "userId": 0,
            "recipeId": 18,
            "content": "Rich and decadent!",
            "date": "2025-05-06",
            "rating": 0,
            "isBanned": false
          }
        ],
        "nutrition": {
          "id": 7,
          "calories": "538 calories",
          "fat": "37g fat (16g saturated fat)",
          "cholesterol": "69mg cholesterol",
          "sodium": "303mg sodium",
          "carbohydrate": "45g carbohydrate (23g sugars",
          "fiber": "3g fiber)",
          "protein": "11g protein.",
          "recipeId": 18
        },
        "detailedInstructions": [
          {
            "id": 7562,
            "name": "Make the crust",
            "text": "Preheat the oven to 350°F. Line an 8-inch square baking pan with parchment, letting the ends extend up the sides.",
            "image": "http://localhost:8080/JamesThewWebApplication/api/images/instructions/dubai-chocolate-cheesecake-bars-instruction-1.jpg",
            "recipeId": 18
          }
        ]
      }
    ],
    "currentPage": 1,
    "totalPages": 154,
    "totalItems": 1534
  }
}`
        },
        {
          title: 'Get Free Recipes by Area',
          method: 'GET',
          url: '/recipes',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/recipes',
          description: 'Retrieve free recipes filtered by area/cuisine',
          requiresAuth: false,
          accessLevel: 'public',
          parameters: [
            { name: 'page', type: 'number', description: 'The page number (default = 1)', required: false },
            { name: 'size', type: 'number', description: 'The number of records per page (default = 10)', required: false },
            { name: 'area', type: 'string', description: 'The area name (e.g., American, British)', required: true }
          ],
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/recipes?area=American&page=1&size=10')
  .then(res => res.json())
  .then(console.log);`,
          sampleResponse: `{
  "success": true,
  "status": 200,
  "message": "Free recipes fetched successfully",
  "data": {
    "recipes": [
      {
        "id": 18,
        "name": "Tyler Smith's Air-Fryer Buffalo Wings",
        "category": "Miscellaneous",
        "area": "American",
        "accessType": "FREE"
      }
    ],
    "currentPage": 1,
    "totalPages": 154,
    "totalItems": 1534
  }
}`
        },
        {
          title: 'Get Free Recipes by Category',
          method: 'GET',
          url: '/recipes',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/recipes',
          description: 'Retrieve free recipes filtered by category',
          requiresAuth: false,
          accessLevel: 'public',
          parameters: [
            { name: 'page', type: 'number', description: 'The page number (default = 1)', required: false },
            { name: 'size', type: 'number', description: 'The number of records per page (default = 10)', required: false },
            { name: 'category', type: 'string', description: 'The category name (e.g., Beef, Dessert)', required: true }
          ],
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/recipes?category=Beef&page=1&size=10')
  .then(res => res.json())
  .then(console.log);`,
          sampleResponse: `{
  "success": true,
  "status": 200,
  "message": "Free recipes fetched successfully",
  "data": {
    "recipes": [
      {
        "id": 5,
        "name": "Beef Stew",
        "category": "Beef",
        "area": "American",
        "accessType": "FREE"
      }
    ],
    "currentPage": 1,
    "totalPages": 25,
    "totalItems": 248
  }
}`
        },
        {
          title: 'Discover Free Recipes',
          method: 'GET',
          url: '/discover/recipes',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/discover/recipes',
          description: 'Search and discover free recipes with multiple filters',
          requiresAuth: false,
          accessLevel: 'public',
          parameters: [
            { name: 'keyword', type: 'string', description: 'The search keyword', required: false },
            { name: 'category', type: 'string', description: 'The recipe category', required: false },
            { name: 'area', type: 'string', description: 'The recipe area', required: false },
            { name: 'recipedBy', type: 'number', description: "The creator's user ID", required: false },
            { name: 'page', type: 'number', description: 'The page number (default = 1)', required: false },
            { name: 'size', type: 'number', description: 'The number of records per page (default = 10)', required: false }
          ],
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/discover/recipes?keyword=chicken&category=Miscellaneous&page=1&size=10')
  .then(res => res.json())
  .then(console.log);`,
          sampleResponse: `{
  "success": true,
  "status": 200,
  "message": "Free recipes fetched successfully",
  "data": {
    "recipes": [
      {
        "id": 18,
        "name": "Tyler Smith's Air-Fryer Buffalo Wings",
        "category": "Miscellaneous",
        "area": "American",
        "accessType": "FREE"
      }
    ],
    "currentPage": 1,
    "totalPages": 154,
    "totalItems": 1534
  }
}`
        }
      ]
    },
    contests: {
      title: 'Contests API',
      description: 'Access public contest information',
      endpoints: [
        {
          title: 'Get All Contests',
          method: 'GET',
          url: '/contests',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/contests',
          description: 'Retrieve all available contests',
          requiresAuth: false,
          accessLevel: 'public',
          parameters: [],
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/contests')
  .then(res => res.json())
  .then(console.log);`,
          sampleResponse: `{
  "success": true,
  "status": 200,
  "message": "All contests fetched",
  "data": [
    {
      "id": 1,
      "articleBody": "With the current price of eggs, we decided this isn't the best time for this contest. So, get creative and show off your favorite lunches instead!",
      "headline": "'Let's Do Lunch' Recipe Contest Announcement",
      "description": "Who's ready for lunch? Show off your favorite lunch recipes for the chance to win $500.",
      "datePublished": "2025-03-01",
      "dateModified": "2025-03-01",
      "accessRole": "GENERAL",
      "contestImages": [
        {
          "id": 1,
          "contestId": 1,
          "imagePath": "http://localhost:8080/JamesThewWebApplication/api/images/contests/Lets_Do_Lunch_Recipe_Contest_Announcement_1.jpg"
        },
        {
          "id": 2,
          "contestId": 1,
          "imagePath": "http://localhost:8080/JamesThewWebApplication/api/images/contests/Lets_Do_Lunch_Recipe_Contest_Announcement_2.jpg"
        }
      ],
      "isFree": true,
      "isClosed": false
    }
  ]
}`
        },
        {
          title: 'Get Contest by ID',
          method: 'GET',
          url: '/contests',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/contests',
          description: 'Retrieve a specific contest by its ID',
          requiresAuth: false,
          accessLevel: 'public',
          parameters: [
            { name: 'id', type: 'number', description: 'The ID of the contest', required: true }
          ],
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/contests?id=1')
  .then(res => res.json())
  .then(console.log);`,
          sampleResponse: `{
  "success": true,
  "status": 200,
  "message": "Contest fetched successfully",
  "data": {
    "id": 4,
    "articleBody": "Grand Prize: Chicken & Green Chile Stuffed Shells3 ReviewsContest WinnerTest Kitchen ApprovedTotal Time1 hour 15 minServings12 servingsGo to RecipeFrom the Recipe Creator:For a family potluck, I was told to bring a pasta casserole.",
    "headline": "Our Make Take Recipe Contest",
    "description": "Home cooks shared their favorite 'dish to pass' that they rely on for potlucks, get-togethers and bake sales. The best crowd-pleasing dishes in this list range from casseroles to cakes.",
    "datePublished": "2024-11-11",
    "dateModified": "2024-11-11",
    "accessRole": "SUBSCRIBER",
    "contestImages": [
      {
        "id": 7,
        "contestId": 4,
        "imagePath": "http://localhost:8080/JamesThewWebApplication/api/images/contests/Our_Make_Take_Recipe_Contest_1.jpg"
      },
      {
        "id": 8,
        "contestId": 4,
        "imagePath": "http://localhost:8080/JamesThewWebApplication/api/images/contests/Our_Make_Take_Recipe_Contest_2.jpg"
      }
    ],
    "isFree": true,
    "isClosed": true
  }
}`
        }
      ]
    },
    areas: {
      title: 'Areas API',
      description: 'Access cuisine areas and regions',
      endpoints: [
        {
          title: 'Get All Areas',
          method: 'GET',
          url: '/areas',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/areas',
          description: 'Retrieve all available cuisine areas/regions',
          requiresAuth: false,
          accessLevel: 'public',
          parameters: [],
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/areas')
  .then(res => res.json())
  .then(console.log);`,
          sampleResponse: `{
  "success": true,
  "status": 200,
  "message": "All areas fetched successfully",
  "data": [
    {
      "id": 1,
      "name": "American",
      "description": "American cuisine and recipes",
      "recipeCount": 245
    },
    {
      "id": 2,
      "name": "British",
      "description": "British cuisine and recipes",
      "recipeCount": 156
    },
    {
      "id": 3,
      "name": "Italian",
      "description": "Italian cuisine and recipes",
      "recipeCount": 189
    }
  ]
}`
        },
        {
          title: 'Get Area by ID',
          method: 'GET',
          url: '/areas',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/areas',
          description: 'Retrieve a specific area by its ID',
          requiresAuth: false,
          accessLevel: 'public',
          parameters: [
            { name: 'id', type: 'number', description: 'The ID of the area', required: true }
          ],
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/areas?id=1')
  .then(res => res.json())
  .then(console.log);`,
          sampleResponse: `{
  "success": true,
  "status": 200,
  "message": "Area fetched successfully",
  "data": {
    "id": 1,
    "name": "American",
    "description": "American cuisine and recipes",
    "recipeCount": 245,
    "popularRecipes": [
      {
        "id": 18,
        "name": "Tyler Smith's Air-Fryer Buffalo Wings"
      }
    ]
  }
}`
        }
      ]
    },
    categories: {
      title: 'Categories API',
      description: 'Access recipe categories and types',
      endpoints: [
        {
          title: 'Get All Categories',
          method: 'GET',
          url: '/categories',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/categories',
          description: 'Retrieve all available recipe categories',
          requiresAuth: false,
          accessLevel: 'public',
          parameters: [],
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/categories')
  .then(res => res.json())
  .then(console.log);`,
          sampleResponse: `{
  "success": true,
  "status": 200,
  "message": "All categories fetched successfully",
  "data": [
    {
      "id": 1,
      "name": "Beef",
      "description": "Beef-based recipes",
      "recipeCount": 78
    },
    {
      "id": 2,
      "name": "Dessert",
      "description": "Sweet desserts and treats",
      "recipeCount": 156
    },
    {
      "id": 3,
      "name": "Miscellaneous",
      "description": "Various other recipes",
      "recipeCount": 234
    }
  ]
}`
        },
        {
          title: 'Get Category by ID',
          method: 'GET',
          url: '/categories',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/categories',
          description: 'Retrieve a specific category by its ID',
          requiresAuth: false,
          accessLevel: 'public',
          parameters: [
            { name: 'id', type: 'number', description: 'The ID of the category', required: true }
          ],
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/categories?id=1')
  .then(res => res.json())
  .then(console.log);`,
          sampleResponse: `{
  "success": true,
  "status": 200,
  "message": "Category fetched successfully",
  "data": {
    "id": 1,
    "name": "Beef",
    "description": "Beef-based recipes",
    "recipeCount": 78,
    "popularRecipes": [
      {
        "id": 5,
        "name": "Classic Beef Stew"
      },
      {
        "id": 12,
        "name": "Beef Stroganoff"
      }
    ]
  }
}`
        }
      ]
    }
  }
};
