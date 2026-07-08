// User returned by the backend (never contains a password)
export interface User {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  role: string;
  createdAt?: string;
  updatedAt?: string;
}

// Request payload for user registration
export interface RegisterPayload {
  firstName: string;
  lastName: string;
  email: string;
  password: string;
}

// Request payload for user login
export interface LoginPayload {
  email: string;
  password: string;
}

// Request payload for updating user details
export interface UpdateUserPayload {
  firstName: string;
  lastName: string;
  email: string;
}

// Request payload for changing password
export interface ChangePasswordPayload {
  currentPassword: string;
  newPassword: string;
  confirmPassword?: string;
}
