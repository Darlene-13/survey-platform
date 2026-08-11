import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

export const roleGuard = (role: 'ADMIN' | 'RESPONDENT'): CanActivateFn => () => {
  const auth = inject(AuthService);
  const router = inject(Router);
  const user = auth.user();
  return user?.role === role ? true : router.createUrlTree([`/signin/${role.toLowerCase()}`]);
};
