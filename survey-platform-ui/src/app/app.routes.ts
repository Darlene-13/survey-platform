import { Routes } from '@angular/router';
import { HomeComponent } from './features/home/home.component';
import { AdminDashboardComponent } from './features/admin/admin-dashboard.component';
import { SurveyResponseComponent } from './features/respondent/survey-response.component';
import { SignInComponent } from './features/auth/sign-in.component';
import { roleGuard } from './core/guards/role.guard';
import { InterviewBuilderComponent } from './features/admin/interview-builder/interview-builder.component';
import { RegisterComponent } from './features/auth/register/register.component';

export const routes: Routes = [
  { path: '', component: HomeComponent, title: 'Respondly | Interview responses made simple' },
  { path: 'admin', redirectTo: 'admin/dashboard', pathMatch: 'full' },
  { path: 'signin/admin', component: SignInComponent, data: { role: 'ADMIN' }, title: 'Admin sign in | Respondly' },
  { path: 'signin/respondent', component: SignInComponent, data: { role: 'RESPONDENT' }, title: 'Respondent sign in | Respondly' },
  { path: 'register/respondent', component: RegisterComponent, title: 'Create respondent account | Respondly' },
  { path: 'admin/dashboard', component: AdminDashboardComponent, canActivate: [roleGuard('ADMIN')], title: 'Interview dashboard | Respondly' },
  { path: 'admin/interviews/new', component: InterviewBuilderComponent, canActivate: [roleGuard('ADMIN')], title: 'Create interview | Respondly' },
  { path: 'admin/interviews/:surveyId/edit', component: InterviewBuilderComponent, canActivate: [roleGuard('ADMIN')], title: 'Edit interview | Respondly' },
  { path: 'interviews/:surveyId', component: SurveyResponseComponent, canActivate: [roleGuard('RESPONDENT')], title: 'Interview | Respondly' },
  { path: 'respond', redirectTo: 'interviews/1' },
  { path: '**', redirectTo: '' }
];
