import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SubmitCommentData } from './models/submit-comment-data.model';
import { AuthService } from './auth.service';

export interface Comments {
  id: number;                 
  text: string;               
  status: string;             // use string to match Enum's name
  createdAt: string;          // use string for ISO date format
  category: Category;         
  user: User;
  editing: boolean;                 
}
export interface Category {
  id: number;
  name: string;
}
export interface User {
  id: number;
  username: string;
  email: string;
  password: string;
  role: string;
  status: string;
  createdAt: Date;
  permissionsList: Permission[]; 
}

export interface Permission{
  id: number;
  add: boolean;
  edit: boolean;
  delete: boolean;
  category: Category;
}


export interface Page<T> {
  content: T[];
  totalPages: number;
  totalElements: number;
  size: number;
  number: number;
}



@Injectable({
  providedIn: 'root'
})
export class CommentsService {
  private apiUrl = 'https://localhost:8443/api/v1/comments/';


  constructor(private http: HttpClient, private authServ: AuthService) { }



  getComments(categoryId:number, page: number, size: number): Observable<Page<Comments>> {
    let params = new HttpParams().set('categoryId', categoryId.toString()).set('page', page.toString()).set('size', size.toString());
    // const headers = this.authServ.createHeaders();
    return this.http.get<Page<Comments>>(this.apiUrl + 'paginated/category', { params });
  }

  uploadComment(subComment: SubmitCommentData): Observable<any>{ 
    const headers = this.authServ.createHeaders();
    return this.http.post<any>(this.apiUrl + 'add', subComment, {headers, responseType: 'text' as 'json'});
  }

  deleteComment(id: number): Observable<any>{
    let params = new HttpParams().set('id', id.toString());
    const headers = this.authServ.createHeaders();
    return this.http.delete<any>(this.apiUrl + 'delete', { headers, params, responseType: 'text' as 'json' });
  }

  updateComment(id: number, text: string): Observable<any>{
    const headers = this.authServ.createHeaders();
    return this.http.put<any>(this.apiUrl + 'update/text', text, {headers, params: {id: id.toString()}, responseType: 'text' as 'json'});
  }

}





