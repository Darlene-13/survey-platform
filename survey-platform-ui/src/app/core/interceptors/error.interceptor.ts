import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { catchError, throwError } from 'rxjs';

const MESSAGES: Record<number, string> = {
  0: 'Cannot reach the server. Check your connection.',
  400: 'The request was invalid. Please review your input.',
  404: 'The requested resource was not found.',
  413: 'File too large. Check the upload size limit.',
  500: 'Something went wrong on the server. Try again later.',
};

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const snackBar = inject(MatSnackBar); // error pop

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      const message = MESSAGES[error.status] ?? 'An unexpected error occurred.';
      snackBar.open(message, 'Dismiss', { duration: 5000 });
      return throwError(() => error);
    })
  );
};

