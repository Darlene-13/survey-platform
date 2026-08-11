import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

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
      error: () => { this.submitting = false; this.message = 'We could not create this account. The email may already be registered.'; }
    });
  }
}
