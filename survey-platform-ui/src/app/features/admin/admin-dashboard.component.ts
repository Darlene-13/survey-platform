import { Component, signal } from '@angular/core';
import { RouterLink } from '@angular/router';

interface SurveyRow { title: string; status: 'Live' | 'Draft' | 'Closed'; responses: number; completion: number; updated: string; }

@Component({
  selector: 'app-admin-dashboard',
  imports: [RouterLink],
  templateUrl: './admin-dashboard.component.html',
  styleUrl: './admin-dashboard.component.scss'
})
export class AdminDashboardComponent {
  protected readonly mobileNavOpen = signal(false);
  protected readonly surveys: SurveyRow[] = [
    { title: 'Frontend Engineer Interview', status: 'Live', responses: 486, completion: 82, updated: '12 min ago' },
    { title: 'Graduate Developer Intake', status: 'Live', responses: 231, completion: 74, updated: '1 hour ago' },
    { title: 'Product Designer Interview', status: 'Draft', responses: 0, completion: 0, updated: 'Yesterday' },
    { title: 'Backend Engineer Intake', status: 'Closed', responses: 918, completion: 91, updated: 'Aug 02' }
  ];
}
