export const staffEndpoints = {
  title: 'Staff Endpoints',
  description: 'Staff functions - requires STAFF role with specific permissions',
  accessLevel: 'private',
  role: 'STAFF',
  subcategories: {
    comments: {
      title: 'Comments Moderation',
      description: 'Moderate and ban/unban comments',
      endpoints: [
        {
          title: 'Ban/Unban Comment',
          method: 'PUT',
          url: '/protected/staff/comments',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/staff/comments',
          description: 'Ban or unban a comment by ID',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'STAFF',
          permissions: ['MODERATE_CONTENT'],
          parameters: [
            { name: 'id', type: 'number', description: 'Comment ID to ban/unban', required: true }
          ],
          validation: {
            required: ['id'],
            type: {
              id: 'number'
            }
          },
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/protected/staff/comments?id=25', {
  method: 'PUT',
  headers: {
    'Authorization': 'Bearer <token>',
    'Content-Type': 'application/json'
  }
})
.then(res => res.json())
.then(console.log);`,
          sampleResponse: `{
  "status": 200,
  "message": "Comment banned successfully"
}`
        }
      ]
    },
    contests: {
      title: 'Contest Management',
      description: 'Manage contests and competitions',
      endpoints: [
        {
          title: 'Create Contest',
          method: 'POST',
          url: '/protected/staff/contests',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/staff/contests',
          description: 'Create a new contest with image uploads',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'STAFF',
          permissions: ['MANAGE_CONTESTS'],
          contentType: 'multipart/form-data',
          requestBody: {
            headline: "Spring Cooking Contest 2025",
            articleBody: "Join our exciting spring cooking contest featuring seasonal ingredients and creative recipes...",
            description: "A contest celebrating spring flavors and fresh ingredients",
            datePublished: "2025-04-01",
            dateModified: "2025-04-01",
            prize: "$1000 First Prize, $500 Second Prize",
            isFree: "true",
            isClosed: "false",
            accessRole: "GENERAL"
          },
          validation: {
            required: ['headline', 'articleBody', 'description', 'datePublished', 'dateModified', 'prize', 'isFree', 'isClosed', 'accessRole'],
            minLength: {
              headline: 5,
              articleBody: 50,
              description: 20,
              prize: 5
            },
            maxLength: {
              headline: 200,
              articleBody: 5000,
              description: 500,
              prize: 500
            },
            enum: {
              isFree: ['true', 'false'],
              isClosed: ['true', 'false'],
              accessRole: ['GENERAL', 'PREMIUM', 'STAFF', 'ADMIN']
            },
            pattern: {
              datePublished: /^\d{4}-\d{2}-\d{2}$/,
              dateModified: /^\d{4}-\d{2}-\d{2}$/
            },
            fileTypes: {
              contestImages: ['jpg', 'jpeg', 'png', 'gif', 'webp']
            }
          },
          parameters: [],
          sampleRequest: `const formData = new FormData();
formData.append('headline', 'Spring Cooking Contest 2025');
formData.append('articleBody', 'Join our exciting spring cooking contest featuring seasonal ingredients and creative recipes...');
formData.append('description', 'A contest celebrating spring flavors and fresh ingredients');
formData.append('datePublished', '2025-04-01');
formData.append('dateModified', '2025-04-01');
formData.append('prize', '$1000 First Prize, $500 Second Prize');
formData.append('isFree', 'true');
formData.append('isClosed', 'false');
formData.append('accessRole', 'GENERAL');
// Add multiple images
for(let i = 0; i < contestImageFiles.length; i++) {
  formData.append('contestImages', contestImageFiles[i]);
}

fetch('http://localhost:8080/JamesThewWebApplication/api/protected/staff/contests', {
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
  "status": 201,
  "message": "Contest created successfully",
  "data": {
    "id": 5,
    "headline": "Spring Cooking Contest 2025",
    "articleBody": "Join our exciting spring cooking contest featuring seasonal ingredients and creative recipes...",
    "description": "A contest celebrating spring flavors and fresh ingredients",
    "datePublished": "2025-04-01",
    "dateModified": "2025-04-01",
    "prize": "$1000 First Prize, $500 Second Prize",
    "isFree": true,
    "isClosed": false,
    "accessRole": "GENERAL",
    "images": [
      "http://localhost:8080/JamesThewWebApplication/api/images/contests/spring-contest-1.jpg",
      "http://localhost:8080/JamesThewWebApplication/api/images/contests/spring-contest-2.jpg"
    ],
    "createdAt": "2025-07-16T10:30:00Z"
  }
}`
        },
        {
          title: 'Update Contest',
          method: 'PUT',
          url: '/protected/staff/contests',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/staff/contests',
          description: 'Update an existing contest or toggle status',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'STAFF',
          permissions: ['MANAGE_CONTESTS'],
          contentType: 'multipart/form-data',
          requestBody: {
            id: "5",
            headline: "Spring Cooking Contest 2025 - Extended",
            articleBody: "Join our exciting spring cooking contest featuring seasonal ingredients and creative recipes. Contest extended until May 1st!",
            description: "A contest celebrating spring flavors and fresh ingredients - now extended!",
            datePublished: "2025-04-01",
            dateModified: "2025-07-16",
            prize: "$1500 First Prize, $750 Second Prize, $250 Third Prize",
            isFree: "true",
            isClosed: "false",
            accessRole: "GENERAL"
          },
          validation: {
            required: ['id'],
            optional: ['headline', 'articleBody', 'description', 'datePublished', 'dateModified', 'prize', 'isFree', 'isClosed', 'accessRole', 'contestImages'],
            type: {
              id: 'number'
            },
            minLength: {
              headline: 5,
              articleBody: 50,
              description: 20,
              prize: 5
            },
            maxLength: {
              headline: 200,
              articleBody: 5000,
              description: 500,
              prize: 500
            },
            enum: {
              isFree: ['true', 'false'],
              isClosed: ['true', 'false'],
              accessRole: ['GENERAL', 'PREMIUM', 'STAFF', 'ADMIN']
            },
            pattern: {
              datePublished: /^\d{4}-\d{2}-\d{2}$/,
              dateModified: /^\d{4}-\d{2}-\d{2}$/
            },
            fileTypes: {
              contestImages: ['jpg', 'jpeg', 'png', 'gif', 'webp']
            }
          },
          parameters: [],
          sampleRequest: `const formData = new FormData();
formData.append('id', '5');
formData.append('headline', 'Spring Cooking Contest 2025 - Extended');
formData.append('articleBody', 'Join our exciting spring cooking contest featuring seasonal ingredients and creative recipes. Contest extended until May 1st!');
formData.append('description', 'A contest celebrating spring flavors and fresh ingredients - now extended!');
formData.append('datePublished', '2025-04-01');
formData.append('dateModified', '2025-07-16');
formData.append('prize', '$1500 First Prize, $750 Second Prize, $250 Third Prize');
formData.append('isFree', 'true');
formData.append('isClosed', 'false');
formData.append('accessRole', 'GENERAL');
// Optional: Add new images
for(let i = 0; i < newContestImageFiles.length; i++) {
  formData.append('contestImages', newContestImageFiles[i]);
}

fetch('http://localhost:8080/JamesThewWebApplication/api/protected/staff/contests', {
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
  "message": "Contest updated successfully",
  "data": {
    "id": 5,
    "headline": "Spring Cooking Contest 2025 - Extended",
    "description": "A contest celebrating spring flavors and fresh ingredients - now extended!",
    "dateModified": "2025-07-16",
    "prize": "$1500 First Prize, $750 Second Prize, $250 Third Prize",
    "updatedAt": "2025-07-16T11:00:00Z"
  }
}`
        },
        {
          title: 'Delete Contest',
          method: 'DELETE',
          url: '/protected/staff/contests',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/staff/contests',
          description: 'Delete a contest by ID',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'STAFF',
          permissions: ['MANAGE_CONTESTS'],
          parameters: [
            { name: 'id', type: 'number', description: 'Contest ID to delete', required: true }
          ],
          validation: {
            required: ['id'],
            type: {
              id: 'number'
            }
          },
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/protected/staff/contests?id=5', {
  method: 'DELETE',
  headers: {
    'Authorization': 'Bearer <token>',
    'Content-Type': 'application/json'
  }
})
.then(res => res.json())
.then(console.log);`,
          sampleResponse: `{
  "status": 200,
  "message": "Contest deleted successfully"
}`
        }
      ]
    },
    scoreContestEntryExaminers: {
      title: 'Contest Entry Examiners',
      description: 'Manage contest entry scoring and examiner assignments',
      endpoints: [
        {
          title: 'Get Contest Entry Examiners',
          method: 'GET',
          url: '/protected/staff/score-contest-entry-examiners',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/staff/score-contest-entry-examiners',
          description: 'Retrieve contest entry examiners by ID, contestEntryId, or examinerId',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'STAFF',
          permissions: ['MANAGE_CONTESTS'],
          parameters: [
            { name: 'id', type: 'number', description: 'Examiner record ID', required: false },
            { name: 'contestEntryId', type: 'number', description: 'Contest Entry ID', required: false },
            { name: 'examinerId', type: 'number', description: 'Examiner User ID', required: false }
          ],
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/protected/staff/score-contest-entry-examiners?contestEntryId=4', {
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
  "message": "Contest entry examiners retrieved successfully",
  "data": [
    {
      "id": 15,
      "contestEntryId": 4,
      "examinerId": 101,
      "score": 8.5,
      "feedback": "Excellent presentation and flavor combination. Creative use of traditional ingredients.",
      "dateEvaluated": "2025-07-15",
      "examinerName": "Chef Maria Rodriguez"
    }
  ]
}`
        },
        {
          title: 'Add Contest Entry Examiner',
          method: 'POST',
          url: '/protected/staff/score-contest-entry-examiners',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/staff/score-contest-entry-examiners',
          description: 'Add an examiner to score a contest entry',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'STAFF',
          permissions: ['MANAGE_CONTESTS'],
          requestBody: {
            contestEntryId: 4,
            examinerId: 102,
            score: 9.0,
            feedback: "Outstanding dish with perfect execution. Innovative approach to traditional recipe."
          },
          validation: {
            required: ['contestEntryId', 'examinerId', 'score', 'feedback'],
            type: {
              contestEntryId: 'number',
              examinerId: 'number',
              score: 'number'
            },
            range: {
              score: { min: 0, max: 10 }
            },
            minLength: {
              feedback: 10
            },
            maxLength: {
              feedback: 1000
            }
          },
          parameters: [],
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/protected/staff/score-contest-entry-examiners', {
  method: 'POST',
  headers: {
    'Authorization': 'Bearer <token>',
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    contestEntryId: 4,
    examinerId: 102,
    score: 9.0,
    feedback: "Outstanding dish with perfect execution. Innovative approach to traditional recipe."
  })
})
.then(res => res.json())
.then(console.log);`,
          sampleResponse: `{
  "status": 200,
  "message": "Contest entry examiners added successfully",
  "data": {
    "id": 16,
    "contestEntryId": 4,
    "examinerId": 102,
    "score": 9.0,
    "feedback": "Outstanding dish with perfect execution. Innovative approach to traditional recipe.",
    "dateEvaluated": "2025-07-16"
  }
}`
        },
        {
          title: 'Update Contest Entry Examiner',
          method: 'PUT',
          url: '/protected/staff/score-contest-entry-examiners',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/staff/score-contest-entry-examiners',
          description: 'Update examiner score and feedback',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'STAFF',
          permissions: ['MANAGE_CONTESTS'],
          requestBody: {
            id: 16,
            contestEntryId: 4,
            examinerId: 102,
            score: 9.5,
            feedback: "Outstanding dish with perfect execution. Innovative approach to traditional recipe. Exceptional presentation skills demonstrated."
          },
          validation: {
            required: ['id', 'contestEntryId', 'examinerId', 'score', 'feedback'],
            type: {
              id: 'number',
              contestEntryId: 'number',
              examinerId: 'number',
              score: 'number'
            },
            range: {
              score: { min: 0, max: 10 }
            },
            minLength: {
              feedback: 10
            },
            maxLength: {
              feedback: 1000
            }
          },
          parameters: [],
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/protected/staff/score-contest-entry-examiners', {
  method: 'PUT',
  headers: {
    'Authorization': 'Bearer <token>',
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    id: 16,
    contestEntryId: 4,
    examinerId: 102,
    score: 9.5,
    feedback: "Outstanding dish with perfect execution. Innovative approach to traditional recipe. Exceptional presentation skills demonstrated."
  })
})
.then(res => res.json())
.then(console.log);`,
          sampleResponse: `{
  "status": 200,
  "message": "Contest entry examiner updated successfully",
  "data": {
    "id": 16,
    "contestEntryId": 4,
    "examinerId": 102,
    "score": 9.5,
    "feedback": "Outstanding dish with perfect execution. Innovative approach to traditional recipe. Exceptional presentation skills demonstrated.",
    "dateEvaluated": "2025-07-16"
  }
}`
        },
        {
          title: 'Remove Contest Entry Examiner',
          method: 'DELETE',
          url: '/protected/staff/score-contest-entry-examiners',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/staff/score-contest-entry-examiners',
          description: 'Remove an examiner from a contest entry',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'STAFF',
          permissions: ['MANAGE_CONTESTS'],
          parameters: [
            { name: 'contestEntryId', type: 'number', description: 'Contest Entry ID', required: true },
            { name: 'examinerId', type: 'number', description: 'Examiner User ID', required: true }
          ],
          validation: {
            required: ['contestEntryId', 'examinerId'],
            type: {
              contestEntryId: 'number',
              examinerId: 'number'
            }
          },
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/protected/staff/score-contest-entry-examiners?contestEntryId=4&examinerId=102', {
  method: 'DELETE',
  headers: {
    'Authorization': 'Bearer <token>',
    'Content-Type': 'application/json'
  }
})
.then(res => res.json())
.then(console.log);`,
          sampleResponse: `{
  "status": 200,
  "message": "Contest entry examiner deleted successfully"
}`
        }
      ]
    },
    users: {
      title: 'User Management',
      description: 'Manage user accounts and status',
      endpoints: [
        {
          title: 'Get Users',
          method: 'GET',
          url: '/protected/staff/users',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/staff/users',
          description: 'Retrieve users - single user by ID or all users',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'STAFF',
          permissions: ['MANAGE_USERS'],
          parameters: [
            { name: 'id', type: 'number', description: 'User ID (optional - returns single user)', required: false }
          ],
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/protected/staff/users?id=123', {
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
  "message": "User retrieved successfully",
  "data": {
    "id": 123,
    "username": "cookingfan2024",
    "email": "user@example.com",
    "firstName": "John",
    "lastName": "Doe",
    "role": "GENERAL",
    "isActive": true,
    "joinedAt": "2024-01-15T10:30:00Z",
    "lastLogin": "2025-07-15T14:20:00Z"
  }
}`
        },
        {
          title: 'Toggle User Status',
          method: 'PUT',
          url: '/protected/staff/users',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/staff/users',
          description: 'Toggle user status between active and inactive',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'STAFF',
          permissions: ['MANAGE_USERS'],
          parameters: [
            { name: 'id', type: 'number', description: 'User ID to toggle status', required: true }
          ],
          validation: {
            required: ['id'],
            type: {
              id: 'number'
            }
          },
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/protected/staff/users?id=123', {
  method: 'PUT',
  headers: {
    'Authorization': 'Bearer <token>',
    'Content-Type': 'application/json'
  }
})
.then(res => res.json())
.then(console.log);`,
          sampleResponse: `{
  "status": 200,
  "message": "User status toggled successfully"
}`
        },
        {
          title: 'Delete User',
          method: 'DELETE',
          url: '/protected/staff/users',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/staff/users',
          description: 'Delete a user account',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'STAFF',
          permissions: ['MANAGE_USERS'],
          parameters: [
            { name: 'id', type: 'number', description: 'User ID to delete', required: true }
          ],
          validation: {
            required: ['id'],
            type: {
              id: 'number'
            }
          },
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/protected/staff/users?id=123', {
  method: 'DELETE',
  headers: {
    'Authorization': 'Bearer <token>',
    'Content-Type': 'application/json'
  }
})
.then(res => res.json())
.then(console.log);`,
          sampleResponse: `{
  "status": 200,
  "message": "User deleted successfully"
}`
        }
      ]
    },
    questions: {
      title: 'Question Management',
      description: 'Answer user questions and provide support',
      endpoints: [
        {
          title: 'Get Questions',
          method: 'GET',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/staff/questions',
          description: 'Retrieve pending questions that need answers',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'STAFF',
          permissions: ['ANSWER_QUESTIONS'],
          parameters: [
            { name: 'status', type: 'string', description: 'Filter by question status', required: false },
            { name: 'limit', type: 'number', description: 'Number of questions to return', required: false }
          ],
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/protected/staff/questions?status=pending', {
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
      "id": 789,
      "userId": 456,
      "question": "How do I submit my contest entry?",
      "category": "contest",
      "status": "pending",
      "created": "2024-03-14T14:30:00Z"
    }
  ]
}`
        },
        {
          title: 'Answer Question',
          method: 'POST',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/staff/questions/answer',
          description: 'Provide an answer to a user question',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'STAFF',
          permissions: ['ANSWER_QUESTIONS'],
          requestBody: {
            questionId: 789,
            answer: "Thank you for your question. Here's the solution...",
            status: "resolved"
          },
          parameters: [],
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/protected/staff/questions/answer', {
  method: 'POST',
  headers: {
    'Authorization': 'Bearer <token>',
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    questionId: 789,
    answer: "Thank you for your question. Here's the solution...",
    status: "resolved"
  })
})
.then(res => res.json())
.then(console.log);`,
          sampleResponse: `{
  "success": true,
  "message": "Answer submitted successfully",
  "questionId": 789,
  "answerId": 101,
  "answeredBy": 456,
  "timestamp": "2024-03-15T15:45:00Z"
}`
        },
        {
          title: 'Update Answer',
          method: 'PUT',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/staff/questions/answer',
          description: 'Update an existing answer',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'STAFF',
          permissions: ['ANSWER_QUESTIONS'],
          requestBody: {
            answerId: 101,
            answer: "Updated answer with more detailed information...",
            status: "resolved"
          },
          parameters: [],
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/protected/staff/questions/answer', {
  method: 'PUT',
  headers: {
    'Authorization': 'Bearer <token>',
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    answerId: 101,
    answer: "Updated answer with more detailed information...",
    status: "resolved"
  })
})
.then(res => res.json())
.then(console.log);`,
          sampleResponse: `{
  "success": true,
  "message": "Answer updated successfully",
  "answerId": 101,
  "updated": "2024-03-15T16:00:00Z"
}`
        },
        {
          title: 'Delete Answer',
          method: 'DELETE',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/staff/questions/answer',
          description: 'Delete an answer (marks question as unanswered)',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'STAFF',
          permissions: ['ANSWER_QUESTIONS'],
          requestBody: {
            answerId: 101
          },
          parameters: [],
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/protected/staff/questions/answer', {
  method: 'DELETE',
  headers: {
    'Authorization': 'Bearer <token>',
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    answerId: 101
  })
})
.then(res => res.json())
.then(console.log);`,
          sampleResponse: `{
  "success": true,
  "message": "Answer deleted successfully",
  "deletedAnswerId": 101,
  "questionStatus": "pending"
}`
        }
      ]
    },
    recipes: {
      title: 'Recipe Management',
      description: 'Manage recipes and recipe content',
      endpoints: [
        {
          title: 'Get Recipe by ID',
          method: 'GET',
          url: '/protected/staff/recipes',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/staff/recipes',
          description: 'Retrieve a specific recipe by its ID',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'STAFF',
          permissions: ['MANAGE_RECIPES'],
          parameters: [
            { name: 'id', type: 'number', description: 'The ID of the recipe', required: true }
          ],
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/protected/staff/recipes?id=1', {
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
          url: '/protected/staff/recipes',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/staff/recipes',
          description: 'Retrieve all free recipes with pagination',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'STAFF',
          permissions: ['MANAGE_RECIPES'],
          parameters: [
            { name: 'page', type: 'number', description: 'The page number (default = 1)', required: false },
            { name: 'size', type: 'number', description: 'Number of records per page (default = 10)', required: false }
          ],
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/protected/staff/recipes?page=1&size=10', {
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
          title: 'Get Recipes by Area',
          method: 'GET',
          url: '/protected/staff/recipes',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/staff/recipes',
          description: 'Retrieve recipes filtered by area/cuisine',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'STAFF',
          permissions: ['MANAGE_RECIPES'],
          parameters: [
            { name: 'area', type: 'string', description: 'The area/cuisine name', required: true },
            { name: 'page', type: 'number', description: 'The page number (default = 1)', required: false },
            { name: 'size', type: 'number', description: 'Number of records per page (default = 10)', required: false }
          ],
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/protected/staff/recipes?area=American&page=1&size=10', {
  method: 'GET',
  headers: {
    'Authorization': 'Bearer <token>',
    'Content-Type': 'application/json'
  }
})
.then(res => res.json())
.then(console.log);`
        },
        {
          title: 'Get Recipes by Category',
          method: 'GET',
          url: '/protected/staff/recipes',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/staff/recipes',
          description: 'Retrieve recipes filtered by category',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'STAFF',
          permissions: ['MANAGE_RECIPES'],
          parameters: [
            { name: 'category', type: 'string', description: 'The category name', required: true },
            { name: 'page', type: 'number', description: 'The page number (default = 1)', required: false },
            { name: 'size', type: 'number', description: 'Number of records per page (default = 10)', required: false }
          ],
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/protected/staff/recipes?category=Beef&page=1&size=10', {
  method: 'GET',
  headers: {
    'Authorization': 'Bearer <token>',
    'Content-Type': 'application/json'
  }
})
.then(res => res.json())
.then(console.log);`
        },
        {
          title: 'Discover Recipes',
          method: 'GET',
          url: '/protected/staff/discover/recipes',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/staff/discover/recipes',
          description: 'Search and discover recipes with multiple filters',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'STAFF',
          permissions: ['MANAGE_RECIPES'],
          parameters: [
            { name: 'keyword', type: 'string', description: 'Search keyword', required: false },
            { name: 'category', type: 'string', description: 'Recipe category', required: false },
            { name: 'area', type: 'string', description: 'Recipe area/cuisine', required: false },
            { name: 'recipedBy', type: 'number', description: 'Creator\'s user ID', required: false },
            { name: 'page', type: 'number', description: 'The page number (default = 1)', required: false },
            { name: 'size', type: 'number', description: 'Number of records per page (default = 10)', required: false }
          ],
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/protected/staff/discover/recipes?keyword=chicken&category=Main&area=American&page=1&size=10', {
  method: 'GET',
  headers: {
    'Authorization': 'Bearer <token>',
    'Content-Type': 'application/json'
  }
})
.then(res => res.json())
.then(console.log);`
        },
        {
          title: 'Create Recipe',
          method: 'POST',
          url: '/protected/staff/recipes',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/staff/recipes',
          description: 'Create a new recipe with form-data (including image upload)',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'STAFF',
          permissions: ['MANAGE_RECIPES'],
          contentType: 'multipart/form-data',
          requestBody: {
            name: "ABCD",
            category: "Dessert",
            area: "French",
            instructions: "Mix the ingredients",
            accessType: "FREE",
            prepareTime: "20 minutes",
            cookingTime: "30 minutes",
            yield: "8 servings",
            shortDescription: "Delicious tart",
            image: "beef.jpg",
            publishedOn: "2025-05-19",
            recipedBy: "1",
            ingredients: "Sugar, Salt, Fish Sauce"
          },
          validation: {
            required: ['name', 'category', 'area', 'instructions', 'accessType', 'prepareTime', 'cookingTime', 'yield', 'shortDescription', 'publishedOn', 'recipedBy', 'ingredients'],
            minLength: {
              name: 3,
              instructions: 10,
              shortDescription: 10,
              ingredients: 5
            },
            maxLength: {
              name: 100,
              shortDescription: 500,
              instructions: 5000,
              ingredients: 1000
            },
            enum: {
              accessType: ['FREE', 'PREMIUM'],
              category: ['Beef', 'Chicken', 'Dessert', 'Lamb', 'Miscellaneous', 'Pasta', 'Pork', 'Seafood', 'Side', 'Starter', 'Vegan', 'Vegetarian'],
              area: ['American', 'British', 'Canadian', 'Chinese', 'Croatian', 'Dutch', 'Egyptian', 'French', 'Greek', 'Indian', 'Irish', 'Italian', 'Jamaican', 'Japanese', 'Kenyan', 'Malaysian', 'Mexican', 'Moroccan', 'Polish', 'Portuguese', 'Russian', 'Spanish', 'Thai', 'Tunisian', 'Turkish', 'Unknown', 'Vietnamese']
            },
            pattern: {
              publishedOn: /^\d{4}-\d{2}-\d{2}$/,
              prepareTime: /^\d+\s(minutes?|hours?|hrs?)$/i,
              cookingTime: /^\d+\s(minutes?|hours?|hrs?)$/i,
              yield: /^\d+\s(servings?|portions?)$/i
            },
            fileTypes: {
              image: ['jpg', 'jpeg', 'png', 'gif', 'webp']
            }
          },
          parameters: [],
          sampleRequest: `const formData = new FormData();
formData.append('name', 'ABCD');
formData.append('category', 'Dessert');
formData.append('area', 'French');
formData.append('instructions', 'Mix the ingredients');
formData.append('accessType', 'FREE');
formData.append('prepareTime', '20 minutes');
formData.append('cookingTime', '30 minutes');
formData.append('yield', '8 servings');
formData.append('shortDescription', 'Delicious tart');
formData.append('image', fileInput.files[0]); // File object
formData.append('publishedOn', '2025-05-19');
formData.append('recipedBy', '1');
formData.append('ingredients', 'Sugar, Salt, Fish Sauce');

fetch('http://localhost:8080/JamesThewWebApplication/api/protected/staff/recipes', {
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
  "message": "Recipe created successfully",
  "data": {
    "id": 156,
    "name": "ABCD",
    "category": "Dessert",
    "area": "French",
    "instructions": "Mix the ingredients",
    "accessType": "FREE",
    "prepareTime": "20 minutes",
    "cookingTime": "30 minutes",
    "yield": "8 servings",
    "shortDescription": "Delicious tart",
    "image": "http://localhost:8080/JamesThewWebApplication/api/images/recipes/beef.jpg",
    "publishedOn": "2025-05-19",
    "recipedBy": 1,
    "ingredients": "Sugar, Salt, Fish Sauce",
    "createdAt": "2025-07-15T10:30:00Z"
  }
}`
        },
        {
          title: 'Update Recipe',
          method: 'PUT',
          url: '/protected/staff/recipes',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/staff/recipes',
          description: 'Update an existing recipe with form-data (including image upload)',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'STAFF',
          permissions: ['MANAGE_RECIPES'],
          contentType: 'multipart/form-data',
          requestBody: {
            id: "156",
            name: "Updated ABCD Recipe",
            category: "Dessert",
            area: "French",
            instructions: "Mix the ingredients thoroughly and bake",
            accessType: "PREMIUM",
            prepareTime: "25 minutes",
            cookingTime: "35 minutes",
            yield: "10 servings",
            shortDescription: "Delicious updated tart recipe",
            ingredients: "Sugar, Salt, Fish Sauce, Vanilla Extract"
          },
          validation: {
            required: ['id'],
            optional: ['name', 'category', 'area', 'instructions', 'accessType', 'prepareTime', 'cookingTime', 'yield', 'shortDescription', 'image', 'ingredients'],
            minLength: {
              name: 3,
              instructions: 10,
              shortDescription: 10,
              ingredients: 5
            },
            maxLength: {
              name: 100,
              shortDescription: 500,
              instructions: 5000,
              ingredients: 1000
            },
            enum: {
              accessType: ['FREE', 'PREMIUM'],
              category: ['Beef', 'Chicken', 'Dessert', 'Lamb', 'Miscellaneous', 'Pasta', 'Pork', 'Seafood', 'Side', 'Starter', 'Vegan', 'Vegetarian'],
              area: ['American', 'British', 'Canadian', 'Chinese', 'Croatian', 'Dutch', 'Egyptian', 'French', 'Greek', 'Indian', 'Irish', 'Italian', 'Jamaican', 'Japanese', 'Kenyan', 'Malaysian', 'Mexican', 'Moroccan', 'Polish', 'Portuguese', 'Russian', 'Spanish', 'Thai', 'Tunisian', 'Turkish', 'Unknown', 'Vietnamese']
            },
            pattern: {
              publishedOn: /^\d{4}-\d{2}-\d{2}$/,
              prepareTime: /^\d+\s(minutes?|hours?|hrs?)$/i,
              cookingTime: /^\d+\s(minutes?|hours?|hrs?)$/i,
              yield: /^\d+\s(servings?|portions?)$/i
            },
            fileTypes: {
              image: ['jpg', 'jpeg', 'png', 'gif', 'webp']
            }
          },
          parameters: [],
          sampleRequest: `const formData = new FormData();
formData.append('id', '156');
formData.append('name', 'Updated ABCD Recipe');
formData.append('category', 'Dessert');
formData.append('area', 'French');
formData.append('instructions', 'Mix the ingredients thoroughly and bake');
formData.append('accessType', 'PREMIUM');
formData.append('prepareTime', '25 minutes');
formData.append('cookingTime', '35 minutes');
formData.append('yield', '10 servings');
formData.append('shortDescription', 'Delicious updated tart recipe');
formData.append('ingredients', 'Sugar, Salt, Fish Sauce, Vanilla Extract');
// formData.append('image', fileInput.files[0]); // Optional: new image file

fetch('http://localhost:8080/JamesThewWebApplication/api/protected/staff/recipes', {
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
  "message": "Recipe updated successfully",
  "data": {
    "id": 156,
    "name": "Updated ABCD Recipe",
    "category": "Dessert",
    "area": "French",
    "instructions": "Mix the ingredients thoroughly and bake",
    "accessType": "PREMIUM",
    "prepareTime": "25 minutes",
    "cookingTime": "35 minutes",
    "yield": "10 servings",
    "shortDescription": "Delicious updated tart recipe",
    "ingredients": "Sugar, Salt, Fish Sauce, Vanilla Extract",
    "updatedAt": "2025-07-15T11:00:00Z"
  }
}`
        },
        {
          title: 'Delete Recipe',
          method: 'DELETE',
          url: '/protected/staff/recipes',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/staff/recipes',
          description: 'Delete a recipe',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'STAFF',
          permissions: ['MANAGE_RECIPES'],
          requestBody: {
            id: "156"
          },
          validation: {
            required: ['id'],
            type: {
              id: 'number'
            }
          },
          parameters: [],
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/protected/staff/recipes', {
  method: 'DELETE',
  headers: {
    'Authorization': 'Bearer <token>',
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    id: 156
  })
})
.then(res => res.json())
.then(console.log);`,
          sampleResponse: `{
  "success": true,
  "status": 200,
  "message": "Recipe deleted successfully",
  "deletedRecipeId": 156
}`
        }
      ]
    },
    moderation: {
      title: 'Content Moderation',
      description: 'Moderate user-generated content',
      endpoints: [
        {
          title: 'Get Flagged Content',
          method: 'GET',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/staff/moderation/flagged',
          description: 'Retrieve content flagged for moderation',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'STAFF',
          permissions: ['MODERATE_CONTENT'],
          parameters: [
            { name: 'type', type: 'string', description: 'Content type (recipe, comment, etc.)', required: false },
            { name: 'status', type: 'string', description: 'Moderation status', required: false }
          ],
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/protected/staff/moderation/flagged?type=comment', {
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
  "flaggedContent": [
    {
      "id": 123,
      "type": "comment",
      "contentId": 456,
      "content": "Inappropriate comment content...",
      "flaggedBy": 789,
      "reason": "Inappropriate language",
      "status": "pending",
      "flaggedAt": "2024-03-15T10:30:00Z"
    }
  ]
}`
        },
        {
          title: 'Moderate Content',
          method: 'POST',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/staff/moderation/action',
          description: 'Take action on flagged content',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'STAFF',
          permissions: ['MODERATE_CONTENT'],
          requestBody: {
            flagId: 123,
            action: "remove",
            reason: "Violates community guidelines"
          },
          parameters: [],
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/protected/staff/moderation/action', {
  method: 'POST',
  headers: {
    'Authorization': 'Bearer <token>',
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    flagId: 123,
    action: "remove",
    reason: "Violates community guidelines"
  })
})
.then(res => res.json())
.then(console.log);`,
          sampleResponse: `{
  "success": true,
  "message": "Moderation action completed",
  "action": "remove",
  "moderatedBy": 456,
  "timestamp": "2024-03-15T10:30:00Z"
}`
        }
      ]
    }
  }
};
