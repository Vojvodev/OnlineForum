import { Injectable } from '@angular/core';
import { User } from './comments.service';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { RouterLink } from '@angular/router';
import { response } from 'express';
import { error } from 'console';
import { AuthService } from './auth.service';


@Injectable({
  providedIn: 'root'
})
export class UserService {
  private url = 'https://localhost:8443/api/v1/';


  constructor(private http: HttpClient, private authServ: AuthService) { }


  getAllUsers(): Observable<User[]> {
    const headers = this.authServ.createHeaders();
    console.log(headers);
    return this.http.get<User[]>(this.url + 'allusers', { headers });
  }


  getBlockedUsers(): Observable<User[]>{
    const headers = this.authServ.createHeaders();
    return this.http.get<User[]>(this.url + 'users/blocked', { headers })
  }


  updateRole(userId: number, role: string){
    role = role.toUpperCase();
    const headers = this.authServ.createHeaders();
    this.http.put(this.url + 'users/update/role', null, { 
      headers,
      params: {
        id: userId.toString(), 
        role: role} 
    }).subscribe(response => {
      console.log('Role updated!');
    }, error => {
      console.log('ERROR updating user role!');
    });
  }


  updatePermission(userid: number, categoryId: number, type: string){
    const headers = this.authServ.createHeaders();
    this.http.put(this.url + 'users/update/permission', null, { 
      headers, 
      params: {
        categoryId: categoryId.toString(),
        userId: userid.toString(), 
        type: type} 
    }).subscribe(response => {
      console.log('Permissions updated!');
    }, error => {
      console.log('ERROR updating user permissions!');
    });
  }


  updateStatus(userId: number){
    const headers = this.authServ.createHeaders();
    this.http.put(this.url + 'users/update/status', null, { 
      headers, 
      params: {
        userId: userId.toString()
      } 
    }).subscribe(response => {
      console.log('Status updated!');
    }, error => {
      console.log('ERROR updating user status!');
    });
  }

}
