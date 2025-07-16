export const adminEndpoints = {
  title: 'Admin Endpoints',
  description: 'Administrative functions - requires ADMIN role',
  accessLevel: 'private',
  role: 'ADMIN',
  subcategories: {
    areas: {
      title: 'Areas Management',
      description: 'Manage recipe areas and cuisines',
      endpoints: [
        {
          title: 'Get All Areas',
          method: 'GET',
          url: '/protected/admin/areas',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/admin/areas',
          description: 'Retrieve all recipe areas',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'ADMIN',
          permissions: [],
          parameters: [],
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/protected/admin/areas', {
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
  "message": "All areas fetched successfully",
  "data": [
    {
      "id": 1,
      "name": "American"
    },
    {
      "id": 2,
      "name": "British"
    }
  ]
}`
        },
        {
          title: 'Get Area by ID',
          method: 'GET',
          url: '/protected/admin/areas',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/admin/areas',
          description: 'Retrieve area by ID',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'ADMIN',
          permissions: [],
          parameters: [
            { name: 'id', type: 'number', description: 'Area ID', required: true }
          ],
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/protected/admin/areas?id=2', {
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
  "message": "Area fetched successfully",
  "data": {
    "id": 2,
    "name": "British"
  }
}`
        },
        {
          title: 'Get Area by Name',
          method: 'GET',
          url: '/protected/admin/areas',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/admin/areas',
          description: 'Retrieve area by name',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'ADMIN',
          permissions: [],
          parameters: [
            { name: 'name', type: 'string', description: 'Area name', required: true }
          ],
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/protected/admin/areas?name=British', {
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
  "message": "Area fetched successfully",
  "data": {
    "id": 2,
    "name": "British"
  }
}`
        },
        {
          title: 'Create Area',
          method: 'POST',
          url: '/protected/admin/areas',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/admin/areas',
          description: 'Create a new recipe area',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'ADMIN',
          permissions: [],
          requestBody: {
            name: "Chinese"
          },
          validation: {
            required: ['name'],
            minLength: {
              name: 2
            },
            maxLength: {
              name: 50
            }
          },
          parameters: [],
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/protected/admin/areas', {
  method: 'POST',
  headers: {
    'Authorization': 'Bearer <token>',
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    name: "Chinese"
  })
})
.then(res => res.json())
.then(console.log);`,
          sampleResponse: `{
  "success": true,
  "status": 200,
  "message": "Area added successfully"
}`
        },
        {
          title: 'Update Area',
          method: 'PUT',
          url: '/protected/admin/areas',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/admin/areas',
          description: 'Update an existing area',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'ADMIN',
          permissions: [],
          requestBody: {
            id: 34,
            name: "Chinese"
          },
          validation: {
            required: ['id', 'name'],
            type: {
              id: 'number'
            },
            minLength: {
              name: 2
            },
            maxLength: {
              name: 50
            }
          },
          parameters: [],
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/protected/admin/areas', {
  method: 'PUT',
  headers: {
    'Authorization': 'Bearer <token>',
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    id: 34,
    name: "Chinese"
  })
})
.then(res => res.json())
.then(console.log);`,
          sampleResponse: `{
  "success": true,
  "status": 200,
  "message": "Area updated successfully"
}`
        },
        {
          title: 'Delete Area',
          method: 'DELETE',
          url: '/protected/admin/areas',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/admin/areas',
          description: 'Delete an area',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'ADMIN',
          permissions: [],
          requestBody: {
            id: 34
          },
          validation: {
            required: ['id'],
            type: {
              id: 'number'
            }
          },
          parameters: [],
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/protected/admin/areas', {
  method: 'DELETE',
  headers: {
    'Authorization': 'Bearer <token>',
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    id: 34
  })
})
.then(res => res.json())
.then(console.log);`,
          sampleResponse: `{
  "success": true,
  "status": 200,
  "message": "Area deleted successfully"
}`
        }
      ]
    },
    announcements: {
      title: 'Announcements Management',
      description: 'Manage contest announcements and winners',
      endpoints: [
        {
          title: 'Get All Announcements',
          method: 'GET',
          url: '/protected/admin/announcements',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/admin/announcements',
          description: 'Retrieve all announcements',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'ADMIN',
          permissions: [],
          parameters: [],
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/protected/admin/announcements', {
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
  "message": "Announcements fetched successfully",
  "data": [
    {
      "id": 1,
      "title": "ABCD",
      "announcementDate": "2025-07-16",
      "description": "ABCD",
      "contest": {
        "id": 1,
        "headline": "'Let's Do Lunch' Recipe Contest Announcement",
        "description": "Who's ready for lunch? Show off your favorite lunch recipes for the chance to win $500.",
        "datePublished": "2025-03-01",
        "isFree": true,
        "isClosed": false,
        "accessRole": "GENERAL"
      },
      "winners": [
        {
          "id": 1,
          "contestEntry": {
            "id": 1,
            "contestId": 1,
            "userId": 2,
            "name": "Banh Mi Cha",
            "image": "banh-mi-cha-1751377985199.jpg",
            "status": "REVIEWED"
          },
          "ranking": "GRAND_PRIZE"
        }
      ]
    }
  ]
}`
        },
        {
          title: 'Get Announcement by ID',
          method: 'GET',
          url: '/protected/admin/announcements',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/admin/announcements',
          description: 'Retrieve announcement by ID',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'ADMIN',
          permissions: [],
          parameters: [
            { name: 'id', type: 'number', description: 'Announcement ID', required: true }
          ],
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/protected/admin/announcements?id=1', {
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
  "message": "Announcement found",
  "data": {
    "id": 1,
    "title": "ABCD",
    "announcementDate": "2025-07-16",
    "description": "ABCD",
    "contest": {
      "id": 1,
      "headline": "'Let's Do Lunch' Recipe Contest Announcement",
      "description": "Who's ready for lunch? Show off your favorite lunch recipes for the chance to win $500."
    },
    "winners": []
  }
}`
        },
        {
          title: 'Create Announcement',
          method: 'POST',
          url: '/protected/admin/announcements',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/admin/announcements',
          description: 'Create a new announcement',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'ADMIN',
          permissions: [],
          requestBody: {
            title: "ABC",
            description: "AAAAA",
            contestId: 1,
            winners: [
              {
                contestEntryId: 1,
                ranking: "GRAND_PRIZE"
              },
              {
                contestEntryId: 4,
                ranking: "GRAND_PRIZE"
              }
            ]
          },
          validation: {
            required: ['title', 'description', 'contestId'],
            optional: ['winners'],
            type: {
              contestId: 'number'
            },
            minLength: {
              title: 3,
              description: 5
            },
            maxLength: {
              title: 200,
              description: 1000
            },
            enum: {
              'winners.ranking': ['GRAND_PRIZE', 'FIRST_PLACE', 'SECOND_PLACE', 'THIRD_PLACE']
            }
          },
          parameters: [],
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/protected/admin/announcements', {
  method: 'POST',
  headers: {
    'Authorization': 'Bearer <token>',
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    title: "ABC",
    description: "AAAAA",
    contestId: 1,
    winners: [
      {
        contestEntryId: 1,
        ranking: "GRAND_PRIZE"
      }
    ]
  })
})
.then(res => res.json())
.then(console.log);`,
          sampleResponse: `{
  "success": true,
  "status": 201,
  "message": "Announcement created successfully"
}`
        },
        {
          title: 'Update Announcement',
          method: 'PUT',
          url: '/protected/admin/announcements',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/admin/announcements',
          description: 'Update an existing announcement',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'ADMIN',
          permissions: [],
          requestBody: {
            id: 1,
            title: "ABCD",
            description: "ABCD",
            contestId: 1,
            winners: [
              {
                announcementId: 1,
                contestEntryId: 1,
                ranking: "GRAND_PRIZE"
              },
              {
                announcementId: 1,
                contestEntryId: 4,
                ranking: "GRAND_PRIZE"
              }
            ]
          },
          validation: {
            required: ['id'],
            optional: ['title', 'description', 'contestId', 'winners'],
            type: {
              id: 'number',
              contestId: 'number'
            },
            minLength: {
              title: 3,
              description: 5
            },
            maxLength: {
              title: 200,
              description: 1000
            },
            enum: {
              'winners.ranking': ['GRAND_PRIZE', 'FIRST_PLACE', 'SECOND_PLACE', 'THIRD_PLACE']
            }
          },
          parameters: [],
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/protected/admin/announcements', {
  method: 'PUT',
  headers: {
    'Authorization': 'Bearer <token>',
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    id: 1,
    title: "ABCD",
    description: "ABCD",
    contestId: 1,
    winners: [
      {
        announcementId: 1,
        contestEntryId: 1,
        ranking: "GRAND_PRIZE"
      }
    ]
  })
})
.then(res => res.json())
.then(console.log);`,
          sampleResponse: `{
  "success": true,
  "status": 200,
  "message": "Announcement updated successfully"
}`
        },
        {
          title: 'Delete Announcement',
          method: 'DELETE',
          url: '/protected/admin/announcements',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/admin/announcements',
          description: 'Delete an announcement',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'ADMIN',
          permissions: [],
          requestBody: {
            id: 1
          },
          validation: {
            required: ['id'],
            type: {
              id: 'number'
            }
          },
          parameters: [],
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/protected/admin/announcements', {
  method: 'DELETE',
  headers: {
    'Authorization': 'Bearer <token>',
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    id: 1
  })
})
.then(res => res.json())
.then(console.log);`,
          sampleResponse: `{
  "success": true,
  "status": 200,
  "message": "Announcement deleted successfully"
}`
        }
      ]
    },
    permissions: {
      title: 'Permissions Management',
      description: 'Manage system permissions',
      endpoints: [
        {
          title: 'Get All Permissions',
          method: 'GET',
          url: '/protected/admin/permissions',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/admin/permissions',
          description: 'Retrieve all permissions',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'ADMIN',
          permissions: [],
          parameters: [],
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/protected/admin/permissions', {
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
  "message": "All permissions fetched successfully",
  "data": [
    {
      "id": 1,
      "name": "MANAGE_CONTESTS",
      "description": "Can manage and moderate cooking contests"
    },
    {
      "id": 2,
      "name": "ANSWER_QUESTIONS",
      "description": "Can answer user questions and provide support"
    }
  ]
}`
        },
        {
          title: 'Get Permission by ID',
          method: 'GET',
          url: '/protected/admin/permissions',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/admin/permissions',
          description: 'Retrieve permission by ID',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'ADMIN',
          permissions: [],
          parameters: [
            { name: 'id', type: 'number', description: 'Permission ID', required: true }
          ],
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/protected/admin/permissions?id=2', {
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
  "message": "Permission fetched successfully",
  "data": {
    "id": 2,
    "name": "ANSWER_QUESTIONS",
    "description": "Can answer user questions and provide support"
  }
}`
        },
        {
          title: 'Get Permission by Name',
          method: 'GET',
          url: '/protected/admin/permissions',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/admin/permissions',
          description: 'Retrieve permission by name',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'ADMIN',
          permissions: [],
          parameters: [
            { name: 'name', type: 'string', description: 'Permission name', required: true }
          ],
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/protected/admin/permissions?name=MANAGE_CONTESTS', {
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
  "message": "Permission fetched successfully",
  "data": {
    "id": 1,
    "name": "MANAGE_CONTESTS",
    "description": "Can manage and moderate cooking contests"
  }
}`
        },
        {
          title: 'Create Permission',
          method: 'POST',
          url: '/protected/admin/permissions',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/admin/permissions',
          description: 'Create a new permission',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'ADMIN',
          permissions: [],
          requestBody: {
            name: "APPROVE_ARTICLES",
            description: "Can review and approve articles submitted by writers"
          },
          validation: {
            required: ['name', 'description'],
            minLength: {
              name: 3,
              description: 10
            },
            maxLength: {
              name: 50,
              description: 200
            },
            pattern: {
              name: /^[A-Z_]+$/
            }
          },
          parameters: [],
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/protected/admin/permissions', {
  method: 'POST',
  headers: {
    'Authorization': 'Bearer <token>',
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    name: "APPROVE_ARTICLES",
    description: "Can review and approve articles submitted by writers"
  })
})
.then(res => res.json())
.then(console.log);`,
          sampleResponse: `{
  "success": true,
  "status": 200,
  "message": "Permission added successfully"
}`
        },
        {
          title: 'Update Permission',
          method: 'PUT',
          url: '/protected/admin/permissions',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/admin/permissions',
          description: 'Update an existing permission',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'ADMIN',
          permissions: [],
          requestBody: {
            id: 3,
            name: "APPROVE_ARTICLES",
            description: "Can review and approve articles submitted by writers"
          },
          validation: {
            required: ['id'],
            optional: ['name', 'description'],
            type: {
              id: 'number'
            },
            minLength: {
              name: 3,
              description: 10
            },
            maxLength: {
              name: 50,
              description: 200
            },
            pattern: {
              name: /^[A-Z_]+$/
            }
          },
          parameters: [],
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/protected/admin/permissions', {
  method: 'PUT',
  headers: {
    'Authorization': 'Bearer <token>',
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    id: 3,
    name: "APPROVE_ARTICLES",
    description: "Can review and approve articles submitted by writers"
  })
})
.then(res => res.json())
.then(console.log);`,
          sampleResponse: `{
  "success": true,
  "status": 200,
  "message": "Permission updated successfully"
}`
        },
        {
          title: 'Delete Permission',
          method: 'DELETE',
          url: '/protected/admin/permissions',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/admin/permissions',
          description: 'Delete a permission',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'ADMIN',
          permissions: [],
          requestBody: {
            id: 3
          },
          validation: {
            required: ['id'],
            type: {
              id: 'number'
            }
          },
          parameters: [],
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/protected/admin/permissions', {
  method: 'DELETE',
  headers: {
    'Authorization': 'Bearer <token>',
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    id: 3
  })
})
.then(res => res.json())
.then(console.log);`,
          sampleResponse: `{
  "success": true,
  "status": 200,
  "message": "Permission deleted successfully"
}`
        }
      ]
    },
    roles: {
      title: 'Roles Management',
      description: 'Manage system roles',
      endpoints: [
        {
          title: 'Get All Roles',
          method: 'GET',
          url: '/protected/admin/roles',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/admin/roles',
          description: 'Retrieve all roles',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'ADMIN',
          permissions: [],
          parameters: [],
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/protected/admin/roles', {
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
  "message": "All roles fetched successfully",
  "data": [
    {
      "id": 1,
      "name": "ADMIN"
    },
    {
      "id": 2,
      "name": "GENERAL"
    }
  ]
}`
        },
        {
          title: 'Get Role by ID',
          method: 'GET',
          url: '/protected/admin/roles',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/admin/roles',
          description: 'Retrieve role by ID',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'ADMIN',
          permissions: [],
          parameters: [
            { name: 'id', type: 'number', description: 'Role ID', required: true }
          ],
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/protected/admin/roles?id=2', {
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
  "message": "Role fetched successfully",
  "data": {
    "id": 2,
    "name": "STAFF"
  }
}`
        },
        {
          title: 'Get Role by Name',
          method: 'GET',
          url: '/protected/admin/roles',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/admin/roles',
          description: 'Retrieve role by name',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'ADMIN',
          permissions: [],
          parameters: [
            { name: 'name', type: 'string', description: 'Role name', required: true }
          ],
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/protected/admin/roles?name=STAFF', {
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
  "message": "Role fetched successfully",
  "data": {
    "id": 2,
    "name": "STAFF"
  }
}`
        },
        {
          title: 'Create Role',
          method: 'POST',
          url: '/protected/admin/roles',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/admin/roles',
          description: 'Create a new role',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'ADMIN',
          permissions: [],
          requestBody: {
            name: "WRITER"
          },
          validation: {
            required: ['name'],
            minLength: {
              name: 3
            },
            maxLength: {
              name: 20
            },
            pattern: {
              name: /^[A-Z_]+$/
            }
          },
          parameters: [],
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/protected/admin/roles', {
  method: 'POST',
  headers: {
    'Authorization': 'Bearer <token>',
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    name: "WRITER"
  })
})
.then(res => res.json())
.then(console.log);`,
          sampleResponse: `{
  "success": true,
  "status": 200,
  "message": "Role added successfully"
}`
        },
        {
          title: 'Update Role',
          method: 'PUT',
          url: '/protected/admin/roles',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/admin/roles',
          description: 'Update an existing role',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'ADMIN',
          permissions: [],
          requestBody: {
            id: 4,
            name: "WRITER"
          },
          validation: {
            required: ['id'],
            optional: ['name'],
            type: {
              id: 'number'
            },
            minLength: {
              name: 3
            },
            maxLength: {
              name: 20
            },
            pattern: {
              name: /^[A-Z_]+$/
            }
          },
          parameters: [],
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/protected/admin/roles', {
  method: 'PUT',
  headers: {
    'Authorization': 'Bearer <token>',
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    id: 4,
    name: "WRITER"
  })
})
.then(res => res.json())
.then(console.log);`,
          sampleResponse: `{
  "success": true,
  "status": 200,
  "message": "Role updated successfully"
}`
        },
        {
          title: 'Delete Role',
          method: 'DELETE',
          url: '/protected/admin/roles',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/admin/roles',
          description: 'Delete a role',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'ADMIN',
          permissions: [],
          requestBody: {
            id: 4
          },
          validation: {
            required: ['id'],
            type: {
              id: 'number'
            }
          },
          parameters: [],
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/protected/admin/roles', {
  method: 'DELETE',
  headers: {
    'Authorization': 'Bearer <token>',
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    id: 4
  })
})
.then(res => res.json())
.then(console.log);`,
          sampleResponse: `{
  "success": true,
  "status": 200,
  "message": "Role deleted successfully"
}`
        }
      ]
    },
    userRoles: {
      title: 'User-Roles Management',
      description: 'Assign and manage user roles',
      endpoints: [
        {
          title: 'Assign Role to User',
          method: 'POST',
          url: '/protected/admin/user-roles',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/admin/user-roles',
          description: 'Assign a role to a user',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'ADMIN',
          permissions: [],
          requestBody: {
            userId: 2,
            roleId: 3
          },
          validation: {
            required: ['userId', 'roleId'],
            type: {
              userId: 'number',
              roleId: 'number'
            }
          },
          parameters: [],
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/protected/admin/user-roles', {
  method: 'POST',
  headers: {
    'Authorization': 'Bearer <token>',
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    userId: 2,
    roleId: 3
  })
})
.then(res => res.json())
.then(console.log);`,
          sampleResponse: `{
  "success": true,
  "status": 200,
  "message": "Role assigned successfully"
}`
        },
        {
          title: 'Remove Role from User',
          method: 'DELETE',
          url: '/protected/admin/user-roles',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/admin/user-roles',
          description: 'Remove a role from a user',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'ADMIN',
          permissions: [],
          requestBody: {
            userId: 2,
            roleId: 5
          },
          validation: {
            required: ['userId', 'roleId'],
            type: {
              userId: 'number',
              roleId: 'number'
            }
          },
          parameters: [],
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/protected/admin/user-roles', {
  method: 'DELETE',
  headers: {
    'Authorization': 'Bearer <token>',
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    userId: 2,
    roleId: 5
  })
})
.then(res => res.json())
.then(console.log);`,
          sampleResponse: `{
  "success": true,
  "status": 200,
  "message": "Role deleted successfully"
}`
        }
      ]
    },
    staffPermissions: {
      title: 'Staff-Permissions Management',
      description: 'Assign and manage staff permissions',
      endpoints: [
        {
          title: 'Assign Permission to Staff',
          method: 'POST',
          url: '/protected/admin/staff-permissions',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/admin/staff-permissions',
          description: 'Assign a permission to a staff member',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'ADMIN',
          permissions: [],
          requestBody: {
            userId: 2,
            permissionId: 3
          },
          validation: {
            required: ['userId', 'permissionId'],
            type: {
              userId: 'number',
              permissionId: 'number'
            }
          },
          parameters: [],
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/protected/admin/staff-permissions', {
  method: 'POST',
  headers: {
    'Authorization': 'Bearer <token>',
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    userId: 2,
    permissionId: 3
  })
})
.then(res => res.json())
.then(console.log);`,
          sampleResponse: `{
  "success": true,
  "status": 200,
  "message": "Permission assigned successfully"
}`
        },
        {
          title: 'Remove Permission from Staff',
          method: 'DELETE',
          url: '/protected/admin/user-permissions',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/admin/user-permissions',
          description: 'Remove a permission from a staff member',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'ADMIN',
          permissions: [],
          requestBody: {
            userId: 2,
            roleId: 5
          },
          validation: {
            required: ['userId', 'roleId'],
            type: {
              userId: 'number',
              roleId: 'number'
            }
          },
          parameters: [],
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/protected/admin/user-permissions', {
  method: 'DELETE',
  headers: {
    'Authorization': 'Bearer <token>',
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    userId: 2,
    roleId: 5
  })
})
.then(res => res.json())
.then(console.log);`,
          sampleResponse: `{
  "success": true,
  "status": 200,
  "message": "Permission deleted successfully"
}`
        }
      ]
    },
    users: {
      title: 'User Management',
      description: 'Manage user accounts and permissions',
      endpoints: [
        {
          title: 'Get All Users',
          method: 'GET',
          url: '/protected/admin/users',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/admin/users',
          description: 'Retrieve all user accounts',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'ADMIN',
          permissions: [],
          parameters: [
            { name: 'page', type: 'number', description: 'Page number for pagination', required: false },
            { name: 'limit', type: 'number', description: 'Number of users per page', required: false },
            { name: 'role', type: 'string', description: 'Filter by user role', required: false }
          ],
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/protected/admin/users?page=1&limit=10', {
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
  "users": [
    {
      "id": 1,
      "username": "admin_user",
      "email": "admin@example.com",
      "firstName": "Admin",
      "lastName": "User",
      "role": "ADMIN",
      "status": "active",
      "created": "2024-01-15T10:30:00Z",
      "lastLogin": "2024-03-15T10:30:00Z"
    }
  ],
  "totalUsers": 100,
  "currentPage": 1,
  "totalPages": 10
}`
        },
        {
          title: 'Update User Role',
          method: 'PUT',
          url: '/protected/admin/users/role',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/admin/users/role',
          description: 'Update a user\'s role',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'ADMIN',
          permissions: [],
          requestBody: {
            userId: 2,
            role: "STAFF"
          },
          parameters: [],
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/protected/admin/users/role', {
  method: 'PUT',
  headers: {
    'Authorization': 'Bearer <token>',
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    userId: 2,
    role: "STAFF"
  })
})
.then(res => res.json())
.then(console.log);`,
          sampleResponse: `{
  "success": true,
  "message": "User role updated successfully",
  "user": {
    "id": 2,
    "username": "staff_user",
    "role": "STAFF",
    "updated": "2024-03-15T10:30:00Z"
  }
}`
        },
        {
          title: 'Deactivate User',
          method: 'PUT',
          url: '/protected/admin/users/deactivate',
          endpoint: 'http://localhost:8080/JamesThewWebApplication/api/protected/admin/users/deactivate',
          description: 'Deactivate a user account',
          requiresAuth: true,
          accessLevel: 'private',
          role: 'ADMIN',
          permissions: [],
          requestBody: {
            userId: 3,
            reason: "Terms of service violation"
          },
          parameters: [],
          sampleRequest: `fetch('http://localhost:8080/JamesThewWebApplication/api/protected/admin/users/deactivate', {
  method: 'PUT',
  headers: {
    'Authorization': 'Bearer <token>',
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    userId: 3,
    reason: "Terms of service violation"
  })
})
.then(res => res.json())
.then(console.log);`,
          sampleResponse: `{
  "success": true,
  "message": "User account deactivated successfully",
  "user": {
    "id": 3,
    "status": "deactivated",
    "deactivatedAt": "2024-03-15T10:30:00Z",
    "reason": "Terms of service violation"
  }
}`
        }
      ]
    }
  }
};
