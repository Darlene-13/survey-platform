import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-sign-in',
  imports: [FormsModule, RouterLink],
  templateUrl: './sign-in.component.html',
  styleUrl: './sign-in.component.scss'
})
export class SignInComponent {
  private readonly auth = inject(AuthService);
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);
  protected email = '';
  protected password = '';
  protected submitting = false;
  protected message = '';
  protected readonly expectedRole = this.route.snapshot.data['role'] as 'ADMIN' | 'RESPONDENT';
  protected readonly returnUrl = this.route.snapshot.queryParamMap.get('returnUrl');

  protected submit(): void {
    this.submitting = true;
    this.message = '';
    this.auth.login({ email: this.email, password: this.password }).subscribe({
      next: response => {
        this.submitting = false;
        if (response.user.role !== this.expectedRole) {
          this.auth.logout();
          this.message = `This account is not a ${this.expectedRole.toLowerCase()} account.`;
          return;
        }
        const returnUrl = this.route.snapshot.queryParamMap.get('returnUrl');
        void this.router.navigateByUrl(returnUrl ?? (this.expectedRole === 'ADMIN' ? '/admin/dashboard' : '/'));
      },
      error: () => {
        this.submitting = false;
        this.message = 'Sign in failed. Check your email and password.';
      }
    });
  }
}
