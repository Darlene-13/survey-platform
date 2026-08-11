import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';
import { apiRoutes } from '../api-routes';
import { InterviewSubmission, PagedResponses, ResponseQuery } from '../models/response.model';
import { XmlParserService } from './xml-parser.service';

@Injectable({ providedIn: 'root' })
export class ResponseService {
  private readonly http = inject(HttpClient);
  private readonly xml = inject(XmlParserService);

  list(surveyId: number, query: ResponseQuery): Observable<PagedResponses> {
    let params = new HttpParams().set('page', query.page).set('pageSize', query.pageSize);
    if (query.email) params = params.set('email', query.email);
    return this.http.get(apiRoutes.surveys.responses(surveyId), { params, responseType: 'text' }).pipe(
      map(value => this.xml.parse<{
        currentPage: string;
        lastPage: string;
        pageSize: string;
        totalCount: string;
        questionResponse?: PagedResponses['items'][number] | PagedResponses['items'];
      }>(value)),
      map(body => ({
        currentPage: Number(body.currentPage),
        lastPage: Number(body.lastPage),
        pageSize: Number(body.pageSize),
        totalCount: Number(body.totalCount),
        items: body.questionResponse === undefined
          ? []
          : Array.isArray(body.questionResponse) ? body.questionResponse : [body.questionResponse]
      }))
    );
  }

  submit(surveyId: number, submission: InterviewSubmission): Observable<void> {
    const body = new FormData();
    body.set('answers', new Blob([this.answersXml(submission.answers)], { type: 'application/xml' }));
    for (const [questionName, files] of Object.entries(submission.files ?? {})) {
      for (const file of files) body.append(questionName, file, file.name);
    }
    return this.http.post(apiRoutes.surveys.responses(surveyId), body, { responseType: 'text' }).pipe(map(() => undefined));
  }

  private answersXml(answers: Record<string, string>): string {
    return `<response>${Object.entries(answers).map(([name, value]) => `<answer question="${this.xml.escape(name)}">${this.xml.escape(value)}</answer>`).join('')}</response>`;
  }
}
