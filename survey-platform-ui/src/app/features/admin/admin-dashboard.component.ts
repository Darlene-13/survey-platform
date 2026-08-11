import { Component, inject, OnInit, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Survey } from '../../core/models/survey.model';
import { AuthService } from '../../core/services/auth.service';
import { SurveyService } from '../../core/services/survey.service';

@Component({
  selector: 'app-admin-dashboard',
  imports: [RouterLink],
  templateUrl: './admin-dashboard.component.html',
  styleUrl: './admin-dashboard.component.scss'
})
export class AdminDashboardComponent implements OnInit {
  private readonly surveyService = inject(SurveyService);
  private readonly auth = inject(AuthService);
  protected readonly mobileNavOpen = signal(false);
  protected readonly surveys = signal<Survey[]>([]);
  protected readonly loading = signal(true);
  protected readonly message = signal('');
  protected readonly user = this.auth.user();

  ngOnInit(): void {
    this.surveyService.list().subscribe({
      next: surveys => { this.surveys.set(surveys); this.loading.set(false); },
      error: () => { this.loading.set(false); this.message.set('Interviews could not be loaded.'); }
    });
  }

  protected statusClass(status: Survey['status']): string { return status.toLowerCase(); }
}
