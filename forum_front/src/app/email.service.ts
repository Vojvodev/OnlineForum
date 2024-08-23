import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class EmailService {
  private apiUrl = 'https://localhost:8443/api/v1/email/';

  constructor(private http: HttpClient, private authServ: AuthService) {}


  
  sendEmail(to: string, subject: string, body: string): Observable<void> {
    // const headers = this.authServ.createHeaders();
    return this.http.post<void>(this.apiUrl + 'send', { to, subject, body });
  }


}
