import { HttpInterceptorFn } from '@angular/common/http';

/**
 * Sets XML headers for all API calls.
 * Skips Content-Type for FormData bodies (multipart submissions) so the
 * browser can set its own boundary header.
 */
export const xmlInterceptor: HttpInterceptorFn = (req, next) => {
  if (!req.url.startsWith('/api')) {
    return next(req);
  }

  let headers = req.headers.set('Accept', 'application/xml');

  if (req.body !== null && !(req.body instanceof FormData)) {
    headers = headers.set('Content-Type', 'application/xml');
  }

  return next(req.clone({ headers }));
};
