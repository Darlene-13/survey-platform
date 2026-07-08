export interface Certificate {
  id: number;
  fileName: string;
}

export interface SurveyResponse {
  responseId: number;
  /** Dynamic answers keyed by question name, e.g. { full_name: 'Jane Doe' } */
  answers: Record<string, string>;
  certificates: Certificate[];
  dateResponded: string;
}

export interface PagedResponses {
  currentPage: number;
  lastPage: number;
  pageSize: number;
  totalCount: number;
  items: SurveyResponse[];
}

export interface ResponseQuery {
  page: number;
  pageSize: number;
  email?: string;
}
