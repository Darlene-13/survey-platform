// Anti-Corruptio layer for data communication.

import {Injectable} from '@angular/core';
import {Survey, SurveyPayload} from '../models/survey.model';


@Injectable({providedIn: 'root'})
export class XmlParserService{

  private readonly domParsr = new DOMParser();

  // Passing XML to the models
  parseSurveys(xml: string): Survey[]{
    return this.elements(this.parse(xml), 'survey').map((el) => this.toSurvey(el));
  }

  private elements(parse: any, survey: string) {
    
  }

  private parse(xml: string) {
    
  }

  private toSurvey(el) {
    
  }
}
