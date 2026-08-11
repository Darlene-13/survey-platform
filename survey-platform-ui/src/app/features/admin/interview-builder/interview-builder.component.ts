import { Component, inject, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { Question, QuestionType } from '../../../core/models/question.model';
import { Survey } from '../../../core/models/survey.model';
import { QuestionService } from '../../../core/services/question.service';
import { SurveyService } from '../../../core/services/survey.service';

@Component({
  selector: 'app-interview-builder',
  imports: [FormsModule, RouterLink],
  templateUrl: './interview-builder.component.html',
  styleUrl: './interview-builder.component.scss'
})
export class InterviewBuilderComponent implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly surveys = inject(SurveyService);
  private readonly questionService = inject(QuestionService);

  protected readonly interview = signal<Survey | null>(null);
  protected readonly questions = signal<Question[]>([]);
  protected readonly saving = signal(false);
  protected readonly message = signal('');
  protected readonly linkCopied = signal(false);
  protected name = '';
  protected description = '';
  protected questionText = '';
  protected questionDescription = '';
  protected questionName = '';
  protected questionType: QuestionType = 'short_text';
  protected required = true;
  protected multiple = false;
  protected optionsText = '';
  protected fileFormat = '.pdf';
  protected maxFileSize = 2;

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('surveyId'));
    if (!id) return;
    this.surveys.get(id).subscribe({
      next: interview => {
        this.interview.set(interview);
        this.name = interview.name; this.description = interview.description;
      },
      error: () => this.message.set('The interview could not be loaded.')
    });
    this.questionService.list(id).subscribe({
      next: questions => this.questions.set(questions),
      error: () => this.message.set('The interview opened, but its questions could not be loaded.')
    });
  }

  protected saveInterview(): void {
    if (!this.name.trim()) { this.message.set('Enter an interview name.'); return; }
    this.saving.set(true); this.message.set('');
    const request = this.interview()?.id
      ? this.surveys.update(Number(this.interview()!.id), { name: this.name.trim(), description: this.description.trim() })
      : this.surveys.create({ name: this.name.trim(), description: this.description.trim() });
    request.subscribe({
      next: interview => {
        this.interview.set(interview); this.saving.set(false); this.message.set('Draft saved. You can now add questions.');
        if (!this.route.snapshot.paramMap.get('surveyId')) void this.router.navigate(['/admin/interviews', interview.id, 'edit'], { replaceUrl: true });
      },
      error: () => { this.saving.set(false); this.message.set('The interview could not be saved.'); }
    });
  }

  protected addQuestion(): void {
    const interviewId = this.interview()?.id;
    if (!interviewId) { this.message.set('Save the interview before adding questions.'); return; }
    if (!this.questionText.trim() || !this.questionName.trim()) { this.message.set('Question text and machine name are required.'); return; }
    const options = this.optionsText.split('\n').map(value => value.trim()).filter(Boolean)
      .map(label => ({ label, value: label.toUpperCase().replace(/[^A-Z0-9]+/g, '_').replace(/^_|_$/g, '') }));
    const question: Question = {
      name: this.questionName.trim().toLowerCase().replace(/[^a-z0-9]+/g, '_').replace(/^_|_$/g, ''),
      type: this.questionType,
      required: this.required,
      text: this.questionText.trim(),
      description: this.questionDescription.trim(),
      sortOrder: this.questions().length,
      multiple: this.multiple,
      options: this.questionType === 'choice' ? options : undefined,
      fileProperties: this.questionType === 'file' ? { format: this.fileFormat, maxFileSize: this.maxFileSize, maxFileSizeUnit: 'mb', multiple: this.multiple } : undefined
    };
    this.saving.set(true); this.message.set('');
    this.questionService.create(Number(interviewId), question).subscribe({
      next: created => { this.questions.update(items => [...items, created]); this.resetQuestion(); this.saving.set(false); this.message.set('Question added.'); },
      error: () => { this.saving.set(false); this.message.set('The question could not be added. Check its fields and try again.'); }
    });
  }

  protected deleteQuestion(question: Question): void {
    if (!this.interview()?.id || !question.id) return;
    this.questionService.delete(Number(this.interview()!.id), Number(question.id)).subscribe({
      next: () => this.questions.update(items => items.filter(item => item.id !== question.id)),
      error: () => this.message.set('The question could not be removed.')
    });
  }

  protected publish(): void {
    if (!this.interview()?.id) return;
    this.saving.set(true);
    this.surveys.publish(Number(this.interview()!.id)).subscribe({
      next: interview => { this.interview.set(interview); this.saving.set(false); this.message.set('Interview published and ready for respondents.'); },
      error: () => { this.saving.set(false); this.message.set('Add at least one question before publishing.'); }
    });
  }

  protected close(): void {
    if (!this.interview()?.id) return;
    this.surveys.close(Number(this.interview()!.id)).subscribe({ next: interview => this.interview.set(interview) });
  }

  protected respondentLink(): string {
    const interviewId = this.interview()?.id;
    return interviewId ? `${window.location.origin}/interviews/${interviewId}` : '';
  }

  protected async copyRespondentLink(): Promise<void> {
    const link = this.respondentLink();
    if (!link) return;
    try {
      await navigator.clipboard.writeText(link);
      this.linkCopied.set(true);
      window.setTimeout(() => this.linkCopied.set(false), 2200);
    } catch {
      this.message.set('The link could not be copied. Select it and copy it manually.');
    }
  }

  private resetQuestion(): void {
    this.questionText = ''; this.questionDescription = ''; this.questionName = ''; this.questionType = 'short_text';
    this.required = true; this.multiple = false; this.optionsText = '';
  }
}
