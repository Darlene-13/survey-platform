export type QuestionType = 'short_text' | 'long_text' | 'email' | 'choice' | 'file';

export interface QuestionOption {
  value: string; // e.g. 'REACT'
  label: string; // e.g. 'React JS'
}

export interface FileProperties {
  format: string; // '.pdf'
  maxFileSize: number; // 1
  maxFileSizeUnit: string; // 'mb'
  multiple: boolean;
}

export interface Question {
  id?: number;
  name: string; // machine name, e.g. 'full_name'
  type: QuestionType;
  required: boolean;
  text: string;
  description: string;
  multiple?: boolean; // only for 'choice'
  options?: QuestionOption[]; // only for 'choice'
  fileProperties?: FileProperties; // only for 'file'
}
