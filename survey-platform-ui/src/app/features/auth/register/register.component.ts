import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { HttpErrorResponse } from '@angular/common/http';
import { XmlParserService } from '../../../core/services/xml-parser.service';

@Component({
  selector: 'app-register',
  imports: [FormsModule, RouterLink],
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss'
})
export class RegisterComponent {
  private readonly auth = inject(AuthService);
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);
  private readonly xml = inject(XmlParserService);
  protected firstName = '';
  protected lastName = '';
  protected email = '';
  protected password = '';
  protected confirmPassword = '';
  protected submitting = false;
  protected message = '';
  protected readonly returnUrl = this.route.snapshot.queryParamMap.get('returnUrl');
  protected readonly adminRegistration = this.route.snapshot.queryParamMap.get('admin') === 'true';

  protected submit(): void {
    if (this.password !== this.confirmPassword) { this.message = 'The passwords do not match.'; return; }
    this.submitting = true; this.message = '';
    this.auth.register({ firstName: this.firstName, lastName: this.lastName, email: this.email, password: this.password }, !this.adminRegistration).subscribe({
      next: () => {
        void this.router.navigateByUrl(this.adminRegistration ? '/admin/dashboard' : this.returnUrl ?? '/');
      },
      error: (error: HttpErrorResponse) => {
        this.submitting = false;
        this.message = this.errorMessage(error);
      }
    });
  }

  private errorMessage(error: HttpErrorResponse): string {
    if (error.status === 0 || error.status === 404) return 'The Respondly server is unavailable. Make sure the backend is running on port 8080.';
    if (typeof error.error === 'string') {
      try { return this.xml.parse<{ message: string }>(error.error).message || 'Registration failed.'; }
      catch { return 'Registration failed. Please check your details and try again.'; }
    }
    return error.status === 409 ? 'That email is already registered. Sign in instead.' : 'Registration failed. Please check your details and try again.';
  }
}
