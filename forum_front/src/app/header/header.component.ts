import { NgIf } from '@angular/common';
import { Component } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../auth.service';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [NgIf, RouterModule],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent {

  constructor(
    private router: Router,
    private authServ: AuthService
  ){}

  goHome(){
    this.router.navigate(['']);
  }


  logout(){
    this.authServ.logout();
  }

  isAdmin(){
    return (this.authServ.currentUser != null && this.authServ.currentUser.role === 'ADMIN');
  }

}
