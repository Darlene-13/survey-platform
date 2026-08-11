import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';
import { apiRoutes } from '../api-routes';
import { Survey, SurveyPayload } from '../models/survey.model';
import { XmlParserService } from './xml-parser.service';

@Injectable({ providedIn: 'root' })
export class SurveyService {
  private readonly http = inject(HttpClient);
  private readonly xml = inject(XmlParserService);

  list(): Observable<Survey[]> {
    return this.request<{ surveys?: { survey?: Survey | Survey[] }; survey?: Survey | Survey[] }>(apiRoutes.surveys.collection)
      .pipe(map(body => this.array(body.surveys?.survey ?? body.survey)));
  }

  get(surveyId: number): Observable<Survey> { return this.request<Survey>(apiRoutes.surveys.one(surveyId)); }
  create(payload: SurveyPayload): Observable<Survey> { return this.send('post', apiRoutes.surveys.collection, payload); }
  update(surveyId: number, payload: SurveyPayload): Observable<Survey> { return this.send('put', apiRoutes.surveys.one(surveyId), payload); }
  delete(surveyId: number): Observable<void> { return this.http.delete<void>(apiRoutes.surveys.one(surveyId)); }

  private request<T>(url: string): Observable<T> { return this.http.get(url, { responseType: 'text' }).pipe(map(xml => this.xml.parse<T>(xml))); }
  private send(method: 'post' | 'put', url: string, payload: SurveyPayload): Observable<Survey> {
    const body = `<survey><name>${this.xml.escape(payload.name)}</name><description>${this.xml.escape(payload.description)}</description></survey>`;
    return this.http[method](url, body, { responseType: 'text' }).pipe(map(xml => this.xml.parse<Survey>(xml)));
  }
  private array<T>(value?: T | T[]): T[] { return value === undefined ? [] : Array.isArray(value) ? value : [value]; }
}
