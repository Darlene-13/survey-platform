export const API_ROOT = '/api/v1';

export const apiRoutes = {
  auth: {
    register: `${API_ROOT}/auth/register`,
    login: `${API_ROOT}/auth/login`,
    refresh: `${API_ROOT}/auth/refresh`,
    logout: `${API_ROOT}/auth/logout`
  },
  surveys: {
    collection: `${API_ROOT}/surveys`,
    one: (surveyId: number) => `${API_ROOT}/surveys/${surveyId}`,
    questions: (surveyId: number) => `${API_ROOT}/surveys/${surveyId}/questions`,
    question: (surveyId: number, questionId: number) => `${API_ROOT}/surveys/${surveyId}/questions/${questionId}`,
    responses: (surveyId: number) => `${API_ROOT}/surveys/${surveyId}/responses`
  },
  certificates: {
    download: (certificateId: number) => `${API_ROOT}/certificates/${certificateId}`
  }
} as const;
