import { HttpClient } from '@angular/common/http';
import { inject, Injectable, signal } from '@angular/core';
import { map, Observable, tap } from 'rxjs';
import { apiRoutes } from '../api-routes';
import { LoginPayload, LoginResponse, RegisterPayload, User } from '../models/auth.model';
import { XmlParserService } from './xml-parser.service';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly xml = inject(XmlParserService);
  private readonly tokenKey = 'respondly_access_token';
  private readonly userKey = 'respondly_user';
  private readonly refreshTokenKey = 'respondly_refresh_token';
  readonly currentUser = signal<User | null>(null);

  login(payload: LoginPayload): Observable<LoginResponse> { return this.authenticate(apiRoutes.auth.login, payload); }
  register(payload: RegisterPayload, persistSession = true): Observable<LoginResponse> { return this.authenticate(apiRoutes.auth.register, payload, persistSession); }
  token(): string | null { return typeof localStorage === 'undefined' ? null : localStorage.getItem(this.tokenKey); }
  user(): User | null {
    if (typeof localStorage === 'undefined') return this.currentUser();
    const value = localStorage.getItem(this.userKey);
    return value ? JSON.parse(value) as User : null;
  }

  logout(): void {
    if (typeof localStorage !== 'undefined') {
      localStorage.removeItem(this.tokenKey);
      localStorage.removeItem(this.userKey);
      localStorage.removeItem(this.refreshTokenKey);
    }
    this.currentUser.set(null);
  }

  private authenticate(url: string, payload: LoginPayload | RegisterPayload, persistSession = true): Observable<LoginResponse> {
    const fields = Object.entries(payload).map(([key, value]) => `<${this.snakeCase(key)}>${this.xml.escape(value)}</${this.snakeCase(key)}>`).join('');
    const root = 'firstName' in payload ? 'register_request' : 'login_request';
    return this.http.post(url, `<${root}>${fields}</${root}>`, { responseType: 'text' }).pipe(
      map(value => this.xml.parse<LoginResponse>(value)),
      tap(response => {
        if (!persistSession) return;
        if (typeof localStorage !== 'undefined') {
          localStorage.setItem(this.tokenKey, response.token);
          localStorage.setItem(this.userKey, JSON.stringify(response.user));
          if (response.refreshToken) localStorage.setItem(this.refreshTokenKey, response.refreshToken);
        }
        this.currentUser.set(response.user);
      })
    );
  }

  private snakeCase(value: string): string { return value.replace(/[A-Z]/g, letter => `_${letter.toLowerCase()}`); }
}
