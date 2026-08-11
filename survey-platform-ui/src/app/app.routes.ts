import { Routes } from '@angular/router';
import { HomeComponent } from './features/home/home.component';
import { AdminDashboardComponent } from './features/admin/admin-dashboard.component';
import { SurveyResponseComponent } from './features/respondent/survey-response.component';
import { SignInComponent } from './features/auth/sign-in.component';
import { roleGuard } from './core/guards/role.guard';

export const routes: Routes = [
  { path: '', component: HomeComponent, title: 'Respondly | Interview responses made simple' },
  { path: 'admin', redirectTo: 'admin/dashboard', pathMatch: 'full' },
  { path: 'signin/admin', component: SignInComponent, data: { role: 'ADMIN' }, title: 'Admin sign in | Respondly' },
  { path: 'signin/respondent', component: SignInComponent, data: { role: 'RESPONDENT' }, title: 'Respondent sign in | Respondly' },
  { path: 'admin/dashboard', component: AdminDashboardComponent, canActivate: [roleGuard('ADMIN')], title: 'Interview dashboard | Respondly' },
  { path: 'interviews/:surveyId', component: SurveyResponseComponent, canActivate: [roleGuard('RESPONDENT')], title: 'Interview | Respondly' },
  { path: 'respond', redirectTo: 'interviews/1' },
  { path: '**', redirectTo: '' }
];
