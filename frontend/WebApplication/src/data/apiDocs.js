// Import all endpoint modules
import { publicEndpoints } from './endpoints/publicEndpoints.js';
import { adminEndpoints } from './endpoints/adminEndpoints.js';
import { staffEndpoints } from './endpoints/staffEndpoints.js';
import { writerEndpoints } from './endpoints/writerEndpoints.js';
import { subscriberEndpoints } from './endpoints/subscriberEndpoints.js';
import { generalEndpoints } from './endpoints/generalEndpoints.js';

// Combine all endpoints into a single export
export const apiDocs = {
  public: publicEndpoints,
  admin: adminEndpoints,
  staff: staffEndpoints,
  writer: writerEndpoints,
  subscriber: subscriberEndpoints,
  general: generalEndpoints
};

// Export individual endpoints for direct access if needed
export {
  publicEndpoints,
  adminEndpoints,
  staffEndpoints,
  writerEndpoints,
  subscriberEndpoints,
  generalEndpoints
};
