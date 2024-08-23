import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-welcome-page',
  standalone: true,
  imports: [RouterModule],
  templateUrl: './welcome-page.component.html',
  styleUrl: './welcome-page.component.css'
})
export class WelcomePageComponent {


  githubLogin(){
    window.location.href="https://github.com/login/oauth/authorize?client_id=Ov23liYFRQ9jiXfEM3a5&redirect_uri=https://localhost:4200/login&scope=user:email&prompt=login";
  }

}
