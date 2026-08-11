import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { forkJoin } from 'rxjs';
import { Question } from '../../core/models/question.model';
import { Survey } from '../../core/models/survey.model';
import { QuestionService } from '../../core/services/question.service';
import { ResponseService } from '../../core/services/response.service';
import { SurveyService } from '../../core/services/survey.service';

@Component({
  selector: 'app-survey-response',
  imports: [FormsModule, RouterLink],
  templateUrl: './survey-response.component.html',
  styleUrl: './survey-response.component.scss'
})
export class SurveyResponseComponent implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly surveyService = inject(SurveyService);
  private readonly questionService = inject(QuestionService);
  private readonly responseService = inject(ResponseService);

  protected readonly survey = signal<Survey | null>(null);
  protected readonly questions = signal<Question[]>([]);
  protected readonly step = signal(1);
  protected readonly loading = signal(true);
  protected readonly submitting = signal(false);
  protected readonly submitted = signal(false);
  protected readonly message = signal('');
  protected readonly answers = signal<Record<string, string>>({});
  protected readonly files = signal<Record<string, File[]>>({});
  protected readonly currentQuestion = computed(() => this.questions()[this.step() - 1]);
  protected readonly progress = computed(() => this.questions().length ? Math.round(this.step() / this.questions().length * 100) : 0);

  ngOnInit(): void {
    const surveyId = Number(this.route.snapshot.paramMap.get('surveyId'));
    if (!Number.isInteger(surveyId) || surveyId < 1) {
      this.loading.set(false); this.message.set('This interview link is invalid.'); return;
    }
    forkJoin({ survey: this.surveyService.get(surveyId), questions: this.questionService.list(surveyId) }).subscribe({
      next: ({ survey, questions }) => {
        this.survey.set(survey);
        this.questions.set(questions);
        this.loading.set(false);
        if (survey.status !== 'LIVE') this.message.set('This interview is not currently accepting responses.');
        else if (!questions.length) this.message.set('This interview has no questions yet.');
      },
      error: () => { this.loading.set(false); this.message.set('We could not load this interview. Please try again later.'); }
    });
  }

  protected answer(question: Question): string { return this.answers()[question.name] ?? ''; }
  protected setAnswer(question: Question, value: string): void { this.answers.update(answers => ({ ...answers, [question.name]: value })); this.message.set(''); }
  protected selected(question: Question, value: string): boolean { return this.answer(question).split(',').filter(Boolean).includes(value); }
  protected choose(question: Question, value: string): void {
    if (!question.multiple) { this.setAnswer(question, value); return; }
    const selected = new Set(this.answer(question).split(',').filter(Boolean));
    selected.has(value) ? selected.delete(value) : selected.add(value);
    this.setAnswer(question, Array.from(selected).join(','));
  }
  protected selectFiles(question: Question, event: Event): void {
    const input = event.target as HTMLInputElement;
    this.files.update(files => ({ ...files, [question.name]: Array.from(input.files ?? []) }));
    this.message.set('');
  }

  protected next(): void {
    const question = this.currentQuestion();
    if (!question || !this.valid(question)) { this.message.set('Please answer this question before continuing.'); return; }
    if (this.step() < this.questions().length) { this.step.update(value => value + 1); this.message.set(''); }
    else this.submit();
  }
  protected previous(): void { if (this.step() > 1) { this.step.update(value => value - 1); this.message.set(''); } }
  protected firstName(): string {
    const nameQuestion = this.questions().find(question => question.name === 'full_name' || question.name === 'name');
    return nameQuestion ? this.answer(nameQuestion).trim().split(/\s+/)[0] : '';
  }

  private valid(question: Question): boolean {
    if (!question.required) return true;
    return question.type === 'file' ? (this.files()[question.name]?.length ?? 0) > 0 : this.answer(question).trim().length > 0;
  }
  private submit(): void {
    const surveyId = this.survey()?.id;
    if (!surveyId) return;
    this.submitting.set(true);
    this.responseService.submit(surveyId, { answers: this.answers(), files: this.files() }).subscribe({
      next: () => { this.submitting.set(false); this.submitted.set(true); },
      error: () => { this.submitting.set(false); this.message.set('Your response could not be submitted. Please review it and try again.'); }
    });
  }
}
