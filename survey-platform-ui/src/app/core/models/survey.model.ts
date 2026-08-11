export interface Survey {
  id?: number;
  name: string;
  description: string;
  status: 'DRAFT' | 'LIVE' | 'CLOSED';
}

export interface SurveyPayload {
  name: string;
  description: string;
}
