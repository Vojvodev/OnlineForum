import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { User } from './comments.service';
import { SignupData } from './models/signup-data.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private baseUrl = 'https://localhost:8443/api/v1/auth';
  // public currentUser: User | null = null; 
  
  
  private currentUserSubject = new BehaviorSubject<User | null>(null);
  public currentUser$ = this.currentUserSubject.asObservable();

  get currentUser(): User | null {
    return this.currentUserSubject.value;
  }

  set currentUser(user: User | null) {
    this.currentUserSubject.next(user);
  }



  constructor(private http: HttpClient) { }



  public createHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    return new HttpHeaders().set('Authorization', `Bearer ${token}`);
  }

  signup(signupData: SignupData): Observable<any>{
    // const headers = this.createHeaders();
    // const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post<any>(`${this.baseUrl}/signup`, signupData, { responseType: 'text' as 'json' });
  }

  login(username: string, password: string): Observable<any> {
    // const headers = this.createHeaders();
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post<any>(`${this.baseUrl}/login`, { username, password }, { headers, responseType: 'text' as 'json' });
  }

  getJWT(userId: number): Observable<any> {
    // const headers = this.createHeaders();
    return this.http.post<any>(`${this.baseUrl}/generate-jwt`, null, { params: {id: userId} });
  }


  logout() {
    this.currentUser = null;
    const headers = this.createHeaders();
    const token = localStorage.getItem('token'); 
    localStorage.removeItem('token');

    this.http.post(`${this.baseUrl}/logout`, { token }, {headers, responseType: 'text' as 'json'}).subscribe( (response) => {
      console.log(response + ' - user: ' + this.currentUser);
    });
  }


  

}
