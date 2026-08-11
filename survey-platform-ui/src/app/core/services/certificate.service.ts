import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { apiRoutes } from '../api-routes';

@Injectable({ providedIn: 'root' })
export class CertificateService {
  private readonly http = inject(HttpClient);
  download(certificateId: number): Observable<Blob> {
    return this.http.get(apiRoutes.certificates.download(certificateId), { responseType: 'blob' });
  }
}
