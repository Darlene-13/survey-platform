import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';
import { apiRoutes } from '../api-routes';
import { Question, QuestionOption } from '../models/question.model';
import { XmlParserService } from './xml-parser.service';

type WireQuestion = Omit<Question, 'required' | 'multiple' | 'options' | 'fileProperties' | 'sortOrder'> & {
  required: string; sortOrder?: string;
  options?: { multiple?: string; option?: WireOption | WireOption[] };
  fileProperties?: { format: string; maxFileSize: string; maxFileSizeUnit: string; multiple?: string };
};
type WireOption = { value: string; text?: string };

@Injectable({ providedIn: 'root' })
export class QuestionService {
  private readonly http = inject(HttpClient);
  private readonly xml = inject(XmlParserService);

  list(surveyId: number): Observable<Question[]> {
    return this.http.get(apiRoutes.surveys.questions(surveyId), { responseType: 'text' }).pipe(
      map(value => this.xml.parse<{ question?: WireQuestion | WireQuestion[] }>(value).question),
      map(value => (value === undefined ? [] : Array.isArray(value) ? value : [value]).map(question => this.fromWire(question)))
    );
  }

  get(surveyId: number, questionId: number): Observable<Question> {
    return this.http.get(apiRoutes.surveys.question(surveyId, questionId), { responseType: 'text' })
      .pipe(map(value => this.fromWire(this.xml.parse<WireQuestion>(value))));
  }
  create(surveyId: number, question: Question): Observable<Question> { return this.send('post', apiRoutes.surveys.questions(surveyId), question); }
  update(surveyId: number, questionId: number, question: Question): Observable<Question> { return this.send('put', apiRoutes.surveys.question(surveyId, questionId), question); }
  delete(surveyId: number, questionId: number): Observable<void> { return this.http.delete<void>(apiRoutes.surveys.question(surveyId, questionId)); }

  private send(method: 'post' | 'put', url: string, question: Question): Observable<Question> {
    return this.http[method](url, this.toXml(question), { responseType: 'text' })
      .pipe(map(value => this.fromWire(this.xml.parse<WireQuestion>(value))));
  }

  private fromWire(question: WireQuestion): Question {
    const options = question.options?.option;
    const optionList = options === undefined ? [] : Array.isArray(options) ? options : [options];
    return {
      ...question,
      required: this.yes(question.required),
      sortOrder: Number(question.sortOrder ?? 0),
      multiple: this.yes(question.options?.multiple),
      options: optionList.map(option => ({ value: option.value, label: option.text ?? option.value })),
      fileProperties: question.fileProperties ? {
        format: question.fileProperties.format,
        maxFileSize: Number(question.fileProperties.maxFileSize),
        maxFileSizeUnit: question.fileProperties.maxFileSizeUnit,
        multiple: this.yes(question.fileProperties.multiple)
      } : undefined
    };
  }

  private toXml(question: Question): string {
    const options = question.type === 'choice' ? `<options multiple="${question.multiple ? 'yes' : 'no'}">${(question.options ?? []).map(option => this.optionXml(option)).join('')}</options>` : '';
    const file = question.type === 'file' && question.fileProperties
      ? `<file_properties format="${this.xml.escape(question.fileProperties.format)}" max_file_size="${question.fileProperties.maxFileSize}" max_file_size_unit="${this.xml.escape(question.fileProperties.maxFileSizeUnit)}" multiple="${question.fileProperties.multiple ? 'yes' : 'no'}"/>` : '';
    return `<question name="${this.xml.escape(question.name)}" type="${question.type}" required="${question.required ? 'yes' : 'no'}" sort_order="${question.sortOrder ?? 0}"><text>${this.xml.escape(question.text)}</text><description>${this.xml.escape(question.description)}</description>${options}${file}</question>`;
  }
  private optionXml(option: QuestionOption): string { return `<option value="${this.xml.escape(option.value)}">${this.xml.escape(option.label)}</option>`; }
  private yes(value?: string): boolean { return value === 'yes' || value === 'true'; }
}
