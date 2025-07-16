// Token management utilities for role-based authentication
export const TokenManager = {
  // Keys for localStorage based on roles
  TOKEN_KEYS: {
    ADMIN: 'jamesThewWebApp_adminToken',
    GENERAL: 'jamesThewWebApp_generalToken',
    SUBSCRIBER: 'jamesThewWebApp_subscriberToken',
    STAFF: 'jamesThewWebApp_staffToken',
    STAFF_LIMITED: 'jamesThewWebApp_staffLimitedToken',
    WRITER: 'jamesThewWebApp_writerToken'
  },
  
  // Save token to localStorage by role
  saveToken: (token, role) => {
    if (token && token.trim() && role) {
      const key = TokenManager.TOKEN_KEYS[role.toUpperCase()];
      if (key) {
        localStorage.setItem(key, token.trim());
        return true;
      }
    }
    return false;
  },
  
  // Get token from localStorage by role
  getToken: (role) => {
    if (!role) return '';
    const key = TokenManager.TOKEN_KEYS[role.toUpperCase()];
    return key ? localStorage.getItem(key) || '' : '';
  },
  
  // Remove token from localStorage by role
  clearToken: (role) => {
    if (!role) return;
    const key = TokenManager.TOKEN_KEYS[role.toUpperCase()];
    if (key) {
      localStorage.removeItem(key);
    }
  },
  
  // Clear all tokens
  clearAllTokens: () => {
    Object.values(TokenManager.TOKEN_KEYS).forEach(key => {
      localStorage.removeItem(key);
    });
  },
  
  // Check if token exists for role
  hasToken: (role) => {
    const token = TokenManager.getToken(role);
    return token && token.trim().length > 0;
  },
  
  // Get all stored tokens with their roles
  getAllTokens: () => {
    const tokens = {};
    Object.entries(TokenManager.TOKEN_KEYS).forEach(([role, key]) => {
      const token = localStorage.getItem(key);
      if (token) {
        tokens[role] = token;
      }
    });
    return tokens;
  },
  
  // Get authorization header object for specific role
  getAuthHeader: (role) => {
    const token = TokenManager.getToken(role);
    if (token) {
      return { 'Authorization': `Bearer ${token}` };
    }
    return {};
  }
};

// Event to notify components when token changes
export const createTokenChangeEvent = (token) => {
  window.dispatchEvent(new CustomEvent('tokenChanged', { detail: { token } }));
};

// Login function that handles token storage
export const handleLogin = async (credentials) => {
  try {
    const response = await fetch('http://localhost:8080/JamesThewWebApplication/api/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(credentials)
    });
    
    const data = await response.json();
    
    if (data.success && data.data) {
      // Save token automatically - token is in data.data
      TokenManager.saveToken(data.data);
      // Notify other components
      createTokenChangeEvent(data.data);
      return { success: true, data };
    }
    
    return { success: false, error: data.message || 'Login failed' };
  } catch (error) {
    return { success: false, error: error.message };
  }
};
