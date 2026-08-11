import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';
import { apiRoutes } from '../api-routes';
import { Question } from '../models/question.model';
import { XmlParserService } from './xml-parser.service';

@Injectable({ providedIn: 'root' })
export class QuestionService {
  private readonly http = inject(HttpClient);
  private readonly xml = inject(XmlParserService);

  list(surveyId: number): Observable<Question[]> {
    return this.http.get(apiRoutes.surveys.questions(surveyId), { responseType: 'text' }).pipe(
      map(value => this.xml.parse<{ question?: Question | Question[] }>(value).question),
      map(value => value === undefined ? [] : Array.isArray(value) ? value : [value])
    );
  }

  delete(surveyId: number, questionId: number): Observable<void> {
    return this.http.delete<void>(apiRoutes.surveys.question(surveyId, questionId));
  }
}
